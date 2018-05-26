package server;

import java.io.*;
import interfaces.IDisposable;

abstract class ManageableThread extends Thread implements IDisposable {
    private boolean _isActive;
    public boolean isActive() {
        return this._isActive;
    }
    protected void isActive(boolean value) {
        this._isActive = value;
    }

    private boolean _isInProcess;
    public boolean isInProcess() {
        return this._isInProcess;
    }
    protected void isInProcess(boolean value) {
        this._isInProcess = value;
    }

    public ManageableThread() {
        this.isActive(true);
        this.isInProcess(true);
    }

    public void dispose() {
        this._isInProcess = false;
        this._isActive = false;
    }
}