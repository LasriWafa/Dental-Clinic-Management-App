package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.TypeRDV;
import java.time.LocalDate;

@Getter @AllArgsConstructor @NoArgsConstructor @Setter

public class RendezVous {

    private Long idRDV;
    private String motif;
    private LocalDate temps;
    private DossierMedical dossierMedical;
    private Consultation consultation;
    private TypeRDV typeRDV;
    private LocalDate dateRDV;
}
