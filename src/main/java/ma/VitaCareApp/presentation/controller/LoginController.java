package ma.VitaCareApp.presentation.controller;

import ma.VitaCareApp.models.enums.UserRole;
import ma.VitaCareApp.presentation.dto.LoginDTO;
import ma.VitaCareApp.presentation.view.commun.LoginView;
import ma.VitaCareApp.presentation.view.dentistView.DentistView;
import ma.VitaCareApp.presentation.view.secretaryView.SecretaryView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private final LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        attachListeners();
    }

    private void attachListeners() {
        loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = loginView.getEmail();
                String password = loginView.getPassword();

                // Authenticate the user
                LoginDTO loginDTO = authenticate(email, password);

                if (loginDTO != null) {
                    loginView.clearError();
                    System.out.println("Login successful!");

                    // Open the appropriate interface based on the user's role
                    if (loginDTO.getRole() == UserRole.DENTIST) {
                        openDentistView(email); // Pass the user's email
                    } else if (loginDTO.getRole() == UserRole.SECRETARY) {
                        openSecretaryView(email); // Pass the user's email
                    }
                } else {
                    loginView.showError("Invalid email or password.");
                }
            }
        });
    }

    private LoginDTO authenticate(String email, String password) {
        // Replace this with actual authentication logic (e.g., check against a file or database)
        if (email.equals("dentist@example.com") && password.equals("dentist123")) {
            return new LoginDTO(email, password, UserRole.DENTIST);
        } else if (email.equals("secretary@example.com") && password.equals("secretary123")) {
            return new LoginDTO(email, password, UserRole.SECRETARY);
        }
        return null; // Authentication failed
    }

    private void openDentistView(String userEmail) {
        loginView.dispose(); // Close the login view
        new DentistView(userEmail); // Open the dentist view with the user's email
    }

    private void openSecretaryView(String userEmail) {
        loginView.dispose(); // Close the login view
        new SecretaryView(userEmail); // Open the secretary view with the user's email
    }
}