package ma.VitaCareApp.models;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class PrescriptionMedicament {

    private Long idPrescription;
    private int uniteMinAPrendre;
    private int uniteMaxAPrendre;
    private String contraintesAlimentaire;
    private Ordonnance ordonnance;
    private String contraintesTemps;
    private  Medicament medicamentAPrescrire;

}
