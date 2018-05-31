package game.card;

import java.util.*;

public class Card {
    private String _name; //カードの名前
    public String name() {
        return this._name;
    }

    private int _strength; //カードの強さ
    public int strength(){
        return this._strength;
    }

    private String _effectText; //カード効果の説明文（余裕があれば実装）
    public String getEffectText(){
        return this._effectText;
    }

    public Card(String name, int strength, String effectText){
        this._name = name;
        this._strength = strength;
        this._effectText = effectText;
    }
}