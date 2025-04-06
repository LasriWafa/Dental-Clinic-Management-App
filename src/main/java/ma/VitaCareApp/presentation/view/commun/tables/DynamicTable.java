package ma.VitaCareApp.presentation.view.commun.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class DynamicTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public DynamicTable() {
        setLayout(new BorderLayout());

        // Table model with dynamic columns
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Load data into the table
    public void loadData(List<Map<String, String>> data) {
        tableModel.setRowCount(0); // Clear existing data

        // Set columns dynamically
        if (!data.isEmpty()) {
            tableModel.setColumnIdentifiers(data.get(0).keySet().toArray());
        }

        // Add rows
        for (Map<String, String> row : data) {
            tableModel.addRow(row.values().toArray());
        }
    }

    // Get the selected row as a map
    public Map<String, String> getSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Map<String, String> rowData = new java.util.HashMap<>();
            for (int i = 0; i < table.getColumnCount(); i++) {
                rowData.put(table.getColumnName(i), table.getValueAt(selectedRow, i).toString());
            }
            return rowData;
        }
        return null;
    }
}