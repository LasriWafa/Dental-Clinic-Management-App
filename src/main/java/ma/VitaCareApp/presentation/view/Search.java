package ma.VitaCareApp.presentation.view;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Search extends JPanel {
    private JTextField textField;
    private String placeholder;

    // Hover and focus colors
    private Color borderDefaultColor = new Color(200, 200, 200);
    private Color borderHoverColor = new Color(100, 150, 255);
    private Color borderFocusColor = new Color(50, 100, 255);

    private boolean isHovered = false;

    // Constructeur qui prend un ImageIcon et une String comme arguments
    public Search(ImageIcon icon, String placeholder) {
        this.placeholder = placeholder;

        // Set layout and initial properties
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 35)); // Taille modifiée pour la barre de recherche
        setBackground(new Color(240, 240, 240)); // Fond plus clair
        setOpaque(false);

        // Add the search icon on the left (adjust the size as needed)
        JLabel iconLabel = new JLabel();
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH); // Ajuster la taille de l'icône
            iconLabel.setIcon(new ImageIcon(img));
        }
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Marge autour de l'icône
        add(iconLabel, BorderLayout.WEST);

        // Input field
        textField = new JTextField();
        styleInputField(textField);
        textField.setText(placeholder);
        add(textField, BorderLayout.CENTER);

        // Add focus listener for placeholder behavior
        addFocusListener(textField);

        // Add hover effect
        addHoverEffect();
    }

    private void styleInputField(JTextComponent inputField) {
        inputField.setBorder(null);
        inputField.setOpaque(false);
        inputField.setBackground(new Color(240, 240, 240)); // Couleur plus claire pour l'arrière-plan
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setForeground(Color.GRAY);
    }

    private void addFocusListener(JTextComponent inputField) {
        inputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals(placeholder)) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText(placeholder);
                    inputField.setForeground(Color.GRAY);
                }
                repaint();
            }
        });
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public String getText() {
        return textField.getText().equals(placeholder) ? "" : textField.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Border
        if (hasFocus()) {
            g2.setColor(borderFocusColor); // Border color on focus
        } else if (isHovered) {
            g2.setColor(borderHoverColor); // Border color on hover
        } else {
            g2.setColor(borderDefaultColor); // Default border color
        }
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);

        super.paintComponent(g);
    }
}
