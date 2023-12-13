package dao;

import io.vavr.control.Either;
import model.Credentials;

import java.util.List;

public interface LoginDAO {
    Either<String, List<Credentials>> getAll();

}
