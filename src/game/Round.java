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
        this._alivePlayer = playerList.size();
        this._deck = deck;
        this._discard = new ArrayList<Card>();
        this._hasDuchess = hasDuchess;
        this._hasPrince = hasPrince;
        this._hasKing = hasKing;
    }

    // LoveLetterのゲームの1ラウンドを開始するメソッド.
    public void start() throws IOException {
        shufflePlayer();
        shuffleCard();

        Console.sendMsgAll(this._playerList, Console.red + "[round]" + Console.cyan + " Player order : " + Console.green + alivePlayer() + Console.reset);

/*** ↓ for debug ↓ **/
        StringBuilder buf = new StringBuilder();
        buf.setLength(0);
        for(int i = 0; i < this._deck.size(); i++) {
            buf.append(this._deck.get(i).name());
            if(i != (this._deck.size() - 1)) buf.append(Console.cyan + ", " + Console.blue);
        }
        String plist = buf.toString();

        Console.sendMsgAll(this._playerList, Console.red + "[round]" + Console.cyan + " Cards : " + Console.blue + plist + Console.reset);
/*** ↑ for debug ↑ ***/

        wait(3000);
        setInitialCard();

        boolean endFlag = true;
        while(endFlag) {
            for(int i = 0; i < this._playerList.size(); i++) {
                wait(5000);
                turn(i);
                if(finished()) {
                    winJudge();
                    endFlag = false;
                    break;
                }
            }
        }

        //カードをもとに戻す
        this._deck = this._discard;
    }

    // 一人のプレイヤーがカードを引き、その効果を発揮させるメソッド
    private void turn(int n) throws IOException {
        Player turnPlayer = this._playerList.get(n);
        Console.sendMsgAll(this._playerList, Console.red + "[round] " + Console.green + turnPlayer.name() + Console.cyan + "'s turn." + Console.reset);
        turnPlayer.clearProtected();

        Card drawCard = this._deck.remove(0);
        wait(1000);
        Console.sendMsg(turnPlayer.out(), Console.red + "[round]" + Console.cyan + "You drew a card." + Console.reset);
        explainCard(turnPlayer, drawCard);

        boolean deathFlag = true;
        wait(3000);
        deathFlag = drawCard.drawMethod(turnPlayer);

        if(deathFlag) {
            wait(2000);
            Card invokeCard = selectCard(turnPlayer, drawCard);
            invokeCard.throwMethod(turnPlayer);
            throwCard(invokeCard);
        }
    }

/*
            case "King":
                lose(turnPlayer);
                throwCard(drawCard);
                break;
*/
/*
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
*/

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

    // プレイヤーに引いたカードの説明をするメソッド
    private void explainCard(Player player, Card card) {
        Console.sendMsg(player.out(), "Name    : " + Console.blue + card.name() + Console.reset);
        Console.sendMsg(player.out(), "Strength: " + card.strength());
        Console.sendMsg(player.out(), "Effect  : " + card.effectText());
    }

    //各プレイヤーに初期カードを配って通知するメソッド
    private void setInitialCard() {
        for(int i = 0; i < this._playerList.size(); i++) {
            Player player = this._playerList.get(i);
            player.setHand(this._deck.remove(0));
            Console.sendMsg(player.out(), Console.red + "[round]" + Console.cyan + "Your initial card : " + Console.reset);
            explainCard(player, player.getHand());
        }
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
        Console.sendMsgAll(this._playerList, Console.red + "[round] " + Console.green + loser.name() + Console.cyan + " has dropped out." + Console.reset);
        Console.sendMsgAll(this._playerList, Console.red + "[round]" + Console.cyan + " Alive : " + Console.green + alivePlayer() + Console.reset);
    }

    // 山札から引いたカードを手札に残すか否かを確認するメソッド
    private Card selectCard(Player turnPlayer, Card drawCard) throws IOException {
        try {
            Console.sendMsg(turnPlayer.out(), "/console readAorB 0 1 " + Console.red + "[round] " + Console.cyan + "Select discard. 0: " + Console.blue + drawCard.name() + Console.cyan + ", 1: " + Console.blue + turnPlayer.getHand().name() + Console.reset);
            String ans = Console.acceptMsg(turnPlayer.in()); /***あとで例外処理***/
            if(ans.equals("0")) {
                return drawCard;
            }
            else if(ans.equals("1")) {
                return turnPlayer.exchangeHand(drawCard);
            }
            else {
                Console.sendMsgAll(this._playerList, Console.magenta + "error" + Console.reset);
                return drawCard;
            }
        } catch(IOException e) {
            return drawCard;
        }
    }

    // ゲームが終了したか否かを判定するメソッド
    private boolean finished() {
        return this._alivePlayer == 1 || this._deck.size() == 0;
    }

    // 勝者を決定し、勝ち点を与えるメソッド
    private void winJudge() {
        Player winPlayer = null;
        int winPoint = 1;

        wait(2000);

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
                        Console.sendMsgAll(this._playerList, Console.red + "[round]" + Console.cyan + " Draw game. No one get point." + Console.reset);
                        return;
                    }
                }
            }
        }

        // 勝者のカードが姫だった場合
        if(winPlayer.getHand().name().equals("Prince")) {
            winPoint++;
        }

        Console.sendMsgAll(this._playerList, Console.red + "[round]" + Console.cyan + " Winner : " + Console.green + winPlayer.name() + Console.cyan + " !" + Console.reset);
        wait(500);
        Console.sendMsgAll(this._playerList, Console.red + "[round] " + Console.green + winPlayer.name() + Console.cyan + " get " + winPoint + " point." + Console.reset);
        winPlayer.incrementPoints(winPoint);
        wait(3000);
    }

    public void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}