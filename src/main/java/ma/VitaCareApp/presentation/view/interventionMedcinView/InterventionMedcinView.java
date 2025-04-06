package ma.VitaCareApp.presentation.view.interventionMedcinView;

import ma.VitaCareApp.models.Acte;
import ma.VitaCareApp.models.InterventionMedcin;
import ma.VitaCareApp.presentation.controller.implementationController.InterventionMedcinController;
import ma.VitaCareApp.presentation.view.commun.buttons.CrudButtonPanel;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.services.implementationService.ActeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class InterventionMedcinView extends JPanel {
    private final InterventionMedcinController interventionMedcinController;
    private final ActeService acteService; // Add ActeService
    private TablePanel tablePanel;
    private CrudButtonPanel crudButtonPanel;

    public InterventionMedcinView(InterventionMedcinController interventionMedcinController, ActeService acteService) {
        this.interventionMedcinController = interventionMedcinController;
        this.acteService = acteService; // Initialize ActeService
        initializeUI();
        loadInterventionMedcinData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Add a title for the table
        JLabel titleLabel = new JLabel("Interventions Medcin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18)); // Set font and size
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding
        add(titleLabel, BorderLayout.NORTH);

        // Initialize the table panel
        String[] columnNames = {"ID", "Note Medcin", "Acte ID", "Dent", "Prix Patient"};
        tablePanel = new TablePanel(columnNames);
        add(tablePanel, BorderLayout.CENTER);

        // Initialize the CRUD button panel
        crudButtonPanel = new CrudButtonPanel(
                this::handleCreate,
                this::handleEdit,
                this::handleDelete,
                e -> loadInterventionMedcinData()
        );
        add(crudButtonPanel, BorderLayout.SOUTH);
    }

    private void loadInterventionMedcinData() {
        try {
            // Fetch interventionMedcin data from the controller
            List<InterventionMedcin> interventionMedcins = interventionMedcinController.findAll();

            // Convert interventionMedcins to table data
            List<Object[]> tableData = interventionMedcins.stream()
                    .map(im -> new Object[]{
                            im.getIdIntervention(),
                            im.getNoteMedcin(),
                            im.getActe().getIdActe(),
                            im.getDent(),
                            im.getPrixPatient()
                    })
                    .collect(Collectors.toList());

            // Refresh the table
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load interventionMedcin data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        // Open a form dialog for creating a new interventionMedcin
        JPanel formPanel = createInterventionMedcinForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New InterventionMedcin", formPanel);
        dialog.setVisible(true);
    }

    private void handleEdit(ActionEvent e) {
        // Get the selected interventionMedcin from the table
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an interventionMedcin to edit.", "No InterventionMedcin Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the interventionMedcin ID from the selected row
        Long interventionMedcinId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);

        try {
            // Fetch the interventionMedcin from the controller
            InterventionMedcin interventionMedcinToEdit = interventionMedcinController.findById(interventionMedcinId);

            // Open a form dialog for editing the interventionMedcin
            JPanel formPanel = createInterventionMedcinForm(interventionMedcinToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit InterventionMedcin", formPanel);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve interventionMedcin: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(ActionEvent e) {
        // Get the selected interventionMedcin from the table
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an interventionMedcin to delete.", "No InterventionMedcin Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the interventionMedcin ID from the selected row
        Long interventionMedcinId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this interventionMedcin?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete the interventionMedcin using the controller
                interventionMedcinController.deleteById(interventionMedcinId);

                // Refresh the table
                loadInterventionMedcinData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete interventionMedcin: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createInterventionMedcinForm(InterventionMedcin interventionMedcin) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2));

        // Add form fields
        JTextField noteMedcinField = new JTextField(interventionMedcin != null ? interventionMedcin.getNoteMedcin() : "");
        JTextField acteIdField = new JTextField(interventionMedcin != null ? interventionMedcin.getActe().getIdActe().toString() : "");
        JTextField dentField = new JTextField(interventionMedcin != null ? interventionMedcin.getDent().toString() : "");
        JTextField prixPatientField = new JTextField(interventionMedcin != null ? interventionMedcin.getPrixPatient().toString() : "");

        formPanel.add(new JLabel("Note Medcin:"));
        formPanel.add(noteMedcinField);
        formPanel.add(new JLabel("Acte ID:"));
        formPanel.add(acteIdField);
        formPanel.add(new JLabel("Dent:"));
        formPanel.add(dentField);
        formPanel.add(new JLabel("Prix Patient:"));
        formPanel.add(prixPatientField);

        // Add a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                // Fetch the Acte object using the Acte ID
                Long acteId = Long.parseLong(acteIdField.getText());
                Acte acte = acteService.findById(acteId); // Fetch Acte from the service

                if (acte == null) {
                    JOptionPane.showMessageDialog(formPanel, "Acte not found with ID: " + acteId, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create or update the InterventionMedcin object
                InterventionMedcin newInterventionMedcin = new InterventionMedcin();
                newInterventionMedcin.setNoteMedcin(noteMedcinField.getText());
                newInterventionMedcin.setActe(acte); // Set the fetched Acte object
                newInterventionMedcin.setDent(Long.parseLong(dentField.getText()));
                newInterventionMedcin.setPrixPatient(Double.parseDouble(prixPatientField.getText()));

                if (interventionMedcin != null) {
                    newInterventionMedcin.setIdIntervention(interventionMedcin.getIdIntervention());
                    interventionMedcinController.update(newInterventionMedcin);
                } else {
                    interventionMedcinController.save(newInterventionMedcin);
                }

                // Refresh the table
                loadInterventionMedcinData();

                // Close the dialog
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Failed to save interventionMedcin: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}