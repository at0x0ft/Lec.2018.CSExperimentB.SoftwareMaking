package game;

import java.io.*;
import java.util.*;
import main.Console;
import game.Game;
import game.cards.*;

public class Round {
    private Game _game;

    // random access members
    private int[] _playerOrder;
    private int _playerOrderPtr;    // 0 -> 1 -> ...
    private void incrementPlayerOrderPtr() {
        this._playerOrderPtr++;
        this._playerOrderPtr % numOfPlayers();
        if(!player(turnPlayerID()).isAlive()) {
            incrementPlayerOrderPtr();
        }
    }
    private int[] _cardOrder;
    private int _cardOrderPtr;      // 0 -> 1 -> ...
    private void incrementCardOrderPtr() {
        this._cardOrderPtr++;
    }

    // static access members
    private Player player(int i) {
        return this._game.player(i);
    }
    private Card card(int i) {
        return this._game.card(i);
    }
    private int numOfPlayers() {
        return this._game.numOfPlayers();
    }
    private int numOfAlivePlayers() {
        return numOfPlayers() - this._numOfLosers;
    }
    private int numOfCards() {
        return this._game.numOfCards();
    }
    private int numOfTheRestOfTheCards() {
        return numOfCards() - this._cardOrderPtr - 1;
    }
    private int turnPlayerID() {
        return this._playerOrder[this._playerOrderPtr];
    }
    private int drawCardID() {
        return this._cardOrder[this._cardOrderPtr];
    }

    private ArrayList<Player> _losers;  // 脱落したプレイヤーを記録するための配列
    private void addLoser(Player player) {
        this._losersInfo += player.info() + " ";
        this._losers.add(player);
    }
    private int numOfLosers() {
        return this._losers.size();
    }
    private Card[] _discard; // 捨て札のカードを管理するための配列
    private void throwCard(Card card) { // 捨て札を捨て札管理配列に追加する関数
        this._discardsInfo += card.shortInfo() + ", ";
        this._discard.add(card);
    }
    private int numOfDiscards() {
        return this._discard.size();
    }

    private String _playerOrderInfo;
    private String _losersInfo;
    private String _discardsInfo;

    private String fieldInfo() {
        return null;
    }

    // ゲームが終了したか否かを判定するメソッド
    private boolean isfinished() {
        return numOfAlivePlayers() ==  1 || numOfTheRestOfTheCards() == 0;
    }

    public Round(Game game) {
        this._game = game;
        this._playerOrder = Game.generateRandomNumOrder(numOfPlayers());
        this._cardOrder = Game.generateRandomNumOrder(numOfCards());
        this._losers = new ArrayList<Player>();
        this._discard = new ArrayList<Card>();
        for (int i = 0; i < numOfPlayers(); i++) {
            player(i).revive();
        }
        this._playerOrderPtr = 0;
        this._cardOrderPtr = 0;

        this._playerOrderInfo = "Player order : ";
        extractPlayerInfo();
        this._losersInfo = "Loser : ";
        this._discardsInfo = "Discards : ";
    }

    private void extractPlayerInfo() {
        for (int i = 0; i < numOfPlayers(); i++) {
            this._playerOrderInfo += player(this._playerOrder[i]).shortInfo() + ", ";
        }
    }

    // LoveLetterのゲームの1ラウンドを開始するメソッド.
    public boolean start() {
        /* Start round notification for all */
        initialize();

        while(true) {
            turn();
            if(isfinished()) {
                winJudge(); // true -> winnwer is only one false -> draw
                break;
            }
            incrementPlayerOrderPtr();
        }
        return this._playerQueue;
    }

    private void initialize() { // initial distribution
        // distribute the cards to the player
        Player p = null;
        Card c = null;
        Player hasKingPlayer = null;
        for(int i = 0; i < numOfPlayers(); i++) {
            p = player(turnPlayerID());
            c = card(drawCardID());
            p.hand(c);
            if(this._game.hasKing() && checkKing(c)) {
                hasKingPlayer = p;
            }
            incrementPlayerOrderPtr();
            incrementCardOrderPtr();
        }

        /*** show the cards ***/

        if(this._game.hasKing() && hasKingPlayer != null) {
            lose(hasKingPlayer);
        }
    }

