package game;

import server.ClientThread;
import game.cards.Card;

public class ClientPlayer extends Player {
    private ClientThread _clientThread; // Client Thread
    private StateManager _stateManager; // 

    public ClientPlayer(ClientThread clientThread, int id) {
        super(clientThread.clientName(), id);
        this._clientThread = clientThread;
    }

    public void sendMessage(String message) {
        this._clientThread.sendMessage(message);
    }

    // 山札から引いたカードを手札に残すか否かを確認するメソッド
    @Override
    public Card selectCard(Card drawCard) {
        /*** 選択を促す "Select discard. Enter 0/1. " ***/
        // Selected Loop
        // if(true) {
        //     return super.exchangeHand(drawCard);
        // }
        // else {
        //     return drawCard;
        // }
        // select
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
        return null;
    }

    @Override
    public Card predictCard() {
        // select card
        return null;
    }

    @Override
    public void see(Card card) {
        // see the card.
    }
}