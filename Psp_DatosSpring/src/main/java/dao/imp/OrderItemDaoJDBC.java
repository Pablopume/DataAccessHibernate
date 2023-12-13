package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.OrderItemDAO;
import dao.imp.maps.MapOrderItem;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.errors.OrderError;
import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoJDBC implements OrderItemDAO {

    private DBConnectionPool db;

    @Inject
    public OrderItemDaoJDBC(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<OrderError, List<OrderItem>> getAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(db.getDataSource());
        String query = SqlQueries.QUERY;
        List<OrderItem> orderItems = jdbcTemplate.query(query, new MapOrderItem());
        return Either.right(orderItems);

    }

    @Override
    public Either<OrderError, Integer> save(List<OrderItem> c) {
        Either<OrderError, Integer> result;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.INSERT_INTO_ORDER_ITEMS_ORDER_ID_MENU_ITEM_ID_QUANTITY_VALUES)) {
            int rs = 0;
            for (OrderItem orderItem : c) {
                preparedStatement.setInt(1, orderItem.getIdOrder());
                preparedStatement.setInt(2, orderItem.getMenuItem().getId());
                preparedStatement.setInt(3, orderItem.getQuantity());
                rs = rs + preparedStatement.executeUpdate();
            }

            if (rs == 0) {
                result = Either.left(new OrderError(Constants.ERROR_SAVING_ORDERS));
            } else {
                result = Either.right(0);
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }


    public Either<OrderError, Integer> update(List<OrderItem> c) {
        Either<OrderError, Integer> result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatementDelete = con.prepareStatement(SqlQueries.DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID2);
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.INSERT_INTO_ORDER_ITEMS_ORDER_ID_MENU_ITEM_ID_QUANTITY_VALUES1)) {
            preparedStatementDelete.setInt(1, c.get(0).getIdOrder());
            preparedStatementDelete.executeUpdate();
            for (OrderItem orderItem : c) {
                preparedStatement.setInt(1, orderItem.getIdOrder());
                preparedStatement.setInt(2, orderItem.getMenuItem().getId());
                preparedStatement.setInt(3, orderItem.getQuantity());
                preparedStatement.executeUpdate();
            }
            result = Either.right(0);
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }

    @Override
    public Either<OrderError, Integer> delete(Order o) {
        Either<OrderError, Integer> result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.DELETE_FROM_ORDER_ITEMS_WHERE_ORDER_ID1)) {
            preparedStatement.setInt(1, o.getId());
            preparedStatement.executeUpdate();
            result = Either.right(1);
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }

    private List<OrderItem> readRS(ResultSet rs) throws SQLException {
        List<OrderItem> result = new ArrayList<>();
        while (rs.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(rs.getInt(Constants.ORDER_ITEM_ID));
            orderItem.setIdOrder(rs.getInt(Constants.ORDER_ID));
            orderItem.setMenuItem(getMenuItem(rs.getInt(Constants.MENU_ITEM_ID2)));
            orderItem.setQuantity(rs.getInt(Constants.QUANTITY));
            result.add(orderItem);
        }
        return result;
    }


    private MenuItem getMenuItem(int id) {
        MenuItem result = null;
        try (Connection con = db.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SqlQueries.SELECT_FROM_MENU_ITEMS_WHERE_MENU_ITEM_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            result = readRSMenuItem(rs);
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = null;
        }
        return result;
    }

    private MenuItem readRSMenuItem(ResultSet rs) throws SQLException {
        MenuItem result = new MenuItem();
        while (rs.next()) {
            result.setId(rs.getInt("menu_item_id"));
            result.setName(rs.getString("name"));
            result.setDescription(rs.getString("description"));
            result.setPrice(rs.getDouble("price"));
        }
        return result;
    }
}
