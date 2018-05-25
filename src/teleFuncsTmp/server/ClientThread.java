package server;

import java.io.*;
import java.net.*;
import main.LoveLetter;

public class ClientThread extends ManageableThread {
    private Socket _socket;
    private Server _server;
    private BufferedReader _exin;
    private PrintWriter _exout;
    private String _playerName;
    public String playerName() {
        return this._playerName;
    }

    public boolean isInProcess() {
        return super.isInProcess();
    }
    public void isInProcess(boolean value) {
        super.isInProcess(value);
    }

    public ClientThread(Socket socket, Server server) {
        super();
        this._socket = socket;
        this._server = server;
        try {
            this._exin = new BufferedReader(new InputStreamReader(this._socket.getInputStream())); // Set the buffer for data serving.
            this._exout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this._socket.getOutputStream())), true); // Set the buffer for sending.
        }
        catch(IOException ioe) {
            dispose();
        }
    }

    @Override
    public void run() {
        try {
            this._exout.println(this._server.masterPlayerName());
    
            String response = this._exin.readLine();
            switch(response) {
                case "y": {
                    break;
                }
                case "n": {
                    return;
                }
            }
    
            this._playerName = this._exin.readLine();
            if(!participantsCheck(playerName())) {
                // Access permission denied...
                this._exout.println("n");
                return;
            }

            this._exout.println("y");

            // notifying register finished.
            super.isInProcess(false);   // 4debug

            System.out.println(this._playerName + " is now taking part in!");

            // System.out.println("isActiveflg : " + super.isActive() + ", isInProcess : " + super.isInProcess()); // 4debug

            // wait and start the game
            while(true);    // 4debug
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            dispose();
        }
    }

    private synchronized static boolean participantsCheck(String playerName) throws IOException {
        System.out.println("Connected from " + playerName + "!");
        return acceptYN();
    }

    private synchronized static boolean acceptYN() throws IOException {
        String check = null;
        while (true) {
            System.out.println("Would you accept this player? Enter y/n");
            check = LoveLetter.cInputLn();
            if(check.equals("y")) {
                return true;
            }
            else if(check.equals("n")) {
                return false;
            }
            System.out.println("Wrong character! Please enter the correct ones.");
        }
    }

    @Override
    public void dispose() {
        System.out.println("disposed ct...");    // 4debug
        super.dispose();
        if(this._socket != null) {
            try {
                this._socket.close();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
            finally {
            }
        }
    }
}