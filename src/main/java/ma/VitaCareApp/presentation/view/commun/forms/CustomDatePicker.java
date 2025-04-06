package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import java.awt.*;

public class CustomDatePicker extends JPanel {
    public CustomDatePicker() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JTextField dateField = new JTextField(10);
        dateField.setPreferredSize(new Dimension(150, 30));
        dateField.setFont(new Font("Poppins", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JButton calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(40, 30));
        calendarButton.setFocusPainted(false);

        add(dateField);
        add(calendarButton);
    }
}
