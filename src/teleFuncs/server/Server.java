package server;

import java.io.*;
import java.net.*;
import java.lang.InterruptedException;
import java.util.function.Predicate;
import main.LoveLetter;
import interfaces.IConnectable;

public class Server implements IConnectable {
    public static final int PORT = 8080; // Set the Port Number.
    private String _masterPlayerName;
    public String masterPlayerName() {
        return this._masterPlayerName;
    }
    
    private ServerSocket _serverSocket;
    private Socket _socket;

    private boolean startInvitation() throws IOException {
        System.out.println("Waiting for other player(s)...");

        Server server = this;
        ThreadManager tm = this._threadManager;
        
        try {
            this._connectionWaitingThread = new Thread(new Runnable() {
                public void run() {
                    while(true) {
                        try {
                            Socket s = ().accept();
                            tm.addCandidate(new ClientThread(, server));
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
        try {
            this._serverSocket = new ServerSocket(Server.PORT);
            this._socket = this._serverSocket.accept();
        }
        finally {
        }
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
        if(this._socket != null) {
            this._socket.close();
        }
        if(this._serverSocket != null) {
            this._serverSocket.close();
        }
    }
}