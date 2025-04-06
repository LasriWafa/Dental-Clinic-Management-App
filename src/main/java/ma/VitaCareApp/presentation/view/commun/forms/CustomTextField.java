package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
        super(columns);
        setPreferredSize(new Dimension(200, 30));
        setFont(new Font("Poppins", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
}
