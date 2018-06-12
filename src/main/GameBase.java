package main;

import java.io.*;
import interfaces.IDisposable;

public abstract class GameBase implements IDisposable {
    public abstract boolean establishConnection() throws IOException;
    public abstract void startGame() throws IOException;
    public abstract void dispose() throws IOException;
}