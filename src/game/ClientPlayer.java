package game;

import java.io.IOException;
import server.ClientThread;
import server.StateManager;
import game.cards.Card;
import model.Message;

public class ClientPlayer extends Player {
    private ClientThread _clientThread; // Client Thread
    private StateManager _stateManager; // 

    public ClientPlayer(ClientThread clientThread, int id) {
        super(clientThread.clientName(), id);
        this._clientThread = clientThread;
    }

    @Override
    public String sendMessage(int msgType, String message) throws IOException {
        // sendMessage
        this._stateManager.registerMessage(msgType, message);
        this._stateManager.restartWaitingThread(super.id());

        String response = null;
        switch(msgType) {
            case 7:
            case 13:
            case 17:
            case 21:
            case 22:
            case 23:
            case 24: {  // need response message
                // wait response
                this._stateManager.waitThread(super.id());

                // receive response
                response = this._stateManager.popMessage();
                break;
            }
        }

        return response;
    }

    // 山札から引いたカードを手札に残すか否かを確認するメソッド
    @Override
    public Card selectCard(Card drawCard) throws IOException {
        /*** 選択を促す "Select discard. Enter h/d. " ***/
        String response = this.sendMessage(7, Message.serialize(7, null, this, drawCard));
        // receive Message = response
        // h/d return
        switch (response) {
            case "h": { // throw hand card
                return super.exchangeHand(drawCard);
            }
            case "d": { // throw draw card
                return drawCard;
            }
        }

        return null;
    }

    @Override
    public Player selectPlayer(Player[] list) throws IOException {
        // select player
        String response = this.sendMessage(13, "select player//" + super.extractPlayerInfo(list));
        // receive Message = response (1 ~ Player num)

        return list[Integer.parseInt(response) - 1];
    }

    @Override
    public String predictCard(Game game) throws IOException {
        String minNumStr = null;
        if(game.hasKing()) {
            minNumStr = "0";
        }
        else {
            minNumStr = "1";
        }

        // predict the card
        String response = this.sendMessage(16, "predict card//" + super.extractCardTypeInfo(game) + "//" + minNumStr);

        return Card.detectCardType(Integer.parseInt(response), game);
    }

    @Override
    public boolean see(Player object) throws IOException {
        // see the card.
        String response = this.sendMessage(17, Message.serialize(17, null, object, null));
        // wait OK response.
        return response != null;
    }
}