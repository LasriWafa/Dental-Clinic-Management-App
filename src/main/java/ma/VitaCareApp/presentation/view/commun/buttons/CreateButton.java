package ma.VitaCareApp.presentation.view.commun.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreateButton extends buttons {
    public CreateButton(ActionListener action) {
        super("Ajouter", getIcon("/icons/add.png"), new Color(85, 185, 85)); // Vert
        setBackground(new Color(85, 185, 85));
        addActionListener(action);
    }

    private static Icon getIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(CreateButton.class.getResource(path));
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
