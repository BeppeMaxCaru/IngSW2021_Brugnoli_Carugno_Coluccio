package Message.MessageReceived;

import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.Message;

import java.io.Serializable;

public class UpdateClientMarket extends Message implements Serializable {

    Market market;

    public UpdateClientMarket(Market market) {
        this.market = market;
    }

    public Market getMarket() {
        return this.market;
    }
}
