package ui.screens.orders.listorders;

import lombok.Data;
import model.Order;

import java.util.List;

@Data
public class ListOrderState {
    private final List<Order> listOrders;
    private final String error;
}
