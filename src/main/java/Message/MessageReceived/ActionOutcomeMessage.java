package Message.MessageReceived;

import Message.Message;

import java.io.Serializable;

public class ActionOutcomeMessage extends Message implements Serializable {

    private boolean actionOutcome;

    public ActionOutcomeMessage (boolean actionOutcome) {
        this.actionOutcome = actionOutcome;
    }

    public boolean isActionOutcome() {
        return this.actionOutcome;
    }

    public void setActionOutcome(boolean actionOutcome) {
        this.actionOutcome = actionOutcome;
    }
}
