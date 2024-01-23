package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.model2.OrderItemsEntity;
import model.model2.OrdersEntity;
import model.model2.RestaurantTablesEntity;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class Order {
    private int id;
    private LocalDateTime date;
    private int customer_id;
    private int table_id;
    private List<OrderItem> orderItemList;

    public Order(LocalDateTime date, int customer_id, int table_id, List<OrderItem> orderItemList) {
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
        this.orderItemList = orderItemList;
    }

    public Order(LocalDateTime date, int customer_id, int table_id) {
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
    }

    public Order(int id, LocalDateTime date, int customer_id, int table_id) {
        this.id = id;
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
    }

    public Order(String fileLine) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[] elemArray = fileLine.split(";");
        this.id = Integer.parseInt(elemArray[0]);
        this.date = LocalDateTime.parse(elemArray[1], formatter);
        this.customer_id = Integer.parseInt(elemArray[2]);
        this.table_id = Integer.parseInt(elemArray[3]);

    }

    public OrdersEntity toOrdersEntity() {

        int numberSeats;
        if (table_id == 1) {
            numberSeats = 4;

        } else if (table_id == 2) {
            numberSeats = 6;
        } else if (table_id == 3) {
            numberSeats = 4;
        } else if (table_id == 4) {
            numberSeats = 2;
        } else {
            numberSeats = 2;
        }
        return new OrdersEntity(Timestamp.valueOf(date), customer_id, table_id, new RestaurantTablesEntity(table_id,numberSeats ), new ArrayList<>());
    }

    public String toStringTextFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return id + ";" + date.format(formatter) + ";" + customer_id + ";" + table_id;
    }
}
