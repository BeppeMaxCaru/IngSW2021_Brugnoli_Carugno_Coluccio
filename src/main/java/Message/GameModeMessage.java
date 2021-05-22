package Message;

import java.io.Serializable;

public class GameModeMessage implements Serializable {

    private int gameMode;

    public GameModeMessage(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getGameMode() {
        return this.gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }
}
