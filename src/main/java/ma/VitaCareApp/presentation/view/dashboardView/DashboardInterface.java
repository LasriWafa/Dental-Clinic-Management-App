package ma.VitaCareApp.presentation.view.dashboardView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DashboardInterface extends JPanel {
    private String title;
    private String value;

    private Color startColor = new Color(118, 136, 136);
    private Color endColor = new Color(65, 230, 230);
    private Color borderColor = new Color(65, 230, 230);

    public DashboardInterface(String title, String value) {
        this.title = title;
        this.value = value;

        setPreferredSize(new Dimension(200, 120));
        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Roboto", Font.BOLD, 22));
        valueLabel.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);

        gbc.gridy = 1; // Placer la valeur juste après le titre
        centerPanel.add(valueLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
                0, 0, startColor,
                getWidth(), getHeight(), endColor
        );

        Shape cardShape = new RoundRectangle2D.Float(
                10, 10, getWidth() - 20, getHeight() - 20, 50, 50
        );

        g2d.setPaint(gradient);
        g2d.fill(cardShape);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(cardShape);

        g2d.dispose();
        super.paintComponent(g);
    }
}
