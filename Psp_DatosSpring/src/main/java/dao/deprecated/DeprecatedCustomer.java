package dao.deprecated;

import common.Constants;
import common.SqlQueries;
import dao.imp.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.errors.CustomerError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeprecatedCustomer {
    private final DBConnectionPool db;

    @Inject
    public DeprecatedCustomer(DBConnectionPool db) {
        this.db = db;
    }

    public Either<CustomerError, List<Customer>> add(Customer customer) {
        Either<CustomerError, List<Customer>> result = null;
        Connection myConnection = null;
        try {
            myConnection = db.getConnection();
            myConnection.setAutoCommit(false);

            try (PreparedStatement credentialsStatement = myConnection.prepareStatement(SqlQueries.INSERT_INTO_CREDENTIALS_ID_USER_NAME_PASSWORD_VALUES)) {
                credentialsStatement.setInt(1, customer.getId());
                credentialsStatement.setString(2, customer.getFirst_name());
                credentialsStatement.setString(3, customer.getFirst_name().toLowerCase());
                credentialsStatement.executeUpdate();

                myConnection.commit();

                result = Either.right(getAll().get());
                try (PreparedStatement statement = myConnection.prepareStatement(SqlQueries.INSERT_INTO_CUSTOMERS_ID_FIRST_NAME_LAST_NAME_EMAIL_PHONE_DATE_OF_BIRTH_VALUES, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, customer.getId());
                    statement.setString(2, customer.getFirst_name());
                    statement.setString(3, customer.getLast_name());
                    statement.setString(4, customer.getEmail());
                    statement.setString(5, customer.getPhone());
                    statement.setDate(6, Date.valueOf(customer.getDob()));
                    statement.executeUpdate();
                    ResultSet rs = statement.getGeneratedKeys();
                    rs.next();

                }
            }
        } catch (SQLException e) {
            if (myConnection != null) {
                try {
                    myConnection.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        } finally {
            if (myConnection != null) {
                try {
                    myConnection.setAutoCommit(true);
                    myConnection.close();
                } catch (SQLException excep) {
                    result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
                }
            }
        }
        return result;
    }

    public Either<CustomerError, Integer> delete(Customer customer, boolean delete) {
        Either<CustomerError, Integer> result;
        if (!delete) {
            try (Connection con = db.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.DELETE_FROM_CUSTOMERS_WHERE_ID);
                 PreparedStatement preparedStatementCredentials = con.prepareStatement(SqlQueries.DELETE_FROM_CREDENTIALS_WHERE_ID)) {
                preparedStatement.setInt(1, customer.getId());
                preparedStatementCredentials.setInt(1, customer.getId());
                int rs = preparedStatement.executeUpdate();
                preparedStatementCredentials.executeUpdate();
                if (rs == 0) {
                    result = Either.left(new CustomerError(1, Constants.ERROR_DELETING_CUSTOMER));
                } else {
                    result = Either.right(0);
                }
                db.closeConnection(con);
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1451) {
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

    public Either<CustomerError, List<Customer>> update(Customer customer) {
        Either<CustomerError, List<Customer>> result = null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement(SqlQueries.UPDATE_CUSTOMERS_SET_FIRST_NAME_LAST_NAME_EMAIL_PHONE_DATE_OF_BIRTH_WHERE_ID)) {
            statement.setString(1, customer.getFirst_name());
            statement.setString(2, customer.getLast_name());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());
            statement.setDate(5, Date.valueOf(customer.getDob()));
            statement.setInt(6, customer.getId());
            statement.executeUpdate();
            result = Either.right(getAll().get());
            db.closeConnection(myConnection);
        } catch (SQLException e) {
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }

    public Either<CustomerError, List<Customer>> get(int id) {
        Either<CustomerError, List<Customer>> result = null;
        try (Connection myConnection = db.getConnection();
             PreparedStatement statement = myConnection.prepareStatement(SqlQueries.SELECT_FROM_CUSTOMERS_WHERE_ID)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            result = Either.right(readRS(statement.getResultSet()).get());
            db.closeConnection(myConnection);
        } catch (SQLException e) {
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;

    }


    public Either<CustomerError, List<Customer>> getAll() {
        Either<CustomerError, List<Customer>> result = null;
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery("select * from customers");
            result = Either.right(readRS(rs).get());
            db.closeConnection(myConnection);
        } catch (SQLException e) {
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }

    private Either<CustomerError, List<Customer>> readRS(ResultSet rs) {
        Either<CustomerError, List<Customer>> either;
        try {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer resultCustomer = new Customer(
                        rs.getInt(Constants.ID),
                        rs.getString(Constants.FIRST_NAME),
                        rs.getString(Constants.LAST_NAME),
                        rs.getString(Constants.EMAIL),
                        rs.getString(Constants.PHONE),
                        rs.getDate(Constants.DATE_OF_BIRTH).toLocalDate()
                );
                customers.add(resultCustomer);
            }
            either = Either.right(customers);

        } catch (SQLException e) {
            either = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return either;
    }

    private Either<CustomerError, Integer> deleteRelationsWithCustomers(Customer customer) {
        Either<CustomerError, Integer> result;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID_IN_SELECT_ORDER_ID_FROM_ORDERS_WHERE_CUSTOMER_ID);
             PreparedStatement preparedStatementDeleteOrders = con.prepareStatement(SqlQueries.DELETE_FROM_ORDERS_WHERE_CUSTOMER_ID);
             PreparedStatement preparedStatementCredentials = con.prepareStatement(SqlQueries.DELETE_FROM_CREDENTIALS_WHERE_ID4);
             PreparedStatement preparedStatementCustomer = con.prepareStatement(SqlQueries.DELETE_FROM_CUSTOMERS_WHERE_ID1)) {
            preparedStatement.setInt(1, customer.getId());
            preparedStatementDeleteOrders.setInt(1, customer.getId());
            preparedStatement.executeUpdate();
            preparedStatementDeleteOrders.executeUpdate();
            preparedStatementCustomer.setInt(1, customer.getId());
            preparedStatementCredentials.setInt(1, customer.getId());
            int rs = preparedStatementCustomer.executeUpdate();
            preparedStatementCredentials.executeUpdate();
            if (rs == 0) {
                result = Either.left(new CustomerError(1, Constants.ERROR_DELETING_CUSTOMER));
            } else {
                result = Either.right(0);
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new CustomerError(3, Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }


}
