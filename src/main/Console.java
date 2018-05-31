package main;

import java.io.*;
import java.util.Scanner;

public class Console {
    private static Scanner _in;
    private static final String errMsgChar = "Wrong character! Please enter the correct ones.";
    private static final String errMsgNum = "Wrong number! Please enter the correct ones.";

    public static void initialize() {
        _in = new Scanner(System.in);
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

    public static String readLn() {
        return _in.next();
    }

    public static String readAorB(String answerA, String answerB, String iniMessage, String inputMessage) {
        String check = null;

        writeLn(iniMessage);
        while(true) {
            write(inputMessage);

            check = readLn();
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

            check = readLn();
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
                check = Integer.parseInt(readLn());
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
}