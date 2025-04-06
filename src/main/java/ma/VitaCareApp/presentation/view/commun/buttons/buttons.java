package ma.VitaCareApp.presentation.view.commun.buttons;

import javax.swing.*;
import java.awt.*;

public class buttons extends JButton {
    private final Color defaultColor; // Couleur par défaut du bouton
    private Color currentColor; // Couleur actuelle du bouton
    private final Color hoverColor; // Couleur lors du survol
    private final Color pressedColor; // Couleur lors du clic
    private int borderRadius = 20;

    public buttons(String text, Icon icon, Color defaultColor) {
        super(text);
        setIcon(icon);
        this.defaultColor = defaultColor;
        this.hoverColor = new Color(50, 150, 50);  // Couleur lors du survol
        this.pressedColor = new Color(255, 255, 255);  // Couleur lors du clic
        this.currentColor = defaultColor;  // Initialiser avec la couleur par défaut

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setBackground(defaultColor); // Couleur par défaut
        setForeground(Color.BLACK);
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(160, 45));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(hoverColor);  // Couleur lors du survol
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(currentColor);  // Garder la couleur actuelle après le survol
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                setBackground(pressedColor);  // Couleur lors du clic
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                setBackground(currentColor);  // Garder la couleur actuelle après le relâchement
                repaint();
            }
        });
    }

    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground()); // Appliquer la couleur de fond
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(180, 180, 180));  // Couleur de la bordure
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        g2.dispose();
    }
}
