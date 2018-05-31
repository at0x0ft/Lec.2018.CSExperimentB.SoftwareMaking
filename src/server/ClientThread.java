package server;

import java.io.*;
import java.net.*;
import interfaces.IDisposable;

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

    public ClientThread(Server server, StateManager stateManager, Socket socket) {
        this._server = server;
        this._stateManager = stateManager;
        this._socket = socket;
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
                this._stateManager.waitThread();
            }

            // start game!
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                if(this._socket != null) {
                    this._socket.close();
                }
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
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