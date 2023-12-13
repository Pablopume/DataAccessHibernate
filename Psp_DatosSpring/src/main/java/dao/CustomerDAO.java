package dao;

import io.vavr.control.Either;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;

import java.time.LocalDate;
import java.util.List;

public interface CustomerDAO {

    Either<CustomerError, List<Customer>> getAll();
    Either<CustomerError, List<Customer>> add(Customer customer);
    Either<CustomerError, List<Customer>> update(Customer customer);
    Either<CustomerError, Integer> delete(Customer customer, boolean delete);
    Either<OrderError, Order> save(Order order);

}

