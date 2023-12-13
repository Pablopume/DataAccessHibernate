package services.impl;

import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credentials;
import services.LoginServices;

import java.util.List;

public class LoginServicesImpl implements LoginServices {
    private final LoginDAO dao;

    @Inject
    public LoginServicesImpl(LoginDAO dao) {
        this.dao = dao;
    }


    public Either<String, List<Credentials>> getAll() {
        return dao.getAll();
    }

    public Credentials getByNameAndPassword(String name, String password) {
        Credentials credentials2 = null;
        List<Credentials> getAll = dao.getAll().get();
        for (Credentials credentials : getAll) {
            if (credentials.getUser().equals(name) && credentials.getPassword().equals(password)) {
                credentials2 = credentials;
            }
        }
        return credentials2;

    }

}