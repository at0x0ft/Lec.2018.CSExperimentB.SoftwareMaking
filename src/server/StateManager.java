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
    public synchronized ClientThread clientThread(int i) {
        return this._clientPlayers[i];
    }
    private boolean[] _activeFlags;
    public synchronized boolean activeFlag(int i) {
        return this._activeFlags[i];
    }

    private int _playerNum;
    public int playerNum() {
        return this._playerNum;
    }
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
    public synchronized void proceedState() {
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
    public synchronized boolean finished() {
        return this._state == 3;
    }

    public StateManager(Server server, int playerNum) {
        this._server = server;
        this._candidates = new ArrayList<ClientThread>();
        this._clientPlayers = new ClientThread[playerNum - 1];
        this._activeFlags = new boolean[playerNum + 1]; // clients + master(serverThread) + this
        this._playerNum = playerNum;
        this._registeredPlayerNum = 0;
        this._state = 0;
        this._message = null;
        this._msgType = -1;
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
                waitThread(this._playerNum);
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
        clientThread.ctidx(this._registeredPlayerNum);
        this._clientPlayers[this._registeredPlayerNum++] = clientThread;
        this._candidates.remove(clientThread);
    }

    private int _waitingThreadNum;
    public int waitingThreadNum() {
        return this._waitingThreadNum;
    }
    public synchronized void waitThread(int key) {
        this._activeFlags[key] = false;
        this._waitingThreadNum++;
        while(!this._activeFlags[key]) {
            try {
                wait();
            }
            catch(InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        this._waitingThreadNum--;
        // System.err.println("out id : " + key);  // 4debug
    }

    public synchronized void restartWaitingThread(int key) {
        this._activeFlags[key] = true;
        notifyAll();
    }

    public synchronized void restartAllWaitingThreads() {
        for(int i = 0 ; i < this._activeFlags.length; i++) {
            this._activeFlags[i] = true;
        }
        notifyAll();
    }

    private String _message;
    private int _msgType;
    public synchronized void registerMessage(int msgType, String msg) {
        this._message = msg;
        this._msgType = msgType;
    }
    public synchronized String popMessage() {
        String retMsg = this._message;
        this._message = null;
        return retMsg;
    }
    public synchronized int popMsgType() {
        int msgType = this._msgType;
        this._msgType = -1;
        return msgType;
    }

    public synchronized void dispose() throws IOException {
        if(this._candidates != null && this._candidates.size() != 0) {
            for (ClientThread ct : this._candidates) {
                ct.dispose();
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