# 情報理工学実験B ソフトウェア制作 5-1班 設計方針･仕様書

## はじめに

ここでは大まかなルール確認と設計方針、クラス構造について説明したいと思います.

指摘等あれば遠慮なくすぐに連絡していただけると幸いです.

## 大まかなルールの確認

* ゲームで使用するカードは16枚. 具体的な内訳は以下の通り.

| カードの種類 |   強さ  |                                    効果                                   |   枚数  |
|:------:|:-----:|:-----------------------------------------------------------------------:|:-----:|
|   兵士   |   1   | プレイヤーを一人指定し、兵士以外のカードの種類を宣言. 指定されたプレイヤーの持っているカードと一致した場合、そのプレイヤーはゲームから脱落. |   5   |
|   道化   |   2   |                     指定したプレイヤーの手札を見る(他のプレイヤーには見せない).                     |   2   |
|   騎士   |   3   |                 プレイヤーを一人指定し、手札のカードを見せ合う. 強さの低いプレイヤーが脱落.                 |   2   |
|   僧侶   |   4   |                次の自分の番まで相手のカードの効果を受けない (効果の対象にはなるが無効となる).                |   2   |
|   魔術師  |   5   |        プレイヤーを一人指定し、手札のカードを捨てさせ、山札からカードを一枚引かせる (姫以外のカードの効果は発動しない).       |   2   |
|   将軍   |   6   |                         プレイヤーを一人指定し、お互いの手札を交換する.                        |   1   |
|   大臣   |   7   |                     山札からカードを引いた際、2枚の手札の合計が12以上なら脱落.                     |   1   |
|    姫   |   8   |                         このカードを捨て札にしたプレイヤーは脱落する.                         |   1   |

なお、拡張カードは以下の通り (余裕があれば実装する). 強さが同じカードと入れ替えて使う. (強さ8の他のカードは更に必要になったら追記 & 追加実装)

| カードの種類 |   強さ  |                       効果                      |   枚数  |
|:------:|:-----:|:---------------------------------------------:|:-----:|
|   女公爵  |   7   | 山札からこのカードを引いた際、2枚の手札の合計が12以上ならこのカードを必ず捨て札とする. |   1   |
|   王子   |   8   |      姫と同じ効果 (プレイヤーが女性のみなら入れるかも (オプション)).      |   1   |
|    王   |   0   |     このカードを引いたプレイヤー脱落 (5人以上でプレイする時のみ追加推奨).     |   1   |

* 勝利条件

  * ラウンド勝利条件

    自分以外が全員脱落するか、複数人が山札が無くなるまで残った際に、最後の手札見せ合い勝負に勝利すること.

  * ゲーム勝利条件

    姫以外で勝利した場合には1ポイント、姫で勝利した場合には2ポイント獲得. 3ポイントでゲーム勝利.

## 大まかなゲームの流れ

1. 16枚のカードを選定 (オプションカード入れるかどうかの判定)

1. 山札を切る

1. 一人一枚ずつカードを引く.

1. じゃんけんして、順番を決める. (サーバサイドのプレイヤーがゲームマスターの予定. 面倒ならまずはその人からセッション順に順番を回す方向で考える.)

1. 順番にカードを引いてその場でカードを一枚捨てる. (山札は実質一枚しか持てない.)

1. 捨て札にした時の効果を発動させつつ、一人になるか山札が尽きるまで繰り返す.

1. (一人以外全員脱落した場合) 残った人が勝利.

1. (山札が尽きた場合) 残った人同士で手札を見せ合う. 強さが一番強い人が勝利.

2.~8.までを1ラウンドと定義する. 誰か一人が3ポイント獲得するまで、このラウンドを続ける.

7.でも8.のパターンであっても、最後に残った人のカードが姫だった場合にはその人に2ポイント、それ以外の場合にはラウンド勝利者に1ポイントを与える.

## 設計方針

