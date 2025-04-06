package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import java.awt.*;

public class DeleteButton extends JButton {
    public DeleteButton() {
        super("Supprimer"); // Texte par défaut du bouton
        setBackground(Color.RED);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setFont(new Font("Arial", Font.BOLD, 12));
    }
}
