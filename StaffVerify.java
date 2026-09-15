import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

//StaffVerify is the staff check-in page for event day
//Staff enter a booking ID to verify an attendee at the event
//it will reads bookings.txt to confirm the booking is valid and activ.

public class StaffVerify extends BaseEventPage {

    private final CardLayout card;
    private final Container c;

    private JTextField ticketField;
    private JLabel status;
    private JLabel eventLabel;
    private JLabel seatLabel;
    private JLabel categoryLabel;
    private JLabel nameLabel;
    private JLabel icLabel;
    private JLabel contactLabel;

    // Small UI theme (kept inside this file to stay beginner-friendly).
    private static final Color BG = new Color(243, 243, 246);
    private static final Color TEXT = new Color(20, 30, 54);
    private static final Color MUTED = new Color(96, 108, 130);
    private static final Color BORDER = new Color(231, 232, 238);

    public StaffVerify(CardLayout card, Container c) {
        
        super(card,c);
        this.card = card;
        this.c = c;

        setLayout(new BorderLayout());
        setBackground(BG);
        refreshPage();
    
        }

    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) refreshPage();
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void refreshPage() {
        try {
        removeAll();
        add(buildTopByRole(), BorderLayout.NORTH);
        add(enterID(), BorderLayout.CENTER);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel enterID(){
        try {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        JLabel title = new JLabel("Ticket Verification", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        main.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        center.setBackground(BG);

        JLabel label = new JLabel("Enter Ticket ID");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(MUTED);

        ticketField = new JTextField();
        ticketField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ticketField.setMaximumSize(new Dimension(250, 80));
        ticketField.setBorder(new LineBorder(BORDER, 1, true));
        ticketField.setBackground(Color.WHITE);

        // Primary action button (kept as original simple blue style).
        JButton verify = new JButton("Verify Ticket");
        verify.setFont(new Font("SansSerif",Font.BOLD,16));
        verify.setFocusPainted(false);
        verify.setBorderPainted(false);
        verify.setBackground(new Color(73, 60, 255));
        verify.setForeground(Color.WHITE);
        verify.setAlignmentX(Component.CENTER_ALIGNMENT);
        verify.addActionListener(e -> {
            try {
                verifyTicket();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        center.add(label);
        center.add(Box.createVerticalStrut(10));
        center.add(ticketField);
        center.add(Box.createVerticalStrut(10));
        center.add(verify);
        center.add(Box.createVerticalStrut(20));

        JPanel resultPanel = new JPanel(new GridLayout(7, 1));
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Verification Result"),
            new EmptyBorder(10, 10, 10, 10)
        ));

        status = new JLabel("", SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 22));

        eventLabel = new JLabel("");
        seatLabel = new JLabel("");
        categoryLabel = new JLabel("");
        nameLabel = new JLabel("");
        icLabel = new JLabel("");
        contactLabel = new JLabel("");

        resultPanel.add(status);
        resultPanel.add(styleResultLine(eventLabel));
        resultPanel.add(styleResultLine(seatLabel));
        resultPanel.add(styleResultLine(categoryLabel));
        resultPanel.add(styleResultLine(nameLabel));
        resultPanel.add(styleResultLine(icLabel));
        resultPanel.add(styleResultLine(contactLabel));

        center.add(resultPanel);
        center.add(Box.createVerticalStrut(10));

        main.add(center, BorderLayout.CENTER);
        return main;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //check the ticket is valid or not valid
    private void verifyTicket() {
        try {
        String id = ticketField.getText().trim();
        String[] ticket = TicketIdData.getTicket(id);

        String companyCid = LoginRegisterSystem.getCompanyCidForCurrentUser();

        // Staff can only verify tickets for their own company.
        if (ticket != null && TicketIdData.isTicketOwnedByCompany(ticket, companyCid)) {
            status.setText("VALID TICKET");
            status.setForeground(new Color(0, 150, 0));
            eventLabel.setText("Event: " + ticket[1]);
            seatLabel.setText("Seat: " + ticket[2]);
            categoryLabel.setText("Category: " + ticket[3]);
            nameLabel.setText("Name: " + ticket[4]);
            icLabel.setText("IC: " + ticket[5]);
            contactLabel.setText("Contact: " + ticket[6]);
        } else {
            if (ticket != null) {
                status.setText("INVALID TICKET (OTHER COMPANY)");
            } else {
                status.setText("INVALID TICKET");
            }
            status.setForeground(Color.RED);
            eventLabel.setText("");
            seatLabel.setText("");
            categoryLabel.setText("");
            nameLabel.setText("");
            icLabel.setText("");
            contactLabel.setText("");
        }
     
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Makes the result lines look consistent (font, color, padding).
    private JLabel styleResultLine(JLabel label) {
        try {
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(TEXT);
        label.setBorder(new EmptyBorder(2, 6, 2, 6));
        return label;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return label;
        }
    }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
