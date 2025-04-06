package ma.VitaCareApp.presentation.view.prescriptionMedicament;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.MedicalHistoryDao;
import ma.VitaCareApp.dao.implementationDAO.MedicamentDao;
import ma.VitaCareApp.dao.implementationDAO.PatientDAO;
import ma.VitaCareApp.dao.implementationDAO.PrescriptionMedicamentDao;
import ma.VitaCareApp.models.Medicament;
import ma.VitaCareApp.models.PrescriptionMedicament;
import ma.VitaCareApp.presentation.controller.implementationController.PrescriptionMedicamentController;
import ma.VitaCareApp.presentation.view.commun.buttons.CrudButtonPanel;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.services.exceptions.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionMedicamentView extends JPanel {
    private final PrescriptionMedicamentController prescriptionMedicamentController;
    private TablePanel tablePanel;
    private CrudButtonPanel crudButtonPanel;

    public PrescriptionMedicamentView(PrescriptionMedicamentController prescriptionMedicamentController) {
        this.prescriptionMedicamentController = prescriptionMedicamentController;
        initializeUI();
        loadPrescriptionMedicamentData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Title for "Prescription Medicament"
        JLabel titleLabel = new JLabel("Prescription Medicament", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Create a panel for the table and buttons
        JPanel tableAndButtonsPanel = new JPanel(new BorderLayout());

        // Initialize the table panel
        String[] columnNames = {"ID", "Min Units", "Max Units", "Food Constraints", "Time Constraints", "Medicament"};
        tablePanel = new TablePanel(columnNames);
        tableAndButtonsPanel.add(tablePanel, BorderLayout.CENTER);

        // Initialize the CRUD button panel
        crudButtonPanel = new CrudButtonPanel(
                this::handleCreate,
                this::handleEdit,
                this::handleDelete,
                e -> loadPrescriptionMedicamentData()
        );
        tableAndButtonsPanel.add(crudButtonPanel, BorderLayout.SOUTH);

        // Add the table and buttons panel to the main panel
        add(tableAndButtonsPanel, BorderLayout.CENTER);
    }

    private void loadPrescriptionMedicamentData() {
        try {
            // Fetch prescription medicament data from the controller
            List<PrescriptionMedicament> prescriptionMedicaments = prescriptionMedicamentController.findAll();

            // Convert prescription medicaments to table data
            List<Object[]> tableData = prescriptionMedicaments.stream()
                    .map(pm -> new Object[]{
                            pm.getIdPrescription(),
                            pm.getUniteMinAPrendre(),
                            pm.getUniteMaxAPrendre(),
                            pm.getContraintesAlimentaire(),
                            pm.getContraintesTemps(),
                            pm.getMedicamentAPrescrire().getName() // Display the medicament name
                    })
                    .collect(Collectors.toList());

            // Refresh the table
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load prescription medicament data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        // Open a form dialog for creating a new prescription medicament
        JPanel formPanel = createPrescriptionMedicamentForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Prescription Medicament", formPanel);
        dialog.setVisible(true);
    }

    private void handleEdit(ActionEvent e) {
        // Get the selected prescription medicament from the table
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a prescription medicament to edit.", "No Prescription Medicament Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the prescription medicament ID from the selected row
        Long prescriptionMedicamentId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);

        try {
            // Fetch the prescription medicament from the controller
            PrescriptionMedicament prescriptionMedicamentToEdit = prescriptionMedicamentController.findById(prescriptionMedicamentId);

            // Open a form dialog for editing the prescription medicament
            JPanel formPanel = createPrescriptionMedicamentForm(prescriptionMedicamentToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Prescription Medicament", formPanel);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve prescription medicament: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(ActionEvent e) {
        // Get the selected prescription medicament from the table
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a prescription medicament to delete.", "No Prescription Medicament Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the prescription medicament ID from the selected row
        Long prescriptionMedicamentId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this prescription medicament?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete the prescription medicament using the controller
                prescriptionMedicamentController.deleteById(prescriptionMedicamentId);

                // Refresh the table
                loadPrescriptionMedicamentData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete prescription medicament: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createPrescriptionMedicamentForm(PrescriptionMedicament prescriptionMedicament) {
        // Create and return a form panel for PrescriptionMedicament
        JPanel formPanel = new JPanel(new GridLayout(0, 2));

        // Add form fields
        JTextField minUnitsField = new JTextField(prescriptionMedicament != null ? String.valueOf(prescriptionMedicament.getUniteMinAPrendre()) : "");
        JTextField maxUnitsField = new JTextField(prescriptionMedicament != null ? String.valueOf(prescriptionMedicament.getUniteMaxAPrendre()) : "");
        JTextField foodConstraintsField = new JTextField(prescriptionMedicament != null ? prescriptionMedicament.getContraintesAlimentaire() : "");
        JTextField timeConstraintsField = new JTextField(prescriptionMedicament != null ? prescriptionMedicament.getContraintesTemps() : "");
        JTextField medicamentIdField = new JTextField(prescriptionMedicament != null ? prescriptionMedicament.getMedicamentAPrescrire().getIdMedicine().toString() : "");

        formPanel.add(new JLabel("Min Units:"));
        formPanel.add(minUnitsField);
        formPanel.add(new JLabel("Max Units:"));
        formPanel.add(maxUnitsField);
        formPanel.add(new JLabel("Food Constraints:"));
        formPanel.add(foodConstraintsField);
        formPanel.add(new JLabel("Time Constraints:"));
        formPanel.add(timeConstraintsField);
        formPanel.add(new JLabel("Medicament ID:"));
        formPanel.add(medicamentIdField);

        // Add a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                // Create or update the prescription medicament object
                PrescriptionMedicament newPrescriptionMedicament = new PrescriptionMedicament();
                newPrescriptionMedicament.setUniteMinAPrendre(Integer.parseInt(minUnitsField.getText()));
                newPrescriptionMedicament.setUniteMaxAPrendre(Integer.parseInt(maxUnitsField.getText()));
                newPrescriptionMedicament.setContraintesAlimentaire(foodConstraintsField.getText());
                newPrescriptionMedicament.setContraintesTemps(timeConstraintsField.getText());

                // Initialize dependencies for MedicamentDao
                PatientDAO patientDAO = new PatientDAO(); // Assuming PatientDAO has a no-args constructor
                MedicalHistoryDao medicalHistoryDao = new MedicalHistoryDao(patientDAO);
                MedicamentDao medicamentDao = new MedicamentDao(medicalHistoryDao);

                // Fetch the Medicament object using its ID
                Medicament medicament = medicamentDao.findById(Long.parseLong(medicamentIdField.getText()));
                newPrescriptionMedicament.setMedicamentAPrescrire(medicament);

                if (prescriptionMedicament != null) {
                    newPrescriptionMedicament.setIdPrescription(prescriptionMedicament.getIdPrescription());
                    prescriptionMedicamentController.update(newPrescriptionMedicament);
                } else {
                    prescriptionMedicamentController.save(newPrescriptionMedicament);
                }

                // Refresh the table
                loadPrescriptionMedicamentData();

                // Close the dialog
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Failed to save prescription medicament: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}