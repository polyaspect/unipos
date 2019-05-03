package unipos.common.remote.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jolly on 21.05.2016.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address  implements Serializable {
    private String street;
    private String houseNumber;
    private String town;
    private String plz;
    private String state;
    private String country;
}
