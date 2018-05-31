package game.cards;

import java.util.*;
import main.Console;
import java.lang.ArrayIndexOutOfBoundsException;

public class Card {
    // Num of cards definition
    public static final int soldierNum = 5;
    public static final int clownNum = 2;
    public static final int knightNum = 2;
    public static final int monkNum = 2;
    public static final int magicianNum = 2;
    public static final int generalNum = 1;
    public static final int ministerNum = 1;
    public static final int princessNum = 1;
    public static final int kingNum = 1;
    public static int sumOfNormalCards() {
        return soldierNum + clownNum + knightNum + monkNum + magicianNum + generalNum + ministerNum + princessNum;
    }

    public static Card[] generateCardList(boolean hasDuchess, boolean hasPrince, boolean hasKing) {
        int arrayLen = sumOfNormalCards();
        if(hasKing) {
            arrayLen++;
        }

        Card[] ret = new Card[arrayLen];
        try {
            int i = 0;
            for(int j = 0; j < soldierNum; j++) {
                ret[i] = new Soldier();
                i++;
            }
            for(int j = 0; j < clownNum; j++) {
                ret[i] = new Clown();
                i++;
            }
            for(int j = 0; j < knightNum; j++) {
                ret[i] = new Knight();
                i++;
            }
            for(int j = 0; j < monkNum; j++) {
                ret[i] = new Monk();
                i++;
            }
            for(int j = 0; j < magicianNum; j++) {
                ret[i] = new Magician();
                i++;
            }
            for(int j = 0; j < generalNum; j++) {
                ret[i] = new General();
                i++;
            }
            for(int j = 0; j < ministerNum; j++) {
                ret[i] = new Minister();
                i++;
            }
            for(int j = 0; j < princessNum; j++) {
                ret[i] = new Princess();
                i++;
            }
            if(hasKing && i < ret.length - 1) {
                for(int j = 0; j < kingNum; j++) {
                    ret[i] = new King();
                    i++;
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
            Console.writeLn("Generating error in Cardlist.");
            Console.writeLn("(Perhaps Num of cards definition wrong?)");
        }

        return ret;
    }

    private String _name; //カードの名前
    public String name() {
        return this._name;
    }

    private int _strength; //カードの強さ
    public int strength(){
        return this._strength;
    }

    private String _effectText; //カード効果の説明文（余裕があれば実装）
    public String effectText(){
        return this._effectText;
    }

    public Card(String name, int strength, String effectText){
        this._name = name;
        this._strength = strength;
        this._effectText = effectText;
    }
}