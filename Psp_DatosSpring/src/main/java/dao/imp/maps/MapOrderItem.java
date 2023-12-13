package dao.imp.maps;

import common.Constants;
import model.MenuItem;
import model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapOrderItem implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderItem(
                rs.getInt(Constants.ORDER_ITEM_ID),
                rs.getInt(Constants.ORDER_ID),
                new MenuItem(rs.getInt(Constants.MENU_ITEM_ID), rs.getString(Constants.NAME), rs.getString(Constants.DESCRIPTION), rs.getDouble(Constants.PRICE)),
                rs.getInt(Constants.QUANTITY));
    }
}
