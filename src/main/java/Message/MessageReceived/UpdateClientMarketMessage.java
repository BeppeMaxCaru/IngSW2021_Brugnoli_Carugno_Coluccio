package Message.MessageReceived;

import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.Message;

import java.io.Serializable;

public class UpdateClientMarketMessage extends Message implements Serializable {

    private final Market market;

    public UpdateClientMarketMessage(Market market) {
        this.market = market;
    }

    public Market getMarket() {
        return this.market;
    }
}
