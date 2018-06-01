package game;

import main.Console;
import game.Game;
import game.cards.Card;

public class MasterPlayer extends Player {
    public MasterPlayer(String name, int id) {
        super(name, id);
    }

    public void clearScreen(long ms) {
        Console.clearingScreen(long ms);
    }

    public void showMessageLn(String message) {
        Console.writeLn(message);
    }

    // 山札から引いたカードを手札に残すか否かを確認するメソッド
    @Override
    public Card selectCard(Card drawCard, int drawCardIdx) {
        /*** 選択を促す "Select discard. Enter 0/1. " ***/
        // // Selected Loop
        // if(true) {
        //     return super.exchangeHand(drawCard);
        // }
        // else {
        //     return drawCard;
        // }
        // select random
        switch (Game.generateCardList(2).[0]) {
            case 0:
                return super.exchangeHand(drawCard);
            default:
                return drawCard;
        }
    }

    @Override
    public Player selectPlayer(ArrayList<Player> list) {
        // select player
        String message = super.extractPlayerInfo(list);
        Console.writeLn(message);
        while(true) {
            Player p = super.checkPlayer(Console.readLn(), list);
            if(p != null) {
                return p;
            }
            Console.writeLn("Wrong player Name. Please enter again.");
        }
    }

    @Override
    public Card predictCard() {
        // select card
        Console.write("Please enter the card number you predict : ");
        String predictCardnum = Console.readLn();
        // convert to card obj
        return null;
    }

    @Override
    public void see(Card card) {
        // see the card.
        Console.writeLn("Opponent Card is : " + card.shortInfo());
    }

    private Player selectPlayerLoop(ArrayList<Player> list) {
        Console.write("choose one player : ");
        for (Player p : list) {
            Console.write(p.name() + ", ");
        }
        Console.newLn();

        String name = 
    }
}