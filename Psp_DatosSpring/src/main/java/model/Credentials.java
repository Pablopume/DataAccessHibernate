package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.model2.CredentialsEntity;

@AllArgsConstructor
@Data
public class Credentials {
    private  int id;
    private final String user;
    private final String password;
public CredentialsEntity toCredentialsEntity() {
    return new CredentialsEntity(id, user, password);
}

}
