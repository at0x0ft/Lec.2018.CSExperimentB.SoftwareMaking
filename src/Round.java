import java.io.*;
import java.net.*;

public class Round {

    private List<Player> _playerQueue;   // ゲーム参加者の順番リスト

    private List<Card> _deck;    // 山札

    private List<Card> _discard; // 捨て札

    private List<Round> _loserList;  // 脱落したプレイヤーのリスト

    private boolean _hasDuchess; // 女公爵が山札に含まれているか否か

    private boolean _hasPrince;  // 王子が山札に含まれているか否か

    private boolean _hasKing;    // 王が山札に含まれているか否か

    public Round(List<Player> playerQueue, List<Card> deck, boolean hasDuchess, boolean hasPrince, boolean hasKing) {
        // 初期化処理
        this.playerQueue 
    }

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