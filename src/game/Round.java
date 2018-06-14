package game;

import java.io.*;
import java.util.*;
import main.Console;
import game.Game;
import game.cards.*;
import model.Message;

public class Round {
    private Game _game;

    private int _turnNum;
    public int turnNum() {
        return this._turnNum;
    }

    // random access members
    private int[] _playerOrder;
    private int _playerOrderPtr;    // 0 -> 1 -> ...
    private void incrementPlayerOrderPtr() {
        this._playerOrderPtr++;
        this._playerOrderPtr %= numOfPlayers();
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
        return numOfPlayers() - numOfLosers();
    }
    private int numOfCards() {
        return this._game.numOfCards();
    }
    public int numOfTheRestOfTheCards() {
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
        this._losers.add(player);
    }
    private int numOfLosers() {
        return this._losers.size();
    }
    private ArrayList<Card> _discard; // 捨て札のカードを管理するための配列
    private void throwCard(Card card) { // 捨て札を捨て札管理配列に追加する関数
        this._discard.add(card);
    }
    private int numOfDiscards() {
        return this._discard.size();
    }

    // ゲームが終了したか否かを判定するメソッド
    private boolean hasFinished() {
        return numOfAlivePlayers() ==  1 || numOfTheRestOfTheCards() == 0;
    }

    public Round(Game game) {
        this._game = game;
        this._turnNum = 0;
        this._playerOrder = Game.generateRandomNumOrder(numOfPlayers());
        this._cardOrder = Game.generateRandomNumOrder(numOfCards());
        this._losers = new ArrayList<Player>();
        this._discard = new ArrayList<Card>();
        for (int i = 0; i < numOfPlayers(); i++) {
            player(i).revive();
        }
        this._playerOrderPtr = 0;
        this._cardOrderPtr = 0;
    }

    // LoveLetterのゲームの1ラウンドを開始するメソッド.
    public boolean start() throws IOException {
        initialize();

        while(true) {
            turn();
            if(hasFinished()) {
                return winJudge(); // true -> winnwer is only one false -> draw
            }
            incrementPlayerOrderPtr();
        }
    }

    private void initialize() throws IOException { // initial distribution
        /* Start round notification for all */
        sendMessageAll(0);

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
            // notify drawcard
            p.sendMessage(4, Message.serialize(4, null, null, p.hand()));
        }

