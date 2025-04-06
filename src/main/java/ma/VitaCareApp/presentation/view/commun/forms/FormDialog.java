package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import java.awt.*;

public class FormDialog extends JDialog {
    private JPanel formPanel;

    public FormDialog(JFrame parent, String title, JPanel formPanel) {
        super(parent, title, true);
        this.formPanel = formPanel;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getParent());
    }

    public JPanel getFormPanel() {
        return formPanel;
    }
}
