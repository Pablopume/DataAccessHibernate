package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.errors.OrderError;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoJDBC implements MenuItemDAO {

    private DBConnectionPool db;

    @Inject
    public MenuItemDaoJDBC(DBConnectionPool db) {
        this.db = db;
    }


    public Either<OrderError, List<MenuItem>> getAll() {
        Either<OrderError, List<MenuItem>> result = null;
        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = stmt.executeQuery(SqlQueries.SELECT_FROM_MENU_ITEMS);
            List<MenuItem> menuItems = readRS(rs);
            if (menuItems.isEmpty()) {
                result = Either.left(new OrderError(Constants.THERE_ARE_NO_MENU_ITEMS));
            } else {
                result = Either.right(menuItems);
            }
            db.closeConnection(con);
        } catch (SQLException ex) {
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }
        return result;
    }

    private List<MenuItem> readRS(ResultSet rs) {
        List<MenuItem> menuItems = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt(Constants.MENU_ITEM_ID);
                String name = rs.getString(Constants.NAME);
                String description = rs.getString(Constants.DESCRIPTION);
                double price = rs.getDouble(Constants.PRICE);
                menuItems.add(new MenuItem(id, name, description, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }
}
