package ma.VitaCareApp.models;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Ordonnance {

    private Long idOrdonnance;
    private LocalDate date;
    private Consultation consultationConcerne;
    private List<PrescriptionMedicament> prescriptionMedicaments;

}
