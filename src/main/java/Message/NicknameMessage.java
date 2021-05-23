package Message;

import java.io.Serializable;
import java.util.Scanner;

public class NicknameMessage extends Message implements Serializable {

    private String nickname;

    public NicknameMessage (String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
