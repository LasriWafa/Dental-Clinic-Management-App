package ma.VitaCareApp.presentation.view.ordonnancesView;

import ma.VitaCareApp.dao.implementationDAO.MedicalHistoryDao;
import ma.VitaCareApp.dao.implementationDAO.MedicamentDao;
import ma.VitaCareApp.dao.implementationDAO.PatientDAO;
import ma.VitaCareApp.dao.implementationDAO.PrescriptionMedicamentDao;
import ma.VitaCareApp.models.Ordonnance;
import ma.VitaCareApp.models.PrescriptionMedicament;
import ma.VitaCareApp.presentation.controller.implementationController.OrdonnanceController;
import ma.VitaCareApp.presentation.controller.implementationController.PrescriptionMedicamentController;
import ma.VitaCareApp.presentation.view.commun.buttons.CrudButtonPanel;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.presentation.view.prescriptionMedicament.PrescriptionMedicamentView;
import ma.VitaCareApp.services.exceptions.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrdonnancesView extends JPanel {
    private final OrdonnanceController ordonnanceController;
    private final PrescriptionMedicamentController prescriptionMedicamentController;
    private TablePanel ordonnanceTablePanel;
    private CrudButtonPanel ordonnanceCrudButtonPanel;
    private PrescriptionMedicamentView prescriptionMedicamentView;

    public OrdonnancesView(OrdonnanceController ordonnanceController, PrescriptionMedicamentController prescriptionMedicamentController) {
        this.ordonnanceController = ordonnanceController;
        this.prescriptionMedicamentController = prescriptionMedicamentController;
        initializeUI();
        loadOrdonnanceData();
    }

    // Inside the initializeUI method of OrdonnancesView
    private void initializeUI() {
        setLayout(new BorderLayout());

        // Title for "Ordonnance"
        JLabel ordonnanceTitle = new JLabel("Ordonnance", SwingConstants.CENTER);
        ordonnanceTitle.setFont(new Font("Serif", Font.BOLD, 24));
        add(ordonnanceTitle, BorderLayout.NORTH);

        // Main content panel for Ordonnance
        JPanel ordonnanceContentPanel = new JPanel(new BorderLayout());

        // Initialize the ordonnance table panel
        String[] ordonnanceColumnNames = {"ID", "Date", "Prescription Medicaments"};
        ordonnanceTablePanel = new TablePanel(ordonnanceColumnNames);

        // Wrap the table in a JScrollPane and set its preferred size
        JScrollPane tableScrollPane = new JScrollPane(ordonnanceTablePanel.getTable());
        tableScrollPane.setPreferredSize(new Dimension(tableScrollPane.getPreferredSize().width, 200)); // Set height to 300 pixels

        // Initialize the ordonnance CRUD button panel
        ordonnanceCrudButtonPanel = new CrudButtonPanel(
                this::handleCreateOrdonnance,
                this::handleEditOrdonnance,
                this::handleDeleteOrdonnance,
                e -> loadOrdonnanceData()
        );

        // Add the table (inside a scroll pane) and buttons to the ordonnance content panel
        ordonnanceContentPanel.add(tableScrollPane, BorderLayout.CENTER);
        ordonnanceContentPanel.add(ordonnanceCrudButtonPanel, BorderLayout.SOUTH);

        // Add the ordonnance content panel to the main panel
        add(ordonnanceContentPanel, BorderLayout.CENTER);

        // Initialize the PrescriptionMedicamentView
        prescriptionMedicamentView = new PrescriptionMedicamentView(prescriptionMedicamentController);

        // Create a panel for the Prescription Medicament section
        JPanel prescriptionPanel = new JPanel(new BorderLayout());
        prescriptionPanel.add(prescriptionMedicamentView, BorderLayout.CENTER);

        // Add the Prescription Medicament panel to the main panel
        add(prescriptionPanel, BorderLayout.SOUTH);
    }

    private void loadOrdonnanceData() {
        try {
            // Fetch ordonnance data from the controller
            List<Ordonnance> ordonnances = ordonnanceController.findAll();

            // Convert ordonnances to table data
            List<Object[]> tableData = ordonnances.stream()
                    .map(ordonnance -> new Object[]{
                            ordonnance.getIdOrdonnance(),
                            ordonnance.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            ordonnance.getPrescriptionMedicaments().stream()
                                    .map(PrescriptionMedicament::getIdPrescription)
                                    .map(Object::toString) // Convert IDs to strings
                                    .collect(Collectors.joining(", "))
                    })
                    .collect(Collectors.toList());

            // Refresh the ordonnance table
            ordonnanceTablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load ordonnance data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreateOrdonnance(ActionEvent e) {
        // Open a form dialog for creating a new ordonnance
        JPanel formPanel = createOrdonnanceForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Ordonnance", formPanel);
        dialog.setVisible(true);
    }

    private void handleEditOrdonnance(ActionEvent e) {
        // Get the selected ordonnance from the table
        int selectedRow = ordonnanceTablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ordonnance to edit.", "No Ordonnance Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the ordonnance ID from the selected row
        Long ordonnanceId = (Long) ordonnanceTablePanel.getTableModel().getValueAt(selectedRow, 0);

        try {
            Ordonnance ordonnanceToEdit = ordonnanceController.findById(ordonnanceId);

            // Open a form dialog for editing the ordonnance
            JPanel formPanel = createOrdonnanceForm(ordonnanceToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Ordonnance", formPanel);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve ordonnance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteOrdonnance(ActionEvent e) {
        // Get the selected ordonnance from the table
        int selectedRow = ordonnanceTablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an ordonnance to delete.", "No Ordonnance Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the ordonnance ID from the selected row
        Long ordonnanceId = (Long) ordonnanceTablePanel.getTableModel().getValueAt(selectedRow, 0);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this ordonnance?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete the ordonnance using the controller
                ordonnanceController.deleteById(ordonnanceId);

                // Refresh the table
                loadOrdonnanceData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete ordonnance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createOrdonnanceForm(Ordonnance ordonnance) {
        // Create and return a form panel for Ordonnance
        JPanel formPanel = new JPanel(new GridLayout(0, 2));

        // Add form fields
        JTextField dateField = new JTextField(ordonnance != null ? ordonnance.getDate().toString() : "");
        JTextField prescriptionMedicamentsField = new JTextField(ordonnance != null ? ordonnance.getPrescriptionMedicaments().stream()
                .map(PrescriptionMedicament::getIdPrescription) // Extract IDs only
                .map(Object::toString) // Convert IDs to strings
                .collect(Collectors.joining(", ")) : ""); // Join IDs with commas

        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Prescription Medicaments (ID1, ID2, ...):"));
        formPanel.add(prescriptionMedicamentsField);

        // Add a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                // Create or update the ordonnance object
                Ordonnance newOrdonnance = new Ordonnance();
                newOrdonnance.setDate(LocalDate.parse(dateField.getText()));

                // Parse prescription medicament IDs
                List<Long> prescriptionMedicamentIds = List.of(prescriptionMedicamentsField.getText().split(","))
                        .stream()
                        .map(String::trim)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                // Fetch PrescriptionMedicament objects by their IDs
                List<PrescriptionMedicament> prescriptionMedicaments = fetchPrescriptionMedicamentsByIds(prescriptionMedicamentIds);

                // Ensure at least one PrescriptionMedicament is provided
                if (prescriptionMedicaments.isEmpty()) {
                    JOptionPane.showMessageDialog(formPanel, "At least one PrescriptionMedicament is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                newOrdonnance.setPrescriptionMedicaments(prescriptionMedicaments);

                if (ordonnance != null) {
                    newOrdonnance.setIdOrdonnance(ordonnance.getIdOrdonnance());
                    ordonnanceController.update(newOrdonnance);
                } else {
                    ordonnanceController.save(newOrdonnance);
                }

                // Refresh the table
                loadOrdonnanceData();

                // Close the dialog
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Failed to save ordonnance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }

    private List<PrescriptionMedicament> fetchPrescriptionMedicamentsByIds(List<Long> ids) {
        try {
            // Initialize PatientDAO
            PatientDAO patientDAO = new PatientDAO(); // Assuming PatientDAO has a no-args constructor

            // Initialize MedicalHistoryDao with its dependency (PatientDAO)
            MedicalHistoryDao medicalHistoryDao = new MedicalHistoryDao(patientDAO);

            // Initialize MedicamentDao with its dependency (MedicalHistoryDao)
            MedicamentDao medicamentDao = new MedicamentDao(medicalHistoryDao);

            // Initialize PrescriptionMedicamentDao with its dependency (MedicamentDao)
            PrescriptionMedicamentDao prescriptionMedicamentDao = new PrescriptionMedicamentDao(medicamentDao);

            // Fetch PrescriptionMedicament objects by their IDs
            return prescriptionMedicamentDao.findAllById(ids);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch PrescriptionMedicaments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return List.of(); // Return an empty list in case of error
        }
    }
}