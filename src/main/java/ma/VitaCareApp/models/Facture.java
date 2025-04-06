package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.TypePayement;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Facture {

    private Long idFacture;
    private Double montantTotale;
    private Double montantRestant;
    private Double montantPaye = 0.0; // Default value
    private Consultation consultation;
    private TypePayement typePayement;
    private LocalDate dateFacturation;

    // Constructor for creating a new Facture with all fields
    public Facture(Long idFacture, Double montantTotale, Double montantPaye, Consultation consultation,
                   TypePayement typePayement, LocalDate dateFacturation) {
        this.idFacture = idFacture;
        this.montantTotale = montantTotale;
        this.montantPaye = montantPaye;
        this.consultation = consultation;
        this.typePayement = typePayement;
        this.dateFacturation = dateFacturation;
        this.calculerMontantRestant(); // Calculate the remaining amount
    }

    // Constructor for creating a new Facture without ID (used for new entries)
    public Facture(Double montantTotale, Double montantPaye, Consultation consultation,
                   TypePayement typePayement, LocalDate dateFacturation) {
        this.montantTotale = montantTotale;
        this.montantPaye = montantPaye;
        this.consultation = consultation;
        this.typePayement = typePayement;
        this.dateFacturation = dateFacturation;
        this.calculerMontantRestant(); // Calculate the remaining amount
    }

    // Setter for montantTotale
    public void setMontantTotale(Double montantTotale) {
        this.montantTotale = montantTotale;
        this.calculerMontantRestant(); // Recalculate montantRestant
    }

    // Setter for montantPaye
    public void setMontantPaye(Double montantPaye) {
        this.montantPaye = montantPaye;
        this.calculerMontantRestant(); // Recalculate montantRestant
    }

    // Calculate the remaining amount
    private void calculerMontantRestant() {
        if (montantTotale == null || montantPaye == null) {
            throw new IllegalArgumentException("MontantTotale and MontantPaye must not be null.");
        }
        if (montantPaye > montantTotale) {
            throw new IllegalArgumentException("MontantPaye cannot be greater than MontantTotale.");
        }
        this.montantRestant = this.montantTotale - this.montantPaye;
    }

    @Override
    public String toString() {
        return String.join(",",
                idFacture != null ? idFacture.toString() : "",
                montantTotale != null ? montantTotale.toString() : "",
                montantRestant != null ? montantRestant.toString() : "",
                montantPaye != null ? montantPaye.toString() : "",
                consultation != null ? consultation.getIdConsultation().toString() : "",
                typePayement != null ? typePayement.name() : "",
                dateFacturation != null ? dateFacturation.toString() : ""
        );
    }
}