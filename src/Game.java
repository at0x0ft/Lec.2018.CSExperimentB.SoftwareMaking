import java.util.*;

public class Game {
    private LinkedList<Player> _playerList;   //カードの参加者リスト

    private LinkedList<Card> _cardList;   //使うカードのリスト

    private LinkedList<Round> _finishedRoundList;   //ラウンドのリスト

    private boolean _finished;    //ゲームが終了したか否か

    private boolean _hasDuchess;    //女公爵が山札に含まれているか否か

    private boolean _hasPrince;   //王子が山札に含まれているか否か

    private boolean _hasKing;   //王が山札に含まれているか否か

    public Game(boolean hasDuchess. boolean hasPrince. boolean hasKing) {
		this._playerList = new LinkedList<Player>();
		this._cardList = new LinkedList<Card>();
		this._finishedRoundList = new LinkedList<Round>();
		this._finished = false;
		this._hasDuchess = hasDuchess;
		this._hasPrince = hasPrince;
		this._hasKing = hasKing;
    }

    //現在のゲームの参加者人数を返すメソッド
    public static int getPlayerNumber() {
        return this._playerList.size();
    }

    //現在のラウンドを返すメソッド
    public static int getNowRound() {
        return this._finishedRoundList.size() + 1;
    }

    //このゲームが終了したかどうか判定するメソッド
    private hasFinished() {
        return this._finished;
    }

    //セッションしたプレイヤーに名前の入力を促すメソッド
    private String registerName() {
        // セッションしたプレイヤーに名前の入力を動かす.
        // player.setName(playerName);
    }

    //名前の入力を求める
    private Player createPlayer(String playerName) {
        // return new Player(playerName);
    }

    //ゲームを開始し終了したラウンドクラスを返す
    private Round startGame() {
        // 
    }

    //ゲーム終了時にゲームログファイルを出力する
    private boolean viewLogFile() {
        // ログファイルの出力 (オプション)
    }
}
