import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.io.FileWriter;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class checker extends JFrame  {
    private static final long serialVersionUID = 1L;
    private List<String> reservations;
    private Date selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private JTextArea textArea;

    public checker() {
        initialize();
        methods m = new methods();
        reservations = m.loadReservations();
    }

    private void initialize () {
        setTitle("Calendar");
        setBounds(100, 100, 1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // Create and configure components
        JDateChooser daychecker = new JDateChooser();
        daychecker.setBounds(163, 220, 243, 20);
        daychecker.setForeground(new Color(0, 0, 0));
        daychecker.setDateFormatString("MMMM d, y");

        List<String> startOptions = generateTimeOptions();
        DefaultComboBoxModel<String> comboBoxModelStart = new DefaultComboBoxModel<>(startOptions.toArray(new String[0]));

        List<String> endOptions = generateTimeOptions();
        DefaultComboBoxModel<String> comboBoxModelEnd = new DefaultComboBoxModel<>(endOptions.toArray(new String[0]));

        JComboBox<String> startBox = new JComboBox<>(comboBoxModelStart);
        startBox.setBounds(186, 306, 195, 22);

        JComboBox<String> endBox = new JComboBox<>(comboBoxModelEnd);
        endBox.setBounds(186, 379, 195, 22);

        JButton checkAvailabilityBtn = new JButton("Check Availability");
        checkAvailabilityBtn.setBounds(213, 428, 146, 23);

        JButton btnNewButton = new JButton("Reserve");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton.setBounds(462, 486, 89, 23);
        btnNewButton.setVisible(false);

        JButton btnNewButton_1 = new JButton("See Reserved Dates");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> reservations = loadReservations();
                reservations = removeDuplicateReservations(reservations);
                sortReservations(reservations);
                displayReservations(reservations);
            }
        });
        btnNewButton_1.setBounds(682, 176, 131, 23);

        // Add components to the content pane
        getContentPane().add(daychecker);
        getContentPane().add(startBox);
        getContentPane().add(endBox);
        getContentPane().add(checkAvailabilityBtn);
        getContentPane().add(btnNewButton);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(582, 220, 327, 231);
        getContentPane().add(scrollPane_1);
        
        
        textArea = new JTextArea();
        scrollPane_1.setViewportView(textArea);
        textArea.setEditable(false);
        getContentPane().add(btnNewButton_1);

        // Register event listeners
        daychecker.getCalendarButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDate = daychecker.getDate();
                String selectedDateString = String.format("Selected Date: %tF", selectedDate);
                System.out.println(selectedDateString);

                try {
                    FileWriter writer = new FileWriter("date_time.txt", true);
                    writer.write(selectedDateString + "\n");
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        checkAvailabilityBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedDate != null && selectedStartTime != null && selectedEndTime != null) {
                    String selectedStartDateTime = getFormattedDateTime(selectedDate, selectedStartTime);
                    String selectedEndDateTime = getFormattedDateTime(selectedDate, selectedEndTime);
                    methods m = new methods();
                    if (m.isTimeRangeAvailable(selectedStartDateTime, selectedEndDateTime)) {
                        String selectedDateTime = selectedStartDateTime + " - " + selectedEndDateTime;
                        reservations.add(selectedDateTime);
                        btnNewButton.setVisible(true);

                        JOptionPane.showMessageDialog(checker.this, "Date and time are available.", "Available",
                                JOptionPane.INFORMATION_MESSAGE);
                        try {
                            FileWriter writer = new FileWriter("reservations.txt", true);
                            writer.write(selectedDateTime + "\n");
                            writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(checker.this, "Date and time are already reserved.",
                                "Reserved", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(checker.this, "Please select a date and time range.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        endBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedEndTime = (String) endBox.getSelectedItem();
                System.out.println("Selected end time: " + selectedEndTime);
            }
        });

        startBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedStartTime = (String) startBox.getSelectedItem();
                System.out.println("Selected start time: " + selectedStartTime);
            }
        });
        
                JLabel lblNewLabel = new JLabel("New label");
                lblNewLabel.setIcon(new ImageIcon(checker.class.getResource("/IMAGES/checkingpage.png")));
                lblNewLabel.setBounds(0, 0, 984, 594);
                getContentPane().add(lblNewLabel);
    }

    private List<String> generateTimeOptions() {
        methods m = new methods();
        return m.generateTimeOptions();
    }

    private String getFormattedDateTime(Date date, String time) {
        methods m = new methods();
        return m.getFormattedDateTime(date, time);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    checker window = new checker();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<String> loadReservations() {
        List<String> reservations = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                reservations.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private List<String> removeDuplicateReservations(List<String> reservations) {
        Set<String> uniqueReservations = new TreeSet<>();
        for (String reservation : reservations) {
            uniqueReservations.add(reservation);
        }
        return new ArrayList<>(uniqueReservations);
    }

    private void sortReservations(List<String> reservations) {
        SimpleDateFormat format = new SimpleDateFormat("y-MM-dd HH:mm");
        reservations.sort(new Comparator<String>() {
            public int compare(String r1, String r2) {
                try {
                    Date d1 = format.parse(r1.split(" - ")[0]);
                    Date d2 = format.parse(r2.split(" - ")[0]);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    private void displayReservations(List<String> reservations) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat inputFormat = new SimpleDateFormat("y-MM-dd HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, y - HH:mm");
        for (String reservation : reservations) {
            String[] parts = reservation.split(" - ");
            try {
                Date startDate = inputFormat.parse(parts[0]);
                Date endDate = inputFormat.parse(parts[1]);
                String formattedReservation = outputFormat.format(startDate) + " - " + outputFormat.format(endDate);
                sb.append(formattedReservation).append("\n\n");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        textArea.setText(sb.toString());
    }
}
