public class Player{
    private String _ipAddress; //IPアドレス
    private String _name; //プレイヤーの名前
    private int _points; //現在の勝点
    private boolean _protection; //僧侶の効果で守られているか、否か
    private Card _hand; //手札

    public Player(String ipAddress, String name, int points, boolean protection, Card hand){
        this._ipAddress = ipAddress;
        this._name = name;
        this._points = points;
        this._protection = protection;
        this._hand = hand;
    }
    public Player(String ipAddress, String name){
        this(ipAddress, name, 0, false, null);
    }
    public Player(){
        this(null, "名無しさん", 0, false, null);
    }

    //IPアドレスを返すメソッド
    public String getIPAdress(){
        return _ipAddress;
    }

    //プレイヤーの名前を返すメソッド
    public String getName(){
        return _name;
    }

    //現在の勝点を返すメソッド
    public int getPoints(){
        return _points;
    }

    //勝点をwinPoint点だけ増やすメソッド
    public void incrementPoints(int winPoint){
      _points += winPoint;
    }

    //プレイヤーの僧侶の保護効果を付与するメソッド
    public void setProtected(){
        _protection = true;
    }

    //プレイヤーの僧侶の保護効果を解除するメソッド
    public void resetProtected(){
        _protection = false;
    }

    //プレイヤーが僧侶の効果で守られているかどうかを返すメソッド
    public boolean isProtected(){
        return _protection;
    }

    //handメンバにcardをセットするメソッド
    public void setHand(Card card){
        this._hand = card;
    }

    //手札のカードインスタンスを返すメソッド
    public Card getHand(){
        return _hand;
    }

    //手札のカードインスタンスをcardに変更し、元持っていたカードを戻り値として返すメソッド
    public Card exchangeHand(Card card){
        Card discard = this._hand;
        setHand(card);
        return discard;
    }
}