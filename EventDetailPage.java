import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//EventDetailPage shows full details of one event for the admin
//user ,speaker,staff can see event detail for approve by admin
//company,admin can view all event information including ticket types and status

public class EventDetailPage extends BaseEventPage {
    private final CardLayout card;
    private final Container c;
    private final JTextArea detailArea;
    private final JLabel title;
    private final JPanel main;
    private JPanel top;
    private final int pageWidth;
    private final int pageHeight;

    //constructor for event detail page
    public EventDetailPage(CardLayout card, Container c) {
        
        super(card, c);
        this.card = card;
        this.c = c;

        setLayout(null);
        setBackground(new Color(243, 243, 246));

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        pageWidth = screen.width;
        pageHeight = screen.height;

        main = new JPanel(null);
        main.setBackground(new Color(243, 243, 246));
        main.setBounds(0, 0, pageWidth, pageHeight);

        top = buildTopByRole();
        top.setBounds(0, 0, pageWidth, 70);
        main.add(top);

        //page title
        title = new JLabel("Event Detail");
        title.setFont(new Font("SansSerif",  Font.BOLD, 28));
        title.setForeground(new Color(20, 30, 54));
        title.setBounds(80, 95, 900, 40);

        //read-only detail text area
        detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setFont(new Font("SansSerif",  Font.PLAIN, 16));
        detailArea.setBackground(Color.WHITE);
        detailArea.setForeground(new Color(30, 30, 40));
        detailArea.setMargin(new Insets(14, 16, 14, 16));

        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setBounds(80, 150, 1080, 520);
        detailScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(231, 232, 238)));
        detailScroll.getViewport().setBackground(Color.WHITE);

        //back button (role-based navigation)
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.setBounds(80, 690, 120, 40);
        backBtn.addActionListener(e -> {
            try {
            String role = LoginRegisterSystem.getSavedRole();
            if ("Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role)) {
                card.show(c, "ViewEvent");
            } else {
                card.show(c, "homePage");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        main.add(title);
        main.add(detailScroll);
        main.add(backBtn);
        add(main);
    
        }

    @Override
    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            refreshTopBar();
            refreshDetail();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void refreshTopBar() {
        try {
        if (top != null) {
            main.remove(top);
        }
        top = buildTopByRole();
        top.setBounds(0, 0, pageWidth, 70);
        main.add(top);
        main.revalidate();
        main.repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //reload event detail content
    private void refreshDetail() {
        try {
        //get selected event ID
        String eventId = ManageSystem.getSelectedEventId();
        //no selection case
        if (eventId == null || eventId.isEmpty()) {
            title.setText("Event Detail");
            detailArea.setText("No event selected.");
            return;
        }

        //fetch event data from system
        String[] parts = ManageSystem.findEventByEventId(eventId);
        if (parts == null || parts.length < 15) {
            title.setText("Event Detail");
            detailArea.setText("Cannot find event data for Event ID: " + eventId);
            return;
        }

        String eventName = get(parts, 0, "-");
        String introduction = get(parts, 1, "-");
        String eventType = get(parts, 2, "-");
        String earlyEnabled = yesNo(get(parts, 3, "false"));
        String vipEnabled = yesNo(get(parts, 4, "false"));
        String giftEnabled = yesNo(get(parts, 5, "false"));
        String standardPrice = get(parts, 6, "-");
        String standardQty = get(parts, 7, "-");
        String earlyPrice = get(parts, 8, "-");
        String earlyQty = get(parts, 9, "-");
        String vipPrice = get(parts, 10, "-");
        String vipQty = get(parts, 11, "-");
        String giftQty = get(parts, 12, "-");
        String date = get(parts, 13, "-");
        String time = get(parts, 14, "-");
        String companyEmail = get(parts, 15, "-");
        String companyName = get(parts, 16, "-");
        String companyId = get(parts, 17, "-");
        String status = get(parts, 19, "-");
        String reason = get(parts, 20, "-");
        String venue = get(parts, 21, "-");
        String capacity = get(parts, 22, "-");
        // Read conference session list for this event.
        String sessionsSummary = ConferenceSessionSystem.buildSessionSummary(eventId);

        //event detail display logic with role-based access control
        String role = LoginRegisterSystem.getSavedRole();
        //check if user has privileged access (Company/Admin)
        boolean privileged = "Company".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role);
        //non-privileged users cannot view unapproved events
        if (!privileged && !"APPROVED".equalsIgnoreCase(status)) {
            title.setText("Event Detail - " + eventName + " (" + eventId + ")");
            detailArea.setText("This event is not approved yet.");
            detailArea.setCaretPosition(0);
            return;
        }

        //set title for event detail page
        title.setText("Event Detail - " + eventName + " (" + eventId + ")");
        //build full event detail text
        String text = ""+ "Event Name: " + eventName + "\n"
                + "Event ID: " + eventId + "\n"
                + "Introduction: " + introduction + "\n"
                + "Type: " + eventType + "\n"
                + "Date: " + date + "\n"
                + "Time: " + time + "\n\n"
                + "Venue: " + venue + "\n"
                + "Capacity: " + capacity + "\n\n"
                + "Ticket Options\n"
                + "- Early Bird Enabled: " + earlyEnabled + "\n"
                + "- VIP Enabled: " + vipEnabled + "\n"
                + "- Gift Enabled: " + giftEnabled + "\n"
                + "- Standard: RM " + standardPrice + " | Qty: " + standardQty + "\n"
                + "- Early Bird: RM " + earlyPrice + " | Qty: " + earlyQty + "\n"
                + "- VIP: RM " + vipPrice + " | Qty: " + vipQty + "\n"
                + "- Gift Qty: " + giftQty + "\n\n"
                + "Company\n"
                + "- Name: " + companyName + "\n"
                + "- Email: " + companyEmail + "\n"
                + "- ID: " + companyId + "\n\n"
                + buildDecisionBlock(status, reason)
                + "Sessions\n"
                + sessionsSummary + "\n";
        //display content in text area
        detailArea.setText(text);
        detailArea.setCaretPosition(0);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String get(String[] parts, int index, String fallback) {
        try {
        if (parts == null || index < 0 || index >= parts.length) 
            return fallback;
        String v = parts[index] == null ? "" : parts[index].trim();
        return v.isEmpty() ? fallback : v;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String yesNo(String value) {
        try {
        return "true".equalsIgnoreCase(value) ? "Yes" : "No";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //only company/admin should see approval status and reason.
    private String buildDecisionBlock(String status, String reason) {
        try {
        String role = LoginRegisterSystem.getSavedRole();
        if (!"Company".equalsIgnoreCase(role) && !"Admin".equalsIgnoreCase(role)) {
            return "";
        }
        return "Situation:\n" + "- Status: " + status + "\n"  + "- Reason: " + reason + "\n\n";
    
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