先述した通り、まずはCUIかつオプションカード無し、プレイ人数2人で実装したいと思います. これが実装できた後には、GUI化を図ります (イラスト等も用意する必要が出てくるが、これに関してはまた後ほど議論). GUI化も終わった場合には、多人数化、ゲームタイトル画面とセッティング画面の実装、それも完了した場合には、オプションカードの実装に取り掛かりたいを考えています.

以下のルールで実装できたら良いなと思います.

1. メソッドはできるだけ小分けにする

1. コメントは、以下のクラス構造から少々逸脱する場合や、説明が難しいアルゴリズムや処理分岐を実装する時に付記する

1. 困ったらググりつつすぐに相談する

## クラス構造

大きな枠組みとして、ゲームクラス、ラウンドクラス、カードクラス (後に継承して、各カードの実装に移る. ここでのクラスはあくまで抽象的なものにとどめておく.) 、プレイヤークラスの3つのクラスで構成していくことを考えています.

以下に必要なメンバ (フィールド)、メソッドを順に書いていきます.

### プレイヤークラス

#### メンバ

* private String _IPAdress : IPアドレス

* private String _name : プレイヤーの名前

* private int _points : 現在の勝点

* private boolean _protection : 僧侶の効果で守られているか、否か

* private Card _hand : 手札

#### コンストラクタ

public Player(String IPAdress, String name)

コンストラクタ引数で変数を初期化. pointsは0で初期化. protectionはfalseで初期化. handはnullで初期化.

#### メソッド

* public String getIPAdress() : IPアドレスを返すメソッド (return IPAddress;)

* public String getName() : プレイヤーの名前を返すメソッド (return name;)

* public int getPoints() : 現在の勝点を返すメソッド (return points;)

* public void incrementPoints(int winPoint) : 勝点をwinPoint点だけ増やすメソッド (points += winPoint;)

* public void setProtected() : プレイヤーの僧侶の保護効果を付与するメソッド (protection = true;).

* public void resetProtected() : プレイヤーの僧侶の保護効果を解除するメソッド (protection = false;).

* public boolean isProtected() : プレイヤーが僧侶の効果で守られているかどうかを返すメソッド (return protection;).

* public void setHand(Card card) : handメンバにcardをセットするメソッド (this.hand = card;)

* public Card getHand() : 手札のカードインスタンスを返すメソッド (return hand;)

* public Card exchangeHand(Card card) : 手札のカードインスタンスをcardに変更し、元持っていたカードを戻り値として返すメソッド (Card discard = this.Hand; setHand(card); return discard;)

### カードクラス (各カードの基底クラス)

#### メンバ

* private String _name : カードの名前

* private int _strength : カードの強さ

* (private String _effectText : カードの効果の説明文 (余裕があれば実装))

#### コンストラクタ

public Player(String name, int strength, String effectText)

コンストラクタ引数で変数を初期化.

#### メソッド

* public String getName() : カードの名前を返すメソッド (return _name;)

* public int getStrength() : カードの強さを返すメソッド (return _points;)

* (public String getEffectText() : カード効果の説明文を返すメソッド (return _effectText;))

### 各カードクラス (カードクラスを継承する)

#### コンストラクタ

public XXXX()

カードの種類の応じた名前、強さ、カード効果の説明文をsuper.(name, strength, effectText);で親クラスのコンストラクタを呼び出してインスタンスを生成する.

### ゲームクラス

#### メンバ

* private List\<Player\> _playerList : ゲーム参加者のリスト (プレイヤークラスのリスト, 2人以上7人以下? (最初の実装では2人固定))

* private List\<Card\> _cardList : 使うカードのリスト (カードクラスのリスト)

* private List\<Round\> _finishedRoundList : ラウンドのリスト

* private boolean _finished : ゲームが終了したか否か

* (private boolean _hasDuchess : 女公爵が山札に含まれているか否か (オプションカード実装時のみ作るメンバ).)

* (private boolean _hasPrince : 王子が山札に含まれているか否か (オプションカード実装時のみ作るメンバ).)

