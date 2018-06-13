package game;

import java.io.*;
import java.util.*;
import main.Console;
import game.cards.*;

public class Round {

    private List<Player> _playerList;   // ゲーム参加者の順番リスト

    private int _alivePlayer; // 生き残っているプレイヤーの数

    private List<Card> _deck;    // 山札

    private List<Card> _discard; // 捨て札

    private boolean _hasDuchess; // 女公爵が山札に含まれているか否か

    private boolean _hasPrince;  // 王子が山札に含まれているか否か

    private boolean _hasKing;    // 王が山札に含まれているか否か

    public Round(List<Player> playerList, List<Card> deck, boolean hasDuchess, boolean hasPrince, boolean hasKing) {
        this._playerList = playerList;
        this._deck = deck;
        this._discard = new ArrayList<Card>();
        this._hasDuchess = hasDuchess;
        this._hasPrince = hasPrince;
        this._hasKing = hasKing;
    }

    // LoveLetterのゲームの1ラウンドを開始するメソッド.
    public void start() {
        shufflePlayer();
        shuffleCard();

/*** ↓ for debug ↓ **/
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < this._playerList.size(); i++) {
            buf.append(this._playerList.get(i).name());
            if(i != (this._playerList.size() - 1)) buf.append(", ");
        }
        String plist = buf.toString();

        Console.sendMsgAll(this._playerList, "[round] Players : " + plist);

        buf.setLength(0);
        for(int i = 0; i < this._deck.size(); i++) {
            buf.append(this._deck.get(i).name());
            if(i != (this._deck.size() - 1)) buf.append(", ");
        }
        plist = buf.toString();

        Console.sendMsgAll(this._playerList, "[round] Cards : " + plist);
/*** ↑ for debug ↑ ***/

        boolean endFlag = true;
        while(endFlag) {
            for(int i = 0; i < this._playerList.size(); i++) {
                turn(i);
                if(finished()) {
                    winJudge();
                    endFlag = false;
                    break;
                }
            }
        }
    }

    // 一人のプレイヤーがカードを引き、その効果を発揮させるメソッド
    private void turn(int n) {
        Player turnPlayer = this._playerList.get(n);
        Console.sendMsgAll(this._playerList, "[round] " + turnPlayer.name() + "'s turn.");
        turnPlayer.clearProtected();

        Card drawCard = this._deck.remove(0);
        //引いたプレイヤーに通知 "引いたカードの名前、強さ、説明"
        Console.sendMsg(turnPlayer.out(), "[round] You drew a card.");
        Console.sendMsg(turnPlayer.out(), "Name    : " + drawCard.name());
        Console.sendMsg(turnPlayer.out(), "Strength: " + drawCard.strength());
        Console.sendMsg(turnPlayer.out(), "Effect  : " + drawCard.effectText());
/*
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
        }*/
    }

    // プレイヤーをシャッフルするメソッド
    private void shufflePlayer() {
        Collections.shuffle(this._playerList);
    }

    // カードをシャッフルするメソッド
    private void shuffleCard() {
        Collections.shuffle(this._deck);
    }

    // 名前からプレイヤーのインデックスを検索するメソッド
    private int searchPlayer(String name) {
        for(int i = 0; i < this._playerList.size(); i++) {
            if(this._playerList.get(i).name().equals(name)) return i;
        }
        return -1; //見つからなかった場合
    }

    // 生き残っているプレイヤーの一覧を文字列で返すメソッド
    private String alivePlayer() {
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < this._playerList.size(); i++) {
            if(this._playerList.get(i).isAlive()) {
                buf.append(this._playerList.get(i).name());
            }
            if(i != (this._playerList.size() - 1)) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }

    // cardを捨て札の一番上に追加するメソッド
    private void throwCard(Card card) {
        this._discard.add(card);
    }

    // 負けプレイヤーの敗戦処理を行うメソッド
    private void lose(Player loser) {
        throwCard(loser.getHand());
        loser.setHand(null);
        loser.clearProtected();
        this._alivePlayer--;
        loser.dead();
        Console.sendMsgAll(this._playerList, "[round] " + loser.name() + " has dropped out.");
        Console.sendMsgAll(this._playerList, "[round] Alive : " + alivePlayer());
    }

    // 山札から引いたカードを手札に残すか否かを確認するメソッド (詳細は後ほど)
//    private Card selectCard(Player selectPlayer, Card drawCard) {
        /*** 選択を促す "Select discard. Enter 0/1. " ***/
//        if(0/*** 引いたカードなら ***/) {
/*            return drawCard;
        }
        else {
            return selectPlayer.exchange(draw);
        }
    }*/
/*
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
*/
    // ゲームが終了したか否かを判定するメソッド
    private boolean finished() {
        return this._alivePlayer == 1 || this._deck.size() == 1;
    }

    // 勝者を決定し、勝ち点を与えるメソッド
    private void winJudge() {
        Player winPlayer = null;
        int winPoint = 1;

        // １人残った場合
        if(this._alivePlayer == 1) {
            for(int i = 0; i < this._playerList.size(); i++) {
                if(this._playerList.get(i).isAlive()) {
                    winPlayer = this._playerList.get(i);
                    break;
                }
            }
        }

        // 山札が尽きた場合
        else {
            int winStrength = -1;
            for(int i = 0; i < this._playerList.size(); i++) {
                if(this._playerList.get(i).isAlive()) {
                    if(this._playerList.get(i).getHand().strength() > winStrength) {
                        winPlayer = this._playerList.get(i);
                        winStrength = winPlayer.getHand().strength();
                    }

                    // 引き分けの場合
                    else if(this._playerList.get(i).getHand().strength() == winStrength) {
                        Console.sendMsgAll(this._playerList, "[round] Draw game. No one get point.");
                        return;
                    }
                }
            }
        }

        // 勝者のカードが姫だった場合
        if(winPlayer.getHand().name().equals("Prince")) {
            winPoint++;
        }

        Console.sendMsgAll(this._playerList, "[round] Winner : " + winPlayer.name() + " !");
        Console.sendMsgAll(this._playerList, "[round] " + winPlayer.name() + " get " + winPoint + " point.");
        winPlayer.incrementPoints(winPoint);
    }
}