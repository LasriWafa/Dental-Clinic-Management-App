package ma.VitaCareApp.presentation.view.commun.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SearchButton extends buttons {
    public SearchButton(ActionListener action) {
        super("Search", getIcon("/icons/search.png"), new Color(255, 193, 7)); // Yellow color
        setBackground(new Color(255, 193, 7)); // Yellow color
        setForeground(Color.BLACK); // Black text for better contrast
        addActionListener(action); // Add the action listener passed from the view
    }

    private static Icon getIcon(String path) {
        ImageIcon originalIcon = new ImageIcon(SearchButton.class.getResource(path));
        Image img = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}