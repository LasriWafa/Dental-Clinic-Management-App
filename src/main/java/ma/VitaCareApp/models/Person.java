package ma.VitaCareApp.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.VitaCareApp.models.enums.*;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter @SuperBuilder

public class Person {

    private Long id;
    private String phone;
    private String email;
    private Sex sex;
    private String address;
    private LocalDate birthDate;
    private String firstName;
    private String lastName;
    private String cin;

//    private String profilePicture;

}
