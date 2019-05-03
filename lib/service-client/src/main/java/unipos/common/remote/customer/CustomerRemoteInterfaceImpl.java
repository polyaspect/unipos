package unipos.common.remote.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;
import unipos.common.remote.customer.model.Customer;

/**
 * Created by jolly on 21.05.2016.
 */
@Service
public class CustomerRemoteInterfaceImpl implements CustomerRemoteInterface {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Customer addCustomer(Customer customer) {
        ResponseEntity<Customer> responseEntity = restTemplate.postForEntity(UrlContainer.BASEURL + UrlContainer.CUSTOMER_ADD_CUSTOMER, customer, Customer.class);
        return responseEntity.getBody();
    }

    @Override
    public Customer findCustomerByGuid(String guid) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.CUSTOMER_FIND_BY_GUID + "/" + guid, Customer.class);
    }
}

