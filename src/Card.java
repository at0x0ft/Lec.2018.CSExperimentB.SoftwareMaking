import java.util.*;

public class Card {
    private String _name; //カードの名前
    private int _strength; //カードの強さ
    private String _effectText; //カード効果の説明文（余裕があれば実装）

    public Card(String name, int strength, String effectText){
        this._name = name;
        this._strength = strength;
        this._effectText = effectText;
    }
    public Card(String name, int strength){
        this(name, strength, "説明はありません");
    }
    public Card(String name){
        this(name, 0, "説明はありません");
    }
    public Card(){
        this("名無しのカード", 0, "説明はありません");
    }

    //カードの名前を返すメソッド
    public String getName(){
        return this._name;
    }

    //カードの強さを返すメソッド
    public int getStrength(){
        return this._strength;
    }

    //カード効果の説明文を返すメソッド
    public String getEffectText(){
        return this._effectText;
    }
}