package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.model2.OrderItemsEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private int id;
    private int idOrder;
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(  MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public OrderItemsEntity toOrderItemsEntity() {
        return new OrderItemsEntity(  menuItem.getId(), quantity);
    }
}
