package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.*;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Person {

    private long id;
    private String phone;
    private String email;
    private Sex sex;
    private String address;
    private String birthDate;
    private String firstName;
    private String lastName;
    private String cin;
    private String profilePicture; // Path to the profile picture

}