        if(this._game.hasKing() && hasKingPlayer != null) {
            lose(hasKingPlayer, p.hand());
        }
    }

    // 一人のプレイヤーがカードを引き、その効果を発揮させるメソッド
    private void turn() throws IOException {
        this._turnNum++;
        Player turnPlayer = player(turnPlayerID());
        /*** 全員に通知 "turnPlayerのターンです" ***/
        sendMessageAll(1);
        sendPlayersActionMessageAll(2, turnPlayer, null);

        turnPlayer.clearProtection();

        Card drawCard = card(drawCardID());
        incrementCardOrderPtr();

        /*** 引いたプレイヤーに通知 "引いたカードの名前、強さ、説明" ***/
        turnPlayer.sendMessage(4, Message.serialize(4, null, null, drawCard));

        if(checkKing(drawCard)) {
            // lose with King
            lose(turnPlayer, drawCard);
            sendPlayersActionMessageAll(9, turnPlayer, drawCard); // notify the throw card
            throwCard(drawCard); // throw the draw card
            return;
        }
        else if(checkDuchess(drawCard) && checkMinister(drawCard)) {
            if(turnPlayer.hand().strength() + drawCard.strength() >= 12) {
                if(checkMinister(drawCard)) {
                    // lose with Minister
                    lose(turnPlayer, drawCard);
                    sendPlayersActionMessageAll(9, turnPlayer, drawCard); // notify the throw card
                    throwCard(drawCard); // throw the draw card
                    return;
                }
                else if(checkDuchess(drawCard)) {
                    sendPlayersActionMessageAll(9, turnPlayer, drawCard); // notify the throw card
                    throwCard(drawCard);
                    return; // turn end
                }
            }
        }

        Card invokeCard = turnPlayer.selectCard(drawCard);  // atode
        sendPlayersActionMessageAll(9, turnPlayer, drawCard); // notify the throw card
        throwCard(invokeCard);

        // throwCard effect check step.

        if(checkPrincessPrince(invokeCard)) {
            // lose with Princess/Prince
            lose(turnPlayer, invokeCard);
            return;
        }
        else if(checkMonk(invokeCard)) {
            turnPlayer.setProtection();
            // notify the monkProtection
            sendPlayersActionMessageAll(11, turnPlayer, null);
            return;
        }

        execute(turnPlayer, invokeCard);
        return;
    }

    // 負けプレイヤーの敗戦処理を行うメソッド
    private void lose(Player loser, Card loseCard) throws IOException {
        /*** 全員に通知 "loserが脱落しました" ***/
        /*** 全員に通知 "残りプレイヤー: a, b, ..." ***/
        sendPlayersActionMessageAll(5, loser, loseCard);

        sendPlayersActionMessageAll(9, loser, loseCard); // notify the throw card
        throwCard(loser.hand()); // throw the hand card
        loser.clearProtection();
        loser.lose();
        addLoser(loser);
    }

    // invokeCardに書かれた効果 (兵士、道化、騎士、魔術師、将軍の効果のみ) を実行するメソッド
    private void execute(Player turnPlayer, Card invokeCard) throws IOException {
        // select the object player
        Player object = turnPlayer.selectPlayer(pickUpAlivePlayers());
        sendSelectedPlayerNotification(turnPlayer, object);
        switch(invokeCard.name()) {
            case "Soldier": {
                String predictedCardName = turnPlayer.predictCard(this._game);
                if(object.hand().is(predictedCardName)) {
                    lose(object, invokeCard);
                }
                break;
            }
            case "Clown": {
                turnPlayer.see(object);
                break;
            }
            case "Knight": {
                // notify the battle situation
                sendKnightBattleMsg(turnPlayer, object);
                if(turnPlayer.hand().strength() > object.hand().strength()) {
                    lose(object, invokeCard);
                }
                else if(turnPlayer.hand().strength() < object.hand().strength()) {
                    lose(turnPlayer, invokeCard);
                }
                break;
            }
            case "Magician": {
                if(numOfTheRestOfTheCards() != 0) {
                    sendPlayersActionMessageAll(9, object, object.hand()); // notify the throw card
                    throwCard(object.hand());
                    if(checkPrincessPrince(object.hand())) {
                        lose(object, invokeCard);
                    }
                    Card drawCard = card(drawCardID());
                    incrementCardOrderPtr();

                    // notify drawCard
                    object.sendMessage(4, Message.serialize(4, null, null, drawCard));
                    if(checkKing(drawCard)) {
                        // lose with King
                        lose(object, drawCard);
                        sendPlayersActionMessageAll(9, object, drawCard); // notify the throw card
                        throwCard(drawCard); // throw the draw card
                        return;
                    }

                    object.hand(drawCard);
                }
                break;
            }
            case "General": {
                // exchange card
                Card objectCard = object.hand();
                object.hand(turnPlayer.hand());
                object.sendMessage(19, Message.serialize(20, null, null, object.hand()));
                turnPlayer.hand(objectCard);
                turnPlayer.sendMessage(19, Message.serialize(20, null, null, turnPlayer.hand()));
                break;
            }
        }
    }

    // 勝者を決定し、勝ち点を与えるメソッド
    private boolean winJudge() throws IOException {
        Player winner = null;
        Player[] aliveList = pickUpAlivePlayers();

        // notify the result
        sendResultAll(aliveList);

        if(numOfAlivePlayers() == 1) {
            // winner player (= next available player)
            winner = player(turnPlayerID()); // winner
        }
        if(numOfAlivePlayers() !=  1) {
            int max = 0;
            int maxnum = 0;
            for (int i = 0; i < aliveList.length; i++) {
                if(aliveList[i].hand().strength() > max) {
                    max = aliveList[i].hand().strength();
                    winner = aliveList[i];
                    maxnum = 1;
                }
                else if(aliveList[i].hand().strength() == max) {
                    maxnum++;
                }
            }
            if(maxnum > 1) {    // 勝者の数が複数人なら引き分け
                // notify draw round
                sendMessageAll(22);
                return false;
            }
        }

        if(checkPrincessPrince(winner.hand())) {
            sendWinMessageAll(2, winner);
            winner.incrementPoints(2);
        }
        else {
            sendWinMessageAll(1, winner);
            winner.incrementPoints(1);
        }

        // log writing (optional)

        if(winner.points() >= 3) {
            sendWinMessageAll(3, winner);
            this._game.endGame();
            // log writing (optional)
        }

        return true;
    }

    // send Message for all players
    private void sendMessageAll(int msgType) throws IOException {
        for (int i = 0; i < numOfPlayers(); i++) {
            player(i).sendMessage(msgType, Message.serialize(msgType, this, player(i), null));
        }
    }

    // send Message of certain player's action (ex: turn notifying, lose...)
    private void sendPlayersActionMessageAll(int msgType, Player p, Card card) throws IOException {
        for (int i = 0; i < numOfPlayers(); i++) {
            switch(msgType) {
                case 2: {
                    if(i == p.id()) {
                        player(i).sendMessage(msgType, Message.serialize(2, null, null, null));
                    }
                    else {
                        player(i).sendMessage(msgType, Message.serialize(3, null, p, null));
                    }
                    break;
                }
                case 5: {
                    if(i == p.id()) {
                        player(i).sendMessage(msgType, Message.serialize(5, null, null, card));
                    }
                    else {
                        player(i).sendMessage(msgType, Message.serialize(6, null, p, card));
                    }
                    break;
                }
                case 9: {
                    if(i == p.id()) {
                        player(i).sendMessage(msgType, Message.serialize(9, null, null, card));
                    }
                    else {
                        player(i).sendMessage(msgType, Message.serialize(10, null, p, card));
                    }
                    break;
                }
                case 11: {
                    if(i == p.id()) {
                        player(i).sendMessage(msgType, Message.serialize(11, null, null, null));
                    }
                    else {
                        player(i).sendMessage(msgType, Message.serialize(12, null, p, null));
                    }
                    break;
                }
            }
        }
    }

    private void sendSelectedPlayerNotification(Player turnPlayer, Player object) throws IOException {
        for (int i = 0; i < numOfPlayers(); i++) {
            if(i == turnPlayer.id()) {
                player(i).sendMessage(14, Message.serialize(14, null, object, null));
            }
            else {
                player(i).sendMessage(15, "notify selected player (other)//" + turnPlayer.name() + " selected " + object.name() + "!");
            }
        }
    }

    private void sendKnightBattleMsg(Player turnPlayer, Player object) throws IOException {
        for (int i = 0; i < numOfPlayers(); i++) {
            player(i).sendMessage(18, "battle//" + turnPlayer.name() + "'s card : " + turnPlayer.hand().strength() + ":" + turnPlayer.hand().name() + " V.S. " + object.hand().strength() + ":" + object.hand().name());
        }
    }

    private void sendResultAll(Player[] aliveList) throws IOException {
        String msg = "round finished//Round is over!//Alive player(s) and his/her(their) hand card : ";
        for (int i = 0; i < aliveList.length; i++) {
            msg += aliveList[i].name() + "'s hand is " + aliveList[i].hand().strength() + ":" + aliveList[i].hand().name();
            if(i != aliveList.length - 1) {
                msg += ", ";
            }
        }
        for (int i = 0; i < numOfPlayers(); i++) {
            player(i).sendMessage(21, msg);
        }
    }

    private void sendWinMessageAll(int type, Player winner) throws IOException {
        for (int i = 0; i < numOfPlayers(); i++) {
            player(i).sendMessage(22 + type, Message.serialize(22 + type, null, winner, null));
        }
    }

    // return alive playerList
    public Player[] pickUpAlivePlayers() {
        Player[] list = new Player[numOfAlivePlayers()];
        int j = 0;
        for(int i = this._playerOrderPtr; i < this._playerOrderPtr + numOfPlayers() && j < numOfAlivePlayers(); i++) {
            if(player(this._playerOrder[i % numOfPlayers()]).isAlive()) {
                list[j] = player(this._playerOrder[i % numOfPlayers()]);
                j++;
            }
        }
        return list;
    }

    // return losers
    public Player[] pickUpLosers() {
        return this._losers.toArray(new Player[numOfLosers()]);
    }

    // return discards
    public Card[] pickUpDiscards() {
        return (Card[])this._discard.toArray();
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