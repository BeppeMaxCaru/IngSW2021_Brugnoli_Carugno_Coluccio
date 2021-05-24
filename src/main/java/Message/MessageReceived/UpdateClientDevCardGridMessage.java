package Message.MessageReceived;

import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Message.Message;

import java.io.Serializable;

public class UpdateClientDevCardGridMessage extends Message implements Serializable {

    DevelopmentCardsDecksGrid developmentCardsDecksGrid;

    public UpdateClientDevCardGridMessage(DevelopmentCardsDecksGrid developmentCardsDecksGrid) {
        this.developmentCardsDecksGrid = developmentCardsDecksGrid;
    }

    public DevelopmentCardsDecksGrid getDevelopmentCardsDecksGrid() {
        return this.developmentCardsDecksGrid;
    }
}
