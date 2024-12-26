package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.TypeConsultation;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Consultation {

    private Long idConsultation;
    private List<Facture> factures;
    private TypeConsultation typeConsultation;
    private Ordonnance ordonnance;
    private LocalDate dateConsultation;
    private DossierMedical dossierMedical;
    private List<InterventionMedcin> interventions;

}
