package ma.VitaCareApp.presentation.view.consultationView;

import ma.VitaCareApp.dao.implementationDAO.InterventionMedcinDao;
import ma.VitaCareApp.models.Consultation;
import ma.VitaCareApp.models.InterventionMedcin;
import ma.VitaCareApp.models.Ordonnance;
import ma.VitaCareApp.models.enums.TypeConsultation;
import ma.VitaCareApp.presentation.controller.implementationController.ConsultationController;
import ma.VitaCareApp.presentation.controller.implementationController.InterventionMedcinController;
import ma.VitaCareApp.presentation.controller.implementationController.OrdonnanceController;
import ma.VitaCareApp.presentation.view.commun.buttons.*;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.presentation.view.interventionMedcinView.InterventionMedcinView;
import ma.VitaCareApp.services.exceptions.ServiceException;
import ma.VitaCareApp.services.implementationService.ActeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsultationView extends JPanel {
    private final ConsultationController consultationController;
    private final OrdonnanceController ordonnanceController;
    private final InterventionMedcinDao interventionMedcinDao;
    private final InterventionMedcinController interventionMedcinController;
    private final ActeService acteService;
    private TablePanel tablePanel;
    private CreateButton addButton;
    private RefreshButton refreshButton;
    private List<Consultation> allConsultations; // Store all consultations for filtering

    public ConsultationView(ConsultationController consultationController,
                            OrdonnanceController ordonnanceController,
                            InterventionMedcinDao interventionMedcinDao,
                            InterventionMedcinController interventionMedcinController,
                            ActeService acteService) {
        this.consultationController = consultationController;
        this.ordonnanceController = ordonnanceController;
        this.interventionMedcinDao = interventionMedcinDao;
        this.interventionMedcinController = interventionMedcinController;
        this.acteService = acteService;
        initializeUI();
        loadConsultationData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top Panel (contains table name, add button, and search bar)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add space at the top

        // Table Name and Icon (centered)
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);

        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/consultation.png"));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JLabel titleLabel = new JLabel("Consultation Table", icon, JLabel.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
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
        searchField.setFont(new Font("Poppins", Font.PLAIN, 14));
        searchField.setText("Search Consultation"); // Placeholder text
        searchPanel.add(searchField);

        SearchButton searchButton = new SearchButton(e -> handleSearch(searchField.getText()));
        searchButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID", "Type", "Ordonnance ID", "Date", "Interventions"};
        tablePanel = new TablePanel(columnNames);
        JScrollPane scrollPane = new JScrollPane(tablePanel.getTable());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Space above and below the table

        // Add row click listener
        tablePanel.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tablePanel.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    showEditDeleteDialog(selectedRow);
                }
            }
        });

        // InterventionMedcinView
        InterventionMedcinView interventionMedcinView = new InterventionMedcinView(interventionMedcinController, acteService);

        // Use JSplitPane to organize components
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, interventionMedcinView);
        splitPane.setResizeWeight(0.7); // Adjust the split ratio
        add(splitPane, BorderLayout.CENTER);

        // Refresh Button (right under the table)
        refreshButton = new RefreshButton(e -> loadConsultationData());
        JPanel refreshButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButtonPanel.setOpaque(false);
        refreshButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20)); // Add padding
        refreshButtonPanel.add(refreshButton);
        add(refreshButtonPanel, BorderLayout.SOUTH);
    }

    private void handleSearch(String searchText) {
        if (allConsultations == null || allConsultations.isEmpty()) {
            return; // No data to search
        }

        // Filter consultations based on search text (case-insensitive)
        List<Consultation> filteredConsultations = allConsultations.stream()
                .filter(consultation -> String.valueOf(consultation.getIdConsultation()).contains(searchText) ||
                        consultation.getTypeConsultation().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                        (consultation.getOrdonnance() != null && consultation.getOrdonnance().getIdOrdonnance().toString().contains(searchText)) ||
                        consultation.getDateConsultation().toString().contains(searchText) ||
                        consultation.getInterventions().stream()
                                .anyMatch(intervention -> intervention.getIdIntervention().toString().contains(searchText)))
                .collect(Collectors.toList());

        // Convert filtered consultations to table data
        List<Object[]> tableData = filteredConsultations.stream()
                .map(consultation -> new Object[]{
                        consultation.getIdConsultation(),
                        consultation.getTypeConsultation(),
                        consultation.getOrdonnance() != null ? consultation.getOrdonnance().getIdOrdonnance().toString() : "N/A",
                        consultation.getDateConsultation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        consultation.getInterventions().stream()
                                .map(intervention -> intervention.getIdIntervention().toString())
                                .collect(Collectors.joining(", "))
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
        Long consultationId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        try {
            Consultation consultationToEdit = consultationController.findById(consultationId);
            JPanel formPanel = createConsultationForm(consultationToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Consultation", formPanel);
            dialog.setSize(600, 400); // Increased size
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve consultation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(int selectedRow) {
        Long consultationId = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this consultation?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                consultationController.deleteById(consultationId);
                loadConsultationData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete consultation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadConsultationData() {
        try {
            allConsultations = consultationController.findAll(); // Store all consultations for filtering
            List<Object[]> tableData = allConsultations.stream()
                    .map(consultation -> new Object[]{
                            consultation.getIdConsultation(),
                            consultation.getTypeConsultation(),
                            consultation.getOrdonnance() != null ? consultation.getOrdonnance().getIdOrdonnance().toString() : "N/A",
                            consultation.getDateConsultation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            consultation.getInterventions().stream()
                                    .map(intervention -> intervention.getIdIntervention().toString())
                                    .collect(Collectors.joining(", "))
                    })
                    .collect(Collectors.toList());
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load consultation data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        JPanel formPanel = createConsultationForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Consultation", formPanel);
        dialog.setSize(600, 400); // Increased size
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createConsultationForm(Consultation consultation) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Added gaps between components
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        JComboBox<TypeConsultation> typeConsultationComboBox = new JComboBox<>(TypeConsultation.values());
        if (consultation != null) {
            typeConsultationComboBox.setSelectedItem(consultation.getTypeConsultation());
        }
        JTextField ordonnanceIdField = new JTextField(consultation != null && consultation.getOrdonnance() != null ? consultation.getOrdonnance().getIdOrdonnance().toString() : "");
        JTextField dateField = new JTextField(consultation != null ? consultation.getDateConsultation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");
        JTextField interventionsField = new JTextField(consultation != null ? consultation.getInterventions().stream()
                .map(intervention -> intervention.getIdIntervention().toString())
                .collect(Collectors.joining(", ")) : "");

        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeConsultationComboBox);
        formPanel.add(new JLabel("Ordonnance ID:"));
        formPanel.add(ordonnanceIdField);
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Intervention IDs (comma-separated):"));
        formPanel.add(interventionsField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                TypeConsultation typeConsultation = (TypeConsultation) typeConsultationComboBox.getSelectedItem();
                Long ordonnanceId = Long.parseLong(ordonnanceIdField.getText());
                LocalDate date = LocalDate.parse(dateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                List<Long> interventionIds = Stream.of(interventionsField.getText().split(","))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                Consultation newConsultation = new Consultation();
                newConsultation.setTypeConsultation(typeConsultation);
                newConsultation.setOrdonnance(ordonnanceController.findById(ordonnanceId));
                newConsultation.setDateConsultation(date);
                newConsultation.setInterventions(interventionMedcinDao.findAllById(interventionIds));

                if (consultation != null) {
                    newConsultation.setIdConsultation(consultation.getIdConsultation());
                    consultationController.update(newConsultation);
                } else {
                    consultationController.save(newConsultation);
                }

                loadConsultationData();
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Invalid input. Please check the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}