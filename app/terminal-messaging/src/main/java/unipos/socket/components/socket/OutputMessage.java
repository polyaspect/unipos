package unipos.socket.components.socket;

import lombok.Data;

import java.util.Date;

/**
 * Created by Dominik on 12.08.2015.
 */

@Data
public class OutputMessage extends Message {

    private Date time;

    public OutputMessage(Message message, Date time ) {
        super.setId(message.getId());
        super.setMessage(message.getMessage());
        this.time = time;
    }
}
