package ma.VitaCareApp.presentation.view.patientView;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.models.enums.BloodType;
import ma.VitaCareApp.models.enums.Mutuelle;
import ma.VitaCareApp.models.enums.Sex;
import ma.VitaCareApp.presentation.controller.implementationController.PatientController;
import ma.VitaCareApp.presentation.view.commun.buttons.*;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;
import ma.VitaCareApp.services.exceptions.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PatientsView extends JPanel {
    private final PatientController patientController;
    private TablePanel tablePanel;
    private CreateButton addButton;
    private RefreshButton refreshButton;
    private List<Patient> allPatients; // Store all patients for filtering

    public PatientsView(PatientController patientController) {
        this.patientController = patientController;
        initializeUI();
        loadPatientData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // Light grey background

        // Top Panel (contains table name, add button, and search bar)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add space at the top

        // Table Name and Icon (centered)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);

        ImageIcon patientIcon = new ImageIcon(getClass().getResource("/icons/user.png"));
        Image img = patientIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        patientIcon = new ImageIcon(img);

        JLabel titleLabel = new JLabel("Patients Table", patientIcon, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33)); // Dark grey text
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.CENTER);

        // Add Button (left)
        addButton = new CreateButton(this::handleCreate);
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButtonPanel.setOpaque(false);
        addButtonPanel.add(addButton);
        addButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Space below add button
        topPanel.add(addButtonPanel, BorderLayout.WEST);

        // Search Panel (right)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Space below search bar

        JTextField searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12) // Padding
        ));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(new Color(33, 33, 33)); // Dark grey text
        searchField.setText("Search Patient"); // Placeholder text
        searchPanel.add(searchField);

        SearchButton searchButton = new SearchButton(e -> handleSearch(searchField.getText()));
        searchButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID", "First Name", "Last Name", "CIN", "Email", "Phone", "Address", "Birth Date", "Sex", "Blood Type", "Mutuelle", "Profession"};
        tablePanel = new TablePanel(columnNames);
        JScrollPane scrollPane = new JScrollPane(tablePanel.getTable());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Space above and below the table
        add(scrollPane, BorderLayout.CENTER);

        tablePanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tablePanel.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    showEditDeleteDialog(selectedRow);
                }
            }
        });

        // Refresh Button
        refreshButton = new RefreshButton(e -> loadPatientData());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleSearch(String searchText) {
        if (allPatients == null || allPatients.isEmpty()) {
            return; // No data to search
        }

        // Filter patients based on search text (case-insensitive)
        List<Patient> filteredPatients = allPatients.stream()
                .filter(patient -> patient.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getCin().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getPhone().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getAddress().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getProfession().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        // Convert filtered patients to table data
        List<Object[]> tableData = filteredPatients.stream()
                .map(patient -> new Object[]{
                        patient.getId(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getCin(),
                        patient.getEmail(),
                        patient.getPhone(),
                        patient.getAddress(),
                        patient.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        patient.getSex(),
                        patient.getBloodType(),
                        patient.getMutuelle(),
                        patient.getProfession()
                })
                .collect(Collectors.toList());

        // Refresh the table with filtered data
        tablePanel.refreshTable(tableData);
    }

    private void showEditDeleteDialog(int selectedRow) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Options", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(400, 150); // Increased size
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(245, 245, 245)); // Light grey background

        EditButton editButton = new EditButton(e -> {
            dialog.dispose();
            handleEdit(selectedRow);
        });

        DeleteButton deleteButton = new DeleteButton(e -> {
            dialog.dispose();
            handleDelete(selectedRow);
        });

        dialog.add(editButton);
        dialog.add(deleteButton);
        dialog.setVisible(true);
    }

    private void handleEdit(int selectedRow) {
        Long patientId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        try {
            Patient patientToEdit = patientController.findById(patientId);
            JPanel formPanel = createPatientForm(patientToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Patient", formPanel);
            dialog.setSize(600, 400); // Increased size
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(int selectedRow) {
        Long patientId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this patient?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                patientController.deleteById(patientId);
                loadPatientData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadPatientData() {
        try {
            allPatients = patientController.findAll(); // Store all patients for filtering
            List<Object[]> tableData = allPatients.stream()
                    .map(patient -> new Object[]{
                            patient.getId(),
                            patient.getFirstName(),
                            patient.getLastName(),
                            patient.getCin(),
                            patient.getEmail(),
                            patient.getPhone(),
                            patient.getAddress(),
                            patient.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            patient.getSex(),
                            patient.getBloodType(),
                            patient.getMutuelle(),
                            patient.getProfession()
                    })
                    .collect(Collectors.toList());
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load patient data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        JPanel formPanel = createPatientForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Patient", formPanel);
        dialog.setSize(600, 400); // Increased size
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createPatientForm(Patient patient) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Added gaps between components
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        formPanel.setBackground(new Color(245, 245, 245)); // Light grey background

        JTextField firstNameField = new JTextField(patient != null ? patient.getFirstName() : "");
        JTextField lastNameField = new JTextField(patient != null ? patient.getLastName() : "");
        JTextField cinField = new JTextField(patient != null ? patient.getCin() : "");
        JTextField emailField = new JTextField(patient != null ? patient.getEmail() : "");
        JTextField phoneField = new JTextField(patient != null ? patient.getPhone() : "");
        JTextField addressField = new JTextField(patient != null ? patient.getAddress() : "");
        JTextField birthDateField = new JTextField(patient != null ? patient.getBirthDate().toString() : "");
        JComboBox<Sex> sexComboBox = new JComboBox<>(Sex.values());
        if (patient != null) {
            sexComboBox.setSelectedItem(patient.getSex());
        }
        JComboBox<BloodType> bloodTypeComboBox = new JComboBox<>(BloodType.values());
        if (patient != null) {
            bloodTypeComboBox.setSelectedItem(patient.getBloodType());
        }
        JComboBox<Mutuelle> mutuelleComboBox = new JComboBox<>(Mutuelle.values());
        if (patient != null) {
            mutuelleComboBox.setSelectedItem(patient.getMutuelle());
        }
        JTextField professionField = new JTextField(patient != null ? patient.getProfession() : "");

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("CIN:"));
        formPanel.add(cinField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Birth Date (yyyy-MM-dd):"));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel("Sex:"));
        formPanel.add(sexComboBox);
        formPanel.add(new JLabel("Blood Type:"));
        formPanel.add(bloodTypeComboBox);
        formPanel.add(new JLabel("Mutuelle:"));
        formPanel.add(mutuelleComboBox);
        formPanel.add(new JLabel("Profession:"));
        formPanel.add(professionField);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(33, 150, 243)); // Blue background
        saveButton.setForeground(Color.WHITE); // White text
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        saveButton.addActionListener(e -> {
            try {
                Patient newPatient = new Patient(
                        phoneField.getText(),
                        emailField.getText(),
                        (Sex) sexComboBox.getSelectedItem(),
                        addressField.getText(),
                        LocalDate.parse(birthDateField.getText()),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        cinField.getText(),
                        (BloodType) bloodTypeComboBox.getSelectedItem(),
                        (Mutuelle) mutuelleComboBox.getSelectedItem(),
                        professionField.getText()
                );

                if (patient != null) {
                    newPatient.setId(patient.getId());
                    patientController.update(newPatient);
                } else {
                    patientController.save(newPatient);
                }

                loadPatientData();
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Failed to save patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}