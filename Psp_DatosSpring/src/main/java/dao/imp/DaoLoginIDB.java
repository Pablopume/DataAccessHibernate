package dao.imp;

import common.Constants;
import common.SqlQueries;
import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credentials;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DaoLoginIDB implements LoginDAO {
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    private DBConnectionPool db;

    @Inject
    public DaoLoginIDB(DBConnectionPool db) {
        this.db = db;
    }


    public Either<String, List<Credentials>> getAll() {
        Either<String, List<Credentials>> response;
        try (
                Connection myConnection = db.getConnection(); Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(SqlQueries.SELECT_FROM_CREDENTIALS);
            List<Credentials> credentialsList = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(Constants.ID);
                String user = rs.getString(USER_NAME);
                String password = rs.getString(PASSWORD);
                Credentials c = new Credentials(id, user, password);
                credentialsList.add(c);
            }
            response = Either.right(credentialsList);
            db.closeConnection(myConnection);
        } catch (SQLException e) {
            response = Either.left(e.getMessage());
        }
        return response;
    }


}
