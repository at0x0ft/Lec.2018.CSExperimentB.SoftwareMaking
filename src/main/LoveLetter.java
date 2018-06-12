package main;

import java.io.*;
import java.net.*;
import java.lang.*;
import main.Console;
//import client.Client;
//import server.Server;
import main.Server;
import main.Client;

public class LoveLetter {
    public static void main(String[] args) throws IOException {

        Console.initialize();

        Console.clearScreen(0);

        Console.writeLn("[game] Welcome to LoveLetter Game!");
        Console.write("[game] Please enter your player name : ");

        String playerName = Console.readLn();
        Console.writeLn("[game] Hi, " + playerName + "!");

        try {
            start(playerName);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if(Console.isScNull()) {
                Console.dispose();
            }
            Console.writeLn("[game] See you.");
        }

        Console.clearScreen(750);

    }

    private static void start(String playerName) throws IOException {
        InetAddress ia = InetAddress.getLocalHost();
        String ip = ia.getHostAddress();       //IPアドレス
        GameBase gameBase = null;
        try {
            switch(createOrFind()) {
                case "c": {
                    //gameBase = new Server(playerName);
                    //JabberServer serv = new JabberServer(playerName);

                    int playerNum;
                    playerNum = Console.readNum(1, 6, "[game] How many players?", "[game] Input 2 ~ 6 : ");

                    try {
                        Runtime rt = Runtime.getRuntime();
                        rt.exec("java main.Server " + playerNum);
                        Client clie = new Client(playerName);
                        clie.client(ip);
                    } catch (IOException ex) {
                        System.out.println("debug: error 1");
                        ex.printStackTrace();
                    }

                    break;
                }
                case "f": {
                    //gameBase = new Client(playerName);
                    Client clie = new Client(playerName);
                    String host = "localhost";
                    System.out.println("Input host IP: ");
                    host = Console.readLn();
                    clie.client(host);
                    break;
                }
                default: {
                    return;
                }
            }
/*
            if(!gameBase.establishConnection()) {
                return;
            }
*/
            Console.clearScreen(1000);

            // start game
            //gameBase.startGame();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
/*
            if(gameBase != null) {
                gameBase.dispose();
            }
*/
        }
    }

    private static String createOrFind() {
        return Console.readAorB(
            "c",
            "f",
            "[game] Would you create new gameroom or find other game rooms?",
            "[game] Enter c (create new gameroom) / f (find other game rooms) : "
            );
    }
}