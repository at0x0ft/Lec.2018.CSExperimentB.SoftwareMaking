package main;

import java.io.*;
import java.net.*;
import java.lang.*;
import client.Client;
import server.Server;
import interfaces.IConnectable;

public class LoveLetter {
    private static BufferedReader _cin;
    public static String cInputLn() throws IOException {
        return _cin.readLine();
    }
    
    public static void main(String[] args) {
        _cin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to LoveLetter Game!");

        String playerName = registerPlayerName();

        System.out.println("Hi, " + playerName + "!");

        start(playerName);

        System.out.println("See you.");
    }
    
    private static String registerPlayerName() {
        while(true) {
            System.out.print("Please enter your player name : ");
            try {
                return cInputLn();
            }
            catch(IOException ioe) {
                System.out.println("IOException occured.");
                System.out.println("Please enter again...");
                continue;
            }
        }
    }

    private static void start(String playerName) {
        System.out.println("Would you wait for the other player or find thed other player?");

        IConnectable gameBase = null;
        try {
            switch(createOrFind()) {
                case "c": {
                    gameBase = new Server(playerName);
                    break;
                }
                case "f": {
                    gameBase = new Client(playerName);
                    break;
                }
                default: {
                    return;
                }
            }

            if(!gameBase.establishConnection()) {
                return;
            }

            // start game
            System.out.println("Now, let's start the game!");   // 4debug
            // System.out.println("Finished... : " + cInputLn());  // 4debug
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if(gameBase != null) {
                gameBase.dispose();
            }
        }
    }

    private static String createOrFind() {
        String check = null;
        while(true) {
            System.out.print("Enter w (wair for the other player) / f (find the other player) : ");
            try {
                check = cInputLn();
                if(check.equals("c") || check.equals("f")) {
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
}