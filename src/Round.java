import java.io.*;
import java.util.*;

public class Round {

    private LinkedList<Player> _playerQueue;   // ゲーム参加者の順番リスト

    private LinkedList<Card> _deck;    // 山札

    private LinkedList<Card> _discard; // 捨て札

    private LinkedList<Round> _loserList;  // 脱落したプレイヤーのリスト

    private boolean _hasDuchess; // 女公爵が山札に含まれているか否か

    private boolean _hasPrince;  // 王子が山札に含まれているか否か

    private boolean _hasKing;    // 王が山札に含まれているか否か

    public Round(LinkedList<Player> playerQueue, LinkedList<Card> deck, boolean hasDuchess, boolean hasPrince, boolean hasKing) {
        // 初期化処理
        this._playerQueue = playerQueue;
        this._deck = deck;
        this._discard = new LinkedList<Card>();
        this._loserList = new LinkedList<Round>();
        this._hasDuchess = hasDuchess;
        this._hasPrince = hasPrince;
        this._hasKing = hasKing;
    }

    // LoveLetterのゲームの1ラウンドを開始するメソッド.
    public LinkedList<Player> start() {
        while(true) {
            turn();
            if(finished()) {
                winJudge();
                break;
            }
        }
    }

    // 一人のプレイヤーがカードを引き、その効果を発揮させるメソッド
    private void turn() {
        Player turnPlayer = this._playerQueue.pop();
        turnPlayer.resetProtection();

        Card drawCard = this._deck.pop();
        if(checkKing(drawCard)) {
            lose(turnPlayer);
            throwCard(drawCard);
            return;
        }
        else if(checkDuchessMinister(drawCard)) {
            if(turnPlayer.getHand().getStrength() >= 5) {
                if(drawCard == "Minister") {
                    lose(turnPlayer);
                    throwCard(drawCard);
                    return;
                }
                else if(drawCard == "Duchess") {
                    turnAround(turnPlayer);
                    throwCard(drawCard);
                    return;
                }

                Card invokeCard = selectCard(turnPlayer, drawCard);
                throwCard(invokeCard);

                if(checkPrincessPrince(invokeCard)) {
                    lose(turnPlayer);
                    return;
                }
                else if(checkMonk(invokeCard)) {
                    turnPlayer.setProtection();
                    turnAround(turnPlayer);
                    return;
                }

                execute(turnPlayer, invokeCard);
                return;
            }
        }
    }

    // playerをplayerQueueの末尾に追加するメソッド
    private void turnAround(Player player) {
        _playerQueue(player);
    }

    // cardを捨て札の一番上に追加するメソッド
    private void throwCard(Card card) {
        this._discard.add(0, card);
    }

    // 負けプレイヤーの敗戦処理を行うメソッド
    private void lose(Player loser) {
        throwCard(loser.getHand());
        loser.setHand(null);
        loser.resetProtection();
        this._loserList.add(loser);
    }

    // 山札から引いたカードを手札に残すか否かを確認するメソッド (詳細は後ほど)
    private Card selectCard(Player selectPlayer, Card drawCard) {
        // 選択を促すウィンドウの表示
        // return drawCard;
        // return selectPlayer.exchange(draw);
    }

    // cardが王であるかどうかをチェックするメソッド
    private boolean checkKing(Card card) {
        return this._hasKing && card.getName() == "King";
    }

    // cardが女公爵や大臣であるかどうかをチェックするメソッド
    private boolean checkDuchessMinister(Card card) {
        return (this._hasDuchess && card.getName() == "Duchess") || card.getName() == "Minister";
    }

    // cardが僧侶であるかどうかをチェックするメソッド
    private boolean checkMonk(Card card) {
        return card.getName() == "Monk";
    }

    // cardが姫や王子であるかをチェックするメソッド
    private boolean checkPrincessPrince(Card card) {
        return card.getName() == "Princess" || (this._hasPrince && card.getName() == "Prince");
    }

    // invokeCardに書かれた効果 (兵士、道化、騎士、魔術師、将軍の効果のみ) を実行するメソッド
    private void execute(Card invokeCard) {
        // 後で考える.
    }

    // ゲームが終了したか否かを判定するメソッド
    private boolean finished() {
        return _playerQueue.size() == 1 || _deck.size() == 1;
    }
昇順
    // 勝者を決定し、勝ち点を与えるメソッド
    private void winJudge() {
        if(_playerQueue.size() != 1) {
            sort(_playerQueue, /* 降順にソート */);
            // 勝者の数が複数人なら引き分け

            
        }
        
        Player winPlayer = this._playerQueue[0];
        if(winPlayer.getHand().getStrength() <= 7) {
            // 面倒なので後で実装.
        }
    }
}