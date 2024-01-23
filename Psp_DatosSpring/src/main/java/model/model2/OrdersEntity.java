package model.model2;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import model.Order;
import model.OrderItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
@NoArgsConstructor
@Entity
@Table(name = "orders", schema = "pabloserrano_restaurant")
public class OrdersEntity {
    public OrdersEntity( Timestamp orderDate, int customerId, int tableId, RestaurantTablesEntity restaurantTablesByTableId) {
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.tableId = tableId;
        this.restaurantTablesByTableId = restaurantTablesByTableId;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;
    @Basic
    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;
    @Basic
    @Column(name = "customer_id", nullable = false)
    private int customerId;
    @Basic
    @Column(name = "table_id", nullable = false, insertable = false, updatable = false)
    private int tableId;
    @OneToMany(mappedBy = "ordersByOrderId",cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    private Collection<OrderItemsEntity> orderItemsByOrderId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private CustomersEntity customersByCustomerId;
    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "table_number_id", nullable = false)
    private RestaurantTablesEntity restaurantTablesByTableId;

    public Order toOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemsEntity orderItemsEntity : orderItemsByOrderId) {
            orderItems.add(orderItemsEntity.toOrderItem());
        }
        return new Order(orderId, orderDate.toLocalDateTime(), customersByCustomerId.getId(), restaurantTablesByTableId.getTableNumberId(),orderItems);
    }




    public Collection<OrderItemsEntity> getOrderItemsByOrderId() {
        return orderItemsByOrderId;
    }

    public void setOrderItemsByOrderId(Collection<OrderItemsEntity> orderItemsByOrderId) {
        this.orderItemsByOrderId = orderItemsByOrderId;
    }

    public CustomersEntity getCustomersByCustomerId() {
        return customersByCustomerId;
    }

    public void setCustomersByCustomerId(CustomersEntity customersByCustomerId) {
        this.customersByCustomerId = customersByCustomerId;
    }

    public RestaurantTablesEntity getRestaurantTablesByTableId() {
        return restaurantTablesByTableId;
    }

    public void setRestaurantTablesByTableId(RestaurantTablesEntity restaurantTablesByTableId) {
        this.restaurantTablesByTableId = restaurantTablesByTableId;
    }
}
