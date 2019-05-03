package unipos.licenseChecker.component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dominik on 04.02.2016.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Module {

    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime expirationDate;
    private List<Device> devices = new ArrayList<>();
}
