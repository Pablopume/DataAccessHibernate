package dao.imp.maps;

import common.Constants;
import model.Customer;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapCustomer implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
                rs.getInt(Constants.ID),
                rs.getString(Constants.FIRST_NAME),
                rs.getString(Constants.LAST_NAME),
                rs.getString(Constants.EMAIL),
                rs.getString(Constants.PHONE),
                rs.getDate(Constants.DATE_OF_BIRTH).toLocalDate()
        );
    }
}
