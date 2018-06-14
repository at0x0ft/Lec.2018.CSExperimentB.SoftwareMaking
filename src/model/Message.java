package model;

import java.io.IOException;
import main.Console;
import game.Game;
import game.Round;
import game.Player;
import game.cards.Card;

public class Message {
    public static final int MAX_MESSAGE_LEN = 10;

    /*
        <<message Type>>
        need response = 7, 13, 17, 21, 22, 23, 24
        one way = others
    */
    public static String serialize(int msgType, Round r, Player p, Card card) {
        String msg = null;
        switch(msgType) {
            case 0: {
                msg = "start round//Start round and start initial draw.";
                break;
            }
            case 1: {
                msg = "start turn";
                msg = parseTurnNum(msg, r);
                msg = parseAlivePlayerOrder(msg, r);
                msg = parseLosers(msg, r);
                msg = parseDeckNum(msg, r);
                msg = parseDiscards(msg, r);
                msg = parseHand(msg, p);
                break;
            }
            case 2: {
                msg = "notify turn (you)";
                msg = parseYTurnPlayer(msg);
                break;
            }
            case 3: {
                msg = "notify turn (other)";
                msg = parseOTurnPlayer(msg, p);
                break;
            }
            case 4: {
                msg = "notify the draw card";
                msg = parseDrawCard(msg, card);
                break;
            }
            case 5: {
                msg = "lose (you)";
                msg = parseYLoseMessage(msg, card);
                break;
            }
            case 6: {
                msg = "lose (other)";
                msg = parseOLoseMessage(msg, p, card);
                break;
            }
            case 7: {
                msg = "select card";
                msg = parseCardSelection(msg, p, card);
                break;
            }
            case 9: {
                msg = "notify throw card (you)";
                msg = parseYThrowCard(msg, card);
                break;
            }
            case 10: {
                msg = "notify throw card (other)";
                msg = parseOThrowCard(msg, p, card);
                break;
            }
            case 11: {
                msg = "notify monk protection (you)";
                msg = parseYMonkNotification(msg);
                break;
            }
            case 12: {
                msg = "notify throw card (other)";
                msg = parseOMonkNotification(msg, p);
                break;
            }
            /*case 13: { // not use with this method.
                msg = "select player";
                msg += "// 1:player1, 2:player2, ...";
                break;
            }*/
            case 14: {
                msg = "notify selected player (you)";
                msg += parseSelectedPlayerNotification(msg, p);
                break;
            }
            /*case 15: {  // not use with this method.
                msg = "notify selected player (other)";
                msg += "//XXX selected YYY!";
                break;
            }*/
            /*case 16: { // not use with this method.
                msg = "predict card";
                msg += "// 1:player1, 2:player2, ...";
                break;
            }*/
            case 17: {
                msg = "see card";
                msg = parseSeeCard(msg, p);
                break;
            }
            /*case 18: {  // not use with this method.
                msg = "battle";
                msg = "//XXX V.S. YYY";
                break;
            }*/
            case 19: {
                msg = "notify exchanged card";
                msg = parseExchangedCard(msg, card);
                break;
            }
            /*case 20: {  // not use with this method
                msg = "round finished//Round is over!//";
                break;
            }*/
            case 21: {
                msg = "draw round//This round is... draw game! (Because of multiple winners)";
                break;
            }
            case 22: {
                msg = "win 1pt//This round winner is... " + p.name() + "!";
                break;
            }
            case 23: {
                msg = "win 2pt//This round winner is... " + p.name() + "! (with princess/prince)";;
                break;
            }
            case 24: {
                msg = "game set//This game winner is... " + p.name() + "! Congratulations!";
                break;
            }
        }

        return msg;
    }

