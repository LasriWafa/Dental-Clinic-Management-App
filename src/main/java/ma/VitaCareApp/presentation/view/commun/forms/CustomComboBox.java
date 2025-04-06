package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import java.awt.*;

public class CustomComboBox extends JComboBox<String> {
    public CustomComboBox(String[] items) {
        super(items);
        setPreferredSize(new Dimension(200, 30));
        setFont(new Font("Poppins", Font.PLAIN, 14));
    }
}
