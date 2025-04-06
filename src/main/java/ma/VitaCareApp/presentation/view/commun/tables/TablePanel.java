package ma.VitaCareApp.presentation.view.commun.tables;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public TablePanel(String[] columnNames) {
        setLayout(new BorderLayout());

        // Create the table model with column names
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Customize the table appearance
        table.setRowHeight(30);
        table.setFont(new Font("Poppins", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 14));

        // Set the custom cell renderer for alternating row colors and hover effect
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Refresh the table with new data
    public void refreshTable(List<Object[]> data) {
        tableModel.setRowCount(0); // Clear existing data
        for (Object[] row : data) {
            tableModel.addRow(row); // Add new rows
        }
    }

    // Custom cell renderer to implement alternating row colors and hover effect
    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        private static final Color STRIPE_COLOR = new Color(203, 235, 236);
        private static final Color HOVER_COLOR = new Color(91, 198, 198);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set background color based on selection and row
            if (isSelected) {
                comp.setBackground(HOVER_COLOR);
            } else {
                comp.setBackground(row % 2 == 0 ? Color.WHITE : STRIPE_COLOR);
            }

            // Align text to the center
            setHorizontalAlignment(SwingConstants.CENTER);
            return comp;
        }
    }
}
