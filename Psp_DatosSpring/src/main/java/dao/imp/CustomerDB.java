package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.CustomerDAO;
import dao.JPAUtil;

import jakarta.inject.Inject;
import jakarta.persistence.*;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;
import model.model2.CredentialsEntity;
import model.model2.CustomersEntity;
import model.model2.OrderItemsEntity;
import model.model2.OrdersEntity;
import org.springframework.dao.DataAccessException;
import io.vavr.control.Either;
import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Log4j2
public class CustomerDB implements CustomerDAO {

    private final JPAUtil jpautil;
    private EntityManager em;

    @Inject
    public CustomerDB(JPAUtil jpautil) {

        this.jpautil = jpautil;
    }
//Catch DuplicatedKeyException

    @Override
    public Either<CustomerError, List<Customer>> add(Customer customer) {
        Either<CustomerError, List<Customer>> result;
        EntityManager entityManager = jpautil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            CredentialsEntity credentialsEntity = customer.getCredentials().toCredentialsEntity();
            CustomersEntity customersEntity = customer.toCustomerEntity();
            entityManager.persist(credentialsEntity);
            customersEntity.setCredentialsById(credentialsEntity);
            customersEntity.setId(credentialsEntity.getId());
            entityManager.persist(customersEntity);
            List<Customer> customersList = getAll().getOrElse(Collections.emptyList());
            result = Either.right(customersList);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                result = Either.left(new CustomerError(1, Constants.DUPLICATED_USER_NAME));
            } else {
                result = Either.left(new CustomerError(0, "Error while inserting customer"));
            }
        } finally {
            entityManager.close();
        }

        return result;
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean delete) {
        Either<CustomerError, Integer> result;

        if (!delete) {
            EntityManager entityManager = jpautil.getEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();

            try {
                transaction.begin();

                CustomersEntity customersEntity = entityManager.find(CustomersEntity.class, customer.getId());
                if (customersEntity != null) {
                    entityManager.remove(customersEntity);
                }

                transaction.commit();
                result = Either.right(0);
            } catch (PersistenceException e) {
                transaction.rollback();
                if (e instanceof RollbackException) {
                    result = Either.left(new CustomerError(2, Constants.DUPLICATED_USER_NAME));

                } else {
                    result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
                }
            } finally {
                entityManager.close();
            }
        } else {
            result = deleteRelationsWithCustomers(customer);
        }
        return result;
    }


    @Override
    public Either<OrderError, Order> save(Order order) {
        return null;
    }


    public Either<CustomerError, List<Customer>> update(Customer customer) {
        Either<CustomerError, List<Customer>> result;
        EntityManager entityManager = jpautil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            CustomersEntity customersEntity = entityManager.find(CustomersEntity.class, customer.getId());
            if (customersEntity != null) {
                customersEntity.setFirstName(customer.getFirst_name());
                customersEntity.setLastName(customer.getLast_name());
                customersEntity.setEmail(customer.getEmail());
                customersEntity.setPhone(customer.getPhone());
                customersEntity.setDateOfBirth(Date.valueOf(customer.getDob()));

                entityManager.merge(customersEntity);
            }

            transaction.commit();

            // Recuperar la lista actualizada después de la actualización
            List<Customer> updatedCustomers = getAll().get(); // Implementa este método según tus necesidades

            result = Either.right(updatedCustomers);
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            transaction.rollback();
            log.error(e.getCause());

            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        } finally {
            entityManager.close();
        }

        return result;
    }


    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        Either<CustomerError, List<Customer>> result;
        try {
            em = jpautil.getEntityManager();
            List<CustomersEntity> customersEntities = em.createQuery("from CustomersEntity", CustomersEntity.class).getResultList();

            List<Customer> customersList = customersEntities.stream()
                    .map(CustomersEntity::toCustomer)
                    .collect(Collectors.toList());

            if (customersList.isEmpty()) {
                result = Either.left(new CustomerError(0, "Error while retrieving customers"));
            } else {
                result = Either.right(customersList);
            }
        } finally {
            if (em != null) em.close();
        }

        return result;
    }


    private Either<CustomerError, Integer> deleteRelationsWithCustomers(Customer customer) {
        EntityManager entityManager = jpautil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            CustomersEntity customersEntity = entityManager.find(CustomersEntity.class, customer.getId());
            if (customersEntity == null) {
                return Either.left(new CustomerError(1, Constants.ERROR_DELETING_CUSTOMER));
            }

            Collection<OrdersEntity> orders = customersEntity.getOrdersById();
            for (OrdersEntity order : orders) {
                Collection<OrderItemsEntity> orderItems = order.getOrderItemsByOrderId();
                orderItems.forEach(entityManager::remove);
                entityManager.remove(order);
            }

            entityManager.remove(customersEntity);
            transaction.commit();
            return Either.right(0);
        } catch (PersistenceException e) {

            if (transaction.isActive()) {
                transaction.rollback();

            }


        } finally {
            entityManager.close();
        }
        return Either.right(0);
    }


}
