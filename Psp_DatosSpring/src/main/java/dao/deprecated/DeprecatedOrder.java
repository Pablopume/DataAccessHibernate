package dao.deprecated;

import common.Constants;
import common.SqlQueries;
import dao.imp.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.errors.OrderError;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeprecatedOrder {
    private final DBConnectionPool db;

    @Inject
    public DeprecatedOrder(DBConnectionPool db) {
        this.db = db;
    }


    public Either<OrderError, List<Order>> getAll() {
        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(SqlQueries.SELECT_FROM_ORDERS);
            return Either.right(readRS(rs).get());
        } catch (SQLException e) {
            return Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
    }


    private Either<OrderError, List<Order>> readRS(ResultSet rs) {
        try {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt(Constants.ORDER_ID),
                        rs.getTimestamp(Constants.ORDER_DATE).toLocalDateTime(),
                        rs.getInt(Constants.CUSTOMER_ID),
                        rs.getInt(Constants.TABLE_ID)
                );

                orders.add(order);

            }
            return Either.right(orders);
        } catch (SQLException e) {
            return Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }

    }
}
