package Message.MessageReceived;

import Message.Message;

import java.io.Serializable;

public class ServerErrorMessage extends Message implements Serializable {

    private final String errorMessage;

    public ServerErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
