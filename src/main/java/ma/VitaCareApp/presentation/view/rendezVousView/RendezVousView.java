package ma.VitaCareApp.presentation.view.rendezVousView;

import ma.VitaCareApp.models.RendezVous;
import ma.VitaCareApp.models.enums.TypeRDV;
import ma.VitaCareApp.presentation.controller.implementationController.RendezVousController;
import ma.VitaCareApp.presentation.view.commun.buttons.*;
import ma.VitaCareApp.presentation.view.commun.tables.TablePanel;
import ma.VitaCareApp.presentation.view.commun.forms.FormDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RendezVousView extends JPanel {
    private final RendezVousController rendezVousController;
    private TablePanel tablePanel;
    private CreateButton addButton;
    private RefreshButton refreshButton;
    private List<RendezVous> allRendezVous; // Store all RendezVous for filtering

    public RendezVousView(RendezVousController rendezVousController) {
        this.rendezVousController = rendezVousController;
        initializeUI();
        loadRendezVousData();
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

        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/calendar.png"));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JLabel titleLabel = new JLabel("RendezVous", icon, JLabel.CENTER);
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
        searchField.setText("Search RendezVous"); // Placeholder text
        searchPanel.add(searchField);

        SearchButton searchButton = new SearchButton(e -> handleSearch(searchField.getText()));
        searchButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID", "Motif", "Temps", "Type RDV", "Date RDV"};
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
        refreshButton = new RefreshButton(e -> loadRendezVousData());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleSearch(String searchText) {
        if (allRendezVous == null || allRendezVous.isEmpty()) {
            return; // No data to search
        }

        // Filter RendezVous based on search text (case-insensitive)
        List<RendezVous> filteredRendezVous = allRendezVous.stream()
                .filter(rdv -> String.valueOf(rdv.getIdRDV()).contains(searchText) ||
                        rdv.getMotif().toLowerCase().contains(searchText.toLowerCase()) ||
                        rdv.getTemps().toLowerCase().contains(searchText.toLowerCase()) ||
                        rdv.getTypeRDV().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                        rdv.getDateRDV().toString().contains(searchText))
                .collect(Collectors.toList());

        // Convert filtered RendezVous to table data
        List<Object[]> tableData = filteredRendezVous.stream()
                .map(rdv -> new Object[]{
                        rdv.getIdRDV(),
                        rdv.getMotif(),
                        rdv.getTemps(),
                        rdv.getTypeRDV(),
                        rdv.getDateRDV().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
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
        Long idRDV = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        try {
            RendezVous rendezVousToEdit = rendezVousController.findById(idRDV);
            JPanel formPanel = createRendezVousForm(rendezVousToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit RendezVous", formPanel);
            dialog.setSize(600, 400); // Increased size
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve RendezVous: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(int selectedRow) {
        Long idRDV = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this RendezVous?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                rendezVousController.deleteById(idRDV);
                loadRendezVousData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete RendezVous: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadRendezVousData() {
        try {
            allRendezVous = rendezVousController.findAll(); // Store all RendezVous for filtering
            List<Object[]> tableData = allRendezVous.stream()
                    .map(rdv -> new Object[]{
                            rdv.getIdRDV(),
                            rdv.getMotif(),
                            rdv.getTemps(),
                            rdv.getTypeRDV(),
                            rdv.getDateRDV().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    })
                    .collect(Collectors.toList());
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load RendezVous data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        JPanel formPanel = createRendezVousForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New RendezVous", formPanel);
        dialog.setSize(600, 400); // Increased size
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createRendezVousForm(RendezVous rendezVous) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Added gaps between components
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        JTextField motifField = new JTextField(rendezVous != null ? rendezVous.getMotif() : "");
        JTextField tempsField = new JTextField(rendezVous != null ? rendezVous.getTemps() : "");
        JComboBox<TypeRDV> typeRDVComboBox = new JComboBox<>(TypeRDV.values());
        if (rendezVous != null) {
            typeRDVComboBox.setSelectedItem(rendezVous.getTypeRDV());
        }
        JTextField dateRDVField = new JTextField(rendezVous != null ? rendezVous.getDateRDV().toString() : "");

        formPanel.add(new JLabel("Motif:"));
        formPanel.add(motifField);
        formPanel.add(new JLabel("Temps (HH:mm):"));
        formPanel.add(tempsField);
        formPanel.add(new JLabel("Type RDV:"));
        formPanel.add(typeRDVComboBox);
        formPanel.add(new JLabel("Date RDV (yyyy-MM-dd):"));
        formPanel.add(dateRDVField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String motif = motifField.getText();
                String temps = tempsField.getText();
                TypeRDV typeRDV = (TypeRDV) typeRDVComboBox.getSelectedItem();
                LocalDate dateRDV = LocalDate.parse(dateRDVField.getText());

                RendezVous newRendezVous = new RendezVous();
                newRendezVous.setMotif(motif);
                newRendezVous.setTemps(temps);
                newRendezVous.setTypeRDV(typeRDV);
                newRendezVous.setDateRDV(dateRDV);

                if (rendezVous != null) {
                    newRendezVous.setIdRDV(rendezVous.getIdRDV());
                    rendezVousController.update(newRendezVous);
                } else {
                    rendezVousController.save(newRendezVous);
                }

                loadRendezVousData();
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Invalid input. Please check the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}