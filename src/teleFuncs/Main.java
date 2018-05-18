import java.io.*;
import java.net.*;
import client.Client;
import server.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to LoveLetter Game!");
        System.out.print("Please enter your player name : ");
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        try {
            String playerName = cin.readLine();
            
            System.out.println("Hi, " + playerName + "!");
            System.out.println("Could you create new gameroom or find other game rooms?");
            String check = null;
            while(true) {
                System.out.print("Enter c (create new gameroom) / f (find other game rooms) : ");
                check = cin.readLine();
                if(check.equals("c")) {
                    Server server = new Server(playerName);
                    server.waitConnection();
                    break;
                }
                else if(check.equals("f")) {
                    Client client = new Client(playerName);
                    client.connect();
                    break;
                }
                System.out.println("Wrong character! Please enter the correct ones.");
            }
        }
        catch (IOException ioe) {
            System.out.println("IOError Occured.");
        }
    }
}