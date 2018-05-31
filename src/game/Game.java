package game;

import java.util.*;
import java.security.SecureRandom;
import java.lang.ArrayIndexOutOfBoundsException;
import main.Console;
import server.ClientThread;
import game.cards.*;

public class Game {
    private Player[] _players;   // 参加者リスト
    public int playerNumber() {  //現在のゲームの参加者人数を返すメソッド
        return this._players.length;
    }

    private Card[] _cardList;   //使うカードのリスト

    // private ArrayList<Round> _finishedRoundList;   //ラウンドのリスト
    // public int currentRound() {
    //     return this._finishedRoundList.size() + 1;
    // }

    private boolean _finished;    //ゲームが終了したか否か
    private boolean finished() {
        return this._finished;
    }

    // private boolean _hasDuchess;    //女公爵が山札に含まれているか否か

    // private boolean _hasPrince;   //王子が山札に含まれているか否か

    private boolean _hasKing;   //王が山札に含まれているか否か

    // 簡略化されたコンストラクタ (5人以上の時にKingをcardListに加える)
    public Game(String masterName, ClientThread[] clientPlayers) {
        this._players = new Player[clientPlayers.length + 1];
        this._hasKing = this._players.length >= 5;
        this._cardList =  Card.generateCardList(false, false, this._hasKing);

        // create Player Classes
        try {
            int i = 0;
            for (i = 0; i < this._players.length - 1; i++) {
                this._players[i] = new ClientPlayer(clientPlayers[i]);
            }
            this._players[i] = new MasterPlayer(masterName);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            Console.writeLn("PlayerList access error occured in Player generation step.");
        }

        // this._finishedRoundList = new LinkedList<Round>();
        this._finished = false;

        int[] test = null;
        test = generateRandomNumOrder(test, 5);   // 4debug
        for (int i = 0; i < test.length; i++) {
            System.err.println("Random Num Generation test : " + test[i]);
        }   // 4debug
    }

    // shuffle操作の定義. (0 ~ range - 1までの値のランダムな番号の並びをint型配列として返す.)
    public static int[] generateRandomNumOrder(int[] result, int range) {
        if(result == null || result.length != range) {
            result = new int[range];
        }

        for(int i = 0; i < range; i++) {
            result[i] = i;
        }

        SecureRandom rdm = new SecureRandom();
        for(int i = result.length - 1; i > 0; i--) {
            int j = (int)(rdm.nextFloat() * (i + 1));   // remark
            int tmp = result[j];
            result[j] = result[i];
            result[i] = tmp;
        }

        return result;
    }

    //ゲームを開始し終了したラウンドクラスを返す
    // private Round startGame() {
    //     // 
    //     return null;
    // }

    //ゲーム終了時にゲームログファイルを出力する (オプション)
    // private boolean viewLogFile() {
    //     return false;
    // }
}
