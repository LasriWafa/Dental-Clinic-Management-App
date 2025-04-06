package ma.VitaCareApp.presentation.view.commun.Tables;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * TableColumnChooser class allows to show/hide columns, resize columns, arrange columns graphically like
 * the M$ Windows Explorer style.
 * <br />
 * This version only shows the table without additional features like column management or direction.
 */
public class TableColumn implements MouseListener {

    private final JTableHeader header;
    private final JTable table;

    /**
     * Constructs a TableColumnChooser
     * @param header the table header
     */
    public TableColumn(JTableHeader header) {
        this.header = header;
        table = header.getTable();
    }

    /**
     * Installs the TableColumnChooser on the specified table. Call this method after creating the table.
     */
    public void install() {
        header.addMouseListener(this);
    }

    /**
     * Uninstalls the TableColumnChooser
     */
    public void uninstall() {
        header.removeMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
    }

    /**
     * The mouse pressed event to show the popup (no longer necessary in this simplified version)
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
        // No action needed in this simplified version
    }

    /**
     * The mouse released event (no longer necessary)
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        // No action needed in this simplified version
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    // No longer managing column visibility or packing
    public void packAll() {
        // No action needed
    }

    // No longer handling column size adjustments
    public void sizeColumnToFitActionPerformed(ActionEvent e) {
        // No action needed
    }

    // You can add other table-related functionalities here, but the focus is to simplify.
}
