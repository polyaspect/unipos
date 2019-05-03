package unipos.common.remote.data.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.sync.model.Syncable;


/**
 * Created by Dominik on 23.07.2015.
 */

@Data
@Builder
public class PaymentMethod implements Syncable {

    private String id;
    private Long paymentMethodIdentifier;
    private String name;
    private Type type;
    private boolean activated = false;
    private String guid;

    public PaymentMethod() {}

    public PaymentMethod(String name) {
        this.name = name;
    }

    public PaymentMethod(String id, Long paymentMethodIdentifier, String name, Type type, boolean activated, String guid) {
        this.id = id;
        this.paymentMethodIdentifier = paymentMethodIdentifier;
        this.name = name;
        this.type = type;
        this.activated = activated;
        this.guid = guid;
    }

    public enum Type {
        BAR(true),
        BANKOMAT(true),
        KREDITKARTE(true),
        GUTSCHEIN(true),
        ESSENSBON(true),
        RECHNUNG(false);

        private boolean isBarumsatz;

        private Type(boolean isBarumsatz) {
            this.isBarumsatz = isBarumsatz;
        }

        public boolean isBarumsatz() {
            return isBarumsatz;
        }
    }
}
