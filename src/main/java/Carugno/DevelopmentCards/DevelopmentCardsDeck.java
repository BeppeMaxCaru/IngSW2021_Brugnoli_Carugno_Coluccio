package Carugno.DevelopmentCards;

//uso decks solo per playerboard e in grid inizializzo carte una ad una
public class DevelopmentCardsDeck {

    private int index;
    private final DevelopmentCard[] developmentCardsDeck;

    //Si può inseire lo shuffle direttamente nel costruttore!
    public DevelopmentCardsDeck() {
        this.developmentCardsDeck = new DevelopmentCard[4];
        //carte singole da fare con il loro costruttore una alla volta per 4 volte
        //this.developmentCardsDeck[0] = new DevelopmentCard();
    }

    public void setDevelopmentCardsDeck() {

    }

}
