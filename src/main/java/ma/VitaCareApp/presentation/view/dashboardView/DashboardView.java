package ma.VitaCareApp.presentation.view.dashboardView;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JPanel {

    public DashboardView() {
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Create and set up the title label with an icon
        JLabel titleLabel = createTitleLabel();
        add(titleLabel, BorderLayout.NORTH);

        // Add the dashboard card to the center
        add(new DashboardCard(), BorderLayout.CENTER);
    }

    private JLabel createTitleLabel() {
        // Load and scale the dashboard icon
        ImageIcon dashboardIcon = new ImageIcon(getClass().getResource("/icons/dashboard.png"));
        Image scaledImage = dashboardIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        dashboardIcon = new ImageIcon(scaledImage);

        // Create the title label
        JLabel titleLabel = new JLabel("Dashboard", dashboardIcon, JLabel.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        return titleLabel;
    }
}