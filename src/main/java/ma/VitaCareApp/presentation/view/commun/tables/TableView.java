package ma.VitaCareApp.presentation.view.commun.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TableView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] columns;
    private String filePath;

    public TableView(String[] columns, String filePath) {
        this.columns = columns;
        this.filePath = filePath;
        setLayout(new BorderLayout());

        // Table model with columns
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add button
        JButton addButton = new JButton("Add New");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a dialog to add a new entry
                addNewEntry();
            }
        });

        // Action column (Edit/Delete)
        tableModel.addColumn("Actions");
        table.getColumn("Actions").setCellRenderer((TableCellRenderer) new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Load data from file
        loadDataFromFile();

        // Add components to the panel
        add(addButton, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(","); // Adjust delimiter if needed
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewEntry() {
        // Open a dialog to input new data
        JTextField[] fields = new JTextField[columns.length];
        JPanel panel = new JPanel(new GridLayout(columns.length, 2));
        for (int i = 0; i < columns.length; i++) {
            panel.add(new JLabel(columns[i]));
            fields[i] = new JTextField();
            panel.add(fields[i]);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Entry", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Add new row to the table
            String[] rowData = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                rowData[i] = fields[i].getText();
            }
            tableModel.addRow(rowData);

            // Save data to file
            saveDataToFile();
        }
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                List<String> row = new ArrayList<>();
                for (int j = 0; j < tableModel.getColumnCount() - 1; j++) { // Exclude Actions column
                    row.add(tableModel.getValueAt(i, j).toString());
                }
                writer.write(String.join(",", row) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Button renderer and editor for the Actions column
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Edit/Delete");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Edit/Delete");
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle edit/delete action
                    int choice = JOptionPane.showConfirmDialog(TableView.this, "Edit or Delete?", "Action", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        // Edit action
                        editRow(row);
                    } else {
                        // Delete action
                        tableModel.removeRow(row);
                        saveDataToFile();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }

    private void editRow(int row) {
        // Open a dialog to edit the selected row
        JTextField[] fields = new JTextField[columns.length];
        JPanel panel = new JPanel(new GridLayout(columns.length, 2));
        for (int i = 0; i < columns.length; i++) {
            panel.add(new JLabel(columns[i]));
            fields[i] = new JTextField(tableModel.getValueAt(row, i).toString());
            panel.add(fields[i]);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Entry", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Update the row
            for (int i = 0; i < columns.length; i++) {
                tableModel.setValueAt(fields[i].getText(), row, i);
            }
            saveDataToFile();
        }
    }
}