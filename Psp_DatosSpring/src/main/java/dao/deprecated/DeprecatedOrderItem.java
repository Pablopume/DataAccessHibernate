package dao.deprecated;

import common.Constants;
import common.SqlQueries;
import dao.imp.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.OrderItem;
import model.errors.OrderError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeprecatedOrderItem {

    private final DBConnectionPool db;

    @Inject
    public DeprecatedOrderItem(DBConnectionPool db) {
        this.db = db;
    }

    public Either<OrderError, List<OrderItem>> getAll() {
        Either<OrderError, List<OrderItem>> result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.SELECT_FROM_ORDER_ITEMS)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<OrderItem> orderitems = readRS(rs);
            if (orderitems.isEmpty()) {
                result = Either.left(new OrderError(Constants.THEARE_ARE_NO_ORDER_ITEMS_IN_THIS_ORDER));
            } else {
                result = Either.right(orderitems);
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }

    private List<OrderItem> readRS(ResultSet rs) throws SQLException {
        List<OrderItem> result = new ArrayList<>();
        while (rs.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getInt(Constants.ORDER_ITEM_ID));
            orderItem.setIdOrder(rs.getInt(Constants.ORDER_ID));
            orderItem.setQuantity(rs.getInt(Constants.QUANTITY));
            result.add(orderItem);
        }
        return result;
    }
}
