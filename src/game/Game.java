import java.util.*;

public class Game {
    private Player[] _players;   //カードの参加者リスト
    //現在のゲームの参加者人数を返すメソッド
    public static int playerNumber() {
        return this._players.size();
    }

    private Card[] _cardList;   //使うカードのリスト
    private Card[] generateCardList(boolean hasDuchess, boolean hasPrince, boolean hasKing) {
        return new Card[] { new Soldier(), new Soldier(), new Soldier(), new Soldier(), new Soldier() };
    }

    private ArrayList<Round> _finishedRoundList;   //ラウンドのリスト
    public int currentRound() {
        return this._finishedRoundList.size() + 1;
    }

    private boolean _finished;    //ゲームが終了したか否か
    private boolean finished() {
        return this._finished;
    }

    // private boolean _hasDuchess;    //女公爵が山札に含まれているか否か

    // private boolean _hasPrince;   //王子が山札に含まれているか否か

    private boolean _hasKing;   //王が山札に含まれているか否か

    // 簡略化されたコンストラクタ (5人以上の時にKingをcardListに加える)
    public Game(Player[] players) {
        this._playerList = players;
        if(players.length >= 5) {
            this._cardList = generateCardList(false, false, true);
        }
        else {
            this._cardList = new LinkedList<Card>();
        }
        this._finishedRoundList = new LinkedList<Round>();
        this._finished = false;
        this._hasDuchess = hasDuchess;
        this._hasPrince = hasPrince;
        this._hasKing = hasKing;
    }

    // public Game(boolean hasDuchess, boolean hasPrince, boolean hasKing) {
    //     this._playerList = new LinkedList<Player>();        
    //     this._cardList = new LinkedList<Card>();
    //     this._finishedRoundList = new LinkedList<Round>();
    //     this._finished = false;
    //     this._hasDuchess = hasDuchess;
    //     this._hasPrince = hasPrince;
    //     this._hasKing = hasKing;
    // }

    // shuffle操作の定義. (1 ~ rangeまでの値のランダムな番号の並びをint型配列として返す.)
    public static int[] generateRandomNumOrder(int[] result, int range) {
        if(result == null || result.length != range) {
            result = new int[range];
        }

        for(int i = 0; i < range; i++) {
            result[i] = i + 1;
        }

        SecureRandom rdm = new SecureRandom();
        for(int i = result.length - 1; i > 0; i--) {
            int j = Math.floor(rdm.nextFloat() * (i + 1));
            int tmp = result[j];
            result[j] = result[i];
            result[i] = tmp;
        }

        return result;
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
