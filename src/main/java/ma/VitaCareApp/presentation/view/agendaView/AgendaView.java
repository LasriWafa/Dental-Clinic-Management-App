package ma.VitaCareApp.presentation.view.agendaView;

import javax.swing.*;
import java.awt.*;
import ma.VitaCareApp.presentation.view.agendaView.Calendrier;

public class AgendaView extends JPanel {

    public AgendaView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JLabel titleLabel = createTitleLabel();
        add(titleLabel, BorderLayout.NORTH);

        JPanel calendarPanel = createCalendarPanel();
        add(calendarPanel, BorderLayout.CENTER);
    }

    private JLabel createTitleLabel() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/agenda.png"));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JLabel titleLabel = new JLabel("Agenda", icon, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        titleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        titleLabel.setVerticalTextPosition(SwingConstants.CENTER);

        return titleLabel;
    }

    private JPanel createCalendarPanel() {
        Calendrier calendar = new Calendrier();
        return calendar.createCalendarPanel();
    }
}
