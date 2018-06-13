package game;

import java.util.*;
import java.security.SecureRandom;
import java.lang.ArrayIndexOutOfBoundsException;
import main.Console;
import server.ClientThread;
import game.cards.*;

public class Game {
    private List<Player> _playerList;   // 参加者リスト

    private List<Card> _cardList;   //使うカードのリスト

    private boolean _finished;    //ゲームが終了したか否か

    // private ArrayList<Round> _finishedRoundList;   //ラウンドのリスト

    private boolean _hasDuchess;    //女公爵が山札に含まれているか否か

    private boolean _hasPrince;   //王子が山札に含まれているか否か

    private boolean _hasKing;   //王が山札に含まれているか否か

    public int playerNumber() {  //現在のゲームの参加者人数を返すメソッド
        return this._playerList.size();
    }

    // public int currentRound() {
    //     return this._finishedRoundList.size() + 1;
    // }

    private boolean finished() {
        return this._finished;
    }

    public boolean hasDuchess() {
        return this._hasDuchess;
    }

    public boolean hasPrince() {
        return this._hasPrince;
    }

    public boolean hasKing() {
        return this._hasKing;
    }


    /*** コンストラクタ (5人以上の時にKingをcardListに加える) ***/
    public Game(List<Player> playerList) {
        this._playerList = playerList;
        this._finished = false;
        // this._finishedRoundList = new LinkedList<Round>();
        this._hasDuchess = false;
        this._hasPrince = false;
        this._hasKing = this._playerList.size() >= 5;
        this._cardList =  Card.generateCardList(this._hasDuchess, this._hasPrince, this._hasKing);
    }

    /*** ゲームを開始するメソッド ***/
    public void startGame() {

/*** ↓ for debug ↓ **/
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < this._playerList.size(); i++) {
            buf.append(this._playerList.get(i).name());
            if(i != (this._playerList.size() - 1)) buf.append(", ");
        }
        String plist = buf.toString();

        Console.sendMsgAll(this._playerList, "[game] Players : " + plist);

        buf.setLength(0);
        for(int i = 0; i < this._cardList.size(); i++) {
            buf.append(this._cardList.get(i).name());
            if(i != (this._cardList.size() - 1)) buf.append(", ");
        }
        plist = buf.toString();

        Console.sendMsgAll(this._playerList, "[game] Cards : " + plist);
/*** ↑ for debug ↑ ***/


//        while(!this._finished) {
            Round round = new Round(this._playerList, this._cardList, this._hasDuchess, this._hasPrince, this._hasKing);
            round.start();
//        }

/*** ↓ for debug ↓ **/
        buf.setLength(0);
        for(int i = 0; i < this._playerList.size(); i++) {
            buf.append(this._playerList.get(i).name());
            if(i != (this._playerList.size() - 1)) buf.append(", ");
        }
        plist = buf.toString();

        Console.sendMsgAll(this._playerList, "[game] Players : " + plist);

        buf.setLength(0);
        for(int i = 0; i < this._cardList.size(); i++) {
            buf.append(this._cardList.get(i).name());
            if(i != (this._cardList.size() - 1)) buf.append(", ");
        }
        plist = buf.toString();

        Console.sendMsgAll(this._playerList, "[game] Cards : " + plist);
/*** ↑ for debug ↑ ***/

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
