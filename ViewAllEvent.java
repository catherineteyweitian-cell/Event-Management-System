import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//ViewAllEvent shows all events in the system for the staff, admin and company.Events are listed in a scrollable panel with their status badges.

public class ViewAllEvent extends BaseEventPage {

    private static final Color WHITE = Color.WHITE;
    private static final Color PURPLE = new Color(79, 66, 232);
    private static final String EVENT_FILE = "event.txt";
    private static final String USER_FILE = "users.txt";
    private final CardLayout card;
    private final Container c;

    public ViewAllEvent(CardLayout card, Container c) {
        
        super(card, c);
        this.card = card;
        this.c = c;

        setLayout(null);
        setBackground(Color.WHITE);
        refreshEvents();
        setVisible(true);
    
        }

    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            refreshEvents();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refreshEvents() {
        try {
        removeAll();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;
        int screenHeight = screen.height;

        JPanel mainPage = new JPanel(null);
        mainPage.setBackground(new Color(243, 243, 246));
        mainPage.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top = buildTopByRole();

        JLabel eventTitle = new JLabel("All Events");
        eventTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        eventTitle.setBounds(80, 110, 300, 40);

        JLabel staffTitle = new JLabel("All Staff and Speaker:");
        staffTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        staffTitle.setBounds(80, 400, 300, 40);

        if("Company".equalsIgnoreCase(LoginRegisterSystem.getSavedRole())) {
        
        JButton createEventBtn = new JButton("+ Create New Event");
        createEventBtn.setBounds(screenWidth - 260, 90, 180, 40);
        createEventBtn.setFont(new Font("SansSerif",  Font.BOLD, 14));
        createEventBtn.setForeground(WHITE);
        createEventBtn.setBackground(PURPLE);
        createEventBtn.setFocusPainted(false);
        createEventBtn.setBorderPainted(false);
        
        mainPage.add(createEventBtn);

        createEventBtn.addActionListener(e -> {
            try {
                card.show(c, "CreateEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    } 
        JButton EventViewAll = new JButton("View all  ->");
        EventViewAll.setFont(new Font("SansSerif",  Font.BOLD, 14));
        EventViewAll.setForeground(new Color(73, 60, 255));
        EventViewAll.setBackground(new Color(243, 243, 246));
        EventViewAll.setBorderPainted(false);
        EventViewAll.setFocusPainted(false);
        EventViewAll.setBounds(1340,140,130,30);
        EventViewAll.addActionListener(e -> {
            try {
            String role = LoginRegisterSystem.getSavedRole();
            if ("Staff".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role)) {
                card.show(c, "ViewEvent");
            } else {
                card.show(c, "ViewAllEvent");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton StaffViewAll = new JButton("View all  ->");
        StaffViewAll.setFont(new Font("SansSerif",  Font.BOLD, 14));
        StaffViewAll.setForeground(new Color(73, 60, 255));
        StaffViewAll.setBackground(new Color(243, 243, 246));
        StaffViewAll.setBorderPainted(false);
        StaffViewAll.setFocusPainted(false);
        StaffViewAll.setBounds(1340,400,130,30);
        StaffViewAll.addActionListener(e -> {
            try {
                String role = LoginRegisterSystem.getSavedRole();
                if("Company".equalsIgnoreCase(role)) {
                    card.show(c, "companyPending");
                } else if ("Admin".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role)) {
                    card.show(c, "ViewAllUser");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(243, 243, 246));

        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) role = "";
        role = role.trim();

        List<String> eventRows = loadEventsForCurrentAccount();
        if (eventRows.isEmpty()) {
            JLabel empty = new JLabel("No event");
            empty.setFont(new Font("SansSerif",  Font.BOLD, 22));
            empty.setForeground(new Color(120, 126, 142));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(empty);
        } else {
            for (String row : eventRows) {
                String status = normalizeStatus(extractStatus(row));
                JLabel item = new JLabel(row);
                item.setFont(new Font("Monospaced",  Font.PLAIN, 13));
                item.setOpaque(true);
                item.setBackground(Color.WHITE);
                item.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                item.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel eventRowPanel = new JPanel(new BorderLayout());
                eventRowPanel.setBackground(Color.WHITE);
                eventRowPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
                eventRowPanel.add(item, BorderLayout.CENTER);

                if ("Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role)) {
                if ("Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role)) {
                    JLabel statusLabel = new JLabel(status);
                    statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                    if ("APPROVED".equalsIgnoreCase(status)) {
                        statusLabel.setForeground(new Color(40, 167, 69));
                    } else if ("REJECTED".equalsIgnoreCase(status)) {
                        statusLabel.setForeground(new Color(220, 53, 69));
                    } else {
                        statusLabel.setForeground(new Color(255, 140, 0));
                    }
                    eventRowPanel.add(statusLabel, BorderLayout.EAST);
                }
                }

                listPanel.add(eventRowPanel);
                listPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JPanel staffListPanel = new JPanel();
        staffListPanel.setLayout(new BoxLayout(staffListPanel, BoxLayout.Y_AXIS));
        staffListPanel.setBackground(new Color(243, 243, 246));

        List<String> staffRows = loadStaffForCurrentCompany();
        if (staffRows.isEmpty()) {
            JLabel staffEmpty = new JLabel("No staff or speaker");
            staffEmpty.setFont(new Font("SansSerif",  Font.BOLD, 22));
            staffEmpty.setForeground(new Color(120, 126, 142));
            staffEmpty.setAlignmentX(Component.LEFT_ALIGNMENT);
            staffListPanel.add(staffEmpty);
        } else {
            for (String row : staffRows) {
                JLabel staffItem = new JLabel(row);
                staffItem.setFont(new Font("Monospaced",  Font.PLAIN, 13));
                staffItem.setOpaque(true);
                staffItem.setBackground(Color.WHITE);
                staffItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                staffItem.setAlignmentX(Component.LEFT_ALIGNMENT);
                staffListPanel.add(staffItem);
                staffListPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(80, 180, screenWidth - 160, 190);
        scrollPane.setBorder(null);

        JScrollPane staffScrollPane = new JScrollPane(staffListPanel);
        staffScrollPane.setBounds(80, 450, screenWidth - 160, screenHeight - 520);
        staffScrollPane.setBorder(null);

        mainPage.add(top);
        mainPage.add(eventTitle);
        mainPage.add(EventViewAll);
        mainPage.add(StaffViewAll);
        mainPage.add(staffTitle);
        mainPage.add(scrollPane);
        mainPage.add(staffScrollPane);
        add(mainPage);

        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> loadEventsForCurrentAccount() {
        try {
        List<String> result = new ArrayList<>();
        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) 
            role = "";

        role = role.trim();
        String companyCid = LoginRegisterSystem.getCompanyCidForCurrentUser();
        boolean filterByCompany = !"Admin".equalsIgnoreCase(role);
        boolean allowAllStatus = "Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role);
        if (filterByCompany && (companyCid == null || companyCid.trim().isEmpty())) {
            return result;
        }

        File file = new File(EVENT_FILE);
        if (!file.exists()) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 18) {
                    continue;
                }
                String ownerId = parts[17].trim();
                if (filterByCompany && !companyCid.trim().equals(ownerId)) {
                    continue;
                }

                String eventName = parts[0].trim();
                String date = parts[13].trim();
                String time = parts[14].trim();
                String eventType = parts[2].trim();
                String eventId = parts.length >= 19 ? parts[18].trim() : "-";
                String status = parts.length >= 20 ? parts[19].trim() : "PENDING";
                String reason = parts.length >= 21 ? parts[20].trim() : "-";
                String location = parts.length >= 22 ? parts[21].trim() : "-";
                String earlyPrice = parts[8].trim();
                String earlyQty = parts[9].trim();
                String standardPrice = parts[6].trim();
                String standardQty = parts[7].trim();
                String vipPrice = parts[10].trim();
                String vipQty = parts[11].trim();
                String rowText = new EventRow(eventName, eventId, date, time, location, eventType, EventRow.parsePriceOrZero(earlyPrice), EventRow.parseQtyOrZero(earlyQty), EventRow.parsePriceOrZero(standardPrice), EventRow.parseQtyOrZero(standardQty), EventRow.parsePriceOrZero(vipPrice), EventRow.parseQtyOrZero(vipQty), "-", "-", "-", status, reason).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }
        //keep pending items on top, then approved, then rejected
        if ("Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role)) {
            result.sort((a, b) -> Integer.compare(statusRank(extractStatus(a)), statusRank(extractStatus(b))));
        }
        return result;

    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String extractStatus(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | Status: ";
        int start = rowText.indexOf(key);
        if (start < 0) {
            return "";
        }
        start += key.length();
        int end = rowText.indexOf(" | ", start);
        if (end < 0) {
            end = rowText.length();
        }
        return rowText.substring(start, end).trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String normalizeStatus(String raw) {
        try {
        if ("APPROVED".equalsIgnoreCase(raw)) {
            return "APPROVED";
        }
        if ("REJECTED".equalsIgnoreCase(raw)) {
            return "REJECTED";
        }
        return "PENDING";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<String> loadStaffForCurrentCompany() {
        try {
        List<String> result = new ArrayList<>();
        String role = LoginRegisterSystem.getSavedRole();
        String companyCid = LoginRegisterSystem.getCompanyCidForCurrentUser();
        boolean filterByCompany = !"Admin".equalsIgnoreCase(role);
        if (filterByCompany && (companyCid == null || companyCid.trim().isEmpty())) {
            return result;
        }

        File file = new File(USER_FILE);
        if (!file.exists()) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 5) {
                    continue;
                }

                String rowRole = parts[3].trim();
                if (!"Staff".equalsIgnoreCase(rowRole) && !"Speaker".equalsIgnoreCase(rowRole)) {
                    continue;
                }

                String staffName = parts[2].trim();
                String staffUid = parts[4].trim();
                String workId = parts.length >= 7 ? parts[6].trim() : "-";
                String rowCompanyCid = "";
                if ("Staff".equalsIgnoreCase(rowRole)) {
                    rowCompanyCid = parts.length >= 8 ? parts[7].trim() : "";
                } else if ("Speaker".equalsIgnoreCase(rowRole)) {
                    rowCompanyCid = parts.length >= 10 ? parts[9].trim() : "";
                }

                if (filterByCompany && !companyCid.trim().equals(rowCompanyCid)) {
                    continue;
                }

                result.add("Name: " + fixed(staffName, 22) + " | Work ID: " + fixed(workId, 14) + " | UID: " + safeText(staffUid));
            }
        
            } catch (IOException e) {
            return result;
        }
        result.sort((c, d) -> Integer.compare(roleRank(extractRole(c)), roleRank(extractRole(d))));
        return result;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String fixed(String value, int width) {
        try {
            String displayText = safeText(value);
            if (displayText.length() > width) {
                if (width <= 1) {
                    displayText = displayText.substring(0, width);
                } else {
                    displayText = displayText.substring(0, width - 1) + "…";
                }
            }
            return String.format("%-" + width + "s", displayText);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String safeText(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return "-";
            }
            return value.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String extractRole(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | Role: ";
        int start = rowText.indexOf(key);
        if (start < 0) {
            return "";
        }
        start += key.length();
        int end = rowText.indexOf(" | ", start);
        if (end < 0) {
            end = rowText.length();
        }
        return rowText.substring(start, end).trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private int roleRank(String role) {
        try {
        if ("User".equalsIgnoreCase(role)) {
            return 0;
        }
        if ("Speaker".equalsIgnoreCase(role)) {
            return 1;
        }
        if ("Staff".equalsIgnoreCase(role)) {
            return 2;
        }
        if ("Company".equalsIgnoreCase(role)) {
            return 3;
        }
        if ("Admin".equalsIgnoreCase(role)) {
            return 4;
        }
        return 5;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }


        private int statusRank(String status) {
            try {
        if ("PENDING".equalsIgnoreCase(status)) {
            return 0;
        }
        if ("APPROVED".equalsIgnoreCase(status)) {
            return 1;
        }
        if ("REJECTED".equalsIgnoreCase(status)) {
            return 2;
        }
        return 3;
    
        } catch (Exception ex) {
        ex.printStackTrace();
        return 0;
        }
        }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
