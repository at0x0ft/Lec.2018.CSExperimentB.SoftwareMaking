package game;

import server.ClientThread;
import game.Game;
import game.cards.Card;

public class Player {
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
        this._handCardIdx = card.id();
    }
    private int _handCardIdx;
    public int handCardIdx() {
        return this._handCardIdx;
    }
    public void handCardIdx(int hcidx) {
        this._hcidx;
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
        this._name = id;
        this._points = 0;
        this._isProtected = false;
        this._hand = null;
        this._handCardIdx = 0;
        this._isAlive = true;
    }

    public String info() { // ex : hogeo(1/3)
        return this._name + "(" + Integer.toString(this._points) + "/" + Integer.toString(Game.NEEDWINPOINT) + ")";
    }

    public Card selectCard(Card drawCard, int drawCardIdx) {
        return null;
    }

    //手札のカードインスタンスをcardに変更し、元持っていたカードの番号を戻り値として返すメソッド
    protected Card exchangeHand(Card card) {
        Card ret = this.hand();
        hand(card);
        return ret;
    }

    public Player selectPlayer(ArrayList<Player> list) {
        // select player
        return null;
    }

    protected String extractPlayerInfo(ArrayList<Player> list) {
        String message = "choose one player : ";
        for (Player p : list) {
            message += p.shortInfo() + ", ";
        }
        return message;
    }

    protected Player checkPlayer(String name, ArrayList<Player> list) {
        for (Player p : list) {
            if(p.name().equals(name)) {
                return p;
            }
        }
    }

    public Card predictCard() {
        // select card
        return null;
    }
    
    public void see(Card card) {
        // see the card.
    }
}