    // 一人のプレイヤーがカードを引き、その効果を発揮させるメソッド
    private void turn() {
        Player turnPlayer = player(turnPlayerID());
        /*** 全員に通知 "turnPlayerのターンです" ***/
        turnPlayer.clearProtection();

        Card drawCard = card(drawCardID());
        incrementCardOrderPtr();

        /*** 引いたプレイヤーに通知 "引いたカードの名前、強さ、説明" ***/
        if(checkKing(drawCard)) {
            lose(turnPlayer);
            throwCard(drawCard); // throw the draw card
            return;
        }
        else if(checkDuchess(drawCard) && checkMinister(drawCard)) {
            if(turnPlayer.hand().strength() + drawCard.strength() >= 12) {
                if(checkMinister(drawCard)) {
                    lose(turnPlayer);
                    throwCard(drawCard); // throw the draw card
                    return;
                }
                else if(checkDuchess(drawCard)) {
                    throwCard(drawCard);
                    return; // turn end
                }
            }
        }

        Card invokeCard = turnPlayer.selectCard(drawCard);
        throwCard(invokeCard);

        // throwCard effect check step.

        if(checkPrincessPrince(invokeCard)) {
            lose(turnPlayer);
            return;
        }
        else if(checkMonk(invokeCard)) {
            turnPlayer.setProtection();
            return;
        }

        execute(turnPlayer, invokeCard);
        return;
    }

    // 負けプレイヤーの敗戦処理を行うメソッド
    private void lose(Player loser) {
        /*** 全員に通知 "loserが脱落しました" ***/
        /*** 全員に通知 "残りプレイヤー: a, b, ..." ***/
        throwCard(loser.handCardIdx()); // throw the hand card
        loser.clearProtection();
        loser.lose();
        addLoser(this._turnPlayerID);
    }

    // invokeCardに書かれた効果 (兵士、道化、騎士、魔術師、将軍の効果のみ) を実行するメソッド
    private void execute(Player player, Card invokeCard) {
        // // select the object player
        // Player object = selectPlayer(pickUpAlivePlayer(alivePlayerList));
        // // notify selection
        // switch (invokeCard.name()) {
        //     case "Soldier": {
        //         Card predictionCard = player.predictCard();
        //         if(object.hand().is(predictCard)) {
        //             lose(object);
        //         }
        //         break;
        //     }
        //     case "Clown": {
        //         player.see(object.hand());
        //         break;
        //     }
        //     case "Knight": {
        //         if(player.hand().strength() > object.hand().strength()) {
        //             lose(object);
        //         }
        //         else if(player.hand().strength() < object.hand().strength()) {
        //             lose(player);
        //         }
        //         break;
        //         break;
        //     }
        //     case "Magician": {
        //         if(numOfTheRestOfTheCards() != 0) {
        //             throwCard(object.hand());
        //             if(checkPrincessPrince(object.hand())) {
        //                 lose(object);
        //             }
        //             object.hand(Card(drawCardID()));
        //             incrementCardOrderPtr();
        //         }
        //         break;
        //     }
        //     case "General": {
        //         // exchange card
        //         Card objectCard = object.hand();
        //         object.hand(playerCard);
        //         player.hand(objectCard);
        //         break;
        //     }
        // }
    }

    // 勝者を決定し、勝ち点を与えるメソッド
    private boolean winJudge() {
        Player winner = null;
        if(numOfAlivePlayers == 1) {
            // winner player (= next available player)
            // (this game is escape defeat game)
            winner = player(turnPlayerID()); // winner
        }
        if(numOfAlivePlayers() !=  1) {
            // 勝者の数が複数人なら引き分け
            int max = 0;
            int maxnum = 0;
            for (int i = 0; i < numOfPlayers(); i++) {
                if(player(i).isAlive()) {
                    if(player(i).hand().strength() > max) {
                        max = player(i).hand().strength();
                        winner = player(i);
                        maxnum = 1;
                    }
                    else if(player(i).hand().strength() == max) {
                        maxnum++;
                    }
                }
            }
            if(maxnum > 1) {
                // log writing
                return false;
            }
        }
        if(checkPrincessPrince(winner.hand())) {
            winner.inclimentPoints(2);
        }
        else {
            winner.inclimentPoints(1);
        }

        // log writing

        if(winner.points() >= 3) {
            this._game.hasfinished();
            // log writing
        }

        return true;
    }

    private void pickUpAlivePlayer(ArrayList<Player> list) {
        list = new ArrayList<Player>();
        for(int i = 0; i < numOfPlayers; i++) {
            if(player(i).isAlive()) {
                list.add(player(i));
            }
        }
        return list;
    }

    // cardが王であるかどうかをチェックするメソッド
    private boolean checkKing(Card card) {
        return this._game.hasKing() && card.is("King");
    }

    // cardが女公爵であるかどうかをチェックするメソッド
    private boolean checkDuchess(Card card) {
        return this._game.hasDuchess() && card.is("Duchess");
    }

    // cardが大臣であるかどうかをチェックするメソッド
    private boolean checkMinister(Card card) {
        return card.is("Minister");
    }

    // cardが僧侶であるかどうかをチェックするメソッド
    private boolean checkMonk(Card card) {
        return card.is("Monk");
    }

    // cardが姫や王子であるかをチェックするメソッド
    private boolean checkPrincessPrince(Card card) {
        return card.is("Princess") || (this._game.hasPrince() && card.is("Prince"));
    }
}