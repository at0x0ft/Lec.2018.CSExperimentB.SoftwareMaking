public class Player{
    private String _IPAdress; //IPアドレス
    private String _name; //プレイヤーの名前
    private int _points; //現在の勝点
    private boolean _protection; //僧侶の効果で守られているか、否か
    private Card _hand; //手札

    Player(String _IPAdress, String _name, int _points, boolean _protection, Card _hand){
        this._IPAdress = _IPAdress;
        this._name = _name;
        this._points = _points;
        this._protection = _protection;
        this._hand = _hand;
    }
    Player(String _IPAdress, String _name){
        this(_IPAdress, _name, 0, false, null);
    }
    Player(){
        this(null, "名無しさん", 0, false, null);
    }

    //IPアドレスを返すメソッド
    public String getIPAdress(){
        return _IPAdress;
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