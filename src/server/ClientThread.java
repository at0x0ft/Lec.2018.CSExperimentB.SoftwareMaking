package server;

import java.io.*;
import java.net.*;
import interfaces.IDisposable;
import game.ClientPlayer;

public class ClientThread extends Thread implements IDisposable {
    private Server _server;
    private StateManager _stateManager;

    private Socket _socket;
    private BufferedReader _exin;
    private PrintWriter _exout;

    private String _clientName;
    public String clientName() {
        return this._clientName;
    }

    private int _ctidx;
    public int ctidx() {
        return this._ctidx;
    }
    public void ctidx(int idx) {
        this._ctidx = idx;
    }

    private ClientPlayer _clientPlayer;
    private void setClientPlayer(ClientPlayer cp) {
        this._clientPlayer = cp;
    }

    public ClientThread(Server server, StateManager stateManager, Socket socket) {
        this._server = server;
        this._stateManager = stateManager;
        this._socket = socket;
        this._ctidx = -1;
        this._clientPlayer = null;
    }

    @Override
    public void run() {
        try {
            this._exin = new BufferedReader(new InputStreamReader(this._socket.getInputStream())); // Set the buffer for data serving.
            this._exout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream())), true); // Set the buffer for sending.

            if(!registrationRequest()) {
                this._stateManager.removeCandidate(this);
                return;
            }

            this._clientName = this._exin.readLine();

            if(!this._stateManager.masterCheck(this)) {
                // Access permission denied...
                this._exout.println("n");
                return;
            }
            else {
                this._exout.println("y");
            }

            while(!this._stateManager.playing()) {
                this._stateManager.waitThread(ctidx());
            }

            // start game!
            while(!this._stateManager.finished()) {
                sendMessage(this._stateManager.popMsgType(), this._stateManager.popMessage());
                this._stateManager.waitThread(ctidx());
            }

        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if(this._socket != null) {
                try {
                    this._socket.close();
                }
                catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private boolean registrationRequest() throws IOException {
        this._exout.println(this._server.masterPlayerName());

        switch(this._exin.readLine()) {
            case "y": {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public void sendMessage(int msgType, String message) throws IOException {
        // sendMessage
        this._exout.println(message);

        // receive message
        switch(msgType) {
            case 7:
            case 13:
            case 16:
            case 17:
            case 21:
            case 22:
            case 23:
            case 24: {
                System.err.println("before sending msg " + msgType);// 4debug
                String response = this._exin.readLine();
                System.err.println("response received. " + response);   // 4debug
                this._stateManager.registerMessage(msgType, response);
                // restart response waiting main thread
                this._stateManager.restartWaitingThread(this._stateManager.playerNum() - 1);
                System.err.println("after sending msg " + msgType);// 4debug
                break;
            }
        }
    }

    public synchronized void dispose() throws IOException {
        if(this._socket != null) {
            try {
                this._socket.close();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}