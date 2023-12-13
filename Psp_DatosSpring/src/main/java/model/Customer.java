package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@Entity
@Table(name = "customers")
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name="email")
    private String email;
    @Column(name="phone")
    private String phone;
    @Column(name="date_of_birth")
    private LocalDate dob;
    @Transient
    private Credentials credentials;

    public Customer(int id, String first_name, String last_name, String email, String phone, LocalDate dob) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }

    public Customer(String fileLine) {
        String[] elemArray = fileLine.split(";");
        this.id = Integer.parseInt(elemArray[0]);
        this.first_name = elemArray[1];
        this.last_name = elemArray[2];
        this.email = elemArray[3];
        this.phone = elemArray[4];
        this.dob = LocalDate.parse(elemArray[5]);

    }

    public String toStringTextFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return id + ";" + first_name + ";" + last_name + ";" + email + ";" + phone + ";" + dob.format(formatter);
    }


}
