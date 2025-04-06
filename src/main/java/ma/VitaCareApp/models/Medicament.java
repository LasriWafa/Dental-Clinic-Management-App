package ma.VitaCareApp.models;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Medicament {

    private Long idMedicine;
    private Double price;
    private String name;
//    private List<PrescriptionMedicament> prescriptionDeCeMedicaments;
    private String description;
    private List<MedicalHistory> precautions;

    // Helper method to get precaution IDs
    public List<Long> getPrecautionIds() {
        return precautions.stream()
                .map(MedicalHistory::getIdAntecedant) // Extract MedicalHistory IDs
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.join(",",
                idMedicine.toString(),
                price.toString(),
                name,
                description,
                String.join(";", getPrecautionIds().stream().map(Object::toString).toArray(String[]::new)) // Precaution IDs as semicolon-separated string
        );
    }
}
