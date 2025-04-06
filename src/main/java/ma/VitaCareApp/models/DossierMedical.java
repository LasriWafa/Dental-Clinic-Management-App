package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.StatusPayement;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor @Getter @NoArgsConstructor @Setter

public class DossierMedical {

    private String numeroDossier;
    private LocalDate dateCreation;
    private StatusPayement statusPayement;
    private Patient patient;
    private List<RendezVous> rdvs;
    private List<Ordonnance> ordonnances;
    private List<Consultation> consultations;
//    private FinancialSituation financialSituation;
//    private Dentist medcinTraitant;

    // Override toString for a comma-separated representation
    @Override
    public String toString() {
        return String.join(",",
                getNumeroDossier(),
                getDateCreation().toString(),
                getStatusPayement().name(),
                getPatient().getId().toString(), // Store Patient ID
                listToString(getRdvs().stream().map(RendezVous::getIdRDV).collect(Collectors.toList())), // Store RendezVous IDs
                listToString(getOrdonnances().stream().map(Ordonnance::getIdOrdonnance).collect(Collectors.toList())), // Store Ordonnance IDs
                listToString(getConsultations().stream().map(Consultation::getIdConsultation).collect(Collectors.toList())) // Store Consultation IDs
        );
    }

    // Helper method to convert a list of IDs to a semicolon-separated string
    private String listToString(List<Long> ids) {
        StringBuilder sb = new StringBuilder();
        for (Long id : ids) {
            sb.append(id).append(";");
        }
        return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : ""; // Remove the last semicolon
    }
}
