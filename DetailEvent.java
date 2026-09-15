import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//Step 3 of event creation: review all details before saving
//it inherits top bar from BaseEventPage
public class DetailEvent extends BaseEventPage {

    public DetailEvent(CardLayout card, Container c) {
        super(card, c);
        setLayout(new BorderLayout());
        setBackground(new Color(243, 243, 246));
        refreshDetails();
        setVisible(true);
        }

    //refresh event review page (Step 3: summary screen before final submission)
    public void refreshDetails() {
        try {
        removeAll();

         //fixed page size (simple layout approach)
        int pageW = 1280;
        int pageH = 720;

        //colors used in this page
        Color pageBg = new Color(243, 243, 246);
        Color textPrimary = new Color(20, 30, 54);
        Color accent = new Color(73, 60, 255);
        Color border = new Color(231, 232, 238);

        //main container panel
        JPanel page = new JPanel(null);
        page.setBackground(pageBg);
        page.setPreferredSize(new Dimension(pageW, 860));

        //top navigation bar
        JPanel top = buildTopBar();
        top.setBounds(0, 0, pageW, 70);
        page.add(top);

        //page title
        JLabel title = new JLabel("Step 3 : Event Detail Review");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(textPrimary);
        title.setBounds(80, 90, 600, 30);
        page.add(title);

        //section header
        JLabel summary = new JLabel("Summary");
        summary.setFont(new Font("SansSerif", Font.BOLD, 28));
        summary.setForeground(accent);
        summary.setBounds(80, 120, 600, 40);
        page.add(summary);

        // white card
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(border));
        card.setBounds(80, 170, 1080, 560);
        page.add(card);

        int labelX = 20;
        int valueX = 190;
        int rowWLabel = 160;
        int rowWValue = 860;
        int y = 20;

        //basic event details
        y = addRow(card, "Event Name", CreateEventSystem.getDraftEventName(), labelX, valueX, rowWLabel, rowWValue, y);
        y = addIntroRow(card, "Introduction", CreateEventSystem.getDraftIntroduction(), labelX, valueX, rowWLabel, rowWValue, y);
        y = addRow(card, "Type", CreateEventSystem.getDraftType(), labelX, valueX, rowWLabel, rowWValue, y);
        y = addRow(card, "Event ID", CreateEventSystem.getDraftEventId(), labelX, valueX, rowWLabel, rowWValue, y);

        //speaker info (safe null handling)
        String speaker = CreateEventSystem.getDraftSpeakerUid();
        //schedule details
        y = addRow(card, "Speaker UID", (speaker == null || speaker.isEmpty()) ? "-" : speaker, labelX, valueX, rowWLabel, rowWValue, y);
        y = addRow(card, "Date", CreateEventSystem.getDraftEventDate(), labelX, valueX, rowWLabel, rowWValue, y);
        y = addRow(card, "Time", CreateEventSystem.getDraftEventTime(), labelX, valueX, rowWLabel, rowWValue, y);
        y = addRow(card, "Venue", CreateEventSystem.getDraftVenue(), labelX, valueX, rowWLabel, rowWValue, y);
        y = addRow(card, "Capacity", CreateEventSystem.getDraftCapacity(), labelX, valueX, rowWLabel, rowWValue, y);

        //ticket options summary
        JLabel options = new JLabel("Options -> Early Bird: " + yesOrNo(CreateEventSystem.hasEarlyTicket())
                + "  |  VIP: " + yesOrNo(CreateEventSystem.hasVipTicket())
                + "  |  Gift: " + yesOrNo(CreateEventSystem.hasGiftTicket()));
        options.setFont(new Font("SansSerif", Font.PLAIN, 14));
        options.setForeground(new Color(96, 108, 130));
        options.setBounds(20, y, 1040, 22);
        card.add(options);
        y += 32;

        y = addRow(card, "Standard", "Price: RM " + CreateEventSystem.getStandardPrice() + "  |  Qty: " + CreateEventSystem.getStandardQty(), labelX, valueX, rowWLabel, rowWValue, y);
        if (CreateEventSystem.hasEarlyTicket()) {
            y = addRow(card, "Early Bird", "Price: RM " + CreateEventSystem.getEarlyPrice() + "  |  Qty: " + CreateEventSystem.getEarlyQty(), labelX, valueX, rowWLabel, rowWValue, y);
        }
        if (CreateEventSystem.hasVipTicket()) {
            y = addRow(card, "VIP", "Price: RM " + CreateEventSystem.getVipPrice() + "  |  Qty: " + CreateEventSystem.getVipQty(), labelX, valueX, rowWLabel, rowWValue, y);
        }
        if (CreateEventSystem.hasGiftTicket()) {
            y = addRow(card, "Gift", "Qty: " + CreateEventSystem.getGiftQty(), labelX, valueX, rowWLabel, rowWValue, y);
        }

        //image preview info (only filename shown)
        String imgPath = CreateEventSystem.getDraftImagePath();
        if (imgPath != null && !imgPath.isEmpty()) {
            y = addRow(card, "Image", new java.io.File(imgPath).getName(), labelX, valueX, rowWLabel, rowWValue, y);
        }

        // buttons
        JButton backBtn = new JButton("< Back");
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.setBounds(80, 750, 120, 40);
        backBtn.addActionListener(e -> {
            try {
                go("PriceEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        page.add(backBtn);

        //save event button (final step of event creation workflow)
        JButton doneBtn = new JButton("Save Event");
        doneBtn.setBackground(new Color(30, 160, 80));
        doneBtn.setForeground(Color.WHITE);
        doneBtn.setFocusPainted(false);
        doneBtn.setBorderPainted(false);
        doneBtn.setOpaque(true);
        doneBtn.setBounds(220, 750, 150, 40);
        doneBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        doneBtn.addActionListener(e -> {
            try {
            //confirm before saving
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to save this event?", "Confirm Save", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION)
                return;

            //save all event data
            boolean ok = CreateEventSystem.event_save_all();
            //handle save failure
            if (!ok) {
                JOptionPane.showMessageDialog(this,"Failed to save event. Please try again.","Save Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //link speaker if provided
            String draftUid = CreateEventSystem.getDraftSpeakerUid();
            if (draftUid != null && !draftUid.trim().isEmpty() && !"-".equals(draftUid.trim())) {
                SpeakerEventSystem.linkSpeakerToEvent(draftUid, CreateEventSystem.getDraftEventId());
            }
            //go back to dashboard after success
            go("dashboard_company");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        page.add(doneBtn);  //add button to page

        JScrollPane scroll = new JScrollPane(page);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //add a normal label-value row into the review card
    private int addRow(JPanel card, String label, String value, int labelX, int valueX, int labelW, int valueW, int y) {
        try {
        JLabel left = new JLabel(label);
        left.setFont(new Font("SansSerif", Font.BOLD, 14));
        left.setForeground(new Color(96, 108, 130));
        left.setBounds(labelX, y, labelW, 22);

        String safe = (value == null || value.trim().isEmpty()) ? "-" : value.trim();
        JLabel right = new JLabel(safe);
        right.setFont(new Font("SansSerif", Font.PLAIN, 15));
        right.setForeground(new Color(30, 30, 40));
        right.setBounds(valueX, y, valueW, 22);

        //add to card
        card.add(left);
        card.add(right);
        return y + 32;

        } catch (Exception ex) {
            ex.printStackTrace();
            return y;
        }
    }

    private int addIntroRow(JPanel card, String label, String value, int labelX, int valueX, int labelW, int valueW, int y) {
        try {
        JLabel left = new JLabel(label);
        left.setFont(new Font("SansSerif", Font.BOLD, 14));
        left.setForeground(new Color(96, 108, 130));
        left.setBounds(labelX, y, labelW, 22);
        card.add(left);

        String safe = (value == null || value.trim().isEmpty()) ? "-" : value.trim();
        JTextArea area = new JTextArea(safe);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("SansSerif", Font.PLAIN, 15));
        area.setForeground(new Color(30, 30, 40));
        area.setBackground(Color.WHITE);
        area.setBounds(valueX, y, valueW, 80);
        card.add(area);

        return y + 90;

        } catch (Exception ex) {
            ex.printStackTrace();
            return y;
        }
    }

    private String yesOrNo(boolean answer) {
        try {
            return answer ? "Yes" : "No";
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
