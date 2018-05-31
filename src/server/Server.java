package server;

import java.io.*;
import java.net.*;
import java.lang.InterruptedException;
import main.LoveLetter;
import main.GameBase;
import interfaces.IDisposable;
// import game.Game;

public class Server extends GameBase /*implements IDisposable*/ {

    // fields and the constructor Part

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
    
    // Connection Part

    public boolean establishConnection() throws IOException {
        System.out.println("How many players do you want to play with (including you) ?");
        this._stateManager = new StateManager(this, playerNumInput());

        return startInvitation();
    }

    private int playerNumInput() throws IOException {   // remark
        int check = 0;
        while (true) {
            System.out.print("Please input number from 2 to " + this._stateManager.MAXPLAYERNUM + ". : ");
            try {
                check = Integer.parseInt(LoveLetter.cInputLn());
                if(check >= 2 && check <= this._stateManager.MAXPLAYERNUM) {
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
            // this._serverSocket.setSoTimeout(1000);  // Timeout every 1000 [ms].
            while(true) {   // critical section...
                while(this._stateManager.inviting() && this._stateManager.isFullCandidates()) {
                    this._stateManager.waitThread();
                }
                if(!this._stateManager.inviting()) {
                        break;
                }

                // try{
                    this._stateManager.addCandidate(new ClientThread(this, this._stateManager, this._serverSocket.accept()));
                // }
            }

            // waiting for removing all clients which failed to get 
            while(!this._stateManager.playing()) {
                this._stateManager.waitThread();
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
            // clientThreads dispose
            if(this._serverSocket != null) {
                this._serverSocket.close();
            }
            return false;
        }


        this._stateManager.printAllRegisteredPlayerName();  // 4debug
        return true;
    }

    // Game Part

    // private Game _game;

    public void startGame() {
        System.out.println("Now, let's start the game!");

        // create game class from here
    }

    public void dispose() throws IOException {
        if(this._stateManager != null) {
            this._stateManager.dispose();
        }

        if(this._serverSocket != null) {
            try {
                this._serverSocket.close();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}