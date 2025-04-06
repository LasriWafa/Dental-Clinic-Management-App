package ma.VitaCareApp.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.VitaCareApp.models.enums.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Patient extends Person {

    private BloodType bloodType;
    private Mutuelle mutuelle;
    private String profession;

    // Constructor for creating a new patient (without ID)
    public Patient(String phone, String email, Sex sex, String address, LocalDate birthDate,
                   String firstName, String lastName, String cin,
                   BloodType bloodType, Mutuelle mutuelle, String profession) {

        // Call super constructor with Person attributes (ID is set to null)
        super(null, phone, email, sex, address, birthDate, firstName, lastName, cin);

        // Initialize Patient-specific attributes
        this.bloodType = bloodType;
        this.mutuelle = mutuelle;
        this.profession = profession;
    }

    // Constructor for loading an existing patient (with ID)
    public Patient(Long id, String phone, String email, Sex sex, String address, LocalDate birthDate,
                   String firstName, String lastName, String cin,
                   BloodType bloodType, Mutuelle mutuelle, String profession) {

        // Call super constructor with Person attributes
        super(id, phone, email, sex, address, birthDate, firstName, lastName, cin);

        // Initialize Patient-specific attributes
        this.bloodType = bloodType;
        this.mutuelle = mutuelle;
        this.profession = profession;
    }

    @Override
    public String toString() {
        return String.join(",",
                getId() != null ? getId().toString() : "", // Handle null ID
                getPhone(),
                getEmail(),
                getSex().name(),
                getAddress(),
                getBirthDate().toString(),
                getFirstName(),
                getLastName(),
                getCin(),
                bloodType.name(),
                mutuelle.name(),
                profession
        );
    }
}