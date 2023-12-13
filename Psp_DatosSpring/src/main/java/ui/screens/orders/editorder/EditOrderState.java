package ui.screens.orders.editorder;

import lombok.Data;
import model.Order;

import java.util.List;
@Data
public class EditOrderState {
    private final List<Order> listOrders;
    private final String error;
}
