package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.TypeConsultation;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Consultation {

    private Long idConsultation;
//    private List<Facture> factures;
    private TypeConsultation typeConsultation;
    private Ordonnance ordonnance;
    private LocalDate dateConsultation;
//    private DossierMedical dossierMedical;
    private List<InterventionMedcin> interventions;

    // Override toString for a comma-separated representation
    @Override
    public String toString() {
        return String.join(",",
                getIdConsultation().toString(),
                getTypeConsultation().name(),
                getOrdonnance().getIdOrdonnance().toString(), // Store Ordonnance ID
                getDateConsultation().toString(),
                interventionsToString() // Store Intervention IDs
        );
    }

    // Helper method to convert interventions to a string of IDs
    private String interventionsToString() {
        StringBuilder sb = new StringBuilder();
        for (InterventionMedcin intervention : interventions) {
            sb.append(intervention.getIdIntervention()).append(";");
        }
        return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : ""; // Remove the last semicolon
    }


}
