package interfaces;

import java.io.IOException;

public interface IConnectable extends IDisposable {
    public boolean establishConnection() throws IOException;
}