    public static String deserialize(String s) throws IOException {
        String response = null;

        String[] msgStrs = s.split("//", Message.MAX_MESSAGE_LEN);
        switch(msgStrs[0]) {
            case "start round": {   // 0
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "start turn": {    // 1
                Console.clearScreen(500);
                Console.writeLn("Start turn " + msgStrs[1] + ".");
                for (int i = 2; i < msgStrs.length; i++) {
                    Console.writeLn(msgStrs[i]);
                }
                break;
            }
            case "notify turn (you)": { // 2
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "notify turn (other)": {   // 3
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "notify the draw card": {  // 4
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "lose (you)": {    // 5
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "lose (other)": {  // 6
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "select card": {   // 7
                for (int i = 1; i < msgStrs.length; i++) {
                    Console.writeLn(msgStrs[i]);
                    if(i == 2) {
                        Console.newLn();
                    }
                }

                // send response. (return Type is "h/d" string)
                response = cardSelection();
                break;
            }
            case "notify throw card (you)": {   // 9
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "notify throw card (other)": { // 10
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "notify monk protection (you)": {  // 11
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "notify monk protection (other)": { // 12
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "select player": { // 13
                Console.writeLn(msgStrs[1]);

                // send response. (return Type is 1 ~ player num)
                response = playerSelection(msgStrs[2]);
                break;
            }
            case "notify selected player (you)": {  // 14
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "notify selected player (other)": {    // 15
                Console.writeLn(msgStrs[1]);
                break;
            }
            case "predict card": {  // 16
                Console.writeLn(msgStrs[1]);

                response = cardPrediction(msgStrs[2]);
                break;
            }
            case "see card": {  // 17
                Console.writeLn(msgStrs[1]);
                // press Enter
                response =  pressEnter();   // response is nonsense string
                break;
            }
            case "battle": {    // 18
                break;
            }
            case "notify exchanged card": { // 19
                break;
            }
            case "round finished": {    // 20
                for (int i = 1; i < msgStrs.length; i++) {
                    Console.writeLn(msgStrs[i]);
                }
                break;
            }
            case "draw round": {    // 21
                Console.writeLn(msgStrs[1]);
                // press Enter
                response =  pressEnter();   // response is nonsense string
                response = "ROUND END";
                break;
            }
            case "win 1pt": {   // 22
                Console.writeLn(msgStrs[1]);
                // press Enter
                response =  pressEnter();   // response is nonsense string
                response = "ROUND END";
                break;
            }
            case "win 2pt": {   // 23
                Console.writeLn(msgStrs[1]);
                // press Enter
                response =  pressEnter();   // response is nonsense string
                response = "ROUND END";
                break;
            }
            case "game set": {  // 24
                Console.writeLn(msgStrs[1]);
                // press Enter
                response =  pressEnter();   // response is nonsense string
                response = "GAME END";
                break;
            }
        }

        return response;
    }

    private static String parseTurnNum(String msg, Round r) {
        return msg + "//" + Integer.toString(r.turnNum());
    }

    private static String parseYTurnPlayer(String msg) {
        return msg + "//Now, it's your turn!";
    }

    private static String parseOTurnPlayer(String msg, Player p) {
        return msg + "//Now, it's " + p.name() + "'s turn!";
    }

    private static String parseAlivePlayerOrder(String msg, Round r) {
        msg += "//Player Order : ";
        Player[] alivePlayerList = r.pickUpAlivePlayers();
        for (int i = 0; i < alivePlayerList.length; i++) {
            if(i == 0) {
                msg += ">> " + alivePlayerList[i].name() + "(" + Integer.toString(alivePlayerList[i].points()) + "/" + Integer.toString(Game.NEEDWINPOINT) + ")" + " <<, ";
            }
            else if(i == alivePlayerList.length - 1) {
                msg +=  alivePlayerList[i].name() + "(" + Integer.toString(alivePlayerList[i].points()) + "/" + Integer.toString(Game.NEEDWINPOINT) + ")";
            }
            else {
                msg +=  alivePlayerList[i].name() + "(" + Integer.toString(alivePlayerList[i].points()) + "/" + Integer.toString(Game.NEEDWINPOINT) + ")" + ", ";
            }
        }
        return msg;
    }

    private static String parseLosers(String msg, Round r) {
        msg += "//Losers : ";
        Player[] loserList = r.pickUpLosers();
        for (int i = 0; i < loserList.length; i++) {
            if(i == loserList.length - 1) {
                msg += loserList[i].name() + "(" + Integer.toString(loserList[i].points()) + "/" + Integer.toString(Game.NEEDWINPOINT) + ")";
            }
            else {
                msg +=  loserList[i].name() + "(" + Integer.toString(loserList[i].points()) + "/" + Integer.toString(Game.NEEDWINPOINT) + ")" + ", ";
            }
        }
        return msg;
    }

    private static String parseDeckNum(String msg, Round r) {
        return msg + "//Remaining card(s) in deck : " + Integer.toString(r.numOfTheRestOfTheCards());
    }

    private static String parseDiscards(String msg, Round r) {
        msg += "//Discards : ";
        Card[] discardList = r.pickUpDiscards();
        for (int i = 0; i < discardList.length; i++) {
            if(i == discardList.length - 1) {
                msg += Integer.toString(discardList[i].strength()) + ":" + discardList[i].name();
            }
            else {
                msg += Integer.toString(discardList[i].strength()) + ":" + discardList[i].name() +", ";
            }
        }
        return msg;
    }

    private static String parseHand(String msg, Player p) {
        return msg + "//Your hand card : " + Integer.toString(p.hand().strength()) + ":" + p.hand().name();
    }

    private static String parseDrawCard(String msg, Card drawCard) {
        return msg + "//Your draw card : " + Integer.toString(drawCard.strength()) + ":" + drawCard.name();
    }

    private static String parseYLoseMessage(String msg, Card drawCard) {
        msg += "//You lose! (...Because of " + drawCard.name() + "'s effect.)";
        msg +=  "//(" + Integer.toString(drawCard.strength()) + ":" + drawCard.name() + " = " + drawCard.effectText() + ")";
        return msg;
    }

    private static String parseOLoseMessage(String msg, Player p, Card drawCard) {
        msg += "//" + p.name() + " lose. (...Because of " + drawCard.name() + "'s effect.)";
        msg +=  "//(" + Integer.toString(drawCard.strength()) + ":" + drawCard.name() + " = " + drawCard.effectText() + ")";
        return msg;
    }

    private static String parseCardSelection(String msg, Player p, Card drawCard) {
        msg += "//Your handCard is " + Integer.toString(p.hand().strength()) + ":" + p.hand().name();
        msg += "//HandCard effect : " + p.hand().effectText();
        msg += "//And your drawCard is " + Integer.toString(drawCard.strength()) + ":" + drawCard.name();
        msg += "//DrawCard effect : " + drawCard.effectText();
        return msg;
    }

    private static String parseYThrowCard(String msg, Card throwCard) {
        return msg + "//You threw " + Integer.toString(throwCard.strength()) + ":" + throwCard.name() + "!";
    }

    private static String parseOThrowCard(String msg, Player p, Card throwCard) {
        return msg + "//" + p.name() + " threw " + Integer.toString(throwCard.strength()) + ":" + throwCard.name() + "!";
    }

    private static String parseYMonkNotification(String msg) {
        return msg + "//You protected with Monk (until next your turn).";
    }

    private static String parseOMonkNotification(String msg, Player p) {
        return msg + "//" + p.name() + " protected with Monk (until next " + p.name() + " turn).";
    }

    private static String parseSelectedPlayerNotification(String msg, Player object) {
        return msg + "//You selected " + object.name() + "!";
    }

    private static String parseSeeCard(String msg, Player object) {
        return msg + "//" + object.name() + "'s handCard is " + Integer.toString(object.hand().strength()) + ":" + object.hand().name();
    }

    private static String parseExchangedCard(String msg, Card newHandCard) {
        return msg + "//Your new handCard is " + Integer.toString(newHandCard.strength()) + ":" + newHandCard.name();
    }


    private static String cardSelection() {
        return Console.readAorB(
            "h",
            "d",
            "",
            "Which card would you throw? Enter h/d (h = hand, d = draw). : "
            );
    }

    private static String playerSelection(String maxNumStr) {
        return Integer.toString(Console.readNum(
            1,
            Integer.parseInt(maxNumStr),
            "",
            "Which player would you select? Enter number (1 ~ " + maxNumStr + " ). : "
            ));
    }

    private static String cardPrediction(String minNumStr) {
        return Integer.toString(Console.readNum(
            Integer.parseInt(minNumStr),
            8,
            "",
            "Which card would you predict? Enter number ( " + minNumStr + " ~ 8 ). : "
            ));
    }

    private static String pressEnter() {
        Console.write("press Enter : ");
        return Console.readLn();
    }
}