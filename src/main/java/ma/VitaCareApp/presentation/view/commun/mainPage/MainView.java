package ma.VitaCareApp.presentation.view.commun.mainPage;

import ma.VitaCareApp.dao.implementationDAO.*;
import ma.VitaCareApp.presentation.controller.implementationController.*;
import ma.VitaCareApp.presentation.view.agendaView.AgendaView;
import ma.VitaCareApp.presentation.view.dashboardView.DashboardView;
import ma.VitaCareApp.presentation.view.factureView.FactureView;
import ma.VitaCareApp.presentation.view.commun.LoginView;
import ma.VitaCareApp.presentation.view.commun.headers.NavBar;
import ma.VitaCareApp.presentation.view.commun.sideMenu.SideMenu;
import ma.VitaCareApp.presentation.view.dossierMedicalView.DossierMedicalView;
import ma.VitaCareApp.presentation.view.ordonnancesView.OrdonnancesView;
import ma.VitaCareApp.presentation.view.patientView.PatientsView;
import ma.VitaCareApp.presentation.view.profileView.ProfileView;
import ma.VitaCareApp.presentation.view.rendezVousView.RendezVousView;
import ma.VitaCareApp.presentation.view.consultationView.ConsultationView;
import ma.VitaCareApp.presentation.view.situationFinanciereView.SituationFinancialView;
import ma.VitaCareApp.services.implementationService.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    private static final String LOADING = "Loading";
    private static final String DASHBOARD = "Dashboard";
    private static final String PROFILE = "Profile";
    private static final String PATIENTS = "Patients";
    private static final String AGENDA = "Agenda";
    private static final String FACTURE = "Facture";
    private static final String DOSSIER_MEDICAL = "DossierMedical";
    private static final String ORDONNANCE = "Ordonnance";
    private static final String CONSULTATION = "Consultation";
    private static final String RENDEZ_VOUS = "RendezVous";
    private static final String SITUATION_FINANCIERE = "SituationFinancière";

    private String userEmail;
    private JPanel contentPanel;
    private JPanel secondaryContentPanel;
    private JPanel secondaryNavBar;
    private NavBar navBar;
    private SideMenu sideMenu;

    public MainView(String role, String userEmail) {
        this.userEmail = userEmail;
        initializeUI(role);
        initializeDataLoading(role);
    }

    private void initializeUI(String role) {
        setTitle(role + " Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        navBar = new NavBar(userEmail);
        sideMenu = new SideMenu(role, new MenuButtonListener());
        contentPanel = new JPanel(new CardLayout());
        secondaryNavBar = createSecondaryNavBar();
        secondaryContentPanel = new JPanel(new CardLayout());

        add(navBar, BorderLayout.NORTH);
        add(sideMenu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        contentPanel.add(loadingLabel, LOADING);
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, LOADING);
    }

    private void initializeDataLoading(String role) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                initializeDependencies(role);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
                    cardLayout.show(contentPanel, DASHBOARD);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainView.this, "Failed to initialize application: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
        setVisible(true);
    }

    private void initializeDependencies(String role) {
        PatientDAO patientDAO = new PatientDAO();
        PatientService patientService = new PatientService(patientDAO);
        PatientController patientController = new PatientController(patientService);

        RendezVousDao rdvDao = new RendezVousDao();
        MedicalHistoryDao medicalHistoryDao = new MedicalHistoryDao(patientDAO);
        MedicamentDao medicamentDao = new MedicamentDao(medicalHistoryDao);
        PrescriptionMedicamentDao prescriptionMedicamentDao = new PrescriptionMedicamentDao(medicamentDao);
        OrdonnanceDao ordonnanceDao = new OrdonnanceDao(prescriptionMedicamentDao);
        ActeDao acteDao = new ActeDao();
        InterventionMedcinDao interventionMedcinDao = new InterventionMedcinDao(acteDao);
        ConsultationDao consultationDao = new ConsultationDao(ordonnanceDao, interventionMedcinDao);
        ConsultationService consultationService = new ConsultationService(consultationDao);
        ConsultationController consultationController = new ConsultationController(consultationService);
        DossierMedicalDao dossierMedicalDao = new DossierMedicalDao(patientDAO, rdvDao, ordonnanceDao, consultationDao);
        DossierMedicalService dossierMedicalService = new DossierMedicalService(dossierMedicalDao);
        DossierMedicalController dossierMedicalController = new DossierMedicalController(dossierMedicalService);
        RendezVousService rendezVousService = new RendezVousService(rdvDao);
        RendezVousController rendezVousController = new RendezVousController(rendezVousService);
        OrdonnanceService ordonnanceService = new OrdonnanceService(ordonnanceDao);
        OrdonnanceController ordonnanceController = new OrdonnanceController(ordonnanceService);
        PrescriptionMedicamentService prescriptionMedicamentService = new PrescriptionMedicamentService(prescriptionMedicamentDao);
        PrescriptionMedicamentController prescriptionMedicamentController = new PrescriptionMedicamentController(prescriptionMedicamentService);
        InterventionMedcinService interventionMedcinService = new InterventionMedcinService(interventionMedcinDao);
        InterventionMedcinController interventionMedcinController = new InterventionMedcinController(interventionMedcinService);
        ActeService acteService = new ActeService(acteDao);
        FactureDao factureDao = new FactureDao(consultationDao);
        FactureService factureService = new FactureService(factureDao);
        FactureController factureController = new FactureController(factureService);
        FinancialSituationDao financialSituationDao = new FinancialSituationDao(dossierMedicalDao, factureDao);

        addContentPanels(role, patientController, dossierMedicalController, rendezVousController, ordonnanceController, prescriptionMedicamentController, consultationController, interventionMedcinDao, interventionMedcinController, acteService, patientDAO, factureController, consultationDao, financialSituationDao, dossierMedicalDao, factureDao);
    }

    private void addContentPanels(String role, PatientController patientController, DossierMedicalController dossierMedicalController, RendezVousController rendezVousController, OrdonnanceController ordonnanceController, PrescriptionMedicamentController prescriptionMedicamentController, ConsultationController consultationController, InterventionMedcinDao interventionMedcinDao, InterventionMedcinController interventionMedcinController, ActeService acteService, PatientDAO patientDAO, FactureController factureController, ConsultationDao consultationDao, FinancialSituationDao financialSituationDao, DossierMedicalDao dossierMedicalDao, FactureDao factureDao) {
        String userName = role.equals("Dentist") ? "Dr. Amine Benyahia" : "Secrétaire Sarah";
        contentPanel.add(new ProfileView(role, userName, userEmail), PROFILE);
        contentPanel.add(new PatientsView(patientController), PATIENTS);
        contentPanel.add(new AgendaView(), AGENDA);
        contentPanel.add(new FactureView(factureController, consultationDao), FACTURE);
        contentPanel.add(new DashboardView(), DASHBOARD);

        if (role.equals("Dentist")) {
            secondaryContentPanel.add(new DossierMedicalView(dossierMedicalController, rendezVousController, ordonnanceController, prescriptionMedicamentController, consultationController, interventionMedcinDao, interventionMedcinController, acteService, patientDAO), DOSSIER_MEDICAL);
            secondaryContentPanel.add(new OrdonnancesView(ordonnanceController, prescriptionMedicamentController), ORDONNANCE);
            JScrollPane ordonnanceScrollPane = new JScrollPane(new OrdonnancesView(ordonnanceController, prescriptionMedicamentController));
            ordonnanceScrollPane.setPreferredSize(new Dimension(ordonnanceScrollPane.getPreferredSize().width, 300));
            secondaryContentPanel.add(ordonnanceScrollPane, ORDONNANCE);
            secondaryContentPanel.add(new ConsultationView(consultationController, ordonnanceController, interventionMedcinDao, interventionMedcinController, acteService), CONSULTATION);
            secondaryContentPanel.add(new RendezVousView(rendezVousController), RENDEZ_VOUS);
            secondaryContentPanel.add(new SituationFinancialView(financialSituationDao, dossierMedicalDao, factureDao), SITUATION_FINANCIERE);

            JPanel dossierMedicalContainer = new JPanel(new BorderLayout());
            dossierMedicalContainer.add(secondaryNavBar, BorderLayout.NORTH);
            dossierMedicalContainer.add(secondaryContentPanel, BorderLayout.CENTER);
            contentPanel.add(dossierMedicalContainer, DOSSIER_MEDICAL);
        } else if (role.equals("Secretary")) {
            contentPanel.add(new RendezVousView(rendezVousController), RENDEZ_VOUS);
        }
    }

    private JPanel createSecondaryNavBar() {
        JPanel secondaryNavBar = new JPanel();
        secondaryNavBar.setBackground(new Color(240, 240, 240));
        secondaryNavBar.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        Font buttonFont = new Font("Poppins", Font.BOLD, 14);
        Color buttonColor = new Color(104, 182, 201);
        Color hoverColor = new Color(72, 131, 145);
        Color pressedColor = new Color(57, 102, 110);

        JButton dossierButton = createStyledButton("DossierMedical", buttonFont, buttonColor, hoverColor, pressedColor);
        JButton ordonnanceButton = createStyledButton("Ordonnances", buttonFont, buttonColor, hoverColor, pressedColor);
        JButton consultationButton = createStyledButton("Consultation", buttonFont, buttonColor, hoverColor, pressedColor);
        JButton rendezVousButton = createStyledButton("RendezVous", buttonFont, buttonColor, hoverColor, pressedColor);
        JButton situationFinanciereButton = createStyledButton("Situation Financière", buttonFont, buttonColor, hoverColor, pressedColor);

        dossierButton.addActionListener(e -> switchSecondaryView(DOSSIER_MEDICAL));
        ordonnanceButton.addActionListener(e -> switchSecondaryView(ORDONNANCE));
        consultationButton.addActionListener(e -> switchSecondaryView(CONSULTATION));
        rendezVousButton.addActionListener(e -> switchSecondaryView(RENDEZ_VOUS));
        situationFinanciereButton.addActionListener(e -> switchSecondaryView(SITUATION_FINANCIERE));

        secondaryNavBar.add(dossierButton);
        secondaryNavBar.add(ordonnanceButton);
        secondaryNavBar.add(consultationButton);
        secondaryNavBar.add(rendezVousButton);
        secondaryNavBar.add(situationFinanciereButton);

        return secondaryNavBar;
    }

    private JButton createStyledButton(String text, Font font, Color backgroundColor, Color hoverColor, Color pressedColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
        });

        return button;
    }

    private void switchSecondaryView(String viewName) {
        CardLayout cardLayout = (CardLayout) secondaryContentPanel.getLayout();
        cardLayout.show(secondaryContentPanel, viewName);
    }

    private class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String menuItem = e.getActionCommand();
            CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
            if (menuItem.equals("Logout")) {
                dispose();
                new LoginView();
            } else if (menuItem.equals(DOSSIER_MEDICAL)) {
                secondaryNavBar.setVisible(true);
                cardLayout.show(contentPanel, DOSSIER_MEDICAL);
            } else {
                secondaryNavBar.setVisible(false);
                cardLayout.show(contentPanel, menuItem);
            }
        }
    }
}