package model.model2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Customer;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers", schema = "pabloserrano_restaurant")
public class CustomersEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;
    @Basic
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Basic
    @Column(name = "phone", nullable = true, length = 20)
    private String phone;
    @Basic
    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private CredentialsEntity credentialsById;
    @OneToMany(mappedBy = "customersByCustomerId")
    private Collection<OrdersEntity> ordersById;

    public CustomersEntity(int id, String firstName, String lastName, String email, String phone, Date dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }

    public Customer toCustomer() {
        return new Customer(id, firstName, lastName, email, phone, dateOfBirth.toLocalDate());
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomersEntity that = (CustomersEntity) o;
        return id == that.id && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(phone, that.phone) && Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, dateOfBirth);
    }


}
