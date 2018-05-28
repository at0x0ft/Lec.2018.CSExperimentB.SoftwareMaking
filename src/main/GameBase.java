package server;

import java.io.*;
import java.net.*;
import interfaces.IConnectable;

abstract class GameBase implements IConnectable {   // remark
    abstract boolean establishConnection() throws IOException;
    abstract void startGame() throws IOException;
}