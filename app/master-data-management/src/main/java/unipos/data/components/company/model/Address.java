package unipos.data.components.company.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

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
