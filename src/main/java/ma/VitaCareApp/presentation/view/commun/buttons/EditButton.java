package ma.VitaCareApp.presentation.view.commun.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EditButton extends buttons {
    public EditButton(ActionListener action) {
        super("Modifier", getIcon("/icons/edit.png"), new Color(27, 47, 182)); // Bleu
        setBackground(new Color(27, 47, 182));
        addActionListener(action);
    }

    private static Icon getIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(EditButton.class.getResource(path));
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
