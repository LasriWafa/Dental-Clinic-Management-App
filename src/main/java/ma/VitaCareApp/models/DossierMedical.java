package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.StatusPayement;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @Getter @NoArgsConstructor @Setter

public class DossierMedical {

    private List<RendezVous> rdvs;
    private LocalDate dateCreation;
    private Patient patient;
    private List<Ordonnance> ordonnances;
    private FinancialSituation financialSituation;
    private StatusPayement statusPayement;
    private String numeroDossier;
    private Dentist medcinTraitant;
    private List<Consultation> consultations;

}
