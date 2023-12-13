package ui.screens.customers.editcustomers;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Customer;
import services.MenuItemService;
import services.CustomerServices;

import java.util.ArrayList;
import java.util.List;
@Data
public class EditCustomerViewModel {
    private final MenuItemService menuItemService;
    private final CustomerServices services;
    private final ObjectProperty<EditCustomerState> state;
    @Inject
    public EditCustomerViewModel(MenuItemService menuItemService, CustomerServices services) {
        this.menuItemService = menuItemService;
        this.services = services;
        this.state =  new SimpleObjectProperty<>(new EditCustomerState(new ArrayList<>(), null));;
    }

    public ReadOnlyObjectProperty<EditCustomerState> getState() {
        return state;
    }

    public void  voidState(){state.set(new EditCustomerState(null,null));}

    public void loadState() {
        List<Customer> listCust = new ArrayList<>();
        if (services.getAll().isEmpty()) {
            state.set(new EditCustomerState(listCust,"There are no customers"));


        }else {
            listCust = services.getAll().get();
            state.set(new EditCustomerState(listCust,null));
        }
    }
}
