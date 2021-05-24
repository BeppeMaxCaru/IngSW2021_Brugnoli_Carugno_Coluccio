package Communication.ServerSide;

import Maestri.MVC.GameController;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;
import Message.MessageReceived.ActionOutcomeMessage;
import Message.MessageSent.DiscardLeaderMessage;
import Message.MessageSent.EndTurnMessage;
import Message.MessageSent.PlayLeaderMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayerThread implements Runnable {

    private String nickName;
    private int playerThreadNumber;

    private int currentPlayerNumber;

    private Socket playerSocket;
    private Scanner inScanner;
    private PrintWriter outPrintWriter;

    private ObjectInputStream receiver;
    private ObjectOutputStream sender;

    //The controller for this player
    private GameController gameController;

    private boolean mainAction = false;

    //Il game controller va assegnato con setter siccome creato prima
    public PlayerThread(Socket clientSocket) {
        try {
            this.playerSocket = clientSocket;

            this.receiver = new ObjectInputStream(clientSocket.getInputStream());
            this.sender = new ObjectOutputStream(clientSocket.getOutputStream());

            //this.inScanner = new Scanner(new InputStreamReader(this.playerSocket.getInputStream()));
            //this.outPrintWriter = new PrintWriter(this.playerSocket.getOutputStream(), true);
            //this.gameController = gameController;

            //Va fatto con oggetti
            //this.outPrintWriter.println("Welcome to Masters of Renaissance online!");
            //this.outPrintWriter.println("Looking for a game in Florence...");

        } catch (Exception e) {
            e.printStackTrace();
            //Thread not working
            //Null this player
            return;
        }
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPlayerThreadNumber() {
        return this.playerThreadNumber;
    }

    public void setPlayerThreadNumber(int playerThreadNumber) {
        this.playerThreadNumber = playerThreadNumber;
    }

    public void setCurrentPlayerNumber(int currentPlayerNumber) {
        this.currentPlayerNumber = currentPlayerNumber;
    }

    public Scanner getInScanner() {
        return this.inScanner;
    }

    public Socket getPlayerSocket() { return this.playerSocket; }

    public void setInScanner(Scanner inScanner) {
        this.inScanner = inScanner;
    }

    public PrintWriter getOutPrintWriter() {
        return this.outPrintWriter;
    }

    public void setOutPrintWriter(PrintWriter outPrintWriter) {
        this.outPrintWriter = outPrintWriter;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void run() {

        //SWITCH THAT READS MESSAGES AND SENDS RESPONSED/DOES ACTIONS

        Player currentPlayer = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber];

        //SYNC PHASE
        try {
            NicknameMessage nicknameMessage = (NicknameMessage) this.receiver.readObject();
            this.nickName = nicknameMessage.getNickname();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ServerStartingMessage serverStartingMessage = new ServerStartingMessage(
                    this.playerThreadNumber, currentPlayer.getPlayerLeaderCards());
            sender.writeObject(serverStartingMessage);
        } catch (Exception e) {
            e.printStackTrace();
            //Errore
        }

        try {
            StartingResourcesMessage startingResourcesMessage = (StartingResourcesMessage) this.receiver.readObject();
            if(this.currentPlayerNumber == startingResourcesMessage.getPlayerNumber())
            {
                while (!startingResourcesMessage.getStartingRes().isEmpty())
                    currentPlayer.setStartingPlayerboard(startingResourcesMessage.getStartingRes().remove(0));
                this.sender.writeObject(true);
            } else this.sender.writeObject(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int cards=0; cards<2; cards++)
        {
            try {
                DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) receiver.readObject();
                if(this.currentPlayerNumber == discardLeaderMessage.getPlayerNumber())
                {
                    currentPlayer.discardLeaderCard(discardLeaderMessage.getDiscarded());
                    this.sender.writeObject(true);
                } else this.sender.writeObject(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //QUI INIZIA LA FASE ASINCRONA
        //
        //SOPRA HO INIZIATO LA FASE SINCRONA
        //
        //VANNO MESSI I CONTROLLI, CHIAMATI E METODI, ECC
        //
        //QUA SOTTO C'è IL CICLO CON SWITCH FINCHè UNO NON VINCE
        //
        //BISOGNA ANCORA GESTIRE IL BREAK

        //ASYNC PHASE

        while (true) {

            Object object = null;

            //SIMO DEVI CORREGERE I CONTROLLI SUL CURRENTPLAYER
            //
            //OGNI VOLTA CHE RIENTRI NEL CICLO WHILE VERIFICHI SE IL PLAYER SEGNATO COME
            //CORRENTE DAL GAMEMODEL COINCIDE CON QUELLO ASSEGNATO A QUESTO
            //THREAD E SE NON COINCIDONO INVII SUBITO OUTCOME(FALSE)
            //SE COINCIDONO LO FAI PROSEGUIRE
            //
            //IL CONTROLLO NELLE AZIONI è QUESTO:
            //
            //if (this.gameController.getCurrentPlayerNumber == this.playerThreadNumber) OK
            //SE FALSE INVII OUTCOME(FALSE)
            //
            //CONTROLLA I CONTROLLI

            //Player currentPlayer = this.gameController.getGameModel().getPlayers()[this.playerThreadNumber];

            //Receive object
            //Check current player here
            try {
                object = receiver.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Controllare var ausiliaria
            //Controlla main action
            //int mainAction = 0;
            //boolean mainAction = false;

            //Check currentPlayer
            //if (this.currentPlayerNumber == playMessage.getPlayerNumber())
            //PlayLeaderMessage
            if (object instanceof PlayLeaderMessage) {
                try {
                    PlayLeaderMessage playLeaderMessage = (PlayLeaderMessage) object;
                    if(this.currentPlayerNumber == playLeaderMessage.getPlayerNumber()) {

                        //First and only parameter is always an int that is the position of the leader card
                        int position = playLeaderMessage.getPlayed();

                        if (this.gameController.checkPlayCards(currentPlayer, position))
                            this.sender.writeObject(new ActionOutcomeMessage(true));
                        else this.sender.writeObject(new ActionOutcomeMessage(false));

                    } else this.sender.writeObject(new ActionOutcomeMessage(false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //DISCARD LEADER MESSAGE
            if (object instanceof DiscardLeaderMessage) {
                try {
                    DiscardLeaderMessage discardLeaderMessage = (DiscardLeaderMessage) object;
                    if(this.currentPlayerNumber == discardLeaderMessage.getPlayerNumber()) {

                        //First and only parameter is always an int that is the position of the leader card
                        int position = discardLeaderMessage.getDiscarded();

                        if (this.gameController.checkDiscardCards(currentPlayer, position))
                            this.sender.writeObject(new ActionOutcomeMessage(true));
                        else this.sender.writeObject(new ActionOutcomeMessage(false));

                    } else this.sender.writeObject(new ActionOutcomeMessage(false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //MARKET RESOURCE MESSAGE
            if (object instanceof MarketResourcesMessage) {
                try {
                    MarketResourcesMessage marketResourcesMessage = (MarketResourcesMessage) object;
                    if(this.currentPlayerNumber == marketResourcesMessage.getPlayerNumber() && !this.mainAction) {

                        //Row/column choice
                        String rowOrColumnChoice = marketResourcesMessage.getRowColumnChoice();
                        //Row/column index
                        int index = marketResourcesMessage.getIndex();
                        //Warehouse/leaderCard choice
                        String wlChoice = marketResourcesMessage.getWarehouseLeaderChoice();
                        //If he has 2 whiteMarbleLeaderCards
                        String chosenMarble = marketResourcesMessage.getWhichWhiteMarbleChoice();

                        if (this.gameController.checkMarketAction(currentPlayer, rowOrColumnChoice, index, wlChoice, chosenMarble)) {
                            this.sender.writeObject(new ActionOutcomeMessage(true));
                            this.mainAction = true;
                        }
                        else this.sender.writeObject(new ActionOutcomeMessage(false));

                    } else this.sender.writeObject(new ActionOutcomeMessage(false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //BISOGNA RENDERLO UN MESSAGGIO UNICO
            //
            //
            //
            //
            //
            //
            //


            //BUY DEVELOPMENT CARD
            if (object instanceof BuyCardMessage) {
                try {
                    BuyCardMessage buyCardMessage = (BuyCardMessage) object;

                    if (this.currentPlayerNumber == buyCardMessage.getPlayerNumber() && !this.mainAction) {

                        //DevCard colour
                        String colour = buyCardMessage.getColour();
                        int column = this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour.toUpperCase());
                        //DevCard level
                        int level = 3 - buyCardMessage.getLevel();
                        //How much resources does the player spend
                        int[] quantity = buyCardMessage.getQuantity();
                        //From which shelf does the player pick resources
                        String[] deposit = buyCardMessage.getShelf();

                        if (this.gameController.checkBuyDevCard(currentPlayer, colour, level, quantity, deposit)) {

                            int pos = buyCardMessage.getPlayerboardPosition();

                            if (currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0])) {
                                if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, pos, deposit)) {
                                    this.sender.writeObject(new ActionOutcomeMessage(true));
                                    this.mainAction = true;
                                } else {
                                    this.sender.writeObject(new ActionOutcomeMessage(false));
                                }
                            } else this.sender.writeObject(new ActionOutcomeMessage(false));

                            /*if (!currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0])) {
                                this.sender.writeObject(new ActionOutcomeMessage(false));
                                break;
                            }

                            if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, pos, deposit)) {
                                this.sender.writeObject(new ActionOutcomeMessage(true));
                                mainAction++;
                            } else {
                                this.sender.writeObject(new ActionOutcomeMessage(false));
                            }*/

                            //Old
                            /*ArrayList<Integer> correctPositions = new ArrayList<>();

                            for (int pos = 0; pos < 3; pos++)
                                if (currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0]))
                                    correctPositions.add(pos);

                            if (correctPositions.size() == 0)
                                this.sender.writeObject(new ActionOutcomeMessage(false));
                            else {
                                this.sender.writeObject(new ActionOutcomeMessage(true));

                                //METTILO GIà IN SENDER!!!!!!

                                ServerCardAvailabilityMessage availabilityMessage = new ServerCardAvailabilityMessage(correctPositions);
                                this.sender.writeObject(availabilityMessage);
                                this.sender.close();

                                DevCardPositionMessage positionMessage = (DevCardPositionMessage) this.receiver.readObject();

                                if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, positionMessage.getCardPosition(), deposit))
                                    this.sender.writeObject(new ActionOutcomeMessage(true));
                                else this.sender.writeObject(new ActionOutcomeMessage(false));*/
                            } else this.sender.writeObject(new ActionOutcomeMessage(false));
                        } else this.sender.writeObject(new ActionOutcomeMessage(false));
                    } //else this.sender.writeObject(new ActionOutcomeMessage(false));
                 catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //INPUT RESOURCE MESSAGE FOR PRODUCTION
            if (object instanceof InputResourceMessage) {
                try {
                    InputResourceMessage inputResourceMessage = (InputResourceMessage) object;

                    if (this.currentPlayerNumber == inputResourceMessage.getPlayerNumber() && !this.mainAction) {

                        int[] activation = new int[6];
                        String[] whichInput = new String[6];
                        int[] whichOutput = new int[3];

                        for (int k = 0; k < 6; k++) {
                            if (inputResourceMessage.getInputs() != null) {
                                activation[k] = 1;
                                whichInput[k] = inputResourceMessage.getInputs();
                                if (k >= 3) {
                                    OutputChoiceResourceMessage messageOutput = (OutputChoiceResourceMessage) this.receiver.readObject();
                                    whichOutput[k - 3] = Integer.parseInt(messageOutput.getResource());
                                }
                            } else activation[k] = 0;
                        }

                        if (this.gameController.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput)) {
                            this.sender.writeObject(new ActionOutcomeMessage(true));
                            this.mainAction = true;
                        }
                        else this.sender.writeObject(new ActionOutcomeMessage(false));

                    } else this.sender.writeObject(new ActionOutcomeMessage(false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (object instanceof EndTurnMessage) {
                this.mainAction = false;
                //Salva come giocatore corrente nel gamecontroller/gamemodel
                //il giocatore successivo a questo per abilitarlo e bloccare questo
            }

            //BISOGNA GESTIRE ATTENTAMENTE IL BREAK E LA SEQUENZA DI OPERAZIONI CHE CAUSA
            //
            //CONTROLLATE CHE HO RICOPIATO CORRETTAMENTE I FALSE E I TRUE
            //IN ACTIONOUTCOMEMESSAGE
            //
            //
            //
            //
            //



            /*try {

                Object object = receiver.readObject();
                //Ci
                PlayLeaderMessage playMessage;
                DiscardLeaderMessage discardMessage;
                MarketResourcesMessage marketMessage;
                BuyCardMessage buyMessage;
                InputResourceMessage activationMessage;

                int mainAction = 0;

                //PLAY LEADER MESSAGE
                if (object instanceof PlayLeaderMessage) {

                    playMessage = (PlayLeaderMessage) object;
                    if(this.currentPlayerNumber == playMessage.getPlayerNumber()) {

                        //First and only parameter is always an int that is the position of the leader card
                        int position = playMessage.getPlayed();

                        if (this.gameController.checkPlayCards(currentPlayer, position))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                //DISCARD LEADER MESSAGE
                if (object instanceof DiscardLeaderMessage) {

                    discardMessage = (DiscardLeaderMessage) object;
                    if(this.currentPlayerNumber == discardMessage.getPlayerNumber()) {

                        //First and only parameter is always an int that is the position of the leader card
                        int position = discardMessage.getDiscarded();

                        if (this.gameController.checkDiscardCards(currentPlayer, position))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                //MARKET RESOURCE
                if (object instanceof MarketResourcesMessage) {

                    marketMessage = (MarketResourcesMessage) object;
                    if(this.currentPlayerNumber == marketMessage.getPlayerNumber()) {

                        //Row/column choice
                        String rowOrColumnChoice = marketMessage.getRowColumnChoice();
                        //Row/column index
                        int index = marketMessage.getIndex();
                        //Warehouse/leaderCard choice
                        String wlChoice = marketMessage.getWarehouseLeaderChoice();
                        //If he has 2 whiteMarbleLeaderCards
                        String chosenMarble = marketMessage.getWhichWhiteMarbleChoice();

                        if (this.gameController.checkMarketAction(currentPlayer, rowOrColumnChoice, index, wlChoice, chosenMarble))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                //BUY DEVELOPMENT CARD
                if (object instanceof BuyCardMessage) {

                    buyMessage = (BuyCardMessage) object;

                    if(this.currentPlayerNumber == buyMessage.getPlayerNumber()) {

                        //DevCard colour
                        String colour = buyMessage.getColour();
                        int column = this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsColours().get(colour.toUpperCase());
                        //DevCard level
                        int level = 3 - buyMessage.getLevel();
                        //How much resources does the player spend
                        int[] quantity = buyMessage.getQuantity();
                        //From which shelf does the player pick resources
                        String[] deposit = buyMessage.getShelf();

                        if(this.gameController.checkBuyDevCard(currentPlayer, colour, level, quantity, deposit))
                        {
                            ArrayList<Integer> correctPositions = new ArrayList<>();

                            for (int pos=0; pos<3; pos++)
                                if(currentPlayer.getPlayerBoard().isCardBelowCompatible(pos, this.gameController.getGameModel().getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[level][column][0]))
                                    correctPositions.add(pos);

                            if (correctPositions.size() == 0)
                                this.sender.writeObject(false);
                            else {
                                this.sender.writeObject(true);

                                ServerCardAvailabilityMessage availabilityMessage = new ServerCardAvailabilityMessage(correctPositions);
                                this.sender.writeObject(availabilityMessage);
                                this.sender.close();

                                DevCardPositionMessage positionMessage = (DevCardPositionMessage) this.receiver.readObject();

                                if (this.gameController.getGameModel().buyDevelopmentCardAction(currentPlayer.getPlayerNumber(), column, level, positionMessage.getCardPosition(), deposit))
                                    this.sender.writeObject(true);
                                else this.sender.writeObject(false);
                            }
                        } else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

                //INPUT RESOURCE MESSAGE FOR PRODUCTION
                if (object instanceof InputResourceMessage) {

                    activationMessage = (InputResourceMessage) object;

                    if(this.currentPlayerNumber == activationMessage.getPlayerNumber()) {

                        int[] activation = new int[6];
                        String[] whichInput = new String[6];
                        int[] whichOutput = new int[3];

                        for(int k = 0; k < 6; k++) {
                            if (activationMessage.getInputs() != null) {
                                activation[k]=1;
                                whichInput[k] = activationMessage.getInputs();
                                if(k >= 3) {
                                    OutputChoiceResourceMessage messageOutput = (OutputChoiceResourceMessage) this.receiver.readObject();
                                    whichOutput[k - 3] = Integer.parseInt(messageOutput.getResource());
                                }
                            } else activation[k]=0;
                        }

                        if(this.gameController.checkActivateProduction(currentPlayer, activation, whichInput, whichOutput))
                            this.sender.writeObject(true);
                        else this.sender.writeObject(false);

                    } else this.sender.writeObject(false);
                }

            } catch (Exception e) {
                break;
            }*/
            //break;
        }
    }

}
