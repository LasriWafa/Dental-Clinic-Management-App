package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.CategoryActe;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter

public class Acte {

    private Long idActe;
    private String libeller;
    private Double prixDeBase;
    private List<InterventionMedcin> interventions;
    private CategoryActe categoryActe;
}
