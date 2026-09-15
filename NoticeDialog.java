import java.awt.*;
import javax.swing.*;

//Notice dialog is shows terms before booking. Still a JDialog 
public class NoticeDialog extends JDialog {
    private final JFrame parentFrame;

    // notice dialog constructor
    // show event terms & conditions before booking
    public NoticeDialog(JFrame parent, Event event, CardLayout card, Container c) {
        
        super(parent, "Notice", true);
        this.parentFrame = parent;
        // close dialog when exit
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // text area for terms & conditions
        JTextArea text = new JTextArea(
            "\t\t\tEvent Ticketing Terms & Conditions\n\n" +
            "1. One ticket per person, specific seating assigned. Please ensure you bring a valid original ID (IC Card / Passport) for verification at the entrance.\n\n" +
            "2. Please ensure your email address is valid to receive the confirmation email and e-ticket(s).\n\n" +
            "3. Please ensure the ticket category/section that you selected is correct before proceeding to payment.\n\n" +
            "4. Please arrive at the venue at least 30 minutes before the event starts. Latecomers will be guided to enter during appropriate breaks by onsite staff.\n\n" +
            "5. Prohibited items, including flammables, explosives, controlled knives, and other illegal items, are strictly forbidden.\n\n" +
            "6. Unauthorized audio and video recording and professional photography are strictly prohibited during the event.\n\n" +
            "7. Tickets are non-refundable and non-exchangeable once purchased.\n\n" +
            "8. The organizer reserves the right to adjust the event schedule or performers based on onsite conditions.\n\n" +
            "9. Participants are responsible for their personal belongings.\n\n" +
            "10. For further details, please refer to the official announcement or onsite notices."
        );

        text.setFont(new Font("Arial", Font.PLAIN, 16));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setOpaque(false);

        JButton accept = new JButton("OK");
        accept.setFont(new Font("SansSerif", Font.BOLD, 14));
        accept.setBackground(new Color(73, 60, 255));
        accept.setForeground(Color.WHITE);
        accept.setBorderPainted(false);
        accept.setFocusPainted(false);
        accept.addActionListener(e -> {
            try {
            setVisible(false);
            dispose();
            if (parentFrame != null) {
                parentFrame.dispose();
            }
            //navigate to SeatSelectionPage inside the main CardLayout
            for (Component comp : c.getComponents()) {
                if (comp instanceof SeatSelectionPage) {
                    ((SeatSelectionPage) comp).loadData(event, 1);
                    break;
                }
            }
            card.show(c, "seatSelection");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(new JScrollPane(text), "Center");
        add(accept, "South");
        setSize(900, 700);
        setLocationRelativeTo(parent);
        setVisible(true);
    
        }

    static {
        try {

        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
