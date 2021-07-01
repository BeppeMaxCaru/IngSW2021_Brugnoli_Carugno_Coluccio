package Communication.ClientSide.RenderingView.CLI;

import Communication.ClientSide.ClientMain;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Message.*;

public class ServerSender extends Thread {

    private final ClientMain clientMain;
    private final CLI cli;
    private final int gameMode;
    private SendingMessages msg;

    public ServerSender (ClientMain clientMain, int gameMode, SendingMessages msg) {
        this.clientMain = clientMain;
        this.cli = new CLI(this.clientMain);
        this.gameMode = gameMode;
        if(this.gameMode==1)
            this.msg = msg;
    }

    @Override
    public void run() {

            String action = "";
            int mainAction = 0;
            //Keeps sending messages with switch until quit for now
            do {

                try {

                    while (!action.equals("END TURN")) {

                        action = this.cli.getActionChoice();

                        switch (action) {
                            case "P":
                            case "PLAY LEADER CARD": {

                                //Before
                                //int leader = this.cli.getPlayedLeader();

                                //After
                                //Test
                                int leader = this.cli.cardPlayer();

                                if(gameMode==0) {
                                    this.clientMain.getLocalPlayers()[0].playLeaderCardUpdated(leader);
                                    this.clientMain.setLeaderCards(this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards());
                                    /*if(this.clientMain.checkLocalLeaders(this.clientMain.getLocalPlayers()[0], leader))
                                    {
                                        if(!this.clientMain.getLocalPlayers()[0].playLeaderCard(leader))
                                            this.cli.notValidAction();
                                    }*/
                                } else this.msg.sendPlayedLeader(leader);
                                break;
                            }
                            case "D":
                            case "DISCARD LEADER CARD": {

                                //Before
                                //int leader = this.cli.getDiscardedLeader();

                                //After
                                //Test
                                //int leader = this.cli.discarder();

                                //After after
                                int leader = this.cli.discarder();

                                if(this.gameMode==0){
                                    //leader = this.cli.getDiscardedLeader();
                                    this.clientMain.getLocalPlayers()[0].discardLeaderCard(leader);
                                    this.clientMain.setLeaderCards(this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards());
                                    //this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards();
                                    //System.out.println(leader);


                                    //For testing
                                    //System.out.println(Arrays.toString(this.clientMain.getLeaderCards()));



                                    //leader = this.cli.getDiscardedLeader();
                                    //this.clientMain.getLocalPlayers()[0].discardLeaderCard(leader);
                                    /*if(this.clientMain.checkLocalLeaders(this.clientMain.getLocalPlayers()[0], leader)){
                                        if(!this.clientMain.getLocalPlayers()[0].discardLeaderCard(leader))
                                            this.cli.notValidAction();
                                    }*/
                                    //System.out.println(Arrays.toString(this.clientMain.getLocalPlayers()[0].getPlayerLeaderCards()));
                                } else {
                                    this.msg.sendDiscardedLeader(leader);
                                }
                                break;
                            }
                            case "M":
                            case "PICK RESOURCES FROM MARKET": {

                                if(this.gameMode==0 && mainAction==1)
                                {
                                    this.cli.notValidAction();
                                    break;
                                }

                                int[] coordinates = this.cli.getMarketCoordinates();
                                String parameter;
                                int index;
                                if(coordinates[0] == -1) break;
                                if(coordinates[0] == 0) parameter = "ROW";
                                else parameter = "COLUMN";
                                index = coordinates[1];
                                String wlChoice = this.cli.getResourcesDestination(parameter);
                                String chosenMarble = this.cli.getWhiteMarbleChoice();

                                if(this.gameMode==0) {
                                    if (this.clientMain.checkLocalMarketAction(this.clientMain.getLocalPlayers()[0].getPlayerBoard(), parameter, index, wlChoice, chosenMarble))
                                        mainAction++;
                                    else this.cli.notValidAction();
                                } else {
                                    this.msg.sendMarketAction(parameter, index, wlChoice, chosenMarble);
                                }
                                break;
                            }
                            case "B":
                            case "BUY DEVELOPMENT CARD": {

                                if(this.gameMode==0 && mainAction==1)
                                {
                                    this.cli.notValidAction();
                                    break;
                                }

                                int[] coordinates = this.cli.getDevelopmentCardsGridCoordinates();
                                int column = coordinates[0];
                                if(column==-1) break;
                                int level = coordinates[1];

                                //Check
                                int[] quantity = new int[4];
                                String[] shelf;
                                String[][] pickedResources = this.cli.getPayedResources();
                                for(int k = 0; k < quantity.length; k++)
                                    quantity[k] = Integer.parseInt(pickedResources[0][k]);
                                shelf = pickedResources[1];
                                int pos = this.cli.getChosenPosition();

                                if(this.gameMode==0){
                                    if(this.clientMain.checkLocalBuyCard(this.clientMain.getLocalPlayers()[0], column, level, quantity, shelf))
                                    {
                                        if(this.clientMain.getLocalPlayers()[0].buyDevelopmentCard(this.clientMain.getDevelopmentCardsDecksGrid(), column, level, pos, shelf))
                                            mainAction++;
                                        else this.cli.notValidAction();
                                    }
                                } else this.msg.sendBuyCardAction(column, level, quantity, shelf, pos);
                                break;
                            }
                            case "A":
                            case "ACTIVATE PRODUCTION POWER": {

                                if(this.gameMode==0 && mainAction==1)
                                {
                                    this.cli.notValidAction();
                                    break;
                                }

                                int[] activation = {0, 0, 0, 0, 0, 0};
                                String[] whichInput = new String[6];
                                String[] whichOutput = new String[3];

                                int stop;

                                do{
                                    stop = this.cli.getActivationProd(activation);
                                    if(stop<6){
                                        activation[stop] = 1;
                                        whichInput[stop] = this.cli.getInputResourceProd();
                                        if(stop>2){
                                            whichOutput[stop-3] = this.cli.getOutputResourceProd(stop);
                                        }
                                    }
                                } while (stop != 6);

                                int[] outputs = new int[3];
                                for(int k = 0; k < whichOutput.length; k++) {
                                    if(whichOutput[k] != null) outputs[k] = Integer.parseInt(whichOutput[k]);
                                    else outputs[k] = -1;
                                }

                                if(this.gameMode==0)
                                {
                                    if(this.clientMain.checkLocalActivateProd(this.clientMain.getLocalPlayers()[0], activation, whichInput))
                                        if(this.clientMain.getLocalPlayers()[0].activateProduction(activation, whichInput, outputs))
                                            mainAction++;
                                        else this.cli.notValidAction();
                                } else {
                                    this.msg.sendActivationProdAction(activation, whichInput, whichOutput);
                                }
                                break;
                            }
                            case "END TURN":
                            {

                                if(this.gameMode==0 && mainAction==0)
                                    this.cli.notValidAction();
                                else if (this.gameMode==0 && mainAction==1)
                                {
                                    mainAction=0;
                                    action="";
                                    this.cli.actionCounter(this.clientMain.getActionCountersDeck().getActionCountersDeck()[this.clientMain.getActionCountersDeck().getTop()]);
                                    this.clientMain.getActionCountersDeck().drawCounter().activate(this.clientMain.getActionCountersDeck(), this.clientMain.getLocalPlayers()[1].getPlayerBoard(), this.clientMain.getDevelopmentCardsDecksGrid());
                                }
                                if(this.gameMode==1)
                                {
                                    action="";
                                    this.msg.sendEndTurn();
                                }
                                if(this.gameMode==0) this.cli.lorenzoFaithPoints();
                                break;
                            }
                            case "QUIT":
                            {
                                this.msg.sendQuitMessage();
                                this.cli.quit();
                                return;
                            }
                        }
                        //Player writes quit
                    }
                } catch (Exception e) {
                    this.cli.senderError(e);
                    //System.exit(1);
                }
            } while (!this.endLocalGame(this.clientMain.getLocalPlayers()));
    }

    public boolean endLocalGame(Player[] localPlayers){
        for(int k=0; k < 4; k++)
            if(this.clientMain.getDevelopmentCardsDecksGrid().getDevelopmentCardsDecks()[0][k][0]==null)
                return true;

        if(localPlayers[0].getPlayerBoard().getFaithPath().getCrossPosition()==24)
            return true;

        if(localPlayers[1].getPlayerBoard().getFaithPath().getCrossPosition()==24)
            return true;

        return localPlayers[0].getPlayerBoard().getDevelopmentCardsBought() == 7;
    }

}
