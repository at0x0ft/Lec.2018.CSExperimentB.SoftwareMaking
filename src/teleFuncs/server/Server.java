package server;

import java.io.*;
import java.net.*;

public class Server {
    private static final int _PORT = 8080; // Set the Port Number.
    public static int getPort() {
        return _PORT;
    }

    private String _playerName;
    public String getPlayerName() {
        return this._playerName;
    }

    private Socket _socket;

    public Server(String playerName) {
        this._playerName = playerName;
    }
    
    public void waitConnection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Server.getPort()); // Construct the socket with port number.
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Waiting other players...");
        try {
            this._socket = serverSocket.accept(); // Wait the connection setting requirements.
            // System.out.println("Started: " + this._socket);    // 4debug
            BufferedReader exin = new BufferedReader(new InputStreamReader(this._socket.getInputStream())); // Set the buffer for data serving.
            PrintWriter exout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream())), true); // Set the buffer for sending.
            try {
                exout.println(getPlayerName());
                if(exin.readLine().equals("y")) {
                    System.out.println("Connected from " + getPlayerName() + "!");
                    playerConfirmationLoop(cin, exout);
                }
            }
            finally {
                System.out.println("closing...");
                this._socket.close();
            }
        } 
        finally {
            serverSocket.close();
        }
    }
    
    private static void playerConfirmationLoop(BufferedReader cin, PrintWriter exout) throws IOException {
        String check = null;
        while (true) {
            System.out.println("Would you accept this player? Enter y/n : ");
            check = cin.readLine();
            if(check.equals("n")) {
                exout.println(check);
                break;
            }
            else if(check.equals("y")) {
                exout.println(check);
                // start game
                System.out.println("Let's start the game!");    // 4debug
                break;
            }

            System.out.print("Wrong character! Please enter the correct ones.");
        }
    }
}