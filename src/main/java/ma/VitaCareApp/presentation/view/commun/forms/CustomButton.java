package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String text, Color backgroundColor, Color textColor) {
        super(text);
        setBackground(backgroundColor);
        setForeground(textColor);
        setFont(new Font("Poppins", Font.BOLD, 14));
        setFocusPainted(false);
        setPreferredSize(new Dimension(120, 35));
    }
}
