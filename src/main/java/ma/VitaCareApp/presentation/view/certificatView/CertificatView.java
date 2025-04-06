package ma.VitaCareApp.presentation.view.certificatView;


import javax.swing.*;
import java.awt.*;

public class CertificatView extends JPanel {
    public CertificatView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Certificat", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JTextArea placeholder = new JTextArea("This is the Certificat view.\n\nHere, you can manage patient certificates.");
        placeholder.setFont(new Font("Poppins", Font.PLAIN, 16));
        placeholder.setEditable(false);
        placeholder.setBackground(Color.WHITE);
        add(placeholder, BorderLayout.CENTER);
    }
}
