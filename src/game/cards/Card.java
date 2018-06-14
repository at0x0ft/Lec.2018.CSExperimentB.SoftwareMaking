package game.cards;

import java.util.*;
import java.lang.ArrayIndexOutOfBoundsException;
import main.Console;
import game.Game;

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

    public static Card[] generateCardList(Game game) {
        int arrayLen = sumOfNormalCards();
        if(game.hasKing()) {
            arrayLen++;
        }

        Card[] ret = new Card[arrayLen];
        try {
            int i = 0;
            for(int j = 0; j < soldierNum; j++) {
                ret[i] = new Soldier(i);
                i++;
            }
            for(int j = 0; j < clownNum; j++) {
                ret[i] = new Clown(i);
                i++;
            }
            for(int j = 0; j < knightNum; j++) {
                ret[i] = new Knight(i);
                i++;
            }
            for(int j = 0; j < monkNum; j++) {
                ret[i] = new Monk(i);
                i++;
            }
            for(int j = 0; j < magicianNum; j++) {
                ret[i] = new Magician(i);
                i++;
            }
            for(int j = 0; j < generalNum; j++) {
                ret[i] = new General(i);
                i++;
            }
            for(int j = 0; j < ministerNum; j++) {
                ret[i] = new Minister(i);
                i++;
            }
            for(int j = 0; j < princessNum; j++) {
                ret[i] = new Princess(i);
                i++;
            }
            if(game.hasKing() && i < ret.length - 1) {
                for(int j = 0; j < kingNum; j++) {
                    ret[i] = new King(i);
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
    public boolean is(String name) {
        return this._name.equals(name);
    }

    private int _strength; //カードの強さ
    public int strength(){
        return this._strength;
    }

    private String _effectText; //カード効果の説明文（余裕があれば実装）
    public String effectText() {
        return this._effectText;
    }

    private int _id;
    public int id() {
        return this._id;
    }

    public String shortInfo() { // ex: 1:soldier
        return Integer.toString(this._strength) + ":" + this._name;
    }

    public String fullInfo() {  // ex: 1:soldier (hogefuga)
        return shortInfo() + " (" + this._effectText + ")";
    }

    public Card(String name, int strength, String effectText, int id) {
        this._name = name;
        this._strength = strength;
        this._effectText = effectText;
        this._id = id;
    }

    public static String detectCardType(int strength, Game game) {
        String cardName = null;
        switch(strength) {
            case 0: {
                if(game.hasKing()) {
                    cardName = "King";
                }
                break;
            }
            case 1: {
                cardName = "Soldier";
                break;
            }
            case 2: {
                cardName = "Clown";
                break;
            }
            case 3: {
                cardName = "Knight";
                break;
            }
            case 4: {
                cardName = "Monk";
                break;
            }
            case 5: {
                cardName = "Magician";
                break;
            }
            case 6: {
                cardName = "General";
                break;
            }
            case 7: {
                if(game.hasDuchess()) {
                    cardName = "Duchess";
                }
                else {
                    cardName = "Minister";
                }
                break;
            }
            case 8: {
                if(game.hasPrince()) {
                    cardName = "Prince";
                }
                else {
                    cardName = "Princess";
                }
                break;
            }
        }
        return cardName;
    }
}