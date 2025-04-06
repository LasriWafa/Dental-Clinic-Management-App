package ma.VitaCareApp.presentation.view.commun.headers;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class NavBar extends JPanel {
    public NavBar(String userEmail) {
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(getWidth(), 50)); // Set height of the nav bar
        setLayout(new BorderLayout());

        // Left: App Logo
        URL logoUrl = getClass().getResource("/images/logo.png");
        if (logoUrl == null) {
            System.err.println("Logo image not found! Check the path.");
        } else {
            // Load and resize the logo
            ImageIcon originalLogoIcon = new ImageIcon(logoUrl);
            Image logoImage = originalLogoIcon.getImage();
            Image resizedLogoImage = logoImage.getScaledInstance(100, 40, Image.SCALE_SMOOTH); // Resize to 100x40
            ImageIcon resizedLogoIcon = new ImageIcon(resizedLogoImage);

            JLabel logoLabel = new JLabel(resizedLogoIcon);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
            add(logoLabel, BorderLayout.WEST);
        }

        // Middle: Welcome Message with User Email
        JLabel welcomeLabel = new JLabel("Welcome, " + userEmail, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.CENTER);

        // Right: User Icon
        URL userIconUrl = getClass().getResource("/icons/user.png");
        if (userIconUrl == null) {
            System.err.println("User icon image not found! Check the path.");
        } else {
            // Load and resize the user icon
            ImageIcon originalUserIcon = new ImageIcon(userIconUrl);
            Image userIconImage = originalUserIcon.getImage();
            Image resizedUserIconImage = userIconImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Resize to 40x40
            ImageIcon resizedUserIcon = new ImageIcon(resizedUserIconImage);

            JLabel userIconLabel = new JLabel(resizedUserIcon);
            userIconLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
            add(userIconLabel, BorderLayout.EAST);
        }
    }
}