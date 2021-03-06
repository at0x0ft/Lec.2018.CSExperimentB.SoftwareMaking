package server;

import java.io.*;
import java.net.*;
import java.lang.InterruptedException;
import main.Console;
import main.GameBase;
import interfaces.IDisposable;
import game.Game;

public class Server extends GameBase {

    // fields and the constructor Part
    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException uhe) {
            Console.writeLn("Unknown Host Exception Occured.");
        }
        return "Unknown Local Host";
    }

    public static final int MINPORTNUM = 1024;
    public static final int MAXPORTNUM = 30000;
    private int _port;
    public int port() {
        return this._port;
    }

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
        Console.newLn();

        while(true) {
            this._port = portNumInput();
            try {
                this._serverSocket = new ServerSocket(port());
                break;
            }
            catch(BindException be) {
                Console.writeLn("Your server PORT : " + port() + " is already used.");
                Console.writeLn("Please assign another PORT again...");
                continue;
            }
        }
        this._stateManager = new StateManager(this, playerNumInput());

        return startInvitation();
    }

    private int portNumInput() {
        return Console.readNum(
            Server.MINPORTNUM,
            Server.MAXPORTNUM,
            "Setting this server's PORT.",
            "Please input PORT ("+ Server.MINPORTNUM + " <= PORT <= " + Server.MAXPORTNUM + ") : "
        );
    }

    private int playerNumInput() {
        return Console.readNum(
            Game.MINPLAYERNUM,
            Game.MAXPLAYERNUM,
            "How many players do you want to play with (including you) ?",
            "Please input number from 2 to " + Game.MAXPLAYERNUM + ". : "
        );
    }

    private boolean startInvitation() throws IOException {
        try {
            Console.writeLn("Your server info : IPAddress : " + Server.getLocalHostName() + ", PORT : " + port());
            Console.writeLn("Waiting for other player(s)...");
            while(true) {   // critical section...
                while(this._stateManager.inviting() && this._stateManager.isFullCandidates()) {
                    this._stateManager.waitThread(this._stateManager.playerNum() - 1);
                }
                if(!this._stateManager.inviting()) {
                        break;
                }

                this._stateManager.addCandidate(new ClientThread(this, this._stateManager, this._serverSocket.accept()));
            }

            // waiting for removing all clients which failed to get 
            while(!this._stateManager.playing()) {
                this._stateManager.waitThread(this._stateManager.playerNum() - 1);
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

        return true;
    }

    // Game Part

    private Game _game;

    public void startGame() throws IOException {
        Console.writeLn("Now, let's start the game!");

        // create game class from here
        this._game = new Game(this._masterPlayerName, this._stateManager);

        this._game.start();

        this._stateManager.proceedState();
    }

    public synchronized void dispose() throws IOException {
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