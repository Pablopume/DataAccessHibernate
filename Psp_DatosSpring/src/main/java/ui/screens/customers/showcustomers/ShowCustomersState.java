package ui.screens.customers.showcustomers;
import lombok.Data;
import model.Customer;

import java.util.List;


@Data

public class ShowCustomersState {

    private final List<Customer> listCustomers;
    private final String error;


}
