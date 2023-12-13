package dao;

import io.vavr.control.Either;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;

import java.util.List;

public interface OrderItemDAO {
    Either<OrderError, List<OrderItem>> getAll();

    Either<OrderError, Integer> save(List<OrderItem> c);
    Either<OrderError, Integer> update(List<OrderItem> c);
    Either<OrderError, Integer> delete(Order o);
}
