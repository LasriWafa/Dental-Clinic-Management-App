package ma.VitaCareApp.models;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class User extends Person{

    private String username;
    private String password;
    private List<Role> role;
    private LocalDate lastConnectionDate;
    private LocalDate modificationDate;
    private LocalDate creationDate;

    public void addRole(Role r) {}
    public boolean hasPrivilege(String str) {
        return false;
    }

}
