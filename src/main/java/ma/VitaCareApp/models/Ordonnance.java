package ma.VitaCareApp.models;

import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Ordonnance {

    private Long idOrdonnance;
    private LocalDate date;
//    private Consultation consultationConcerne;
    private List<PrescriptionMedicament> prescriptionMedicaments;

    public Ordonnance(long l) {
    }

    // Helper method to get PrescriptionMedicament IDs
    public List<Long> getPrescriptionMedicamentIds() {
        return prescriptionMedicaments.stream()
                .map(PrescriptionMedicament::getIdPrescription) // Extract PrescriptionMedicament IDs
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.join(",",
                idOrdonnance.toString(),
                date.toString(),
                String.join(";", getPrescriptionMedicamentIds().stream().map(Object::toString).toArray(String[]::new)) // PrescriptionMedicament IDs as semicolon-separated string
        );
    }
}
