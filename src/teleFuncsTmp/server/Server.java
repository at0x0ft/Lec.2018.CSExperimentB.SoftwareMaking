package server;

import java.io.*;
import java.net.*;
import java.lang.InterruptedException;
import main.LoveLetter;
import interfaces.IConnectable;

public class Server implements IConnectable {
    public static final int PORT = 8080; // Set the Port Number.
    private ServerSocket _serverSocket;

    private String _masterPlayerName;
    public String masterPlayerName() {
        return this._masterPlayerName;
    }

    private StateManager _stateManager;

    public Server(String masterPlayerName) {
        this._masterPlayerName = masterPlayerName;
    }
    
    public boolean establishConnection() throws IOException {
        System.out.println("How many players do you want to play with (including you) ?");
        this._stateManager = StateManager(this, playerNumInput());

        return startInvitation();
    }

    private int playerNumInput() throws IOException {   // remark
        int check = 0;
        while (true) {
            System.out.print("Please input number from 2 to " + this._stateManager.MAXPLAYERNUM + ". : ");
            try {
                check = Integer.parseInt(LoveLetter.cInputLn());
                if(check >= 2 && check <= MAXPLAYERNUM) {
                    return check;
                }

                System.out.println("Wrong number! Please enter the correct ones.");
            }
            catch (NumberFormatException nfe) {
                System.out.println("Wrong number! Please enter the correct ones.");
                continue;
            }
        }
    }

    private boolean startInvitation() throws IOException {
        this._serverSocket = new ServerSocket(Server.PORT);
        try {
            System.out.println("Waiting for other player(s)...");
            this._serverSocket.setSoTimeout(1000);  // Timeout every 1000 [ms].
            while(true) {   // critical section...
                while(this._stateManager.inviting() && this._stateManager.isFullCandidates()) {
                    this._stateManager.waitThread();
                }
                if(!this._stateManager.inviting()) {
                        break;
                }

                this._stateManager.addCandidate(new ClientThread(this._serverSocket.accept(), server));
            }

            // waiting for removing all clients which failed to get 
            while(!this._stateManager.playing()) {
                this._stateManager.waitThread();
            }
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
            // clientThreads dispose
            if(this._serverSocket != null) {
                this._serverSocket.close();
            }
            return false;
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            // clientThreads dispose
            if(this._serverSocket != null) {
                this._serverSocket.close();
            }
            return false;
        }

        return true;
    }
}