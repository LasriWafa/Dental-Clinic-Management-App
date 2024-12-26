package ma.VitaCareApp.models;

import lombok.*;
import ma.VitaCareApp.models.enums.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class Staff extends User{

    private Cabinet cabinetDeTravail;
    private StatusEmployee statusEmployee;
    private Map<DayOfWeek, Disponibilite> disponibilite;
    private Double salaireDeBase;
    private LocalDate DateRetourConge;

    public void takeVacation(LocalDate date, StatusEmployee se) {}

}
