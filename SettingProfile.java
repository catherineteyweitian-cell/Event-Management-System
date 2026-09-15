import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//SettingProfile is the profile settings page for speakers and users
//it shows the user's name, UID, and introduction and the sidebar has Timetable, Ticket, My Notes, and Log Out buttons

public class SettingProfile extends BaseMainFrame {

    public SettingProfile(CardLayout card, Container c) {
        super(card, c);
        initMainFrame();
        }

    @Override
    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            initMainFrame();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void buildMainPage() {
        try {
        setSize(screenWidth, screenHeight);
        setLayout(null);

        JPanel main = new JPanel(null);
        main.setBackground(new Color(243, 243, 246));
        main.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top = buildUserTopBar("profile");

        JPanel box = new JPanel(null);
        box.setBackground(Color.WHITE);
        box.setBounds(screenWidth - 335, 125, 275, 470);

        String eventSummary = buildSpeakerEventSummary();
        JTextArea reminderArea = new JTextArea(eventSummary.isEmpty() ? "No upcoming events." : eventSummary);
        reminderArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        reminderArea.setForeground(new Color(40, 40, 40));
        reminderArea.setBackground(Color.WHITE);
        reminderArea.setEditable(false);
        reminderArea.setLineWrap(true);
        reminderArea.setWrapStyleWord(true);
        reminderArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane reminderScroll = new JScrollPane(reminderArea);
        reminderScroll.setBounds(20, 20, 235, 110);
        reminderScroll.setBorder(null);

        JLabel title = new JLabel("Profile Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(20, 30, 54));
        title.setBounds(120, 90, 420, 50);

        JLabel desc = new JLabel("Update your profile information.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 20));
        desc.setForeground(new Color(90, 90, 110));
        desc.setBounds(120, 120, 500, 40);

        String speakerName = LoginRegisterSystem.getSavedUsername();
        String speakerUid = LoginRegisterSystem.getCurrentUserId();
        String introduction = "";
        String currentEmail = LoginRegisterSystem.getSavedEmail();
        if (currentEmail != null && !currentEmail.trim().isEmpty()) {
            introduction = SpeakerSystem.getSpeakerIntroductionByEmail(currentEmail);
        }
        if (introduction == null || introduction.trim().isEmpty()) {
            String[] speakerProfile = LoginRegisterSystem.getSpeakerProfileByUid(speakerUid);
            if (speakerProfile != null && speakerProfile.length > 0) {
                introduction = speakerProfile[0] == null ? "" : speakerProfile[0].trim();
            }
        }

        JLabel speakerNameLabel = new JLabel("Name: "+ (speakerName == null || speakerName.trim().isEmpty() ? "-" : speakerName.trim()));
        speakerNameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        speakerNameLabel.setForeground(new Color(20, 30, 54));
        speakerNameLabel.setBounds(120, 280, 900, 40);

        JLabel speakerUidLabel = new JLabel("Uid: "+ (speakerUid == null || speakerUid.trim().isEmpty() ? "-" :speakerUid.trim()));
        speakerUidLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        speakerUidLabel.setForeground(new Color(20, 30, 54));
        speakerUidLabel.setBounds(120, 320, 900, 40);

        JLabel introductionLabel = new JLabel("Introduction:");
        introductionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        introductionLabel.setForeground(new Color(20, 30, 54));
        introductionLabel.setBounds(120, 440, 220, 30);

        JTextArea introArea = new JTextArea(introduction.isEmpty() ? "-" : introduction);
        introArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        introArea.setForeground(new Color(90, 90, 110));
        introArea.setBackground(Color.WHITE);
        introArea.setEditable(false);
        introArea.setLineWrap(true);
        introArea.setWrapStyleWord(true);
        introArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JScrollPane introScrollPane = new JScrollPane(introArea);
        introScrollPane.setBounds(120, 500, 900, 250);
        introScrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 214, 224)));

        JButton goProfileBtn = new JButton("Edit");
        goProfileBtn.setBounds(920, 450, 100, 40);
        goProfileBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        goProfileBtn.setForeground(Color.WHITE);
        goProfileBtn.setBackground(new Color(73, 60, 255));
        goProfileBtn.setFocusPainted(false);
        goProfileBtn.setBorderPainted(false);
        goProfileBtn.addActionListener(e -> {
            try {
                go("profile");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JLabel function = new JLabel("Function:");
        function.setBounds(0,150,275,40);
        function.setFont(new Font("SansSerif", Font.BOLD, 20));

        String role = LoginRegisterSystem.getSavedRole();
            if(role.equals("User")){
                JButton historyBtn = new JButton("History");
                historyBtn.setBounds(0,190,275,40);
                historyBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
                historyBtn.setForeground(Color.WHITE);
                historyBtn.setBackground(new Color(73, 60, 255));
                historyBtn.setFocusPainted(false);
                historyBtn.setBorderPainted(false);
                historyBtn.addActionListener(e -> {
                    try {
                        showMyHistory();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                box.add(historyBtn);
            }else{
                JButton timeTableBtn = new JButton("Timetable");
                timeTableBtn.setBounds(0, 190, 275, 40);
                timeTableBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
                timeTableBtn.setForeground(Color.WHITE);
                timeTableBtn.setBackground(new Color(73, 60, 255));
                timeTableBtn.setFocusPainted(false);
                timeTableBtn.setBorderPainted(false);
                timeTableBtn.addActionListener(e -> {
                    try {
                        go("Calendar");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
        //note button only for speakers
        JButton noteBtn = new JButton("My Notes");
        noteBtn.setBounds(0, 290, 275, 40);
        noteBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        noteBtn.setForeground(Color.WHITE);
        noteBtn.setBackground(new Color(73, 60, 255));
        noteBtn.setFocusPainted(false);
        noteBtn.setBorderPainted(false);
        noteBtn.addActionListener(e -> {
            try {
                go("speakerNote");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        box.add(timeTableBtn);
        box.add(noteBtn);   //note button for speakers
            }

        JButton ticketBtn = new JButton("Ticket");
        ticketBtn.setBounds(0, 240, 275, 40);
        ticketBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        ticketBtn.setForeground(Color.WHITE);
        ticketBtn.setBackground(new Color(73, 60, 255));
        ticketBtn.setFocusPainted(false);
        ticketBtn.setBorderPainted(false);
        ticketBtn.addActionListener(e -> {
            try {
                showMyTickets();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            
        });



        JLabel setting = new JLabel("Setting:");
        setting.setBounds(0,350,275,40);
        setting.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBounds(0, 400, 275, 40);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
        logOutBtn.addActionListener(e ->{
            try {
            int confirm = JOptionPane.showConfirmDialog(null,"Confirm to log out? ","Confirm",JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }else{
                            go("login");
                        }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        if(role.equals("User")){
            box.setBounds(screenWidth - 335, 125, 275, 420);
            ticketBtn.setBounds(0, 240, 275, 40);
            setting.setBounds(0, 290, 275, 40);
            logOutBtn.setBounds(0, 340, 275, 40);
        }else{
            box.setBounds(screenWidth - 335, 125, 275, 470);
            ticketBtn.setBounds(0, 240, 275, 40);
            setting.setBounds(0, 350, 275, 40);
            logOutBtn.setBounds(0, 400, 275, 40);
        }

        main.add(top);
        main.add(box);
        main.add(title);
        main.add(desc);
        main.add(speakerNameLabel);
        main.add(speakerUidLabel);
        main.add(introductionLabel);
        main.add(introScrollPane);
        main.add(goProfileBtn);
        box.add(reminderScroll);
        box.add(function);
        box.add(ticketBtn);
        box.add(setting);
        box.add(logOutBtn);
        add(main);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build speaker event list: name | date time | venue.
    private String buildSpeakerEventSummary() {
        try {
        String speakerUid = LoginRegisterSystem.getCurrentUserId();
        if (speakerUid == null || speakerUid.trim().isEmpty()) {
            return "";
        }

        Set<String> eventIds = new HashSet<>();
        File linkFile = new File("speaker_event.txt");
        if (linkFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(linkFile))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\s*\\|\\s*");
                    if (parts.length >= 2 && speakerUid.trim().equals(parts[0].trim())) {
                        eventIds.add(parts[1].trim());
                    }
                }
            
                } catch (IOException ignored) {
                return "";
            }
        }

        if (eventIds.isEmpty()) {
            return "";
        }

        List<String[]> rows = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(6);
        File eventFile = new File("event.txt");
        if (!eventFile.exists()) {
            return "";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                String[] p = line.split("\\s*\\|\\s*");
                if (p.length < 22) 
                    continue;

                String eventId = p[18].trim();
                if (!eventIds.contains(eventId))
                     continue;

                String status = p[19].trim();
                if (!"APPROVED".equalsIgnoreCase(status))
                     continue;

                String name = p[0].trim();
                String date = p[13].trim();
                String time = p[14].trim();
                String venue = p[21].trim();
                LocalDate eventDate;
                try {
                    eventDate = LocalDate.parse(date);
                } catch (DateTimeParseException ex) {
                    continue;
                }
                if (eventDate.isBefore(today) || eventDate.isAfter(end)) {
                    continue;
                }
                rows.add(new String[] { name, date, time, venue });
            }
        
            } catch (IOException ignored) {
            return "";
        }

        if (rows.isEmpty()) {
            return "";
        }

        Collections.sort(rows, Comparator.comparing(a -> a[1] + " " + a[2]));

        StringBuilder buildLine = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) buildLine.append("\n");
            String[] r = rows.get(i);
            buildLine.append(r[0]).append(" | ").append(r[1]).append(" ").append(r[2]).append(" | ").append(r[3]);
        }
        return buildLine.toString();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //show a popup dialog listing all tickets bought by the current user 
    private void showMyTickets() {
        try {
        String userEmail = LoginRegisterSystem.getSavedEmail();
        StringBuilder buildLine = new StringBuilder();
        buildLine.append("My Tickets\n\n");

        java.io.File file = new java.io.File("my_tickets.txt");
        if (!file.exists()) {
            buildLine.append("No tickets found.");
        } else {
            int count = 0;
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) 
                        continue;
                    //format: email || ticketId || eventName || date || time || venue || seat || category || name || ic || phone
                    String[] p = line.split("||");
                    if (p.length < 11) 
                        continue;

                    if (!userEmail.trim().equalsIgnoreCase(p[0].trim())) 
                        continue;
                    count++;
                    buildLine.append("Ticket ").append(count).append(":\n");
                    buildLine.append("  Ticket ID : ").append(p[1].trim()).append("\n");
                    buildLine.append("  Event     : ").append(p[2].trim()).append("\n");
                    buildLine.append("  Date      : ").append(p[3].trim()).append("\n");
                    buildLine.append("  Time      : ").append(p[4].trim()).append("\n");
                    buildLine.append("  Venue     : ").append(p[5].trim()).append("\n");
                    buildLine.append("  Seat      : ").append(p[6].trim()).append("\n");
                    buildLine.append("  Category  : ").append(p[7].trim()).append("\n");
                    buildLine.append("  Holder    : ").append(p[8].trim()).append("\n");
                    buildLine.append("  IC        : ").append(p[9].trim()).append("\n");
                    buildLine.append("  Contact   : ").append(p[10].trim()).append("\n\n");
                }
            
                } catch (java.io.IOException ignored) {}
            if (count == 0)
                 buildLine.append("No tickets found.");
        }

        //show in a scrollable dialog
        javax.swing.JTextArea area = new javax.swing.JTextArea(buildLine.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        area.setMargin(new java.awt.Insets(10, 10, 10, 10));

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(600, 450));
        javax.swing.JOptionPane.showMessageDialog(null, scroll, "My Tickets", javax.swing.JOptionPane.PLAIN_MESSAGE);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //show booking history for current user from bookings.txt
    private void showMyHistory() {
        try {
        String userEmail = LoginRegisterSystem.getSavedEmail();
        StringBuilder buildLine = new StringBuilder();
        buildLine.append("Booking History\n\n");

        if (userEmail == null || userEmail.trim().isEmpty()) {
            buildLine.append("No user email found.");
        } else {
            Map<String, String[]> eventMap = loadEventSummaryMap();
            java.io.File file = new java.io.File("bookings.txt");
            if (!file.exists()) {
                buildLine.append("No booking records found.");
            } else {
                int count = 0;
                try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty())
                             continue;

                        String[] p = line.split("\\s*\\|\\s*");
                        if (p.length < 7) 
                            continue;

                        String email = p[1].trim();
                        if (!userEmail.trim().equalsIgnoreCase(email)) 
                            continue;

                        String status = p[6].trim();
                        if (status.toUpperCase().contains("CANCEL") || status.toUpperCase().contains("REFUND")) {
                            continue;
                        }
                        count++;
                        String eventId = p[0].trim();
                        String[] meta = eventMap.get(eventId);
                        String eventName = meta == null ? "-" : meta[0];
                        String eventDate = meta == null ? "-" : meta[1];
                        String eventTime = meta == null ? "-" : meta[2];
                        String eventVenue = meta == null ? "-" : meta[3];
                        String seat = p[2].trim();
                        String tier = p[3].trim();
                        String amount = p[4].trim();

                        buildLine.append("Booking ").append(count).append(":\n");
                        buildLine.append("  Event     : ").append(eventName).append("\n");
                        buildLine.append("  Event ID  : ").append(eventId).append("\n");
                        buildLine.append("  Date      : ").append(eventDate).append("\n");
                        buildLine.append("  Time      : ").append(eventTime).append("\n");
                        buildLine.append("  Venue     : ").append(eventVenue).append("\n");
                        buildLine.append("  Seat      : ").append(seat).append("\n");
                        buildLine.append("  Tier      : ").append(tier).append("\n");
                        buildLine.append("  Amount    : RM ").append(amount).append("\n");
                        buildLine.append("  Status    : ").append(status).append("\n\n");
                    }
                
                    } catch (java.io.IOException ignored) {}
                if (count == 0) 
                    buildLine.append("No booking records found.");
            }
        }

        javax.swing.JTextArea area = new javax.swing.JTextArea(buildLine.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        area.setMargin(new java.awt.Insets(10, 10, 10, 10));
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(700, 450));
        javax.swing.JOptionPane.showMessageDialog(null, scroll, "History", javax.swing.JOptionPane.PLAIN_MESSAGE);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Map<String, String[]> loadEventSummaryMap() {
        try {
        Map<String, String[]> map = new HashMap<>();
        java.io.File eventFile = new java.io.File("event.txt");

        if (!eventFile.exists()) {
            return map;
        }
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(eventFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                     continue;

                String[] p = line.split("\\s*\\|\\s*");
                if (p.length < 22) 
                    continue;

                String eventId = p[18].trim();
                String name = p[0].trim();
                String date = p[13].trim();
                String time = p[14].trim();
                String venue = p[21].trim();
                map.put(eventId, new String[] { name, date, time, venue });
            }
        
            } catch (java.io.IOException ignored) {}
        return map;
    
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
