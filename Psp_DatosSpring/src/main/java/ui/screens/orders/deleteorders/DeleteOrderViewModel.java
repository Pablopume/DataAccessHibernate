package ui.screens.orders.deleteorders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.Order;
import services.OrderItemService;
import services.OrderServices;
import ui.screens.orders.listorders.ListOrderState;

import java.util.ArrayList;
import java.util.List;
@Data
public class DeleteOrderViewModel {
    private final OrderServices services;
    private final OrderItemService orderItemService;
    private final ObjectProperty<DeleteOrderState> state;



    @Inject
    public DeleteOrderViewModel(OrderServices services, OrderItemService orderItemService) {
        this.orderItemService = orderItemService;

        this.state = new SimpleObjectProperty<>(new DeleteOrderState(new ArrayList<>(), null));
        this.services = services;

    }
    public void voidState() {
        state.set(new DeleteOrderState(null, null));
    }
    public ReadOnlyObjectProperty<DeleteOrderState> getState(){return state;}

    public void loadState() {
        List<Order> listOrd = new ArrayList<>();
            if (!services.getAll().isEmpty()) {
                listOrd = services.getAll().get();
            }
            state.set(new DeleteOrderState(listOrd, null));




        }

}
