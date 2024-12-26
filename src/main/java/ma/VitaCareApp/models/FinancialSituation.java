package ma.VitaCareApp.models;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @Setter @NoArgsConstructor @Getter

public class FinancialSituation {

    private Long idFinancialSituation;
    private DossierMedical dossierMedical;
    private List<Facture> factures;
    private LocalDate dateCreation;
    private Double montantGlobaleRestant;
    private Double montantGlobalePaye;

}
