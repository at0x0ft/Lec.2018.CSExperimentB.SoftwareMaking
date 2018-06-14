package interfaces;

import java.io.IOException;

public interface ISendable {
    public String sendMessage(int msgType, String msg) throws IOException;
}