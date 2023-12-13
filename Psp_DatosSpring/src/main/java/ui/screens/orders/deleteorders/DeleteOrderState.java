package ui.screens.orders.deleteorders;

import lombok.Data;
import model.Order;

import java.util.List;
@Data
public class DeleteOrderState {
    private final List<Order> listOrders;
    private final String error;

}
