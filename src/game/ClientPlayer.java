package game;

import server.ClientThread;
import game.cards.Card;

public class ClientPlayer extends Player {
    private ClientThread _clientThread; // Client Thread

    public ClientPlayer(ClientThread clientThread) {
        super(clientThread.clientName(), 0, false, null);
        this._clientThread = clientThread;
    }
}