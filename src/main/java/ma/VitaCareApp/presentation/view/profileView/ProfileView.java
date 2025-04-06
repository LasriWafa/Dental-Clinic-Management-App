package ma.VitaCareApp.presentation.view.profileView;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ProfileView extends JPanel {

    public ProfileView(String role, String userName, String email) {
        setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));

        // Section supérieure : Titre et icône
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(92, 142, 154));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel roleIcon = new JLabel();
        ImageIcon originalIcon = null;

        if (role.equals("Dentist") || role.equals("Secretary")) {
            originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/user.png")));
        }

        // Redimensionner l'icône
        if (originalIcon != null) {
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            roleIcon.setIcon(resizedIcon);
        }

        roleIcon.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(roleIcon, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new GridLayout(5, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true)
        ));

        // Ajout des icônes et des labels
        JLabel nameLabel = createInfoLabel("Nom : " + userName, "/icons/picture.png");
        JLabel roleLabel = createInfoLabel("Rôle : " + (role.equals("Dentist") ? "Chirurgien Dentiste" : "Secrétaire"), "/icons/tooth.png");
        JLabel emailLabel = createInfoLabel("Email : " + email, "/icons/home.png");
        JLabel addressLabel = createInfoLabel("Adresse : 27, Avenue Annakhil, Hay Riad", "/icons/marker.png");
        JLabel phoneLabel = createInfoLabel("Téléphone : 06 45 67 23 45", "/icons/check.png");

        infoPanel.add(nameLabel);
        infoPanel.add(roleLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(phoneLabel);

        // Encapsuler le panel d'informations dans une carte avec une ombre
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(new Color(245, 245, 245));
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cardPanel.add(infoPanel, BorderLayout.CENTER);

        add(cardPanel, BorderLayout.CENTER);
    }

    // Méthode pour créer un label avec une icône
    private JLabel createInfoLabel(String text, String iconPath) {
        JLabel label = new JLabel(text, JLabel.LEFT);
        label.setFont(new Font("Poppins", Font.PLAIN, 16));
        label.setForeground(new Color(51, 51, 51));

        // Ajouter une icône
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        Image resizedIcon = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(resizedIcon));
        label.setIconTextGap(10);

        return label;
    }
}