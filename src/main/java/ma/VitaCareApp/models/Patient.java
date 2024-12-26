package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class Patient extends Person {

    private BloodType bloodType;
    private LocalDate birthDay;
    private Insurance insurance;
    private List<MedicalHistory> medicalHistory;
    private String profession;
    private DossierMedical dossierMedical;
}