* (private boolean _hasKing : 王が山札に含まれているか否か (オプションカード実装時のみ作るメンバ).)

#### コンストラクタ

public Game(boolean hasDuchess, boolean hasPrince, boolean hasKing)

コンストラクタ引数の値でメンバを初期化. _finishedメンバはfalseで初期化. それ以外のメンバは全てインスタンスを生成しておく.

#### メソッド

* public static int getPlayerNumber() : 現在のゲーム参加者人数を返すメソッド (_playerList.size()とか使えば一瞬で実装できる (多分).)

* public static int getNowRound() : 現在のラウンド (1以上) を返すメソッド (_roundList.size() + 1 で一瞬で実装できるはず (多分).)

* private hadFinished() : このゲームが終了したかどうかを判定するメソッド (return _finished;).

* private String registerName() : セッションしたプレイヤーに名前の入力を促すメソッド. 基本的には下記のcreatePlayerの第一引数内で呼び出す.

* private Player createPlayer(String playerName) : 名前の入力を求め、プレイヤーインスタンスを生成し、戻り値として返すメソッド.

* private Round startGame() : ゲームを開始し、終了したラウンドクラスを返すメソッド

  _playerList、_deckをコピー (新しいList\<Player\>、List\<Card\>インスタンスを作る) & シャッフル (ソートの逆で、リストの順番をランダムにする. 乱数を使って実装すれば良いような気もするが、そもそも便利なライブラリがある可能性も高い.) し、新しく作成したインスタンス二つをRoundクラスのコンストラクタ引数として渡す. その後、新しく生成したRoundインスタンスでstart()メソッドを呼び出し、Roundが終了したら、Roundインスタンスを戻り値として返す.

* (private boolean viewLogFile() : ゲーム終了時にゲームログファイルを出力するファイル (デバッグに役立つのでできれば実装したいところではあるが一応オプション))

  roundListに登録されたRoundに記録されたログを順番にファイル出力するメソッド. 出力出来たか否かを戻り値として返す.

### ラウンドクラス

#### メンバ

* private List\<Player\> _playerQueue : ゲーム参加者の順番リスト

* private List\<Card\> _deck : 山札

* private List\<Card\> _discard : 捨て札

* private List\<Round\> _loserList : 脱落したプレイヤーのリスト

* (private boolean _hasDuchess : 女公爵が山札に含まれているか否か (オプションカード実装時のみ作るメンバ).)

* (private boolean _hasPrince : 王子が山札に含まれているか否か (オプションカード実装時のみ作るメンバ).)

* (private boolean _hasKing : 王が山札に含まれているか否か (オプションカード実装時のみ作るメンバ).)

#### コンストラクタ

public Round(List\<Player\> playerQueue, List\<Card\> deck, boolean hasDuchess, boolean hasPrince, boolean hasKing)

コンストラクタ引数の値でメンバを初期化. それ以外のメンバは全てインスタンスを生成しておく.

#### メソッド

* public List\<Player\> start() : LoveLetterを1ラウンド開始するメソッド.

  以下の内容をwhile(true)ループ内で繰り返すのみ. (一枚目手札引くときのことを考えていない!)

   後述するturn()メソッドを、まず最初に呼び出す. このループの最後に、finished()メソッドを呼び出し、このメソッドの戻り値で示される条件に合致した場合には、以下に示すwinJudge()メソッドを呼び出し、break;によりループを抜け、playerQueueを戻り値として返す. そうでなければループにより最初に戻る.

