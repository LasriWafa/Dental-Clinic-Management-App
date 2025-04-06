package ma.VitaCareApp.presentation.view.commun.forms;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomInput extends JPanel {
    private JTextField textField;
    private JPasswordField passwordField;
    private boolean isPassword;
    private JButton toggleButton;
    private boolean isPasswordVisible = false;
    private String placeholder;

    // Hover and focus colors
    private Color borderDefaultColor = new Color(200, 200, 200);
    private Color borderHoverColor = new Color(150, 150, 255);
    private Color borderFocusColor = new Color(100, 150, 255);

    private boolean isHovered = false;

    public CustomInput(ImageIcon icon, boolean isPassword, String placeholder) {
        this.isPassword = isPassword;
        this.placeholder = placeholder;

        // Set layout and initial properties
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 45));
        setBackground(new Color(245, 245, 245));
        setOpaque(false);

        // Add the icon on the left
        JLabel iconLabel = new JLabel();
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        }
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(iconLabel, BorderLayout.WEST);

        // Input field
        if (isPassword) {
            passwordField = new JPasswordField();
            styleInputField(passwordField);
            passwordField.setEchoChar((char) 0); // Show placeholder initially
            passwordField.setText(placeholder);
            add(passwordField, BorderLayout.CENTER);

            // Add toggle visibility button
            toggleButton = new JButton("👁");
            styleToggleButton(toggleButton);
            toggleButton.addActionListener(e -> togglePasswordVisibility());
            add(toggleButton, BorderLayout.EAST);

            addFocusListener(passwordField);
        } else {
            textField = new JTextField();
            styleInputField(textField);
            textField.setText(placeholder);
            add(textField, BorderLayout.CENTER);

            addFocusListener(textField);
        }

        // Add hover effect
        addHoverEffect();
    }

    private void styleInputField(JTextComponent inputField) {
        inputField.setBorder(null);
        inputField.setOpaque(false);
        inputField.setBackground(new Color(245, 245, 245));
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setForeground(Color.GRAY);
    }

    private void styleToggleButton(JButton button) {
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(40, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addFocusListener(JTextComponent inputField) {
        inputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals(placeholder)) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);

                    if (isPassword && inputField instanceof JPasswordField) {
                        ((JPasswordField) inputField).setEchoChar('•'); // Hide password
                    }
                }
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setText(placeholder);
                    inputField.setForeground(Color.GRAY);

                    if (isPassword && inputField instanceof JPasswordField) {
                        ((JPasswordField) inputField).setEchoChar((char) 0); // Show placeholder
                    }
                }
                repaint();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setEchoChar('•');
            isPasswordVisible = false;
        } else {
            passwordField.setEchoChar((char) 0);
            isPasswordVisible = true;
        }
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
        if (isPassword) {
            return new String(passwordField.getPassword()).equals(placeholder) ? "" : new String(passwordField.getPassword());
        } else {
            return textField.getText().equals(placeholder) ? "" : textField.getText();
        }
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