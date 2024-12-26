package ma.VitaCareApp.models;

import lombok.*;

@NoArgsConstructor @Getter @AllArgsConstructor @Setter

public class InterventionMedcin {

    private Long idIntervention;
    private String noteMedcin;
    private Acte acte;
    private Consultation consultation;
    private Long dent;
    private Double prixPatient;
}
