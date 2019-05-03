package unipos.common.remote.data.model.company;

import lombok.Builder;
import lombok.Data;

/**
 * Created by dominik on 04.09.15.
 */

@Data
@Builder
public class Address {

    private String street;
    private int postCode;
    private String city;
    private String country;

    public Address() {}

    public Address(String street, int postCode, String city, String country) {
        this.street = street;
        this.postCode = postCode;
        this.city = city;
        this.country = country;
    }
}
