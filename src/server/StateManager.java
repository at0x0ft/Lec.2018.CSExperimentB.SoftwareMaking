package server;

// import java.;
import java.io.*;
import java.util.ArrayList;
import java.lang.*;
import main.Console;
import interfaces.IDisposable;


public class StateManager implements IDisposable {
    private Server _server;
    private ArrayList<ClientThread> _candidates;
    private ClientThread[] _clientPlayers;

    public static final int MINPLAYERNUM = 2;
    public static final int MAXPLAYERNUM = 6;

    private int _playerNum;
    private int _registeredPlayerNum;

    public synchronized boolean isFullCandidates() {
        return this._candidates.size() + this._registeredPlayerNum >= this._playerNum - 1;
    }

    private boolean isReady2Start() {
        return this._playerNum - 1 == this._registeredPlayerNum;
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

    public StateManager(Server server, int playerNum) {
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

    public synchronized boolean masterCheck(ClientThread clientThread) throws IOException {
        if(!inviting()) {
            removeCandidate(clientThread);
            if(clientIsEmpty()) {
                proceedState();
                restartAllWaitingThreads();
            }
            return false;
        }

        if(!acceptionYN(clientThread)) {
            removeCandidate(clientThread);
            return false;
        }
        else {
            Console.writeLn(clientThread.clientName() + " is now registered!");
        }

        registerClient(clientThread);

        if(isReady2Start()) {
            proceedState();
            if(clientIsEmpty()) {
                proceedState();
            }

            while(!playing()) {
                waitThread();
            }
            restartAllWaitingThreads();
        }

        return true;
    }

    private boolean acceptionYN(ClientThread ct) throws IOException {
        return Console.readTorF(
            "y",
            "n",
            "Connected from " + ct.clientName() + "!",
            "Would you accept this player? Enter y/n : "
        );
    }

    private void registerClient(ClientThread clientThread) {
        this._clientPlayers[this._registeredPlayerNum++] = clientThread;
        this._candidates.remove(clientThread);
    }
    
    private boolean _pauseThread;
    private int _waitingThreadNum;
    public int waitingThreadNum() {
        return this._waitingThreadNum;
    }
    public synchronized void waitThread() {
        if(this._pauseThread == false) {
            this._pauseThread = true;
        }

        this._waitingThreadNum++;
        while(this._pauseThread) {
            try {
                wait();
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        this._waitingThreadNum--;
    }

    public synchronized void restartAllWaitingThreads() {
        this._pauseThread = false;
        notifyAll();
    }

    public synchronized void printAllRegisteredPlayerName() {   // 4debug
        for (int i = 0; i < this._clientPlayers.length; i++) {
            Console.writeLn(this._clientPlayers[i].clientName());
        }
    }

    public synchronized void dispose() throws IOException {
        synchronized(this) {
            if(this._candidates != null && this._candidates.size() != 0) {
                for (ClientThread ct : this._candidates) {
                    ct.dispose();
                }
            }

            if(this._clientPlayers != null) {
                for (int i = 0; i < this._clientPlayers.length; i++) {
                    if(this._clientPlayers[i] != null) {
                        this._clientPlayers[i].dispose();
                    }
                }
            }
        }
    }
}