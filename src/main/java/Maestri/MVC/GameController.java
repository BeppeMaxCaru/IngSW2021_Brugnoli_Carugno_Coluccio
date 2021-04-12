package Maestri.MVC;

import Maestri.MVC.Model.GModel.GameModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameController {

    private GameModel gameModel;
    private final Map<Integer, String> playerActions;

    Scanner playerInput = new Scanner(System.in);

    public GameController() {
        playerActions = new HashMap<>();
        this.playerActions.put(1,"Get resources from market");
        this.playerActions.put(2,"Buy development card");
        this.playerActions.put(3, "Activate production");
    }

    private int getPlayerAction() {
        for (Integer key : playerActions.keySet()) {
            System.out.println(" " + key + " : " + playerActions.get(key));
        }
        System.out.println("");
        System.out.println("Insert action number to execute: ");
        int action = playerInput.nextInt();
        System.out.println("");
        while (!playerActions.containsKey(action)) {
            System.out.println("Action not valid");
            System.out.println("Insert valid action number to execute: ");
            action = playerInput.nextInt();
        }
        return action;
    }

}
