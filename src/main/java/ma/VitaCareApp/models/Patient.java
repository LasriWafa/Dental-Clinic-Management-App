package ma.VitaCareApp.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.VitaCareApp.models.enums.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class Patient extends Person {

    private BloodType bloodType;
    private LocalDate birthDay;
    private Mutuelle mutuelle;
    private List<MedicalHistory> medicalHistory;
    private String profession;
    private DossierMedical dossierMedical;


    public Patient(long id, String phone, String email, Sex sex, String address, String birthDate,
                   String firstName, String lastName, String cin, String profilePicture,
                   BloodType bloodType, LocalDate birthDay, Mutuelle mutuelle,
                   List<MedicalHistory> medicalHistory, String profession, DossierMedical dossierMedical) {
        super(id, phone, email, sex, address, birthDate, firstName, lastName, cin, profilePicture);
        this.bloodType = bloodType;
        this.birthDay = birthDay;
        this.mutuelle = mutuelle;
        this.medicalHistory = medicalHistory;
        this.profession = profession;
        this.dossierMedical = dossierMedical;
    }


}
