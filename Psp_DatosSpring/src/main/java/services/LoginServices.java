package services;

import io.vavr.control.Either;
import model.Credentials;

import java.util.List;

public interface LoginServices {

    Either<String, List<Credentials>> getAll();

    Credentials getByNameAndPassword(String name, String password);

}
