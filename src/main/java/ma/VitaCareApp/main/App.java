import ma.VitaCareApp.presentation.controller.LoginController;
import ma.VitaCareApp.presentation.view.commun.LoginView;
import javax.swing.*;

public static void main(String[] args) {
    // Ensure the UI is created and initialized on the Event Dispatch Thread (EDT)
    SwingUtilities.invokeLater(() -> {
        // Create the login controller and attach it to the view
        new LoginController(new LoginView());
    });
}