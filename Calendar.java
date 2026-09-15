import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.*;

//Calendar shows a monthly calendar view for the speaker to see their scheduled events
//the current date is highlighted with a purple circle, and days with scheduled events are highlight with a blue background and show the event summary in line
//Prev and Next buttons let the speaker navigate between months to see past and future events

public class Calendar extends BaseMainFrame {

    private JLabel monthLabel;
    private JPanel calendarPanel;
    private YearMonth currentMonth;
    private final CardLayout card;
    private final Container c;
    private final Map<LocalDate, List<String>> speakerEvents = new HashMap<>();

    //calendar page constructor, set layout and initialize card system
    public Calendar(CardLayout card, Container c) {
        super(card, c);
        this.card = card;
        this.c = c;
        setLayout(new BorderLayout());
        }

    @Override
    public void setVisible(boolean aFlag) {
        //aFlag is the parameter for setVisible,it ensure that calendar page is initialized before showing,and only load speaker events once per page show to improve performance
        try {
        if (aFlag) {
            initMainFrame();
        }
        super.setVisible(aFlag);
    // it will call the setVisible method of the parent class (BaseMainFrame) to actually show or hide the page after we do our custom when aFlag is true

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build main page layout and set screen size
    @Override
    protected void buildMainPage() {
        try {
        setSize(screenWidth, screenHeight);
        setLayout(null);

        JPanel main = new JPanel(null);
        main.setBackground(new Color(243, 243, 246));
        main.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top = buildUserTopBar("home");
        //call parent class to build top bar

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(50, 90, 100, 50);
        backBtn.addActionListener(e -> {
            try {
                go("SettingProfile");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        int navY = 90;
        int navHeight = 36;
        int navGroupWidth = 320;
        int navStart = (screenWidth - navGroupWidth) / 2;

        //create calendar navigation buttons and calendar display panel
        JButton prevBtn = new JButton("<");
        prevBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        prevBtn.setBackground(Color.WHITE);
        prevBtn.setBorderPainted(false);
        prevBtn.setFocusPainted(false);
        prevBtn.setBounds(navStart, navY, 50, navHeight);

        //create month title label in center
        monthLabel = new JLabel(" ", JLabel.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        monthLabel.setForeground(new Color(20, 30, 54));
        monthLabel.setBounds(navStart + 50, navY, 220, navHeight);

        //create next month button
        JButton nextBtn = new JButton(">");
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        nextBtn.setBackground(Color.WHITE);
        nextBtn.setBorderPainted(false);
        nextBtn.setFocusPainted(false);
        nextBtn.setBounds(navStart + 270, navY, 50, navHeight);

        //create calendar grid panel to show days
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, 7, 4, 4));
        calendarPanel.setBackground(new Color(243, 243, 246));
        calendarPanel.setBounds(60, 140, screenWidth - 120, screenHeight - 200);

        //go to previous month and refresh calendar
        prevBtn.addActionListener(e -> {
            try {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //go to next month and refresh calendar
        nextBtn.addActionListener(e -> {
            try {
            currentMonth = currentMonth.plusMonths(1);
            //currentMonth is updated to the next month, then we can call updateCalendar() to refresh the page with the new month data
            updateCalendar();
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        main.add(top);
        main.add(backBtn);
        main.add(prevBtn);
        main.add(monthLabel);
        main.add(nextBtn);
        main.add(calendarPanel);
        add(main);

        //load speaker events once per page build
        loadSpeakerEvents();
        //initialize currentMonth here before calling updateCalendar
        currentMonth = YearMonth.now();
        updateCalendar();
        //removed setVisible(true) from here
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //fill the grid with correct day numbers for the current month
    private void updateCalendar() {
        try {
        calendarPanel.removeAll();
        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        //day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel header = new JLabel(day, JLabel.CENTER);
            header.setFont(new Font("SansSerif", Font.BOLD, 15));
            header.setForeground(new Color(90, 90, 110));
            calendarPanel.add(header);
        }

        //empty spaces before day 1
        LocalDate firstDay = currentMonth.atDay(1);
        int startDay = firstDay.getDayOfWeek().getValue() % 7;
        //LocalDate is a import from java.time package
        //it used by date in world and we can use it to get the day of week for the first day in the month,then we can know how many empty space we need before the day 1 in the calendar grid
        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(" "));
        }

        //day numbers
        int length = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= length; day++) {
            JLabel label = new JLabel(String.valueOf(day), JLabel.CENTER);
            label.setFont(new Font("SansSerif", Font.PLAIN, 16));
            label.setOpaque(true);

            //check if current day is today and highlight it
            boolean isToday = currentMonth.getYear() == today.getYear() && currentMonth.getMonthValue() == today.getMonthValue() && day == today.getDayOfMonth();
            //if the current day in the loop is the same as today,then isToday will be true, so we can use isToday to decide whether to highlight the day or not
            if (isToday) {
                label.setBackground(new Color(73, 60, 255));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("SansSerif", Font.BOLD, 16));
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(new Color(30, 30, 30));
            }

            //get events for this date and show them in the calendar cell
            LocalDate date = currentMonth.atDay(day);
            List<String> events = speakerEvents.get(date);
            if (events != null && !events.isEmpty()) {
                if (!isToday) {
                    label.setBackground(new Color(230, 238, 255));
                }
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setVerticalAlignment(JLabel.TOP);
                label.setText(buildInlineText(day, events));
            }

            calendarPanel.add(label);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //ead speaker_event.txt and map event IDs to dates from event.txt
    //and,load out speaker only information to improve performance
    private void loadSpeakerEvents() {
        try {
        speakerEvents.clear();
        String speakerUid = LoginRegisterSystem.getCurrentUserId(); //get the speakerUID from loginRegisterSystem
        if (speakerUid == null || speakerUid.trim().isEmpty()) {
            return;
        }

        //read speaker_event.txt to get all event IDs for this speaker
        Set<String> eventIds = new HashSet<>();
        File linkFile = new File("speaker_event.txt");
        if (linkFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(linkFile))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) 
                        continue;

                    //"\\s*\\|\\s*" is a regular expression that matches a pipe character "|" with optional space on either side
                    //it is used to split the line into parts based on the pipe character, while ignoring any extra spaces that may be around it
                    String[] parts = line.split("\\s*\\|\\s*");
                    if (parts.length >= 2 && speakerUid.trim().equals(parts[0].trim())) {
                        eventIds.add(parts[1].trim());
                    }
                }
            
                } catch (IOException ignored) {
                return;
            }
        }

        //if no event found, stop loading
        if (eventIds.isEmpty()) {
            return;
        }

        //check event file exists before loading event details
        File eventFile = new File("event.txt");
        if (!eventFile.exists()) {
            return;
        }

        //read event.txt and get event details for speaker timetable
        try (BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;
                String[] p = line.split("\\s*\\|\\s*"); //split event data by "|"
                if (p.length < 19)  //skip if data is incomplete
                    continue;
                String eventId = p[18].trim();
                if (!eventIds.contains(eventId)) {  //only process events that belong to this speaker
                    continue;
                }
                String name = p[0].trim();
                String dateStr = p[13].trim();
                String timeStr = p[14].trim();
                String status = p.length > 19 ? p[19].trim() : "";
                String venue = p.length > 21 ? p[21].trim() : "-";
                //only show approved events on speaker timetable
                if (!"APPROVED".equalsIgnoreCase(status)) {
                    continue;
                }
                //convert string date to LocalDate
                LocalDate date;
                try {
                    date = LocalDate.parse(dateStr);
                } catch (DateTimeParseException ex) {
                    continue;
                }
                //create event display text
                String summary = name + " | " + dateStr + " " + timeStr + " | " + venue;
                speakerEvents.computeIfAbsent(date, k -> new ArrayList<>()).add(summary); //add event into calendar map
            }
        
            } catch (IOException ignored) {
            return;
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Build inline HTML text for multiple events.
    private String buildInlineText(int day, List<String> events) {
        try {
        StringBuilder buildLine = new StringBuilder("<html>");
        buildLine.append("<b>").append(day).append("</b>");
        int max = Math.min(2, events.size());
        for (int i = 0; i < max; i++) {
            buildLine.append("<br><span style='font-size:10px;'>").append(events.get(i)).append("</span>");
        }
        if (events.size() > max) {
            buildLine.append("<br><span style='font-size:10px;'>+")
                    .append(events.size() - max)
                    .append(" more</span>");
        }
        buildLine.append("</html>");
        return buildLine.toString();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static {
        try {
            
                } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
