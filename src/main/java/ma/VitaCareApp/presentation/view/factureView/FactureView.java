package ma.VitaCareApp.presentation.view.factureView;

import ma.VitaCareApp.dao.implementationDAO.ConsultationDao;
import ma.VitaCareApp.models.Facture;
import ma.VitaCareApp.models.enums.TypePayement;
import ma.VitaCareApp.presentation.controller.implementationController.FactureController;
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
import java.util.List;
import java.util.stream.Collectors;

public class FactureView extends JPanel {
    private final FactureController factureController;
    private final ConsultationDao consultationDao;
    private TablePanel tablePanel;
    private CreateButton addButton;
    private RefreshButton refreshButton;
    private List<Facture> allFactures; // Store all factures for filtering

    public FactureView(FactureController factureController, ConsultationDao consultationDao) {
        this.factureController = factureController;
        this.consultationDao = consultationDao;
        initializeUI();
        loadFactureData();
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

        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/money.png"));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JLabel titleLabel = new JLabel("Factures", icon, JLabel.CENTER);
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
        searchField.setText("Search Facture"); // Placeholder text
        searchPanel.add(searchField);

        SearchButton searchButton = new SearchButton(e -> handleSearch(searchField.getText()));
        searchButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID Facture", "Montant Totale", "Montant Restant", "Montant Payé", "Consultation", "Type Payement", "Date Facturation"};
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
        refreshButton = new RefreshButton(e -> loadFactureData());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleSearch(String searchText) {
        if (allFactures == null || allFactures.isEmpty()) {
            return; // No data to search
        }

        // Filter factures based on search text (case-insensitive)
        List<Facture> filteredFactures = allFactures.stream()
                .filter(facture -> String.valueOf(facture.getIdFacture()).contains(searchText) ||
                        String.valueOf(facture.getMontantTotale()).contains(searchText) ||
                        String.valueOf(facture.getMontantRestant()).contains(searchText) ||
                        String.valueOf(facture.getMontantPaye()).contains(searchText) ||
                        String.valueOf(facture.getConsultation().getIdConsultation()).contains(searchText) ||
                        facture.getTypePayement().toString().toLowerCase().contains(searchText.toLowerCase()) ||
                        facture.getDateFacturation().toString().contains(searchText))
                .collect(Collectors.toList());

        // Convert filtered factures to table data
        List<Object[]> tableData = filteredFactures.stream()
                .map(facture -> new Object[]{
                        facture.getIdFacture(),
                        facture.getMontantTotale(),
                        facture.getMontantRestant(),
                        facture.getMontantPaye(),
                        facture.getConsultation().getIdConsultation(),
                        facture.getTypePayement(),
                        facture.getDateFacturation()
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
        Long idFacture = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        try {
            Facture factureToEdit = factureController.findById(idFacture);
            JPanel formPanel = createFactureForm(factureToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Facture", formPanel);
            dialog.setSize(600, 400); // Increased size
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve facture: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(int selectedRow) {
        Long idFacture = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this facture?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                factureController.deleteById(idFacture);
                loadFactureData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete facture: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFactureData() {
        try {
            allFactures = factureController.findAll(); // Store all factures for filtering
            List<Object[]> tableData = allFactures.stream()
                    .map(facture -> new Object[]{
                            facture.getIdFacture(),
                            facture.getMontantTotale(),
                            facture.getMontantRestant(),
                            facture.getMontantPaye(),
                            facture.getConsultation().getIdConsultation(),
                            facture.getTypePayement(),
                            facture.getDateFacturation()
                    })
                    .collect(Collectors.toList());
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load facture data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        JPanel formPanel = createFactureForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Facture", formPanel);
        dialog.setSize(600, 400); // Increased size
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createFactureForm(Facture facture) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Added gaps between components
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        JTextField montantTotaleField = new JTextField(facture != null ? facture.getMontantTotale().toString() : "");
        formPanel.add(new JLabel("Montant Totale:"));
        formPanel.add(montantTotaleField);

        JTextField montantPayeField = new JTextField(facture != null ? facture.getMontantPaye().toString() : "");
        formPanel.add(new JLabel("Montant Payé:"));
        formPanel.add(montantPayeField);

        JTextField consultationIdField = new JTextField(facture != null ? facture.getConsultation().getIdConsultation().toString() : "");
        formPanel.add(new JLabel("Consultation ID:"));
        formPanel.add(consultationIdField);

        JComboBox<TypePayement> typePayementComboBox = new JComboBox<>(TypePayement.values());
        if (facture != null) {
            typePayementComboBox.setSelectedItem(facture.getTypePayement());
        }
        formPanel.add(new JLabel("Type Payement:"));
        formPanel.add(typePayementComboBox);

        JTextField dateFacturationField = new JTextField(facture != null ? facture.getDateFacturation().toString() : "");
        formPanel.add(new JLabel("Date Facturation (yyyy-MM-dd):"));
        formPanel.add(dateFacturationField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (montantTotaleField.getText().trim().isEmpty()) {
                    throw new ServiceException("Montant Totale is required.");
                }
                if (montantPayeField.getText().trim().isEmpty()) {
                    throw new ServiceException("Montant Payé is required.");
                }
                if (consultationIdField.getText().trim().isEmpty()) {
                    throw new ServiceException("Consultation ID is required.");
                }
                if (dateFacturationField.getText().trim().isEmpty()) {
                    throw new ServiceException("Date Facturation is required.");
                }

                // Parse inputs
                Double montantTotale = Double.parseDouble(montantTotaleField.getText());
                Double montantPaye = Double.parseDouble(montantPayeField.getText());

                // Validate montantPaye
                if (montantPaye > montantTotale) {
                    throw new ServiceException("Montant Payé cannot be greater than Montant Totale.");
                }

                // Create or update the Facture object
                Facture newFacture = new Facture();
                newFacture.setMontantTotale(montantTotale);
                newFacture.setMontantPaye(montantPaye);
                newFacture.setTypePayement((TypePayement) typePayementComboBox.getSelectedItem());
                newFacture.setDateFacturation(LocalDate.parse(dateFacturationField.getText()));

                // Fetch the Consultation object using its ID
                Long consultationId = Long.parseLong(consultationIdField.getText());
                newFacture.setConsultation(consultationDao.findById(consultationId));

                if (facture != null) {
                    newFacture.setIdFacture(facture.getIdFacture());
                    factureController.update(newFacture);
                } else {
                    factureController.save(newFacture);
                }

                loadFactureData();
                ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(formPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(formPanel, "Failed to save facture: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}