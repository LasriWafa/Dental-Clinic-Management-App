package ma.VitaCareApp.presentation.view.situationFinanciereView;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.DossierMedicalDao;
import ma.VitaCareApp.dao.implementationDAO.FactureDao;
import ma.VitaCareApp.dao.implementationDAO.FinancialSituationDao;
import ma.VitaCareApp.models.DossierMedical;
import ma.VitaCareApp.models.Facture;
import ma.VitaCareApp.models.FinancialSituation;
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
import java.util.stream.Stream;

public class SituationFinancialView extends JPanel {
    private final FinancialSituationDao financialSituationDao;
    private final DossierMedicalDao dossierMedicalDao;
    private final FactureDao factureDao;
    private TablePanel tablePanel;
    private CreateButton addButton;
    private RefreshButton refreshButton;
    private List<FinancialSituation> allFinancialSituations; // Store all financial situations for filtering

    public SituationFinancialView(FinancialSituationDao financialSituationDao, DossierMedicalDao dossierMedicalDao, FactureDao factureDao) {
        this.financialSituationDao = financialSituationDao;
        this.dossierMedicalDao = dossierMedicalDao;
        this.factureDao = factureDao;
        initializeUI();
        loadFinancialSituations();
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

        JLabel titleLabel = new JLabel("Financial Situations", icon, JLabel.CENTER);
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
        searchField.setText("Search Financial Situation"); // Placeholder text
        searchPanel.add(searchField);

        SearchButton searchButton = new SearchButton(e -> handleSearch(searchField.getText()));
        searchButton.setPreferredSize(new Dimension(120, 40)); // Medium size
        searchPanel.add(searchButton);

        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"ID", "Dossier Medical", "Date Creation", "Montant Global Restant", "Montant Global Payé"};
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
        refreshButton = new RefreshButton(e -> loadFinancialSituations());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleSearch(String searchText) {
        if (allFinancialSituations == null || allFinancialSituations.isEmpty()) {
            return; // No data to search
        }

        // Filter financial situations based on search text (case-insensitive)
        List<FinancialSituation> filteredFinancialSituations = allFinancialSituations.stream()
                .filter(fs -> String.valueOf(fs.getIdFinancialSituation()).contains(searchText) ||
                        String.valueOf(fs.getDossierMedical().getNumeroDossier()).contains(searchText) ||
                        fs.getDateCreation().toString().contains(searchText) ||
                        String.valueOf(fs.getMontantGlobaleRestant()).contains(searchText) ||
                        String.valueOf(fs.getMontantGlobalePaye()).contains(searchText))
                .collect(Collectors.toList());

        // Convert filtered financial situations to table data
        List<Object[]> tableData = filteredFinancialSituations.stream()
                .map(fs -> new Object[]{
                        fs.getIdFinancialSituation(),
                        fs.getDossierMedical().getNumeroDossier(),
                        fs.getDateCreation().toString(),
                        fs.getMontantGlobaleRestant(),
                        fs.getMontantGlobalePaye()
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
        Long idFinancialSituation = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        try {
            FinancialSituation financialSituationToEdit = financialSituationDao.findById(idFinancialSituation);
            JPanel formPanel = createFinancialSituationForm(financialSituationToEdit);
            FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Financial Situation", formPanel);
            dialog.setSize(600, 400); // Increased size
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve financial situation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(int selectedRow) {
        Long idFinancialSituation = (Long) tablePanel.getTableModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this financial situation?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                financialSituationDao.deleteById(idFinancialSituation);
                loadFinancialSituations();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete financial situation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFinancialSituations() {
        try {
            allFinancialSituations = financialSituationDao.findAll(); // Store all financial situations for filtering
            List<Object[]> tableData = allFinancialSituations.stream()
                    .map(fs -> new Object[]{
                            fs.getIdFinancialSituation(),
                            fs.getDossierMedical().getNumeroDossier(),
                            fs.getDateCreation().toString(),
                            fs.getMontantGlobaleRestant(),
                            fs.getMontantGlobalePaye()
                    })
                    .collect(Collectors.toList());
            tablePanel.refreshTable(tableData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load financial situations: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreate(ActionEvent e) {
        JPanel formPanel = createFinancialSituationForm(null);
        FormDialog dialog = new FormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Financial Situation", formPanel);
        dialog.setSize(600, 400); // Increased size
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createFinancialSituationForm(FinancialSituation financialSituation) {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Added gaps between components
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Dossier Medical Dropdown
        JComboBox<DossierMedical> dossierMedicalComboBox = new JComboBox<>();
        SwingWorker<Void, Void> dossierWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    List<DossierMedical> dossiers = dossierMedicalDao.findAll();
                    dossiers.forEach(dossierMedicalComboBox::addItem);
                    if (financialSituation != null) {
                        dossierMedicalComboBox.setSelectedItem(financialSituation.getDossierMedical());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(formPanel, "Failed to load dossier medicals: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        };
        dossierWorker.execute();

        formPanel.add(new JLabel("Dossier Medical:"));
        formPanel.add(dossierMedicalComboBox);

        // Factures List
        JTextField facturesField = new JTextField(financialSituation != null ?
                financialSituation.getFactures().stream()
                        .map(f -> f.getIdFacture().toString())
                        .collect(Collectors.joining(",")) : ""); // Use comma as separator
        formPanel.add(new JLabel("Facture IDs (comma-separated):"));
        formPanel.add(facturesField);

        // Date Creation
        JTextField dateCreationField = new JTextField(financialSituation != null ? financialSituation.getDateCreation().toString() : "");
        formPanel.add(new JLabel("Date Creation (yyyy-MM-dd):"));
        formPanel.add(dateCreationField);

        // Montant Global Restant
        JTextField montantGlobaleRestantField = new JTextField(financialSituation != null ? financialSituation.getMontantGlobaleRestant().toString() : "");
        formPanel.add(new JLabel("Montant Global Restant:"));
        formPanel.add(montantGlobaleRestantField);

        // Montant Global Payé
        JTextField montantGlobalePayeField = new JTextField(financialSituation != null ? financialSituation.getMontantGlobalePaye().toString() : "");
        formPanel.add(new JLabel("Montant Global Payé:"));
        formPanel.add(montantGlobalePayeField);

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            SwingWorker<Void, Void> saveWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        // Validate inputs
                        if (dossierMedicalComboBox.getSelectedItem() == null) {
                            throw new ServiceException("Dossier Medical is required.");
                        }
                        if (facturesField.getText().trim().isEmpty()) {
                            throw new ServiceException("Facture IDs are required.");
                        }
                        if (dateCreationField.getText().trim().isEmpty()) {
                            throw new ServiceException("Date Creation is required.");
                        }
                        if (montantGlobaleRestantField.getText().trim().isEmpty()) {
                            throw new ServiceException("Montant Global Restant is required.");
                        }
                        if (montantGlobalePayeField.getText().trim().isEmpty()) {
                            throw new ServiceException("Montant Global Payé is required.");
                        }

                        // Parse Facture IDs
                        List<Long> factureIds = Stream.of(facturesField.getText().split(",")) // Split by comma
                                .map(String::trim) // Trim whitespace
                                .map(Long::parseLong) // Convert to Long
                                .collect(Collectors.toList());

                        // Fetch Factures from the DAO
                        List<Facture> factures = factureDao.findAllById(factureIds);
                        if (factures.size() != factureIds.size()) {
                            throw new ServiceException("One or more Facture IDs do not exist.");
                        }

                        // Create or update the FinancialSituation object
                        FinancialSituation newFinancialSituation = new FinancialSituation();
                        newFinancialSituation.setDossierMedical((DossierMedical) dossierMedicalComboBox.getSelectedItem());
                        newFinancialSituation.setFactures(factures); // Set the fetched Factures
                        newFinancialSituation.setDateCreation(LocalDate.parse(dateCreationField.getText()));
                        newFinancialSituation.setMontantGlobaleRestant(Double.parseDouble(montantGlobaleRestantField.getText()));
                        newFinancialSituation.setMontantGlobalePaye(Double.parseDouble(montantGlobalePayeField.getText()));

                        if (financialSituation != null) {
                            newFinancialSituation.setIdFinancialSituation(financialSituation.getIdFinancialSituation());
                            financialSituationDao.update(newFinancialSituation);
                        } else {
                            financialSituationDao.save(newFinancialSituation);
                        }

                        loadFinancialSituations();
                        ((JDialog) SwingUtilities.getWindowAncestor(formPanel)).dispose();
                    } catch (ServiceException ex) {
                        JOptionPane.showMessageDialog(formPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (DaoException ex) {
                        JOptionPane.showMessageDialog(formPanel, "Failed to save financial situation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(formPanel, "Invalid Facture ID format. Please enter numeric IDs separated by commas.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(formPanel, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return null;
                }
            };
            saveWorker.execute();
        });

        formPanel.add(saveButton);
        return formPanel;
    }
}