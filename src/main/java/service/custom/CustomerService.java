package service.custom;

import javafx.collections.ObservableList;
import dto.Customer;
import service.SuperService;

public interface CustomerService extends SuperService {
    boolean addCustomer(Customer customer);
    boolean deleteCustomer(String id);
    ObservableList<Customer> getAll();
    boolean updateCustomer(Customer customer);
    Customer searchCustomer(String name);

    ObservableList<String> getCustomerIds();
}
