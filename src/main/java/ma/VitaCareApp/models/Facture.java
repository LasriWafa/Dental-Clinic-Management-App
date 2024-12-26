package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.TypePayement;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter

public class Facture {

    private Long idFacture;
    private Double montantTotale;
    private Double montantRestant;
    private Double montantPaye;
    private Consultation consultation;
    private TypePayement typePayement;
    private FinancialSituation financialSituation;
    private LocalDate dateFacturation;

    public static void calculerMontantRestant() {}

}
