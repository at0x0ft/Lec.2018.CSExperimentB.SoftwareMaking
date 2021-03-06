package client;

import java.io.*;
import java.net.*;
import server.Server;
import main.Console;
import main.GameBase;
import interfaces.IDisposable;
import model.Message;

public class Client extends GameBase {
    private BufferedReader _exin;
    private PrintWriter _exout;
    
    private Socket _socket;
    
    private String _playerName;
    public String playerName() {
        return this._playerName;
    }

    private static final String HOSTNAME = "10.24.92.187";

    public Client(String playerName) {
        this._playerName = playerName;
    }

    public boolean establishConnection() throws IOException {
        while(true) {
            if(connect()) {
                Console.writeLn("Accepted!");
                Console.writeLn("Please wait for starting game...");
                return true;
            }

            return false;
        }
    }

    private boolean connect() throws IOException {
        while(true) {
            try {
                this._socket = new Socket(InetAddress.getByName(hostnameInput()), portNumInput()); // Making the socket.
                break;
            }
            catch(UnknownHostException uhe) {
                if(retryConfirmYN().equals("n")) {
                    return false;
                }
                else {
                    continue;
                }
            }
            catch (ConnectException ce) {
                if(retryConfirmYN().equals("n")) {
                    return false;
                }
                else {
                    continue;
                }
            }
        }
        this._exin = new BufferedReader(new InputStreamReader(this._socket.getInputStream())); // Set the buffer for data serving.
        this._exout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream())), true); // Set the buffer for sending.
        
        String ans = roomConfirmYN("Found " + this._exin.readLine() +"'s room.");
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
        
        Console.writeLn("Waiting gameroom master's acceptance...");
        
        if(this._exin.readLine().equals("n")) {
            Console.writeLn("Access denied...");
            return false;
        }

        return true;
    }

    private String retryConfirmYN() {
        return Console.readAorB(
            "y",
            "n",
            "Cannot find any server...",
            "Would you retry? Enter y/n. : "
            );
    }

    public String hostnameInput() {
        Console.write("Please input server's IPAddress : ");
        return Console.readLn();
    }

    public int portNumInput() {
        return Console.readNum(
            Server.MINPORTNUM,
            Server.MAXPORTNUM,
            "",
            "Please input the server PORT ("+ Server.MINPORTNUM + " <= PORT <= " + Server.MAXPORTNUM + ") : "
            );
    }

    private String roomConfirmYN(String iniMessage) throws IOException {
        return Console.readAorB(
            "y",
            "n",
            iniMessage,
            "Is it Correct? Enter y/n. : "
        );
    }

    public void startGame() throws IOException {
        Console.writeLn("Now, let's start the game!");

        while(true) {
            // receive and deserialize the message
            boolean continueFlg = receiveMessage(this._exin.readLine());
            
            if(!continueFlg) {
                break;
            }
        }
    }

    private boolean receiveMessage(String inMsg) throws IOException {
        System.err.println("inMsg (dbg) (in receiveMessage) : " + inMsg);
        String response = Message.deserialize(inMsg);
        if(response != null) {
            this._exout.println(response);
        }
        if(response == "GAME END" /* Game set flag here */) {
            return false;
        }

        return true;
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