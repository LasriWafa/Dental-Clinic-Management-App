package ma.VitaCareApp.presentation.view.commun.sideMenu;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormeBtn extends JButton {
    private Color hoverColor = new Color(104, 182, 201);
    private Color pressedColor = new Color(61, 108, 119);
    private Color defaultColor = Color.WHITE;

    public FormeBtn(String text, ImageIcon icon) {
        super(text);

        if (icon != null) {
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(newImg));
        }

        setPreferredSize(new Dimension(200, 40));
        setMinimumSize(new Dimension(200, 40));
        setMaximumSize(new Dimension(200, 40));
        setHorizontalAlignment(JButton.CENTER);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setMargin(new Insets(5, 5, 5, 5));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        setBackground(defaultColor);
        setForeground(Color.BLACK);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setIconTextGap(10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor);
                repaint();
            }
        });

        setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(pressedColor);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.dispose();
                super.paint(g, c);
            }
        });
    }
}
