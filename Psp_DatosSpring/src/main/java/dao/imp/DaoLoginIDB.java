package dao.imp;

import dao.JPAUtil;
import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.Credentials;
import model.model2.CredentialsEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DaoLoginIDB implements LoginDAO {
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    private DBConnectionPool db;
private final JPAUtil jpautil;
    @Inject
    public DaoLoginIDB(DBConnectionPool db, JPAUtil jpautil) {
        this.db = db;
        this.jpautil = jpautil;
    }


    @Override
    public Either<String, List<Credentials>> getAll() {
        Either<String, List<Credentials>> response;
        EntityManager em = null;

        try {
            em = jpautil.getEntityManager();
            List<CredentialsEntity> credentialsEntities = em.createQuery("from CredentialsEntity", CredentialsEntity.class).getResultList();

            List<Credentials> credentialsList = credentialsEntities.stream()
                    .map(CredentialsEntity::toCredentials)
                    .collect(Collectors.toList());

            if (credentialsList.isEmpty()) {
                response = Either.left("Error while retrieving credentials");
            } else {
                response = Either.right(credentialsList);
            }
        } finally {
            if (em != null) em.close();
        }

        return response;
    }



}
