package Message.MessageReceived;

import Maestri.MVC.Model.GModel.GamePlayer.Playerboard.Playerboard;
import Message.Message;

import java.io.Serializable;

public class UpdateClientPlayerBoardMessage extends Message implements Serializable {

    private final Playerboard playerboard;

    public UpdateClientPlayerBoardMessage (Playerboard playerboard){
        this.playerboard = playerboard;
    }

    public Playerboard getPlayerboard() {
        return this.playerboard;
    }
}
