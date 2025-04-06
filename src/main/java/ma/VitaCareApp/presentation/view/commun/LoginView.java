package ma.VitaCareApp.presentation.view.commun;

import lombok.Getter;
import ma.VitaCareApp.presentation.view.commun.forms.CustomInput;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoginView extends JFrame {
    private CustomInput emailField;
    private CustomInput passwordField;
    @Getter
    private JButton loginButton;
    private JLabel errorLabel;

    public LoginView() {
        setTitle("Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/bg1.jpg")));
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Panel pour le formulaire de connexion
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titleLabel = new JLabel("Veuillez-vous connecter!");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 37));
        titleLabel.setForeground(new Color(35, 71, 93));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);

        // Email
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setOpaque(false);
        emailPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));


        emailField = new CustomInput(null, false, "Entrer votre email");
        emailField.setPreferredSize(new Dimension(150, 45));
        emailPanel.add(emailField);
        formPanel.add(emailPanel);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setOpaque(false);
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));


        passwordField = new CustomInput(null, true, "Entrer votre password");
        passwordField.setPreferredSize(new Dimension(150, 45));
        passwordPanel.add(passwordField);
        formPanel.add(passwordPanel);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(errorLabel);

        loginButton = new JButton("Se connecter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(54, 104, 133));
        loginButton.setForeground(new Color(255, 255, 255, 255));
        loginButton.setFont(new Font("Poppins", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(loginButton);

        mainPanel.add(formPanel);

        add(mainPanel);
        setVisible(true);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
}