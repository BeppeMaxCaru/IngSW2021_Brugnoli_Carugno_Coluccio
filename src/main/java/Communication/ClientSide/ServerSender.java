package Communication.ClientSide;

import Communication.ClientSide.RenderingView.RenderingView;
import Message.*;
import Message.ActivateProdMessage;
import Message.MessageSent.DiscardLeaderMessage;
import Message.MessageSent.PlayLeaderMessage;

import java.io.*;
import java.net.Socket;

public class ServerSender extends Thread {

    private final Socket socket;
    private final ClientMain clientMain;
    private final ObjectOutputStream sender;
    private final RenderingView view;

    public ServerSender (ClientMain clientMain, Socket socket, ObjectOutputStream sender, RenderingView view) {
        this.socket = socket;
        this.clientMain = clientMain;
        this.sender = sender;
        this.view = view;
    }

    @Override
    public void run() {

        String action = "";
        //Keeps sending messages with switch until quit for now
        do {

            try {

                this.sender.reset();

                while (!action.equals("END TURN") && !action.equals("QUIT")) {

                    action = this.view.getActionChoice();

                    switch (action) {
                        case "P":
                        case "PLAY LEADER CARD": {

                            try {
                                int leader = this.view.getPlayedLeader();
                                PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(this.clientMain.getPlayerNumber(), leader);
                                this.sender.writeObject(playLeaderMessage);
                            } catch (Exception e) {
                                this.view.senderError(e);
                                break;
                            }
                            break;
                        }
                        case "D":
                        case "DISCARD LEADER CARD": {

                            try {
                                int leader = this.view.getDiscardedLeader();
                                DiscardLeaderMessage normalDiscardLeaderMessage = new DiscardLeaderMessage(this.clientMain.getPlayerNumber(), leader);
                                this.sender.writeObject(normalDiscardLeaderMessage);

                            } catch (Exception e) {
                                this.view.senderError(e);
                                break;
                            }
                            break;
                        }
                        case "M":
                        case "PICK RESOURCES FROM MARKET": {


                            int[] coordinates = this.view.getMarketCoordinates();
                            String parameter;
                            int index;
                            if(coordinates[0] == 0) parameter = "ROW";
                            else parameter = "COLUMN";
                            index = coordinates[1];
                            String wlChoice = this.view.getResourcesDestination();
                            String chosenMarble = this.view.getWhiteMarbleChoice();

                            MarketResourcesMessage resourcesMessage = new MarketResourcesMessage(this.clientMain.getPlayerNumber(), parameter, index, wlChoice, chosenMarble);
                            this.sender.writeObject(resourcesMessage);
                            break;
                        }
                        case "B":
                        case "BUY DEVELOPMENT CARD": {

                            int[] coordinates = this.view.getDevelopmentCardsGridCoordinates();

                            int column = coordinates[0];
                            int level = 3 - coordinates[1];

                            //Check
                            int[] quantity = new int[4];
                            String[] shelf;
                            String[][] pickedResources = this.view.getPayedResources();
                            for(int k = 0; k < quantity.length; k++)
                                quantity[k] = Integer.parseInt(pickedResources[0][k]);
                            shelf = pickedResources[1];
                            int pos = this.view.getChosenPosition();

                            //Sending Card request
                            BuyCardMessage buyCard = new BuyCardMessage(column, level, this.clientMain.getPlayerNumber(), quantity, shelf, pos);
                            this.sender.writeObject(buyCard);
                            break;
                        }
                        case "A":
                        case "ACTIVATE PRODUCTION POWER": {

                            int[] activation = {0, 0, 0, 0, 0, 0};
                            this.view.setActivation(activation);
                            String[] whichInput = new String[6];
                            String[] whichOutput = new String[3];

                            int stop;

                            do{
                                stop = this.view.getActivationProd();
                                if(stop<6){
                                    activation[stop] = 1;
                                    this.view.setActivation(activation);
                                    whichInput[stop] = this.view.getInputResourceProd();
                                    if(stop>2){
                                        whichOutput[stop-3] = this.view.getOutputResourceProd();
                                    }
                                }
                            } while (stop != 6);

                            for (int k = 0; k < 6; k++) {
                                ActivateProdMessage prodMessage;
                                if (activation[k] == 0) {
                                    prodMessage = new ActivateProdMessage(this.clientMain.getPlayerNumber(), null, null);
                                } else {
                                    if (k <3) prodMessage = new ActivateProdMessage(this.clientMain.getPlayerNumber(), whichInput[k], null);
                                    else prodMessage = new ActivateProdMessage(this.clientMain.getPlayerNumber(), whichInput[k], whichOutput[3-k]);
                                }
                                this.sender.writeObject(prodMessage);
                            }

                            break;
                        }
                        case "END TURN":
                        {
                            this.view.endTurn();
                            break;
                        }
                        case "QUIT":
                        {
                            this.view.quit();
                            break;
                        }
                    }
                    //Player inserisce quit
                }
            } catch (Exception e) {
                this.view.senderError(e);
                System.exit(1);
            }

        } while (!action.equals("QUIT"));

        try {
            this.socket.close();
        } catch (Exception e) {
            this.view.senderError(e);
        }
    }
}
