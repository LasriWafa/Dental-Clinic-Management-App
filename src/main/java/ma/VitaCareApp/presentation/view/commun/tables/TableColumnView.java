package ma.VitaCareApp.presentation.view.commun.tables;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TableColumnView {

    public static JTable createFacturesTable() {
        String[] columnNames = {"Numéro de Facture", "Date", "Montant", "Statut", "Client", "Méthode de Paiement"};
        Object[][] data = {
                {"F001", "2025-01-15", 150.00, "Payée", "Kaoutar El Hazzat", "Carte Bancaire"},
                {"F002", "2025-01-16", 200.00, "En Attente", "Aya", "Espèces"},
                {"F003", "2025-01-17", 250.00, "Payée", "Malak", "Virement"},
                {"F004", "2025-01-18", 100.00, "Annulée", "Wafae", "Chèque"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(scrollPane);

        return table;
    }

    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        private static final Color STRIPE_COLOR = new Color(203, 235, 236);
        private static final Color HOVER_COLOR = new Color(91, 198, 198);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                comp.setBackground(HOVER_COLOR);
            } else {
                comp.setBackground(row % 2 == 0 ? Color.WHITE : STRIPE_COLOR);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return comp;
        }
    }
}
