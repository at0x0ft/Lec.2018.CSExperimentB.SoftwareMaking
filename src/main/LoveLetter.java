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

        Console.writeLn(Console.red + "[LoveLetter]" + Console.cyan + " Welcome to LoveLetter Game!" + Console.reset);
        Console.write(Console.red + "[LoveLetter]" + Console.yellow + " Enter your player name : " + Console.reset);

        String playerName = Console.readLn();
        Console.writeLn(Console.red + "[LoveLetter]" + Console.cyan + " Hi, " + Console.green + playerName + Console.cyan + "!" + Console.reset);

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
            Console.writeLn(Console.red + "[LoveLetter]" + Console.cyan + " See you." + Console.reset);
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
                    playerNum = Console.readNum(
                        1,
                        6,
                        Console.red + "[LoveLetter]" + Console.cyan + " How many players?" + Console.reset,
                        Console.red + "[LoveLetter]" + Console.yellow + " Input 2 ~ 6 : " + Console.reset
                        );

                    try {
                        Runtime rt = Runtime.getRuntime();
                        rt.exec("java main.Server " + playerNum);
                        Client clie = new Client(playerName);
                        clie.client(ip);
                    } catch (IOException ex) {
                        System.out.println(Console.magenta + "debug: error 1" + Console.reset);
                        ex.printStackTrace();
                    }

                    break;
                }
                case "f": {
                    //gameBase = new Client(playerName);
                    Client clie = new Client(playerName);
                    String host = "localhost";
                    Console.write(Console.red + "[LoveLetter]" + Console.yellow + " Input host IP : " + Console.reset);
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
            Console.red + "[LoveLetter]" + Console.cyan + " Create new gameroom or find other game rooms?" + Console.reset,
            Console.red + "[LoveLetter]" + Console.yellow + " Enter c (create) / f (find) : " + Console.reset
            );
    }
}