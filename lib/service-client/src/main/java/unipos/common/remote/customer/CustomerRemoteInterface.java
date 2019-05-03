package unipos.common.remote.customer;

import unipos.common.remote.customer.model.Customer;

/**
 * Created by jolly on 21.05.2016.
 */
public interface CustomerRemoteInterface {
    Customer addCustomer(Customer customer);

    Customer findCustomerByGuid(String guid);
}
