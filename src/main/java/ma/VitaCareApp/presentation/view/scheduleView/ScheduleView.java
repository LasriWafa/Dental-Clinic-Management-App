package ma.VitaCareApp.presentation.view.scheduleView;

import javax.swing.*;
import java.awt.*;

public class ScheduleView extends JPanel {
    public ScheduleView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Placeholder content
        JTextArea placeholder = new JTextArea("This is the Schedule view.\n\nHere, you can view and manage your daily schedule as a dentist.");
        placeholder.setFont(new Font("Poppins", Font.PLAIN, 16));
        placeholder.setEditable(false);
        placeholder.setBackground(Color.WHITE);
        add(placeholder, BorderLayout.CENTER);
    }
}