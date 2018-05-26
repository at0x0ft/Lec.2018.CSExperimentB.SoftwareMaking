package interfaces;

import java.io.IOException;

public interface IConnectable {
    public boolean establishConnection() throws IOException;
}