package ui.screens.customers.deletecustomer;

import lombok.Data;
import model.Customer;

import java.util.List;
@Data
public class DeleteCustomerState {
    private final List<Customer> listCustomers;
    private final String error;

}
