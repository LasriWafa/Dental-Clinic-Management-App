package ma.VitaCareApp.presentation.dto;

import ma.VitaCareApp.models.enums.UserRole;

public class LoginDTO {
    private String email;
    private String password;
    private UserRole role;

    public LoginDTO(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}
