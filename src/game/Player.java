package game;

import java.io.IOException;
import server.ClientThread;
import game.Game;
import game.cards.Card;
import interfaces.ISendable;

public class Player implements ISendable {
    private String _name;
    public String name() {  //プレイヤーの名前を返すメソッド
        return this._name;
    }

    private int _id;
    public int id() {
        return this._id;
    }

    private int _points; //現在の勝点
    public int points() {
        return this._points;
    }

    public String shortInfo() {
        return name() + "(" + points() + ")";
    }

    //勝点をwinPoint点だけ増やすメソッド
    public void incrementPoints(int winPoint) {
      this._points += winPoint;
    }

    private boolean _isProtected; //僧侶の効果で守られているか、否か
    public boolean isProtected() {
        return this._isProtected;
    }
    public void setProtection() {
        this._isProtected = true;
    }
    public void clearProtection() {
        this._isProtected = false;
    }

    private Card _hand; //手札
    public Card hand() {
        return _hand;
    }
    public void hand(Card card) {
        this._hand = card;
        this._handCardIdx = card.id();
    }
    private int _handCardIdx;
    public int handCardIdx() {
        return this._handCardIdx;
    }

    private boolean _isAlive;
    public boolean isAlive() {
        return this._isAlive;
    }
    public void lose() {
        this._isAlive = false;
    }
    public void revive() {
        this._isAlive = true;
    }

    public Player(String name, int id) {
        this._name = name;
        this._id = id;
        this._points = 0;
        this._isProtected = false;
        this._hand = null;
        this._handCardIdx = 0;
        this._isAlive = true;
    }

    public String sendMessage(int msgType, String message) throws IOException { // for override method
        return null;
    }

    public Card selectCard(Card drawCard) throws IOException { // 4 override
        return null;
    }

    //手札のカードインスタンスをcardに変更し、元持っていたカードの番号を戻り値として返すメソッド
    protected Card exchangeHand(Card card) {
        Card ret = this.hand();
        hand(card);
        return ret;
    }

    public Player selectPlayer(Player[] list) throws IOException { // 4 override
        // select player
        return null;
    }

    public String predictCard(Game game) throws IOException {    // 4 override
        // select card
        return null;
    }

    public boolean see(Player Object) throws IOException { // 4 override
        // see the card.
        return false;
    }

    protected String extractPlayerInfo(Player[] list) {
        String message = "Player list : ";
        for (int i = 0; i < list.length; i++) {
            message += Integer.toString(i + 1) + ":" + list[i].name();
            if(list[i].isProtected()) {
                message += "(Protected)";
            }
            if(i != list.length - 1) {
                message += ", ";
            }
        }
        message += "//" + Integer.toString(list.length);
        return message;
    }

    protected String extractCardTypeInfo(Game game) {
        String message = "Card list : ";
        message += "//";
        if(game.hasKing()) {
            message += "0:King, ";
        }
        message += "1:Soldier, 2:Clown, 3:Knight, 4:Monk, 5:Magician, 6:General, ";
        if(game.hasDuchess()) {
            message += "7:Duchess, ";
        }
        else {
            message += "7:Minister, ";
        }
        if(game.hasPrince()) {
            message += "8:Prince";
        }
        else {
            message += "8:Princess";
        }
        return message;
    }
}