package unipos.auth.components.sequence;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dominik on 25.08.15.
 */

@Data
@Builder
@Document(collection = "productLogSequence")
public class SequenceId {

    @Id
    private SequenceTable id;

    private Long seq;
}
