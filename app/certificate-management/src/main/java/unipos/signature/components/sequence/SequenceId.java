package unipos.signature.components.sequence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dominik on 25.08.15.
 */

@Data
@Builder
@Document(collection = "sequence")
public class SequenceId {

    @Id
    private String id;

    private Long seq;

    public enum Name{
        UMSATZ_ZAEHLER;
    }
}
