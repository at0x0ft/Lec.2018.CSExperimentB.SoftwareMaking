public class Game{
  public static void main(String[] args){

    private LinkedList <Player> _playerList; //カードの参加者リスト

    private LinkedList <Card> _cardList;//使うカードのリスト

    private LinkedList <Round> _finishedRoundList;//ラウンドのリスト

    private boolean _finished;//ゲームが終了したか否か

    private boolean _hasDuchess;//女公爵が山札に含まれているか否か

    private boolean _hasPrince;//王子が山札に含まれているか否か

    private boolean _hasKing;//王が山札に含まれているか否か

    public Game(boolean hasDuchess. boolean hasPrince. boolean hasKing);

    public static int getPlayerNumber ();//現在のゲームの参加者人数を返すメソッド

    public static int getNowRound ();//現在のラウンドを返すメソッド

    private hasFinished ();//このゲームが終了したかどうか判定するメソッド

    private String registerName ();//セッションしたプレイヤーに名前の入力を促すメソッド

    private player createPlayer (String playerName);//名前の入力を求める

    private Round startGame ();//ゲームを開始し終了したラウンドクラスを返す

    private boolean viewLogFile ();//ゲーム終了時にゲームログファイルを出力する
  }
}
