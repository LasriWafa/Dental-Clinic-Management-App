package ma.VitaCareApp.models;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class PrescriptionMedicament {

    private Long idPrescription;
    private int uniteMinAPrendre;
    private int uniteMaxAPrendre;
    private String contraintesAlimentaire;
//    private Ordonnance ordonnance;
    private String contraintesTemps;
    private  Medicament medicamentAPrescrire;

    // Helper method to get the Medicament ID
    public Long getMedicamentId() {
        return medicamentAPrescrire != null ? medicamentAPrescrire.getIdMedicine() : null;
    }

    @Override
    public String toString() {
        return String.join(",",
                idPrescription.toString(),
                String.valueOf(uniteMinAPrendre),
                String.valueOf(uniteMaxAPrendre),
                contraintesAlimentaire,
                contraintesTemps,
                getMedicamentId().toString() // Store the Medicament ID
        );
    }

}
