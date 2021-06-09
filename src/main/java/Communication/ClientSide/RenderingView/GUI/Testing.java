package Communication.ClientSide.RenderingView.GUI;

import Communication.ClientSide.ClientMain;
import Communication.ClientSide.RenderingView.RenderingView;
import Communication.ClientSide.ServerReceiver;
import javafx.application.Application;
import javafx.stage.Stage;

public class Testing extends Application implements RenderingView {

    private ClientMain clientMain;


    //FARE TUTTO IN START!!!!!!!!!!!!!!!
    @Override
    public void start(Stage stage) throws Exception {
        Parameters args = getParameters();
        this.clientMain = new ClientMain(args.getUnnamed().get(0), Integer.parseInt(args.getUnnamed().get(1)));
        System.out.println(this.clientMain.getHostName());
        stage.setTitle("Welcome");
    }

    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
        System.out.println(this.clientMain.getClass());
    }

}
