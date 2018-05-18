package client;

import java.io.*;
import java.net.*;
import server.Server;

public class Client {
    private String _playerName;
    public String getPlayerName() {
        return this._playerName;
    }

    public Client(String playerName) {
        this._playerName = playerName;
    }

    public void connect() throws IOException {
        String hostName = "localhost";

        InetAddress addr = InetAddress.getByName(hostName); // Converting to the IP Address.
        // System.out.println("addr = " + addr);   // 4debug
        Socket socket = new Socket(addr, Server.getPort()); // Making the socket.

        try {
            // System.out.println("socket = " + socket);   // 4debug
            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in)); // Console reader.
            BufferedReader exin = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Set the buffer for data serving.
            PrintWriter exout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); // Set the buffer for sending.

            roomConfirmationLoop(cin, exin, exout);
        }
        finally {
            // System.out.println("closing...");   // 4debug
            socket.close();
        }
    }

    private static void roomConfirmationLoop(BufferedReader cin, BufferedReader exin, PrintWriter exout) throws IOException {
        System.out.println("Successfully connected to " + exin.readLine() +"'s room.");   // check the name.
        
        String check = null;
        while(true) {
            System.out.print("Is it Correct? Enter y/n. : ");
            check = cin.readLine();
            if(check.equals("n")) {
                exout.println(check);
                break;
            }
            else if(check.equals("y")) {
                exout.println(check);
                System.out.println("Waiting gameroom master's acceptance...");
                String response = exin.readLine();
                if(response.equals("y")) {
                    // start game;
                    System.out.println("Let's start the game!");    // 4debug
                }
                else if(response.equals("n")) {
                    System.out.println("Access denied...");
                }
                break;
            }
            
            System.out.print("Wrong character! Please enter the correct ones.");
        }

        exout.println("END");
    }
}