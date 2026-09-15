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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//ManageEvent is the admin page for approving or rejecting events
//it reads all pending events from event.txt and lists them
//Admin can approve or reject each event with a reason

public class ManageEvent extends BaseEventPage {

    private static final String EVENT_FILE = "event.txt";
    private static final String USER_FILE = "users.txt";
    private final CardLayout card;
    private final Container c;

    public ManageEvent(CardLayout card, Container c) {
        
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

        // title for event section
        JLabel title = new JLabel("All Events:");
        title.setFont(new Font("SansSerif",  Font.BOLD, 28));
        title.setBounds(80, 110, 300, 40);

        // title for user section
        JLabel userTitle = new JLabel("All Users:");
        userTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        userTitle.setBounds(80, 400, 300, 40);

        // view all events button
        JButton EventViewAll = new JButton("View all  ->");
        EventViewAll.setFont(new Font("SansSerif",  Font.BOLD, 14));
        EventViewAll.setForeground(new Color(73, 60, 255));
        EventViewAll.setBackground(new Color(243, 243, 246));
        EventViewAll.setBorderPainted(false);
        EventViewAll.setFocusPainted(false);
        EventViewAll.setBounds(1340,110,130,30);
        EventViewAll.addActionListener(e -> {
            try {
                card.show(c, "ViewEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton UserViewAll = new JButton("View all  ->");
        UserViewAll.setFont(new Font("SansSerif",  Font.BOLD, 14));
        UserViewAll.setForeground(new Color(73, 60, 255));
        UserViewAll.setBackground(new Color(243, 243, 246));
        UserViewAll.setBorderPainted(false);
        UserViewAll.setFocusPainted(false);
        UserViewAll.setBounds(1340,400,130,30);
        UserViewAll.addActionListener(e -> {
            try {
                card.show(c, "ViewAllUser");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel eventListPanel = new JPanel();
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        eventListPanel.setBackground(new Color(243, 243, 246));

        // load all events
        List<String> eventRows = loadAllEvents();
        if (eventRows.isEmpty()) {
            JLabel empty = new JLabel("No event");
            empty.setFont(new Font("SansSerif",  Font.BOLD, 22));
            empty.setForeground(new Color(120, 126, 142));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            eventListPanel.add(empty);
        } else {
            for (String row : eventRows) {
                String eventId = extractEventId(row);
                String status = extractStatus(row);
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

                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                actionPanel.setOpaque(false);

                // only pending events can be approved
                if ("PENDING".equalsIgnoreCase(status)) {
                    JButton approvedButton = new JButton("Approve");
            approvedButton.setBackground(Color.GREEN);
                    approvedButton.setForeground(Color.WHITE);
                    approvedButton.setBorderPainted(false);
                    approvedButton.setFocusPainted(false);
                    approvedButton.addActionListener(e -> {
                        // check event id valid
                        try {
                        if (eventId == null || eventId.isEmpty() || "-".equals(eventId)) {
                            JOptionPane.showMessageDialog(null, "Invalid event ID.");
                            return;
                        }
                        // confirm approval
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Approve this event?\nEvent ID: " + eventId,
                                "Approve Event",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
                        // update event status
                        boolean ok = ManageSystem.updateEventStatusByEventId(eventId, "APPROVED", "-");
                        if (ok) {
                            refreshEvents();
                        } else {
                            JOptionPane.showMessageDialog(null, "Approve failed.");
                        }
                    
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    // reject event button
                    // admin reject event with reason
                    JButton rejectButton = new JButton("Reject");
            rejectButton.setBackground(Color.RED);
                    rejectButton.setForeground(Color.WHITE);
                    rejectButton.setBorderPainted(false);
                    rejectButton.setFocusPainted(false);
                    rejectButton.addActionListener(e -> {
                        // check event id valid
                        try {
                        if (eventId == null || eventId.isEmpty() || "-".equals(eventId)) {
                            JOptionPane.showMessageDialog(null, "Invalid event ID.");
                            return;
                        }
                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Reject this event?\nEvent ID: " + eventId,
                                "Reject Event",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
                        // get reject reason
                        String reason = JOptionPane.showInputDialog(null, "Reason:");
                        if (reason == null || reason.trim().isEmpty()) {
                            return;
                        }
                        // update status to REJECTED
                        boolean ok = ManageSystem.updateEventStatusByEventId(eventId, "REJECTED", reason);
                        if (ok) {
                            refreshEvents();
                        } else {
                            JOptionPane.showMessageDialog(null, "Reject failed.");
                        }
                    
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    actionPanel.add(approvedButton);
                    actionPanel.add(rejectButton);
                    
                // show status label if not pending
                }else if ("APPROVED".equalsIgnoreCase(status)) {
                    JLabel approvedLabel = new JLabel("Approved");
                    approvedLabel.setForeground(new Color(40, 167, 69));
                    approvedLabel.setFont(new Font("SansSerif",  Font.BOLD, 25));
                    actionPanel.add(approvedLabel);

                } else if ("REJECTED".equalsIgnoreCase(status)) {
                    JLabel rejectedLabel = new JLabel("Rejected");
                    rejectedLabel.setForeground(new Color(220, 53, 69));
                    rejectedLabel.setFont(new Font("SansSerif",  Font.BOLD, 25));
                    actionPanel.add(rejectedLabel);
                }
                eventRowPanel.add(actionPanel, BorderLayout.EAST);
                eventListPanel.add(eventRowPanel);
                eventListPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(new Color(243, 243, 246));

        List<String> userRows = loadAllUsers();
        if (userRows.isEmpty()) {
            JLabel userEmpty = new JLabel("No user");
            userEmpty.setFont(new Font("SansSerif",  Font.BOLD, 22));
            userEmpty.setForeground(new Color(120, 126, 142));
            userEmpty.setAlignmentX(Component.LEFT_ALIGNMENT);
            userListPanel.add(userEmpty);
        } else {
            for (String row : userRows) {
                String uid = extractUid(row);
                String targetRole = extractRole(row);
                JLabel userItem = new JLabel(row);
                userItem.setFont(new Font("Monospaced",  Font.PLAIN, 13));
                userItem.setOpaque(true);
                userItem.setBackground(Color.WHITE);
                userItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                userItem.setAlignmentX(Component.LEFT_ALIGNMENT);

                // delete user button
                JButton deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED);
                deleteButton.setForeground(Color.WHITE);
                deleteButton.setBorderPainted(false);
                deleteButton.setFocusPainted(false);
                deleteButton.addActionListener(e -> {
                    try {
                    if (uid == null || uid.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid user UID.");
                        return;
                    }
                    String currentUid = LoginRegisterSystem.getCurrentUserId();
                    String role = LoginRegisterSystem.getSavedRole();
                    // prevent self delete
                    if (uid.equals(currentUid)) {
                        JOptionPane.showMessageDialog(null, "You cannot delete your own admin account.");
                        return;
                    }

                    // prevent deleting other admin
                    if ("Admin".equalsIgnoreCase(targetRole)) {
                        JOptionPane.showMessageDialog(null, "You cannot delete other admin account.");
                        return;
                    }
                    // confirm delete action
                    int confirm = JOptionPane.showConfirmDialog(
                            null,"Delete this user? UID: " + uid,"Confirm Delete",JOptionPane.YES_NO_OPTION
                    );
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                    
                    // delete user from system
                    boolean ok = ManageSystem.deleteUserByUid(uid,role);
                    if (ok) {
                        refreshEvents();
                    } else {
                        JOptionPane.showMessageDialog(null, "Delete failed.");
                    }
                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                JPanel userRowPanel = new JPanel(new BorderLayout());
                userRowPanel.setBackground(Color.WHITE);
                userRowPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
                userRowPanel.add(userItem, BorderLayout.CENTER);
                userRowPanel.add(deleteButton, BorderLayout.EAST);

                userListPanel.add(userRowPanel);
                userListPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JScrollPane eventScrollPane = new JScrollPane(eventListPanel);
        eventScrollPane.setBounds(80, 180, screenWidth - 160, 190);
        eventScrollPane.setBorder(null);

        JScrollPane userScrollPane = new JScrollPane(userListPanel);
        userScrollPane.setBounds(80, 450, screenWidth - 160, screenHeight - 520);
        userScrollPane.setBorder(null);

        mainPage.add(top);
        mainPage.add(title);
        mainPage.add(userTitle);
        mainPage.add(EventViewAll);
        mainPage.add(UserViewAll);
        mainPage.add(eventScrollPane);
        mainPage.add(userScrollPane);
        add(mainPage);

        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> loadAllEvents() {
        try {
        List<String> result = new ArrayList<>();

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
                String companyEmail = parts[15].trim();
                String companyName = parts[16].trim();
                String companyId = parts[17].trim();

                String rowText = new EventRow(eventName, eventId, date, time, location, eventType,EventRow.parsePriceOrZero(earlyPrice), EventRow.parseQtyOrZero(earlyQty),EventRow.parsePriceOrZero(standardPrice), EventRow.parseQtyOrZero(standardQty),EventRow.parsePriceOrZero(vipPrice), EventRow.parseQtyOrZero(vipQty),companyName, companyEmail, companyId, status, reason).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }

        //keep pending items on top, then approved, then rejected
        result.sort((a, b) -> Integer.compare(statusRank(extractStatus(a)), statusRank(extractStatus(b))));
        return result;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<String> loadAllUsers() {
        try {
        List<String> result = new ArrayList<>();

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

                String email = parts[0].trim();
                String name = parts[2].trim();
                String role = parts[3].trim();
                String uid = parts[4].trim();
                String companyName = parts.length >= 6 ? parts[5].trim() : "-";
                String workOrSpeakerId = parts.length >= 7 ? parts[6].trim() : "-";

                String rowText = new UserRow(name, role, email, uid, companyName, workOrSpeakerId).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }
        // Sort users by role order for a stable admin view.
        result.sort((c, d) -> Integer.compare(roleRank(extractRole(c)), roleRank(extractRole(d))));
        return result;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String extractUid(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | UID: ";
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


    private String extractEventId(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | Event ID: ";
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

    private String extractRole(String roleRowText) {
        try {
        if (roleRowText == null) {
            return "";
        }
        //userRow.toString() uses " | Role: "
        String key = " | Role: ";
        int start = roleRowText.indexOf(key);
        if (start < 0) {
            return "";
        }
        start += key.length();
        int end = roleRowText.indexOf(" | ", start);
        if (end < 0) {
            end = roleRowText.length();
        }
        return roleRowText.substring(start, end).trim();
    
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
        return 4;
    
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
