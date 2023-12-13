package dao;

import io.vavr.control.Either;
import model.Order;
import model.errors.OrderError;

import java.util.List;

public interface OrdersDAO {
    Either<OrderError, List<Order>> getAll();
    Either<OrderError, List<Order>> getAll(int id);
    Either<OrderError, Integer> delete(Order order);
    Either<OrderError, Order> save(Order order);
    Either<OrderError, Integer> update(Order c);

}
