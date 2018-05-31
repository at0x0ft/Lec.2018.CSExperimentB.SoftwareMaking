package client;

import java.io.*;
import java.net.*;
import server.Server;
import main.LoveLetter;
import main.GameBase;
import interfaces.IDisposable;

public class Client extends GameBase /*implements IDisposable*/ {
    private BufferedReader _exin;
    private PrintWriter _exout;
    
    private Socket _socket;
    
    private String _playerName;
    public String playerName() {
        return this._playerName;
    }

    private static final String HOSTNAME = "localhost";

    public Client(String playerName) {
        this._playerName = playerName;
    }

    public boolean establishConnection() throws IOException {
        while(true) {
            if(connect()) {
                System.out.println("Accepted!");
                System.out.println("Please wait for starting game...");

                while(true) {   // 4debug
                    if(LoveLetter.cInputLn().equals("f")) {
                        break;
                    }
                }    // 4debug
                return true;
            }

            System.out.println("Failed to establish connection...");

            switch(retryConfirmYN()) {
                case "y": {
                    continue;
                }
                case "n": {
                    return false;
                }
            }
        }
    }
    
    private String retryConfirmYN() {
        String check = null;
        while(true) {
            System.out.print("Would you retry? Enter y/n. : ");
            
            try {
                check = LoveLetter.cInputLn();
                if(check.equals("y") || check.equals("n")) {
                    return check;
                }
            }
            catch (IOException ioe) {
                System.out.println("IOError Occured.");
                System.out.println("Please enter again...");
                continue;
            }
            System.out.println("Wrong character! Please enter the correct ones.");
        }
    }
    
    private boolean connect() throws IOException, SocketException {
        this._socket = new Socket(InetAddress.getByName(HOSTNAME), Server.PORT); // Making the socket.
        this._exin = new BufferedReader(new InputStreamReader(this._socket.getInputStream())); // Set the buffer for data serving.
        this._exout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream())), true); // Set the buffer for sending.

        System.out.println("Found " + this._exin.readLine() +"'s room.");   // check the name.
        String ans = roomConfirmYN();
        switch(ans) {
            case "y": {
                this._exout.println(ans);
                this._exout.println(this.playerName());
                break;
            }
            case "n": {
                this._exout.println(ans);
                return false;
            }
        }
        
        System.out.println("Waiting gameroom master's acceptance...");

        switch(this._exin.readLine()) {
            case "y": {
                this._exout.println(this.playerName());  // register playerName to ClientThread
                break;
            }
            case "n": {
                System.out.println("Access denied...");
                return false;
            }
        }

        return true;
    }

    private String roomConfirmYN() throws IOException {
        String check = null;
        while(true) {
            System.out.print("Is it Correct? Enter y/n. : ");
            
            try {
                check = LoveLetter.cInputLn();
                if(check.equals("y") || check.equals("n")) {
                    return check;
                }
            }
            catch (IOException ioe) {
                System.out.println("IOError Occured.");
                System.out.println("Please enter again...");
                continue;
            }
            System.out.println("Wrong character! Please enter the correct ones.");
        }
    }

    public void startGame() {
        System.out.println("Now, let's start the game!");

        // create game class from here
    }

    public void dispose() throws IOException {
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