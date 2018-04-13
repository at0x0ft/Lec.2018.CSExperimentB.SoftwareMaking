import java.io.*;
import java.net.*;

public class JabberClient {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        if (args.length != 1) {
            System.err.println("Usage: JabberClient MachineName");
            System.err.println("Connect as localhost...");
        } else {
            hostName = args[0];
        }

        InetAddress addr = InetAddress.getByName(hostName); // Converting to the IP Address.
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, JabberServer.PORT); // Making the socket.

        try {
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Set the buffer for data serving.
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
                    true); // Set the buffer for sending.

            for (int i = 0; i < 10; i++) {
                out.println("howdy " + i); // Sending data.
                String str = in.readLine(); // Serving data.
                System.out.println(str);
            }
            out.println("END");
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}