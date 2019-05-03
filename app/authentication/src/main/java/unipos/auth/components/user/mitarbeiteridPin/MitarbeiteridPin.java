package unipos.auth.components.user.mitarbeiteridPin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.auth.components.user.LoginMethod;
import unipos.auth.components.user.User;
import unipos.common.remote.sync.model.Syncable;

import javax.persistence.Id;

/**
 * Created by Dominik on 02.06.2015.
 */
@Data
@Document(collection = "mitarbeiteridPin")
public class MitarbeiteridPin extends LoginMethod implements Syncable {

    @Id
    private String id;
    private int mitarbeiterid;
    private int pin;
    private String guid;

    public MitarbeiteridPin(){}

    public MitarbeiteridPin(int mitarbeiterid, int pin, User user) {
        this.mitarbeiterid = mitarbeiterid;
        this.pin = pin;
        this.user = user;
        this.guid = guid;
    }

    public MitarbeiteridPin(int mitarbeiterid, int pin, User user, String guid) {
        this.mitarbeiterid = mitarbeiterid;
        this.pin = pin;
        this.user = user;
        this.guid = guid;
    }

    @JsonIgnore
    public int getPin() {
        return pin;
    }

    @JsonProperty
    public void setPin(int pin) {
        this.pin = pin;
    }

}
