package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.TypeRDV;

import java.time.LocalDate;

@Getter @AllArgsConstructor @NoArgsConstructor @Setter
public class RendezVous {
    private Long idRDV;
    private String motif;
    private String temps; // Changed from LocalDate to String
    private TypeRDV typeRDV;
    private LocalDate dateRDV;

    @Override
    public String toString() {
        return String.join(",",
                getIdRDV().toString(),
                getMotif(),
                getTemps(), // No need to format as it's already a String
                getTypeRDV().name(),
                getDateRDV().toString() // Ensure the date format is yyyy-MM-dd
        );
    }
}