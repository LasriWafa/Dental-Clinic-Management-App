package ma.VitaCareApp.models;

import lombok.*;
import java.util.List;

@NoArgsConstructor @Getter @AllArgsConstructor @Setter

public class Caisse {

    private Long idCaisse;
    private Double dayRevenue;
    private Double monthRevenue;
    private Double yearRevenue;
    private List<FinancialSituation> financialSituation;
    private Cabinet cabinet;

}
