import java.io.*;
import java.net.*;

public class JabberServerMult {
    private static final int PORT = 8080; // Set the Port Number.
    private ArrayList<ClientThread> clientThreads;
    
    public void connect() throws IOException {
        System.out.println("Started: " + s);
        clientThreads = new ArrayList<ClientThread>();  // Threads.
        ServerSocket serverSocket = null;
        Socket socket = null;
        
        try {
            serverSocket = new ServerSocket(PORT); // Construct the socket with port number.
            System.out.println("Jabber Server Multi is now built.");    // 4debug
            
            while(true) {
                socket = serverSocket.accept(); // Wait the connection setting requirements.
                clientThreads.add(new ClientThread(socket));
            }
            playerThreads.get(0).start();
            playerThreads.add(new Thread(s.accept()));
            try {
                System.out.println("Connection accepted: " + socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Set the buffer for data serving.
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
                        true); // Set the buffer for sending.

                while (true) {
                    String str = in.readLine(); // Serving data.
                    if (str.equals("END"))
                        break;

                    System.out.println("Echoing : ");
                    out.println(str); // Sending data.
                }
            } finally {
                System.out.println("closing...");
                socket.close();
            }
        } finally {
            s.close();
        }
    }
}

public class ClientThread extends Thread {
    private Socket _s;
    
    private String _playerName;
    public void setPlayerName(String playerName) {
        this._playerName = playerName;
    }
    public String getPlayerName() {
        return this._playerName;
    }

    public ClientThread(Socket s) {
        this._s = s;
        System.out.println("Accepted : " + s.getRemoteSocketAddress()); // 4debug
    }

    try {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String line;
        while ( (line = in.readLine()) != null ) {
            System.out.println(socket.getRemoteSocketAddress() + " 受信: " + line);
            out.println(line);
            System.out.println(socket.getRemoteSocketAddress() + " 送信: " + line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if (socket != null) {
            socket.close();
        }
      } catch (IOException e) {}
      System.out.println("切断されました " + socket.getRemoteSocketAddress());
    }
  }
}