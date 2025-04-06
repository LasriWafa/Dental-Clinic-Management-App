package ma.VitaCareApp.presentation.view.dossierMedicalView;

import ma.VitaCareApp.dao.implementationDAO.*;
import ma.VitaCareApp.models.*;
import ma.VitaCareApp.models.enums.StatusPayement;
import ma.VitaCareApp.presentation.controller.implementationController.*;
import ma.VitaCareApp.presentation.view.Search;
import ma.VitaCareApp.presentation.view.commun.buttons.CrudButtonPanel;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.services.exceptions.ServiceException;
import ma.VitaCareApp.services.implementationService.ActeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DossierMedicalView extends JPanel {
    private final DossierMedicalController dossierMedicalController;
    private final PatientDAO patientDao;
    private TablePanel tablePanel;
    private CrudButtonPanel crudButtonPanel;

    // Additional dependencies (not used in this class but required for other views)
    private final RendezVousController rendezVousController;
    private final OrdonnanceController ordonnanceController;
    private final PrescriptionMedicamentController prescriptionMedicamentController;
    private final ConsultationController consultationController;
    private final InterventionMedcinDao interventionMedcinDao;
    private final InterventionMedcinController interventionMedcinController;
    private final ActeService acteService;

    public DossierMedicalView(DossierMedicalController dossierMedicalController,
                              RendezVousController rendezVousController,
                              OrdonnanceController ordonnanceController,
                              PrescriptionMedicamentController prescriptionMedicamentController,
                              ConsultationController consultationController,
                              InterventionMedcinDao interventionMedcinDao,
                              InterventionMedcinController interventionMedcinController,
                              ActeService acteService,
                              PatientDAO patientDao) {
        this.dossierMedicalController = dossierMedicalController;
        this.patientDao = patientDao;

        // Initialize additional dependencies (not used in this class but required for other views)
        this.rendezVousController = rendezVousController;
        this.ordonnanceController = ordonnanceController;
        this.prescriptionMedicamentController = prescriptionMedicamentController;
        this.consultationController = consultationController;
        this.interventionMedcinDao = interventionMedcinDao;
        this.interventionMedcinController = interventionMedcinController;
        this.acteService = acteService;

        initializeUI();
        loadDossierData();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/dossier.png"));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        JLabel titleLabel = new JLabel("Dossier medical", icon, JLabel.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 20));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        add(titlePanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        ImageIcon searchIcon = new ImageIcon(getClass().getResource("/icons/search.png"));
        Image searchImg = searchIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        searchIcon = new ImageIcon(searchImg);
        Search searchInput = new Search(searchIcon, "Rechercher un dossier medical");
        searchPanel.add(searchInput);
        add(searchPanel);

        add(Box.createRigidArea(new Dimension(0, 10)));

        // Initialize the CRUD button panel (now above the table panel)
        crudButtonPanel = new CrudButtonPanel(
                this::handleCreate,
                this::handleEdit,
                this::handleDelete,
                e -> loadDossierData()
        );
        add(crudButtonPanel);

        add(Box.createRigidArea(new Dimension(0, 10)));

        // Initialize the table panel
        String[] columnNames = {"Numero Dossier", "Date Creation", "Status Payement", "Patient", "RendezVous", "Ordonnances", "Consultations"};
        tablePanel = new TablePanel(columnNames);
        add(tablePanel);
    }


    private void loadDossierData() {
        try {
            List<DossierMedical> dossiers = dossierMedicalController.findAll();
            List<Object[]> tableData = dossiers.stream()
                    .map(dossier -> new Object[]{
                            dossier.getNumeroDossier(),
                            dossier.getDateCreation().toString(),
                            dossier.getStatusPayement(),
                            dossier.getPatient().getFirstName() + " " + dossier.getPatient().getLastName(),
                            dossier.getRdvs().size(),
                            dossier.getOrdonnances().size(),
                            dossier.getConsultations().size()
                    })
                    .collect(Collectors.toList());
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load dossier data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        JPanel formPanel = createDossierMedicalForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Dossier", formPanel);
        dialog.setVisible(true);
    }

    private void handleEdit(ActionEvent e) {
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a dossier to edit.", "No Dossier Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numeroDossier = (String) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        try {
            DossierMedical dossierToEdit = dossierMedicalController.findById(Long.parseLong(numeroDossier));
            JPanel formPanel = createDossierMedicalForm(dossierToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Dossier", formPanel);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve dossier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(ActionEvent e) {
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a dossier to delete.", "No Dossier Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the selected DossierMedical ID from the table
        String numeroDossier = (String) tablePanel.getTableModel().getValueAt(selectedRow, 0);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this dossier?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete the DossierMedical by numeroDossier
                dossierMedicalController.deleteById(Long.parseLong(numeroDossier));
                // Reload the table data
                loadDossierData();
                JOptionPane.showMessageDialog(this, "Dossier deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete dossier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createDossierMedicalForm(DossierMedical dossierMedical) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Numero Dossier
        JTextField numeroDossierField = new JTextField(dossierMedical != null ? dossierMedical.getNumeroDossier() : "");
        formPanel.add(new JLabel("Numero Dossier:"));
        formPanel.add(numeroDossierField);

        // Date Creation
        JTextField dateCreationField = new JTextField(dossierMedical != null ? dossierMedical.getDateCreation().toString() : "");
        formPanel.add(new JLabel("Date Creation (yyyy-MM-dd):"));
        formPanel.add(dateCreationField);

        // Status Payement
        JComboBox<StatusPayement> statusPayementComboBox = new JComboBox<>(StatusPayement.values());
        if (dossierMedical != null) {
            statusPayementComboBox.setSelectedItem(dossierMedical.getStatusPayement());
        }
        formPanel.add(new JLabel("Status Payement:"));
        formPanel.add(statusPayementComboBox);

        // Patient Dropdown
        JComboBox<Patient> patientComboBox = new JComboBox<>();
        try {
            List<Patient> patients = patientDao.findAll();
            patients.forEach(patientComboBox::addItem);
            if (dossierMedical != null) {
                patientComboBox.setSelectedItem(dossierMedical.getPatient());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(formPanel, "Failed to load patients: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientComboBox);

        // RendezVous IDs
        JTextField rendezVousIdsField = new JTextField(dossierMedical != null ?
                dossierMedical.getRdvs().stream().map(r -> r.getIdRDV().toString()).collect(Collectors.joining(",")) : "");
        formPanel.add(new JLabel("RendezVous IDs (comma-separated):"));
        formPanel.add(rendezVousIdsField);

        // Ordonnance IDs
        JTextField ordonnanceIdsField = new JTextField(dossierMedical != null ?
                dossierMedical.getOrdonnances().stream().map(o -> o.getIdOrdonnance().toString()).collect(Collectors.joining(",")) : "");
        formPanel.add(new JLabel("Ordonnance IDs (comma-separated):"));
        formPanel.add(ordonnanceIdsField);

        // Consultation IDs
        JTextField consultationIdsField = new JTextField(dossierMedical != null ?
                dossierMedical.getConsultations().stream().map(c -> c.getIdConsultation().toString()).collect(Collectors.joining(",")) : "");
        formPanel.add(new JLabel("Consultation IDs (comma-separated):"));
        formPanel.add(consultationIdsField);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (numeroDossierField.getText().trim().isEmpty()) {
                    throw new ServiceException("Numero Dossier is required.");
                }
                if (dateCreationField.getText().trim().isEmpty()) {
                    throw new ServiceException("Date Creation is required.");
                }

                // Create or update the DossierMedical object
                DossierMedical newDossier = new DossierMedical();
                newDossier.setNumeroDossier(numeroDossierField.getText());
                newDossier.setDateCreation(LocalDate.parse(dateCreationField.getText()));
                newDossier.setStatusPayement((StatusPayement) statusPayementComboBox.getSelectedItem());
                newDossier.setPatient((Patient) patientComboBox.getSelectedItem());

                // Set RendezVous, Ordonnances, and Consultations
                List<RendezVous> rdvs = new ArrayList<>();
                for (String id : rendezVousIdsField.getText().split(",")) {
                    if (!id.trim().isEmpty()) {
                        rdvs.add(rendezVousController.findById(Long.parseLong(id.trim())));
                    }
                }
                newDossier.setRdvs(rdvs);

                List<Ordonnance> ordonnances = new ArrayList<>();
                for (String id : ordonnanceIdsField.getText().split(",")) {
                    if (!id.trim().isEmpty()) {
                        ordonnances.add(ordonnanceController.findById(Long.parseLong(id.trim())));
                    }
                }
                newDossier.setOrdonnances(ordonnances);

                List<Consultation> consultations = new ArrayList<>();
                for (String id : consultationIdsField.getText().split(",")) {
                    if (!id.trim().isEmpty()) {
                        consultations.add(consultationController.findById(Long.parseLong(id.trim())));
                    }
                }
                newDossier.setConsultations(consultations);

                if (dossierMedical != null) {
                    dossierMedicalController.update(newDossier);
                } else {
                    dossierMedicalController.save(newDossier);
                }

                loadDossierData();
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(formPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Failed to save dossier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }

    private RendezVous selectOrCreateRendezVous() {
        // Open a dialog to select or create a new RendezVous
        // Return the selected or created RendezVous
        return null; // Replace with actual implementation
    }

    private Ordonnance selectOrCreateOrdonnance() {
        // Open a dialog to select or create a new Ordonnance
        // Return the selected or created Ordonnance
        return null; // Replace with actual implementation
    }

    private Consultation selectOrCreateConsultation() {
        // Open a dialog to select or create a new Consultation
        // Return the selected or created Consultation
        return null; // Replace with actual implementation
    }
}