package unipos.common.remote.customer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jolly on 21.05.2016.
 */
@Data
@Builder
@AllArgsConstructor
public class Customer implements Serializable {
    @Id
    private String id;
    private String guid;
    private String name;
    private String surname;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate gebDate;
    private List<String> emails;
    private List<String> telefonNumbers;
    private Address shippingAddress;
    private Address billingAddress;
    private String companyGuid;

    public Customer() {
        emails = new ArrayList<>();
        telefonNumbers = new ArrayList<>();
    }
}

