package ma.VitaCareApp.models;

import lombok.*;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Medicament {

    private Long idMedicine;
    private Double price;
    private String name;
    private List<PrescriptionMedicament> prescriptionDeCeMedicaments;
    private String description;
    private List<MedicalHistory> precautions;

}
