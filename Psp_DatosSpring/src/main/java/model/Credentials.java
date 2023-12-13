package model;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class Credentials {
    private  int id;
    private final String user;
    private final String password;


}
