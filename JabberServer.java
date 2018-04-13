import java.io.*;
import java.net.*;

public class JabberServer {
    public static final int PORT = 8080; // Set the Port Number.

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PORT); // Construct the socket with port number.
        System.out.println("Started: " + s);
        try {
            Socket socket = s.accept(); // Wait the connection setting requirements.
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