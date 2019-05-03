package unipos.socket.components.socket;

import lombok.Data;

/**
 * Created by Dominik on 12.08.2015.
 */

@Data
public class Message {

    private int id;
    private String message;

    public Message() {}

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
