package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.CLI.ServerSender;
import Communication.ClientSide.ServerReceiver;
import Maestri.MVC.Model.GModel.ActionCounters.ActionCountersDeck;
import Maestri.MVC.Model.GModel.DevelopmentCards.DevelopmentCardsDecksGrid;
import Maestri.MVC.Model.GModel.GamePlayer.Player;
import Maestri.MVC.Model.GModel.LeaderCards.LeaderCardDeck;
import Maestri.MVC.Model.GModel.MarbleMarket.Market;
import Message.SendingMessages;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class InitialScenarioGUI {

    private final HandlerGUI handlerGUI;

    public InitialScenarioGUI(HandlerGUI handlerGUI) {
        this.handlerGUI = handlerGUI;
    }

    public void nickname(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);

        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(cons1, cons2);

        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);

        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rcons1, rcons2);

        Label lbl = new Label("Insert nickname:");
        TextField field = new TextField();
        Button okBtn = new Button("Ok");

        GridPane.setHalignment(okBtn, HPos.RIGHT);

        root.add(lbl, 0, 0);
        root.add(field, 1, 0, 3, 1);
        root.add(okBtn, 2, 1);

        Scene scene = new Scene(root, 300, 100);

        stage.setTitle("Choose nickname");
        stage.setScene(scene);
        stage.show();

        okBtn.setOnAction(e -> {
            //this.clientMain.setNickname(field.getText());
            this.handlerGUI.getClientMain().setNickname(field.getText());
            multiOrSinglePlayers(stage);
        });
    }

    public void multiOrSinglePlayers(Stage stage) {
        GridPane root = new GridPane();
        Button button1 = new Button("Single player");
        Button button2 = new Button("Multi player");
        root.add(button1, 0, 0);
        root.add(button2, 3, 0);

        Scene scene = new Scene(root, 300, 100);

        stage.setTitle("Multi Or Single Players?");
        stage.setScene(scene);
        stage.show();

        button1.setOnAction(e -> {
            this.handlerGUI.setGameMode(0);
            Player[] localPlayers = new Player[2];
            localPlayers[0] = new Player(this.handlerGUI.getClientMain().getNickname(), 0);
            localPlayers[1] = new Player("Lorenzo the Magnificent", 1);

            this.handlerGUI.getClientMain().setLocalPlayers(localPlayers);
            this.handlerGUI.getClientMain().setMarket(new Market());
            this.handlerGUI.getClientMain().setDevelopmentCardsDecksGrid(new DevelopmentCardsDecksGrid());
            this.handlerGUI.getClientMain().setActionCountersDeck(new ActionCountersDeck());
            this.handlerGUI.getClientMain().setLeaderCardDeck(new LeaderCardDeck());
            this.handlerGUI.getClientMain().setPlayerboard(localPlayers[0].getPlayerBoard());

            //Put 4 leader cards into first player space
            for(int index = 0; index < localPlayers[0].getPlayerLeaderCards().length; index++)
                this.handlerGUI.getClientMain().getLocalPlayers()[0].setPlayerLeaderCard(index, this.handlerGUI.getClientMain().getLeaderCardDeck().drawOneLeaderCard());
            this.handlerGUI.getClientMain().setLeaderCards(this.handlerGUI.getClientMain().getLocalPlayers()[0].getPlayerLeaderCards());

            this.handlerGUI.getSyncScenarioGUI().discardStartingLeaders(stage, 1, -1);
        });

        button2.setOnAction(e -> {
            this.handlerGUI.connectionSocket();
            this.handlerGUI.sendNickname();
            this.handlerGUI.setGameMode(1);
            welcome(stage);
        });
    }

    public void welcome(Stage stage) {
        this.handlerGUI.getGenericClassGUI().addLabelByCode("Loading...\nHi " + this.handlerGUI.getClientMain().getNickname() +
                "!\nWelcome to Master of Renaissance online!", stage);
        this.handlerGUI.updateMarket();
    }
}
