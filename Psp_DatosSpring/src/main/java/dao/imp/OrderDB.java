package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.OrdersDAO;
import dao.imp.maps.MapCustomer;
import dao.imp.maps.MapOrder;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.Customer;
import model.Order;
import model.errors.OrderError;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named("OrderDB")
public class OrderDB implements OrdersDAO {
    private DBConnectionPool db;

    @Inject
    public OrderDB(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> result;
        JdbcTemplate jtm = new JdbcTemplate(db.getDataSource());
        List<Order> l = jtm.query(SqlQueries.SELECT_FROM_ORDERS, new MapOrder());
        if (l.isEmpty()) {
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        } else {
            result = Either.right(l);
        }

        return result;
    }

    @Override
    public Either<OrderError, List<Order>> getAll(int id) {
        return null;
    }

    public Either<OrderError, Integer> delete(Order c) {
        Either<OrderError, Integer> result;
        Connection myConnection = null;
        try {
            myConnection = db.getConnection();
            myConnection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = myConnection.prepareStatement(SqlQueries.DELETE_FROM_ORDERS_WHERE_ORDER_ID);
                 PreparedStatement preparedStatementItems = myConnection.prepareStatement(SqlQueries.DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID)) {
                preparedStatement.setInt(1, c.getId());
                preparedStatementItems.setInt(1, c.getId());
                preparedStatementItems.executeUpdate();
                preparedStatement.executeUpdate();
                result = Either.right(1);
            }

            myConnection.commit();
        } catch (SQLException ex) {

            try {
                myConnection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        } finally {
            if (myConnection != null) {
                db.closeConnection(myConnection);
            }
        }

        return result;
    }


    public Either<OrderError, Integer> update(Order c) {
        Either<OrderError, Integer> result;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.UPDATE_ORDERS_SET_ORDER_DATE_CUSTOMER_ID_TABLE_ID_WHERE_ORDER_ID + "  ")) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(2, c.getCustomer_id());
            preparedStatement.setInt(3, c.getTable_id());
            preparedStatement.setInt(4, c.getId());
            int rs = preparedStatement.executeUpdate();
            if (rs == 0) {
                result = Either.left(new OrderError(Constants.ERROR_UPDATING_ORDER));
            } else {
                result = Either.right(0);
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }


    @Override
    public Either<OrderError, Order> save(Order c) {
        Either<OrderError, Order> result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.INSERT_INTO_ORDERS_ORDER_DATE_CUSTOMER_ID_TABLE_ID_VALUES, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(c.getDate()));
            preparedStatement.setInt(2, c.getCustomer_id());
            preparedStatement.setInt(3, c.getTable_id());

            int rs = preparedStatement.executeUpdate();
            if (rs == 0) {
                result = Either.left(new OrderError(Constants.ERROR_SAVING_ORDERS));
            } else {
                result = Either.right(new Order(c.getDate(), c.getCustomer_id(), c.getTable_id()));
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }
}
