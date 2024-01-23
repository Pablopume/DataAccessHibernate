package model.model2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Credentials;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credentials", schema = "pabloserrano_restaurant")
public class CredentialsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;
    @Basic
    @Column(name = "password", nullable = false, length = 20)
    private String password;
    @OneToOne(mappedBy = "credentialsById")
    private CustomersEntity customersById;


    public Credentials toCredentials() {
        return new Credentials(id, userName, password);
    }

    public CredentialsEntity(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialsEntity that = (CredentialsEntity) o;
        return id == that.id && Objects.equals(userName, that.userName) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, password);
    }

}
