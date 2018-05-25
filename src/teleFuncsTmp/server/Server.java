package server;

import java.io.*;
import java.net.*;
import java.lang.InterruptedException;
import main.LoveLetter;
import interfaces.IConnectable;

public class Server implements IConnectable {
    public static final int PORT = 8080; // Set the Port Number.
    private ThreadManager _threadManager;
    
    private String _masterPlayerName;
    public String masterPlayerName() {
        return this._masterPlayerName;
    }

    public static final int MAXPLAYERNUM = 6;
    private int _playerNum;
    public int playerNum() {
        return this._playerNum;
    }

    private boolean _isNowPlaying;
    public boolean isNowPlaying() {
        return this._isNowPlaying;
    }
    public void isNowPlaying(boolean value) {
        this._isNowPlaying = value;
    }

    private Thread _connectionWaitingThread;
    private boolean startInvitation() {
        System.out.println("Waiting for other player(s)...");

        Server server = this;
        ThreadManager tm = this._threadManager;
        
        try {
            this._connectionWaitingThread = new Thread(new Runnable() {
                public void run() {
                    while(true) {
                        try {
                            tm.addCandidate(new ClientThread((new ServerSocket(Server.PORT)).accept(), server));
                        }
                        catch(IOException ioe) {
                            ioe.printStackTrace();
                        }
                        finally {
                        }
                    }
                }
            });
            this._connectionWaitingThread.start();
            this._connectionWaitingThread.join();
        }
        catch(InterruptedException ie) {
            if(this._isNowPlaying) {
                return true;
            }
            ie.printStackTrace();
        }
        finally {
        }
    
        return false;
    }
    public void finishInvitaion() {
        this._connectionWaitingThread.interrupt();
    }

    public Server(String masterPlayerName) {
        this._masterPlayerName = masterPlayerName;
    }

    public boolean establishConnection() throws IOException {
        System.out.println("How many players do you want to play with?");
        isNowPlaying(false);
        this._playerNum = playerNumInput();

        this._threadManager = new ThreadManager(this);
        this._threadManager.start();

        return startInvitation();
    }

    private int playerNumInput() throws IOException {
        int check = 0;
        while (true) {
            System.out.print("Please input number from 2 to " + MAXPLAYERNUM + ". : ");
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

    public void dispose() {
        System.out.println("disposed sever...");    // 4debug
        if(this._threadManager != null) {
            this._threadManager.dispose();
        }
    }
}