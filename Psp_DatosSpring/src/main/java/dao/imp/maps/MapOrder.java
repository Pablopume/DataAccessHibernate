package dao.imp.maps;

import common.Constants;
import model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapOrder implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Order(
                rs.getInt(Constants.ORDER_ID),
                rs.getTimestamp(Constants.ORDER_DATE).toLocalDateTime(),
                rs.getInt(Constants.CUSTOMER_ID),
                rs.getInt(Constants.TABLE_ID));
    }
}
