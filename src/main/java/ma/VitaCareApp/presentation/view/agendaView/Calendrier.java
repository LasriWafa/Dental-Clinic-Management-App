package ma.VitaCareApp.presentation.view.agendaView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.*;

public class Calendrier {

    private static final String[] MONTH_NAMES = { "Janvier", "Février",
            "Mars", "Avril", "Mai", "Juin", "Juillet",
            "Août", "Septembre", "Octobre", "Novembre", "Décembre" };

    private static final String[] DAY_NAMES = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

    private Color backgroundColor;

    private JComboBox<String> monthComboBox;
    private JLabel[][] dayLabel;
    private JLabel titleLabel;

    private JTextField dayField;
    private JTextField yearField;

    private LocalDate calendarDate;

    public Calendrier() {
        this.calendarDate = LocalDate.now();
    }

    public JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        backgroundColor = panel.getBackground();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(createTopPanel(), BorderLayout.NORTH);
        panel.add(createDayLabelsPanel(), BorderLayout.CENTER);

        updateDayLabels();
        return panel;
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createDatePanel(), BorderLayout.NORTH);
        panel.add(createTitlePanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        Font font = new Font("Arial", Font.PLAIN, 12);

        JButton previousYearButton = new JButton("<<");
        previousYearButton.addActionListener(event -> changeYear(-1));
        panel.add(previousYearButton);

        JButton previousMonthButton = new JButton("<");
        previousMonthButton.addActionListener(event -> changeMonth(-1));
        panel.add(previousMonthButton);

        monthComboBox = new JComboBox<>(MONTH_NAMES);
        monthComboBox.setFont(font);
        monthComboBox.setSelectedIndex(calendarDate.getMonthValue() - 1);
        panel.add(monthComboBox);

        dayField = new JTextField(2);
        dayField.setFont(font);
        dayField.setText(Integer.toString(calendarDate.getDayOfMonth()));
        panel.add(dayField);

        yearField = new JTextField(4);
        yearField.setFont(font);
        yearField.setText(Integer.toString(calendarDate.getYear()));
        panel.add(yearField);

        JButton updateButton = new JButton("Update Calendar");
        updateButton.addActionListener(new CalendarListener());
        panel.add(updateButton);

        JButton nextMonthButton = new JButton(">");
        nextMonthButton.addActionListener(event -> changeMonth(1));
        panel.add(nextMonthButton);

        JButton nextYearButton = new JButton(">>");
        nextYearButton.addActionListener(event -> changeYear(1));
        panel.add(nextYearButton);

        return panel;
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Font font = new Font("Arial", Font.BOLD, 24);

        titleLabel = new JLabel(" ");
        titleLabel.setFont(font);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createDayLabelsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 7, 2, 2));  // Réduit l'espacement entre les cases

        // Ajouter les noms des jours dans l'en-tête du calendrier
        for (String dayName : DAY_NAMES) {
            JLabel dayNameLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(dayNameLabel);
        }

        int daysInMonth = calendarDate.lengthOfMonth();
        int firstDayOfMonth = calendarDate.withDayOfMonth(1).getDayOfWeek().getValue();

        int rows = (int) Math.ceil((firstDayOfMonth + daysInMonth) / 7.0);
        dayLabel = new JLabel[rows][7];

        Font dayFont = new Font("Arial", Font.PLAIN, 20);

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < 7; i++) {
                JPanel dayPanel = new JPanel(new BorderLayout());
                dayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                dayPanel.setPreferredSize(new Dimension(10, 10));

                dayLabel[j][i] = new JLabel(" ");
                dayLabel[j][i].setFont(dayFont);
                dayLabel[j][i].setHorizontalAlignment(JLabel.CENTER);
                dayPanel.add(dayLabel[j][i], BorderLayout.CENTER);

                panel.add(dayPanel);
            }
        }
        return panel;
    }

    private void updateDayLabels() {
        int month = monthComboBox.getSelectedIndex();
        int day = parseInt(dayField.getText().trim());
        int year = parseInt(yearField.getText().trim());

        if (year > 0 && day > 0) {
            calendarDate = LocalDate.of(year, month + 1, day);
            LocalDate firstDayOfMonth = calendarDate.withDayOfMonth(1);
            int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;

            titleLabel.setText(MONTH_NAMES[month] + " " + year);

            LocalDate currentDay = firstDayOfMonth.minusDays(startDay);

            for (int j = 0; j < dayLabel.length; j++) {
                for (int i = 0; i < 7; i++) {
                    if (currentDay.getMonthValue() == month + 1) {
                        dayLabel[j][i].setText(Integer.toString(currentDay.getDayOfMonth()));
                        dayLabel[j][i].getParent().setBackground(Color.WHITE);
                    } else {
                        dayLabel[j][i].setText(" ");
                        dayLabel[j][i].getParent().setBackground(Color.LIGHT_GRAY);
                    }
                    currentDay = currentDay.plusDays(1);
                }
            }
        }
    }

    private void fillDays(LocalDate monthDate, int year, int month, int day) {
        for (JLabel[] jLabels : dayLabel) {
            for (JLabel jLabel : jLabels) {
                int calMonth = monthDate.getMonthValue();
                int calYear = monthDate.getYear();
                jLabel.getParent().setBackground(backgroundColor);
                jLabel.setText(" ");
                if (year == calYear && month == calMonth) {
                    int calDay = monthDate.getDayOfMonth();
                    jLabel.setText(Integer.toString(calDay));

                    if (day == calDay) {
                        jLabel.getParent().setBackground(Color.YELLOW);
                        jLabel.getParent().revalidate();
                        jLabel.getParent().repaint();
                    }
                }
                monthDate = monthDate.plusDays(1);
            }
        }
    }


    private int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void changeMonth(int delta) {
        int month = monthComboBox.getSelectedIndex();
        month += delta;
        if (month < 0) {
            month = 11;
            changeYear(-1);
        } else if (month > 11) {
            month = 0;
            changeYear(1);
        }
        monthComboBox.setSelectedIndex(month);
        updateDayLabels();
    }

    private void changeYear(int delta) {
        int year = parseInt(yearField.getText().trim());
        yearField.setText(Integer.toString(year + delta));
        updateDayLabels();
    }

    private class CalendarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            updateDayLabels();
        }
    }
}
