package server;

// import java.;
import java.util.ArrayList;


public class StateManager {
    private Server _server;
    private ArrayList<ClientThread> _candidates;
    private ClientThread[] _clientPlayers;

    public static final int MAXPLAYERNUM = 6;

    private int _playerNum;
    private int _registeredPlayerNum;

    public synchronized boolean isFullCandidates() {
        return this._candidates.size() + this._registeredPlayerNum >= this._playerNum;
    }

    private boolean isReady2Start() {
        return this._playerNum == this._registeredPlayerNum;
    }

    private boolean clientIsEmpty() {
        return this._candidates.size() == 0;
    }

    private int _state;
    private void proceedState() {
        this._state++;
    }
    public synchronized boolean inviting() {
        return this._state == 0;
    }
    public synchronized boolean cancellingCandidates() {
        return this._state == 1;
    }
    public synchronized boolean playing() {
        return this._state == 2;
    }

    public ThreadState(Server server, int playerNum) {
        this._server = server;
        this._candidates = new ArrayList<ClientThread>();
        this._clientPlayers = new ClientThread[playerNum - 1];
        this._playerNum = playerNum;
        this._registeredPlayerNum = 0;
        this._state = 0;
        this._pauseThread = false;
    }

    public synchronized void addCandidate(ClientThread clientThread) {
        clientThread.start();
        this._candidates.add(clientThread);
    }

    public synchronized void removeCandidate(ClientThread clientThread) {
        this._candidates.remove(clientThread);
        if(!isFullCandidates()) {
            restartAllWaitingThreads();
            this._server.notify();
        }
    }

    private synchronized boolean masterCheck(ClientThread clientThread) throws IOException {
        if(!this._inviting) {
            removeCandidate(clientThread);
            if(clientIsEmpty()) {
                proceedState();
                restartAllWaitingThreads();
            }
            return false;
        }

        System.out.println("Connected from " + clientThread.clientName() + "!");
        if(!acceptionYN()) {
            removeCandidate(clientThread);
            return false;
        }
        else {
            System.out.println(clientThread.clientName() + " is now registered!");
        }

        registerClient(clientThread);

        if(isReady2Start()) {
            proceedState();
            if(clientIsEmpty()) {
                proceedState();
            }

            while(!playing) {
                waitThread();
            }
            restartAllWaitingThreads();
        }

        return true;
    }

    private boolean acceptionYN() throws IOException {
        String check = null;
        while (true) {
            System.out.println("Would you accept this player? Enter y/n");
            check = LoveLetter.cInputLn();
            if(check.equals("y")) {
                return true;
            }
            else if(check.equals("n")) {
                return false;
            }
            System.out.println("Wrong character! Please enter the correct ones.");
        }
    }

    private void registerClient(ClientThread clientThread) {
        this._clientPlayers[this._registeredPlayerNum++] = clientThread;
        this._candidates.remove(clientThread);
    }
    
    private boolean _pauseThread;
    public synchronized void waitThread() {
        if(this._pauseThread == false) {
            this._threadWaitingState = true;
        }

        this._waitingThreadNum++;
        while(this._pauseThread) {
            wait();
        }
        this._waitingThreadNum--;
    }

    public synchronized void restartAllWaitingThreads() {
        this._pauseThread = false;
        notifyAll();
    }
}