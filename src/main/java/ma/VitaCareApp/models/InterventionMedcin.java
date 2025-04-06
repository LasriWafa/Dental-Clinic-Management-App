package ma.VitaCareApp.models;

import lombok.*;

@NoArgsConstructor @Getter @AllArgsConstructor @Setter

public class InterventionMedcin {

    private Long idIntervention;
    private String noteMedcin;
    private Acte acte;
//    private Consultation consultation;
    private Long dent;
    private Double prixPatient;

    // Helper method to get the Acte ID
    public Long getActeId() {
        return acte != null ? acte.getIdActe() : null;
    }

    @Override
    public String toString() {
        return String.join(",",
                idIntervention.toString(),
                noteMedcin,
                getActeId().toString(), // Store the Acte ID
                dent.toString(),
                prixPatient.toString()
        );
    }

}
