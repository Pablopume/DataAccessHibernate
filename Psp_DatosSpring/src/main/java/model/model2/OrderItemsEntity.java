package model.model2;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import model.OrderItem;

import java.util.Objects;
@NoArgsConstructor
@Entity
@Table(name = "order_items", schema = "pabloserrano_restaurant")
public class OrderItemsEntity {

    public OrderItemsEntity( int menuItemId, int quantity) {

        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    private int orderId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_item_id", nullable = false)
    private int orderItemId;

    @Basic
    @Column(name = "menu_item_id", nullable = false)
    private int menuItemId;
    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private OrdersEntity ordersByOrderId;
    @ManyToOne
    @JoinColumn(name = "menu_item_id", referencedColumnName = "menu_item_id", nullable = false, insertable = false, updatable = false)
    private MenuItemsEntity menuItemsByMenuItemId;

    public OrderItem toOrderItem() {
        return new OrderItem( menuItemsByMenuItemId.toMenuItem(), quantity);
    }
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }





    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public OrdersEntity getOrdersByOrderId() {
        return ordersByOrderId;
    }

    public void setOrdersByOrderId(OrdersEntity ordersByOrderId) {
        this.ordersByOrderId = ordersByOrderId;
    }

    public MenuItemsEntity getMenuItemsByMenuItemId() {
        return menuItemsByMenuItemId;
    }

    public void setMenuItemsByMenuItemId(MenuItemsEntity menuItemsByMenuItemId) {
        this.menuItemsByMenuItemId = menuItemsByMenuItemId;
    }
}
