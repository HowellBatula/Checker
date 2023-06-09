import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class methods {
    private static final String RESERVATIONS_FILE = "reservations.txt";

    List<String> loadReservations() {
        List<String> reservations = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS_FILE));
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

    List<String> removeDuplicateReservations(List<String> reservations) {
        Set<String> uniqueReservations = new TreeSet<>();
        for (String reservation : reservations) {
            uniqueReservations.add(reservation);
        }
        return new ArrayList<>(uniqueReservations);
    }

    void sortReservations(List<String> reservations) {
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

    void displayReservations(List<String> reservations) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat inputFormat = new SimpleDateFormat("y-MM-dd HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
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
        System.out.println(sb.toString());
    }

     List<String> generateTimeOptions() {
        List<String> timeOptions = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date startDate;
        try {
            startDate = dateFormat.parse("07:00");
            Date endDate = dateFormat.parse("23:00");
            while (startDate.before(endDate)) {
                timeOptions.add(dateFormat.format(startDate));
                startDate = new Date(startDate.getTime() + 60 * 60000); // Add 1 hour
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeOptions;
    }


    String getFormattedDateTime(Date date, String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        return formattedDate + " " + time;
    }

    boolean isTimeRangeAvailable(String startDateTime, String endDateTime) {
        List<String> formattedReservations = formatReservations(loadReservations());
        for (String reservation : formattedReservations) {
            String[] parts = reservation.split(" - ");
            String existingStartDateTime = parts[0];
            String existingEndDateTime = parts[1];
            if (isOverlap(startDateTime, endDateTime, existingStartDateTime, existingEndDateTime)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlap(String startDateTime1, String endDateTime1, String startDateTime2, String endDateTime2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate1 = format.parse(startDateTime1);
            Date endDate1 = format.parse(endDateTime1);
            Date startDate2 = format.parse(startDateTime2);
            Date endDate2 = format.parse(endDateTime2);
            return (startDate1.before(endDate2) && endDate1.after(startDate2))
                    || (startDate2.before(endDate1) && endDate2.after(startDate1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    void saveReservations(List<String> reservations) {
        try {
            FileWriter writer = new FileWriter(RESERVATIONS_FILE);
            for (String reservation : reservations) {
                writer.write(reservation + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> formatReservations(List<String> reservations) {
        List<String> formattedReservations = new ArrayList<>();
        for (String reservation : reservations) {
            String[] parts = reservation.split(" - ");
            formattedReservations.add(parts[0] + " - " + parts[1]);
        }
        return formattedReservations;
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
}




