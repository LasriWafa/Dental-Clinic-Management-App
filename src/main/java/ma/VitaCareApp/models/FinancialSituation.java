package ma.VitaCareApp.models;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FinancialSituation {

    private Long idFinancialSituation;
    private DossierMedical dossierMedical;
    private List<Facture> factures;
    private LocalDate dateCreation;
    private Double montantGlobaleRestant;
    private Double montantGlobalePaye;

    @Override
    public String toString() {
        return String.join(",",
                idFinancialSituation.toString(),
                dossierMedical.getNumeroDossier(), // Store DossierMedical ID
                factures.stream().map(Facture::getIdFacture).map(Object::toString).reduce("", (a, b) -> a.isEmpty() ? b : a + ";" + b), // Store Facture IDs as a semicolon-separated string
                dateCreation.toString(),
                montantGlobaleRestant.toString(),
                montantGlobalePaye.toString()
        );
    }
}