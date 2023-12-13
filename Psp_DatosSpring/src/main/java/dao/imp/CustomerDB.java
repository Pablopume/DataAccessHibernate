package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.CustomerDAO;
import dao.imp.maps.MapCustomer;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.Order;
import model.errors.CustomerError;
import model.errors.OrderError;
import org.springframework.dao.DataAccessException;
import io.vavr.control.Either;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.*;
import java.util.List;


@Log4j2
public class CustomerDB implements CustomerDAO {
    private final DBConnectionPool db;

    @Inject
    public CustomerDB(DBConnectionPool db) {
        this.db = db;
    }
//Catch DuplicatedKeyException

    public Either<CustomerError, List<Customer>> add(Customer customer) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(db.getDataSource());
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        Either<CustomerError, List<Customer>> result = null;

        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db.getDataSource());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SqlQueries.INSERT_INTO_CREDENTIALS_ID_USER_NAME_PASSWORD_VALUES, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, customer.getCredentials().getUser());
                ps.setString(2, customer.getCredentials().getPassword());
                return ps;
            }, keyHolder);
            customer.setId(keyHolder.getKey().intValue());
            jdbcTemplate.update(SqlQueries.INSERT_INTO_CUSTOMERS_ID_FIRST_NAME_LAST_NAME_EMAIL_PHONE_DATE_OF_BIRTH_VALUES, customer.getId(), customer.getFirst_name(), customer.getLast_name(), customer.getEmail(), customer.getPhone(), Date.valueOf(customer.getDob()));
            result = Either.right(getAll().get());
            transactionManager.commit(status);
        } catch (DataAccessException e) {
            transactionManager.rollback(status);

            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                result = Either.left(new CustomerError(1, Constants.DUPLICATED_USER_NAME));
            } else {
                result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
            }

        }
        return result;
    }
    public Either<CustomerError, Integer> delete(Customer customer, boolean delete) {
        Either<CustomerError, Integer> result;

        if (!delete) {
            try {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(db.getDataSource());
                int rs = jdbcTemplate.update(SqlQueries.DELETE_FROM_CUSTOMERS_WHERE_ID, customer.getId());
                jdbcTemplate.update(SqlQueries.DELETE_FROM_CREDENTIALS_WHERE_ID, customer.getId());
                if (rs == 0) {
                    result = Either.left(new CustomerError(1, Constants.ERROR_DELETING_CUSTOMER));
                } else {
                    result = Either.right(0);
                }
            } catch (DataAccessException ex) {
                if(ex.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    result = Either.left(new CustomerError(2, Constants.THE_CUSTOMER_HAS_ORDERS));
                } else {
                    result = Either.left(new CustomerError(3, Constants.ERROR_CONNECTING_TO_DATABASE));
                }
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(db.getDataSource());
        try {
            jdbcTemplate.update(SqlQueries.UPDATE_CUSTOMERS_SET_FIRST_NAME_LAST_NAME_EMAIL_PHONE_DATE_OF_BIRTH_WHERE_ID,
                    customer.getFirst_name(),
                    customer.getLast_name(),
                    customer.getEmail(),
                    customer.getPhone(),
                    Date.valueOf(customer.getDob()),
                    customer.getId());
            result = Either.right(getAll().get());
        } catch (DataAccessException e) {
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }




    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        Either<CustomerError, List<Customer>> result;
        JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());

        List<Customer> l = jtm.query("select * from customers", new MapCustomer());
        if(l.isEmpty()){
            result= Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        else{
            result= Either.right(l);
        }
        return result;
    }


    private Either<CustomerError, Integer> deleteRelationsWithCustomers(Customer customer) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(db.getDataSource());
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status2 = transactionManager.getTransaction(def);
        Either<CustomerError, Integer> result;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
        try {

            jdbcTemplate.update(SqlQueries.DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID_IN_SELECT_ORDER_ID_FROM_ORDERS_WHERE_CUSTOMER_ID, customer.getId());
            jdbcTemplate.update(SqlQueries.DELETE_FROM_ORDERS_WHERE_CUSTOMER_ID, customer.getId());
            int rs = jdbcTemplate.update(SqlQueries.DELETE_FROM_CUSTOMERS_WHERE_ID1, customer.getId());
           jdbcTemplate.update(SqlQueries.DELETE_FROM_CREDENTIALS_WHERE_ID4, customer.getId());

            if (rs == 0) {
                result = Either.left(new CustomerError(1, Constants.ERROR_DELETING_CUSTOMER));
            } else {
                result = Either.right(0);
            }
            transactionManager.commit(status2);
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
            transactionManager.rollback(status2);
            result = Either.left(new CustomerError(3, Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }



}
