package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.Specialite;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Dentist extends Staff {

    private Specialite specialite;

}
