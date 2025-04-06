package ma.VitaCareApp.presentation.view.commun.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RefreshButton extends buttons {
    public RefreshButton(ActionListener action) {
        super("Actualiser", getIcon("/icons/refresh.png"), new Color(120, 120, 120)); // Gris
        setBackground(new Color(120, 120, 120));
        addActionListener(action);
    }

    private static Icon getIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(RefreshButton.class.getResource(path));
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