* private void turn() : 一人のプレイヤーがカードを引き、その効果を発揮させるメソッド

  まず、playerQueueの先頭からplayerを取り出し、このプレイヤーをturnPlayer等の名前のPlayerクラスで保持しておく. turnPlayer.resetProtection()メソッドを実行する.

  次に、deckの先頭からカードを取り出し、このカードをdrawCard等の名前のCardクラス変数で保持しておく.
  
  checkKing(drawCard)がtrueであれば、後述するlose(turnPlayer)メソッドを実行し、throwCard(drawCard)メソッドを実行してturn()メソッドを終了(return;)する.
  
  そうでなければ次に、checkDuchessMinister(drawCard)を実行し、この戻り値がtrueであれば、今度は条件:"turnPlayer.getHand().getStrength() >= 5"を判定し、trueであればさらに、条件:"drawCard.getName() == "Minister""を判定し、これがtrueであれば、lose(turnPlayer)メソッドの後、throwCard(drawcard)メソッドを呼び出し、このメソッドを終了する. 条件:"drawCard.getName() == "Duchess""であれば、turnAround(turnPlayer)メソッドを実行し、throwCard(drawCard)メソッドを実行してこのメソッドを終了する.

  上記以外の条件であれば、後述するselectCard(turnPlayer, drawCard)メソッドを呼び出し、戻り値のカードをinvokeCard変数として記録する. このすぐ後に、throwCard(invokeCard)メソッドを実行する.

  次に、後述するcheckPrincessPrince(invokeCard)メソッドを実行し、戻り値がtrueであったならば、lose(turnPlayer)メソッドを呼び出し、turn()メソッドを終了する.

  次に、後述するcheckMonk(invokeCard)メソッドを実行し、戻り値がtrueであったならば、turnPlayer.setProtection()メソッドを実行し、turnAround(turnPlayer)メソッドを実行し、turn()メソッドを終了する.

  それ以外のカードであれば、execute(turnPlayer, invokeCard)メソッドを実行し、turn()メソッドを終了する.

* private void turnAround(Player player) : playerをplayerQueueの末尾に追加するメソッド (playerQueue.add(player);)

* private void throwCard(Card card) : cardを捨て札の一番上に追加するメソッド (discard.add(0, card);)

* private void lose(Player loser) : 負けプレイヤーの敗戦処理を行うメソッド

  throwCard(loser.getHand())を実行した後、loser.setHand(null), loser.resetProtection()を実行し、loserList.add(loser)を実行する.

* private Card selectCard(Card drawCard) : 山札から引いたカードを手札に残すか否かを確認するメソッド (実装の詳細は後ほど追記する). <!-- 後で考える. -->

  プレイヤーにカードの選択を促し、drawCardを選んだ場合には、そのままreturn drawcard;をとし、そうでなければ、return exchange(drawCard);とする.

* private boolean checkKing(Card card) : cardが王であるかどうかをチェックするメソッド (return hasKing && card.getName() == "King";)

* private boolean checkDuchessMinister(Card card) : cardが女公爵や大臣であるかどうかをチェックするメソッド (return (hasDuchess && drawCard.getName() == "Duchess") || drawCard.getName() == "Minister";)

* private boolean checkMonk(Card card) : cardが僧侶であるかどうかをチェックするメソッド (return invokeCard.getName() == "Monk";)

* private boolean checkPrincessPrince(Card card) : cardが姫や王子であるかをチェックするメソッド (return card.getName() == "Princess" || (hasPrince && card.getName() == "Prince");)

* private void execute(Card invokeCard) : invokeCardに書かれた効果(兵士、道化、騎士、魔術師、将軍の効果のみ)を実行するメソッド (後で考える.) <!-- 後で考える. -->

* private boolean finished() : ゲームが終了したか否かを判定するメソッド (return playerQueue.size() == 1 || deck.size() == 1;)

* private void winJudge() : 勝者を決定し、勝ち点を与えるメソッド

  もし、"playerQueue.size() != 1"であれば、playerQueueを手札のカードの強さが昇順となるようにソートする. 最大値人数が複数人、または、一人ではあったが、手札カードの強さが7以下の場合には、winnerList\<Player\>に勝ち点1を与える. 勝者がたった一人で、その手札が姫だった場合には、勝ち点2を与える. (勝ち点最大値判定時に、手札のカードは全てdiscardに捨てていき、resetProtection()とsetHand(null)としながら進めていく. 最後に、loserListとPlayerQueueをloserListから順番に並ぶようにマージし、playerQueueとして保存する.)