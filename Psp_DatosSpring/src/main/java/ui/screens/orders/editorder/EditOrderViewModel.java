package ui.screens.orders.editorder;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Order;
import services.MenuItemService;
import services.OrderItemService;
import services.OrderServices;

import java.util.ArrayList;
import java.util.List;
@Data
public class EditOrderViewModel {
    private final MenuItemService menuItemService;
    private final OrderServices services;
    private final OrderItemService orderItemService;
    private final ObjectProperty<EditOrderState> state;

    @Inject
    public EditOrderViewModel(MenuItemService menuItemService, OrderServices services, OrderItemService orderItemService) {
        this.menuItemService = menuItemService;
        this.orderItemService = orderItemService;

        this.state = new SimpleObjectProperty<>(new EditOrderState(new ArrayList<>(), null));
        this.services = services;

    }

    public void voidState() {
        state.set(new EditOrderState(null, null));
    }

    public ReadOnlyObjectProperty<EditOrderState> getState(){return state;}

    public void loadState() {
        List<Order> listOrd = new ArrayList<>();
        if (services.getAll().isEmpty()) {
            state.set(new EditOrderState(listOrd, Constants.THERE_ARE_NO_ORDERS));


        }else {
            listOrd = services.getAll().get();
            state.set(new EditOrderState(listOrd,null));
        }
    }
}
