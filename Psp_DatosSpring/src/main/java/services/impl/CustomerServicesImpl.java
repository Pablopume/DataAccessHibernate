package services.impl;

import dao.CustomerDAO;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;
import services.CustomerServices;

import java.util.Comparator;
import java.util.List;

@Singleton
public class CustomerServicesImpl implements CustomerServices {
    private final CustomerDAO customerDAO;
    private final OrdersDAO ordersDAO;

    @Inject
    public CustomerServicesImpl(CustomerDAO customerDAO, @Named("OrderXMLImpl") OrdersDAO ordersDAO) {
        this.customerDAO = customerDAO;
        this.ordersDAO = ordersDAO;
    }

    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        return customerDAO.getAll();
    }

    public Either<CustomerError, List<Customer>> add(Customer customer) {
        return customerDAO.add(customer);
    }

    public Either<CustomerError, List<Customer>> update(Customer customer) {
        return customerDAO.update(customer);
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean deleteOrders) {
        return customerDAO.delete(customer, deleteOrders);
    }



    @Override
    public Either<OrderError, Order> save(Order order) {
        return ordersDAO.save(order);
    }


    public String getNameById(int id) {
        List<Customer> customers = customerDAO.getAll().get();
        String name = "";
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                name = customer.getFirst_name();
            }
        }
        return name;
    }

}

