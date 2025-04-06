package ma.VitaCareApp.presentation.view.commun.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DeleteButton extends buttons {
    public DeleteButton(ActionListener action) {
        super("Supprimer", getIcon("/icons/delete.png"), new Color(213, 42, 42)); // Rouge
        setBackground(new Color(213, 42, 42));
        addActionListener(action);
    }

    private static Icon getIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(DeleteButton.class.getResource(path));
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
