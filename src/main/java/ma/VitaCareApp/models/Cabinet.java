package ma.VitaCareApp.models;

import lombok.*;
import java.util.List;

@Getter @AllArgsConstructor @NoArgsConstructor @Setter

public class Cabinet {

    private String name;
    private String logo;
    private Long idCabinet;
    private String email;
    private Caisse caisse;
    private List<Staff> staff;
    private String adresse;
    private String telephone1;
    private String telephone2;
    private String tel;

}
