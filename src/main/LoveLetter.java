package main;

import java.io.*;
import java.net.*;
import java.lang.*;
import main.Console;
import client.Client;
import server.Server;

public class LoveLetter {
    public static void main(String[] args) throws IOException {
        Console.initialize();

        Console.clearScreen(0);

        Console.writeLn("Welcome to LoveLetter Game!");
        Console.write("Please enter your player name : ");
        
        
        String playerName = Console.readLn();
        Console.writeLn("Hi, " + playerName + "!");

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
            Console.writeLn("See you.");
        }

        Console.clearScreen(750);
    }

    private static void start(String playerName) throws IOException {
        GameBase gameBase = null;
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

            Console.clearScreen(1000);

            // start game
            gameBase.startGame();
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
        return Console.readAorB(
            "c",
            "f",
            "Would you create new gameroom or find other game rooms?",
            "Enter c (create new gameroom) / f (find other game rooms) : "
            );
    }
}