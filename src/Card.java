public class Card{
    private String _name; //カードの名前
    private int _strength; //カードの強さ
    private String _effectText; //カード効果の説明文（余裕があれば実装）

    Card(String _name, int _strength, String _effectText){
        this._name = _name;
        this._strength = _strength;
        this._effectText = _effectText;
    }
    Card(){
        this("名無しのカード", 0, "説明はありません");
    }

    //カードの名前を返すメソッド
    public String getName(){
        return _name;
    }

    //カードの強さを返すメソッド
    public int getStrength(){
        return _strength;
    }

    //カード効果の説明文を返すメソッド
    public String getEffectText(){
        return _effectText;
    }
}