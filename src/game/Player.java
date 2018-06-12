package game;

import java.io.*;
import server.ClientThread;
import game.cards.Card;

public class Player {
    private String _name;

    private int _points; //現在の勝点

    private boolean _isAlive; //生き残っているか、否か

    private boolean _isProtected; //僧侶の効果で守られているか、否か

    private Card _hand; //手札

    private BufferedReader _in;

    private PrintWriter _out;

    public Player(String name, int points, boolean protection, Card hand, BufferedReader in, PrintWriter out) {
        this._name = name;
        this._points = points;
        this._isAlive = true;
        this._isProtected = protection;
        this._hand = hand;
        this._in = in;
        this._out = out;
    }

    //プレイヤーの名前を返すメソッド
    public String name() {
        return this._name;
    }

    //プレイヤーのポイントを返すメソッド
    public int points() {
        return this._points;
    }

    //勝点をwinPoint点だけ増やすメソッド
    public void incrementPoints(int winPoint) {
      this._points += winPoint;
    }

    //僧侶の効果で守られているか否かを返すメソッド
    public boolean isProtected() {
        return this._isProtected;
    }

    //僧侶の効果をセットするメソッド
    public void setProtected() {
        this._isProtected = true;
    }

    //僧侶の効果を解除するメソッド
    public void clearProtected() {
        this._isProtected = false;
    }

    //手札を返すメソッド
    public Card hand() {
        return _hand;
    }

    //手札をセットするメソッド
    public void hand(Card card) {
        this._hand = card;
    }

    //手札のカードインスタンスをcardに変更し、元持っていたカードを戻り値として返すメソッド
    public Card exchangeHand(Card card) {
        Card discard = this._hand;
        hand(card);
        return discard;
    }

    public BufferedReader in() {
        return this._in;
    }

    public PrintWriter out() {
        return this._out;
    }
}