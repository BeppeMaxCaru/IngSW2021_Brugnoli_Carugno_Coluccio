package Message;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Message.MessageSent.DiscardLeaderMessage;
import Message.MessageSent.EndTurnMessage;
import Message.MessageSent.PlayLeaderMessage;
import Message.MessageSent.QuitMessage;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SendingMessages {

    private final ClientMain clientMain;
    private ObjectOutputStream sender;
    private final RenderingView view;

    public SendingMessages(ClientMain main, RenderingView view, ObjectOutputStream sender){
        this.clientMain = main;
        this.view= view;
        this.sender = sender;
    }

    public void sendNickname(){
        try {
            this.sender.reset();
            NicknameMessage nicknameMessage = new NicknameMessage(this.clientMain.getNickname());
            this.sender.writeObject(nicknameMessage);
        } catch (Exception e) {
            this.view.gameError(e);
            //e.printStackTrace();
        }
    }

    public void sendStartingRes(ArrayList<String> startingRes){
        try {
            this.sender.reset();
            StartingResourcesMessage resourcesMessage = new StartingResourcesMessage(this.clientMain.getPlayerNumber(), startingRes);
            this.sender.writeObject(resourcesMessage);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendDiscardedLeader(int leader){
        try {
            this.sender.reset();
            //System.out.println(leader);
            DiscardLeaderMessage discardLeaderMessage = new DiscardLeaderMessage(this.clientMain.getPlayerNumber(), leader);
            this.sender.writeObject(discardLeaderMessage);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendPlayedLeader(int leader){
        try {
            this.sender.reset();
            PlayLeaderMessage playLeaderMessage = new PlayLeaderMessage(this.clientMain.getPlayerNumber(), leader);
            this.sender.writeObject(playLeaderMessage);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendMarketAction(String parameter, int index, String wlChoice, String chosenMarble){
        try {
            this.sender.reset();
            MarketResourcesMessage resourcesMessage = new MarketResourcesMessage(this.clientMain.getPlayerNumber(), parameter, index, wlChoice, chosenMarble);
            this.sender.writeObject(resourcesMessage);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendBuyCardAction(int column, int level, int[] quantity, String[] shelf, int pos){
        try {
            this.sender.reset();
            BuyCardMessage buyCard = new BuyCardMessage(column, level, this.clientMain.getPlayerNumber(), quantity, shelf, pos);
            this.sender.writeObject(buyCard);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendActivationProdAction(int[] activation, String[] whichInput, String[] whichOutput){
        try {
            this.sender.reset();
            int[] outputs = new int[3];
            if(whichOutput!=null)
                for(int k=0; k< whichOutput.length; k++)
                {
                    if(whichOutput[k]!=null)
                        outputs[k] = Integer.parseInt(whichOutput[k]);
                }
            ActivateProdMessage prodMessage;
            prodMessage = new ActivateProdMessage(this.clientMain.getPlayerNumber(), activation, whichInput, outputs);
            this.sender.writeObject(prodMessage);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendQuitMessage() {
        try {
            this.sender.writeObject(new QuitMessage());
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendEndTurn(){
        try {
            this.sender.writeObject(new EndTurnMessage(this.clientMain.getPlayerNumber()));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
