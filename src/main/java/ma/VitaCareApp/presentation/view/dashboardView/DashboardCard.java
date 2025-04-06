package ma.VitaCareApp.presentation.view.dashboardView;

import ma.VitaCareApp.presentation.view.dashboardView.DashboardInterface;

import javax.swing.*;
import java.awt.*;

public class DashboardCard extends JPanel {
    public DashboardCard() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));  // Increased spacing
        setBackground(Color.WHITE);
        // Example cards with the new design
        DashboardInterface NumPatient = new DashboardInterface("Nombre de patient", "24");
        DashboardInterface CA = new DashboardInterface("Chiffre d'affaire", "30000");
        DashboardInterface FactureNP = new DashboardInterface("Facture non payé", "7");
        DashboardInterface FactureP = new DashboardInterface("Facture  payé", "25");

        // Add cards to the panel
        add(NumPatient);
        add(CA);
        add(FactureNP);
        add(FactureP);
    }
}