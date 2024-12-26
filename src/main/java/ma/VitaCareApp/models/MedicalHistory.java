package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.CategoryMedicalHistory;
import java.util.List;

@NoArgsConstructor @Getter @AllArgsConstructor @Setter

public class MedicalHistory {

    private Long idAntecedant;
    private List<Patient> patientsWithMedicalHistory;
    private String libelle;
    private CategoryMedicalHistory category;

}
