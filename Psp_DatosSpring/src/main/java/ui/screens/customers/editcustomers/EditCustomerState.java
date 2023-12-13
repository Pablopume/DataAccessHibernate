package ui.screens.customers.editcustomers;

import lombok.Data;
import model.Customer;

import java.util.List;
@Data
public class EditCustomerState {
    private final List<Customer> listCustomers;
    private final String error;

}
