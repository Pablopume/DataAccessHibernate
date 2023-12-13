package ui.screens.orders.listorders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Order;
import services.OrderItemService;
import services.CustomerServices;
import services.OrderServices;

import java.util.ArrayList;
import java.util.List;
@Data
public class ListOrderViewModel {
    private final CustomerServices servicesCustomer;
    private final OrderServices services;
    private final OrderItemService orderItemService;

    private final ObjectProperty<ListOrderState> state;

    @Inject
    public ListOrderViewModel(CustomerServices servicesCustomer, OrderServices services, OrderItemService orderItemService) {
        this.servicesCustomer = servicesCustomer;
        this.orderItemService = orderItemService;

        this.state = new SimpleObjectProperty<>(new ListOrderState(new ArrayList<>(), null));
        this.services = services;

    }

    public void voidState() {
        state.set(new ListOrderState(null, null));
    }

    public ReadOnlyObjectProperty<ListOrderState> getState() {
        return state;
    }

    public void loadState() {
        List<Order> listOrd = new ArrayList<>();
        if (services.getAll().isEmpty()) {
            state.set(new ListOrderState(listOrd, Constants.THERE_ARE_NO_ORDERS));
        } else {
            state.set(new ListOrderState(listOrd, null));
        }
    }


}
