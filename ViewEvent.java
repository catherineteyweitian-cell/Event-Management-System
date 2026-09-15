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

//ViewEvent shows the full details of one selected event and it used by admin and staff to review all event information

public class ViewEvent extends BaseEventPage {

    private static final String EVENT_FILE = "event.txt";
    private final CardLayout card;
    private final Container c;

    public ViewEvent(CardLayout card, Container c) {
        
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
        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) 
            role = "";
        role = role.trim();

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif",  Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.RED);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(700,700,130,30);
        backBtn.addActionListener(e -> {
            try {
            String r = LoginRegisterSystem.getSavedRole();
            if ("Admin".equalsIgnoreCase(r)) {
                card.show(c, "ManageEvent");
            } else {
                card.show(c, "ViewAllEvent");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JLabel title = new JLabel("All Events:");
        title.setFont(new Font("SansSerif",  Font.BOLD, 28));
        title.setBounds(80, 150, 300, 40);

        JPanel eventListPanel = new JPanel();
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        eventListPanel.setBackground(new Color(243, 243, 246));

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

                JButton detailButton = new JButton("Detail");
                detailButton.setBackground(new Color(73, 60, 255));
                detailButton.setForeground(Color.WHITE);
                detailButton.setBorderPainted(false);
                detailButton.setFocusPainted(false);
                detailButton.addActionListener(e -> {
                    try {
                    if (eventId == null || eventId.isEmpty() || "-".equals(eventId)) {
                        JOptionPane.showMessageDialog(null, "Invalid event ID.");
                        return;
                    }
                    ManageSystem.setSelectedEventId(eventId);
                    card.show(c, "eventDetailAdmin");
                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                JPanel eventRowPanel = new JPanel(new BorderLayout());
                eventRowPanel.setBackground(Color.WHITE);
                eventRowPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
                eventRowPanel.add(item, BorderLayout.CENTER);

                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8,0));
                actionPanel.setOpaque(false);
                actionPanel.add(detailButton);
                if ("Admin".equalsIgnoreCase(role) && "PENDING".equalsIgnoreCase(status)) {
                    JButton approvedButton = new JButton("Approve");
            approvedButton.setBackground(Color.GREEN);
                    approvedButton.setForeground(Color.WHITE);
                    approvedButton.setBorderPainted(false);
                    approvedButton.setFocusPainted(false);
                    approvedButton.addActionListener(e -> {
                        try {
                        if (eventId == null || eventId.isEmpty() || "-".equals(eventId)) {
                            JOptionPane.showMessageDialog(null, "Invalid event ID.");
                            return;
                        }
                        int confirm = JOptionPane.showConfirmDialog(null,"Approve this event?\nEvent ID: " + eventId, "Approve Event",JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
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

                    JButton rejectButton = new JButton("Reject");
            rejectButton.setBackground(Color.RED);
                    rejectButton.setForeground(Color.WHITE);
                    rejectButton.setBorderPainted(false);
                    rejectButton.setFocusPainted(false);
                    rejectButton.addActionListener(e -> {
                        try {
                        if (eventId == null || eventId.isEmpty() || "-".equals(eventId)) {
                            JOptionPane.showMessageDialog(null, "Invalid event ID.");
                            return;
                        }
                        int confirm = JOptionPane.showConfirmDialog(null,"Reject this event?\nEvent ID: " + eventId,"Reject Event",JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
                        String reason = JOptionPane.showInputDialog(null, "Reason:");
                        if (reason == null || reason.trim().isEmpty()) {
                            return;
                        }
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

                } else if ("APPROVED".equalsIgnoreCase(status)) {
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

        JScrollPane eventScrollPane = new JScrollPane(eventListPanel);
        eventScrollPane.setBounds(80, 200, screenWidth - 160, 500);
        eventScrollPane.setBorder(null);

        mainPage.add(top);
        mainPage.add(title);
        mainPage.add(eventScrollPane);
        mainPage.add(backBtn);
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

        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) role = "";
        role = role.trim();
        String companyCid = LoginRegisterSystem.getCompanyCidForCurrentUser();
        boolean filterByCompany = !"Admin".equalsIgnoreCase(role);
        boolean allowAllStatus = "Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role);

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
                if (filterByCompany) {
                    if (companyCid == null || companyCid.trim().isEmpty()) {
                        continue;
                    }
                    if (!companyCid.trim().equals(ownerId)) {
                        continue;
                    }
                }

                String eventName = parts[0].trim();
                String date = parts[13].trim();
                String time = parts[14].trim();
                String eventType = parts[2].trim();
                String eventId = parts.length >= 19 ? parts[18].trim() : "-";
                String status = parts.length >= 20 ? parts[19].trim() : "PENDING";
                if (!allowAllStatus && !"APPROVED".equalsIgnoreCase(status)) {
                    continue;
                }
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

                String rowText = new EventRow(eventName, eventId, date, time, location, eventType, EventRow.parsePriceOrZero(earlyPrice), EventRow.parseQtyOrZero(earlyQty), EventRow.parsePriceOrZero(standardPrice), EventRow.parseQtyOrZero(standardQty), EventRow.parsePriceOrZero(vipPrice), EventRow.parseQtyOrZero(vipQty), companyName, companyEmail, companyId, status, reason).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }

        //keep pending items on top, then approved, then rejected (admin and company only).
        if ("Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role)) {
            result.sort((a, b) -> Integer.compare(statusRank(extractStatus(a)), statusRank(extractStatus(b))));
        }
        return result;
    
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


    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
