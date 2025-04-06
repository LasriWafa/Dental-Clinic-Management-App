package ma.VitaCareApp.presentation.view.commun.sideMenu;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SideMenu extends JPanel {
    public SideMenu(String role, ActionListener menuButtonListener) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Création du bouton Profile
        FormeBtn profileButton = new FormeBtn("Profile", createIcon("profile.png"));
        profileButton.setActionCommand("Profile");
        profileButton.addActionListener(menuButtonListener);

        // Création des autres boutons avec écouteurs
        FormeBtn dashboardButton = new FormeBtn("Dashboard", createIcon("dashboard.png"));
        dashboardButton.setActionCommand("Dashboard");
        dashboardButton.addActionListener(menuButtonListener);

        FormeBtn dossierMedicalButton = new FormeBtn("Dossier Médical", createIcon("dossier.png"));
        dossierMedicalButton.setActionCommand("DossierMedical");
        dossierMedicalButton.addActionListener(menuButtonListener);

        FormeBtn patientButton = new FormeBtn("Patient", createIcon("user.png"));
        patientButton.setActionCommand("Patients");
        patientButton.addActionListener(menuButtonListener);

        FormeBtn agendaButton = new FormeBtn("Agenda", createIcon("agenda.png"));
        agendaButton.setActionCommand("Agenda");
        agendaButton.addActionListener(menuButtonListener);

        FormeBtn factureButton = new FormeBtn("Facture", createIcon("money.png"));
        factureButton.setActionCommand("Facture");
        factureButton.addActionListener(menuButtonListener);

        FormeBtn LogoutButton = new FormeBtn("Logout", createIcon("logout.png"));
        LogoutButton.setActionCommand("Logout");
        LogoutButton.addActionListener(menuButtonListener);

        // Ajout des boutons au panel avec espacement
        add(profileButton);
        add(Box.createVerticalStrut(10));
        add(dashboardButton);
        add(Box.createVerticalStrut(10));
        if (role.equals("Dentist")) {
            add(dossierMedicalButton);
            add(Box.createVerticalStrut(10));
        }
        add(patientButton);
        add(Box.createVerticalStrut(10));
        add(agendaButton);
        add(Box.createVerticalStrut(10));
        add(factureButton);
        add(Box.createVerticalStrut(10));
        add(LogoutButton);
    }

    private ImageIcon createIcon(String name) {
        String path = "/icons/" + name;
        java.net.URL imgURL = SideMenu.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
