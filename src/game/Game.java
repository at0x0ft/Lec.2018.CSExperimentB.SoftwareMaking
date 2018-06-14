package game;

import java.util.*;
import java.util.ArrayList;
import java.security.SecureRandom;
import java.lang.ArrayIndexOutOfBoundsException;
import java.io.IOException;
import main.Console;
import server.Server;
import server.ClientThread;
import server.StateManager;
import game.cards.*;

public class Game {
    private StateManager _stateManager;

    public static final int MINPLAYERNUM = 2;
    public static final int MAXPLAYERNUM = 6;

    private Player[] _playersList;   // 参加者リスト Master is last
    public Player player(int i) {
        return this._playersList[i];
    }
    public int numOfPlayers() {  // ゲームの参加人数を返すメソッド
        return this._playersList.length;
    }

    private Card[] _cardList;   //使うカードのリスト
    public Card card(int i) {
        return this._cardList[i];
    }
    public int numOfCards() {
        return this._cardList.length;
    }

    public static final int NEEDWINPOINT = 3;

    private ArrayList<Round> _finishedRoundList;   //ラウンドのリスト
    public int currentRound() {
        return this._finishedRoundList.size() + 1;
    }

    private boolean _finished;    //ゲームが終了したか否か
    private boolean hasFinished() {
        return this._finished;
    }
    public void endGame() {
        this._finished = true;
        this._stateManager.proceedState();
        this._stateManager.restartAllWaitingThreads();
    }

    private boolean _hasDuchess;    //女公爵が山札に含まれているか否か
    public boolean hasDuchess() {
        return this._hasDuchess;
    }
    private boolean _hasPrince;   //王子が山札に含まれているか否か
    public boolean hasPrince() {
        return this._hasPrince;
    }
    private boolean _hasKing;   //王が山札に含まれているか否か
    public boolean hasKing() {
        return this._hasKing;
    }

    public Game(String masterName, StateManager sm) {
        this._stateManager = sm;

        // specialized aria start
        this._hasDuchess = false;
        this._hasPrince = false;
        // specialized aria ended

        this._playersList = new Player[this._stateManager.playerNum()];
        this._hasKing = numOfPlayers() >= 5;
        this._cardList =  Card.generateCardList(this);

        try {
            int i = 0;
            for (i = 0; i < numOfPlayers() - 1; i++) {
                this._playersList[i] = new ClientPlayer(this._stateManager.clientThread(i), i);
            }
            this._playersList[i] = new MasterPlayer(masterName, i);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            Console.writeLn("PlayerList access error occured in Player generation step.");
        }

        this._finishedRoundList = new ArrayList<Round>();
        this._finished = false;
    }

    // shuffle操作の定義. (0 ~ range - 1までの値のランダムな番号の並びをint型配列として返す.)
    public static int[] generateRandomNumOrder(int range) {
        int[] result = new int[range];
        
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
    public void start() throws IOException {
        // Start the game.
        do {
            Round round = new Round(this);
            this._finished = round.start();
            this._finishedRoundList.add(round);
        } while(!hasFinished());
    }

    //ゲーム終了時にゲームログファイルを出力する (オプション)
    // private boolean viewLogFile() {
    //     return false;
    // }
}
