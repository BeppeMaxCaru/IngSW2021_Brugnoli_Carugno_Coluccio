package Message.MessageReceived;

import Message.Message;

import java.io.Serializable;

public class QuitMessage extends Message implements Serializable {

    private String quitMessage;

    public QuitMessage () {
        this.quitMessage = "You left the game";
    }

    public String getQuitMessage() {
        return this.quitMessage;
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }
}
