package model.model2;

import jakarta.persistence.*;
import model.MenuItem;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "menu_items", schema = "pabloserrano_restaurant")
public class MenuItemsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "menu_item_id", nullable = false)
    private int menuItemId;
    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 100)
    private String description;
    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    private double price;
    @OneToMany(mappedBy = "menuItemsByMenuItemId")
    private Collection<OrderItemsEntity> orderItemsByMenuItemId;

    public MenuItem toMenuItem() {
        return new MenuItem(menuItemId, name, description, price);
    }
    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItemsEntity that = (MenuItemsEntity) o;
        return menuItemId == that.menuItemId && Double.compare(price, that.price) == 0 && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuItemId, name, description, price);
    }

    public Collection<OrderItemsEntity> getOrderItemsByMenuItemId() {
        return orderItemsByMenuItemId;
    }

    public void setOrderItemsByMenuItemId(Collection<OrderItemsEntity> orderItemsByMenuItemId) {
        this.orderItemsByMenuItemId = orderItemsByMenuItemId;
    }
}
