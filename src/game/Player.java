package game;

import server.ClientThread;
import game.cards.Card;

public class Player {
    private String _name;
    public String name() {  //プレイヤーの名前を返すメソッド
        return this._name;
    }

    private int _points; //現在の勝点
    public int points() {
        return this._points;
    }

    //勝点をwinPoint点だけ増やすメソッド
    public void incrementPoints(int winPoint) {
      this._points += winPoint;
    }

    private boolean _isProtected; //僧侶の効果で守られているか、否か
    public boolean isProtected() {
        return this._isProtected;
    }
    public void setProtected() {
        this._isProtected = true;
    }
    public void clearProtected() {
        this._isProtected = false;
    }

    private Card _hand; //手札
    public Card hand() {
        return _hand;
    }
    public void hand(Card card) {
        this._hand = card;
    }

    public Player(String name, int points, boolean protection, Card hand) {
        this._name = name;
        this._points = points;
        this._isProtected = protection;
        this._hand = hand;
    }

    //手札のカードインスタンスをcardに変更し、元持っていたカードを戻り値として返すメソッド
    public Card exchangeHand(Card card) {
        Card discard = this._hand;
        hand(card);
        return discard;
    }
}