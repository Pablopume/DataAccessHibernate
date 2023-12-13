package services;

import io.vavr.control.Either;
import model.Order;
import model.errors.OrderError;

import java.time.LocalDate;
import java.util.List;

public interface OrderServices {
    Either<OrderError, List<Order>> getAll();
    Either<OrderError, Integer> delete(Order order);

    List<Order> getOrdersByCustomerId(int id);
    Either<OrderError, Integer> update(Order c);

    Either<OrderError, Order> createOrder(Order order);
    Either<OrderError, List<Order>> filteredListDate(LocalDate date);
    int getLastId();



}
