package main;

import java.io.*;
import java.util.Scanner;

public class Console {
    private static Scanner _in;
    private static BufferedReader _br;
    private static final String errMsgChar = "Wrong character! Please enter the correct ones.";
    private static final String errMsgNum = "Wrong number! Please enter the correct ones.";

    public static void initialize() {
        _in = new Scanner(System.in);
        _br = new BufferedReader(new InputStreamReader(System.in));
    }

    public static boolean isScNull() {
        return _in == null;
    }

    public static void write(String message) {
        System.out.print(message);
    }

    public static void writeLn(String message) {
        System.out.println(message);
    }

    public static void writeSplitLn(String[] message, int start, int end) {
        int i = 0;
        for(i = start; i < end; i++) {
            System.out.print(message[i]);
        }
        if(i == end - 1) {
            System.out.print("\n");
        } else {
            System.out.print(" ");
        }
    }

    public static String readSplitLn(String[] message, int start, int end) {
        StringBuilder buf = new StringBuilder();
        int i = 0;
        for(i = start; i < end; i++) {
            buf.append(message[i] + " ");
        }
        return buf.toString();
    }

    public static void newLn() {
        System.out.println();
    }

    private static String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static void clearScreen(long ms) throws IOException {
        try {
            if(ms != 0) {
                Thread.sleep(ms);
            }

            if(osName().equals("linux") || osName().equals("mac")) {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
            else if(osName().equals("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static String read() {
        return _in.next();
    }

    public static String readLn() throws IOException {
        return _br.readLine();
    }

    public static String readAorB(String answerA, String answerB, String iniMessage, String inputMessage) {
        String check = null;

        writeLn(iniMessage);
        while(true) {
            write(inputMessage);

            check = read();
            if(check.equals(answerA) || check.equals(answerB)) {
                return check;
            }

            writeLn(errMsgChar);
        }
    }

    public static boolean readTorF(String tAns, String fAns, String iniMessage, String inputMessage) {
        String check = null;

        writeLn(iniMessage);
        while(true) {
            write(inputMessage);

            check = read();
            if(check.equals(tAns)) {
                return true;
            }
            else if(check.equals(fAns)) {
                return false;
            }

            writeLn(errMsgChar);
        }
    }

    public static int readNum(int min, int max, String iniMessage, String inputMessage) {
        int check = 0;
        writeLn(iniMessage);
        while (true) {
            write(inputMessage);
            try {
                check = Integer.parseInt(read());
                if(check >= min && check <= max) {
                    return check;
                }
            }
            catch (NumberFormatException nfe) {
                System.out.println(errMsgNum);
                continue;
            }
            System.out.println(errMsgNum);
        }
    }

    public static void dispose() throws IOException {
        if(!isScNull()) {
            _in.close();
        }
    }

    public static String acceptMsg(BufferedReader in) throws IOException {
        return in.readLine();
    }

    public static void sendMsg(PrintWriter out, String msg) {
        out.println(msg);
    }

    public static void sendMsgAll(PrintWriter[] out, String msg) {
        for(int i = 0; i < out.length; i++) {
            out[i].println(msg);
        }
    }

    public static boolean readCommand(BufferedReader in, PrintWriter out, String msg) {
        String[] splitMsg = msg.split(" ");
                    switch(splitMsg[0]) {
                        case "/fin":
                            return false;
                        case "/console":
                            switch(splitMsg[1]) {
                                case "readAorB":
                                    out.println(readAorB(splitMsg[2], splitMsg[3], readSplitLn(splitMsg, 4, splitMsg.length), splitMsg[4] + " " + "Enter " + splitMsg[2] + " / " + splitMsg[3] + " : "));
                            }
                            break;
                        default:
                            Console.write("Unknown message : ");
                            Console.writeLn(msg);
                            break;
                    }
        return true;
    }
}