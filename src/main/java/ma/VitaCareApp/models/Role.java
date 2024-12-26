package ma.VitaCareApp.models;

import lombok.*;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter

public class Role {

    private String roleName;
    private List<String> privileges;

    public void addPrivileges(String str) {}
    public void deletePrivileges(String str) {}
    // public List<String> getPrivileges() { return List.of(); }

}
