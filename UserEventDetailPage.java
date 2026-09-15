import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

//UserEventDetailPage is show event detail page shown to users/speakers before booking
public class UserEventDetailPage extends JFrame {

    //card and container are needed to pass into NoticeDialog → SeatSelectionPage
    public UserEventDetailPage(Event event, CardLayout card, Container c) {
        
        setTitle(event.getName());
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Poster + basic info
        JPanel infoPanel = new JPanel(new BorderLayout());
        JLabel poster = new JLabel();
        ImageIcon icon = new ImageIcon(event.getImage());
        Image img = icon.getImage().getScaledInstance(250, 320, Image.SCALE_SMOOTH);
        poster.setIcon(new ImageIcon(img));
        infoPanel.add(poster, BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(new EmptyBorder(0, 30, 0, 0));

        JLabel title = new JLabel(event.getName());
        title.setFont(new Font("Arial", Font.BOLD, 26));
        details.add(title);
        details.add(Box.createVerticalStrut(10));
        details.add(new JLabel("Date: " + event.getDate() + "  •  " + event.getTime()));
        details.add(new JLabel("Venue: " + event.getVenue()));
        details.add(new JLabel("Age Rating: 12+"));
        infoPanel.add(details, BorderLayout.CENTER);
        main.add(infoPanel);
        main.add(Box.createVerticalStrut(30));

        //ticket info section
        main.add(createSection("Ticket Sale Information", event.getTicketSaleInfo()));

        //important notes
        main.add(createSection("Important Notes",
            "1. One ticket per person. Bring a valid ID (IC / Passport) for entry.\n" +
            "2. Tickets are non-refundable.\n" +
            "3. No outside food allowed inside venue.\n" +
            "4. Arrive at least 30 minutes early."));

        //work out whether Early Bird price is currently active
        String expiry = event.getEarlyBirdExpiry();
        boolean earlyBirdActive = false;
        boolean hasEarlyBird = expiry != null && !expiry.equals("-") && !expiry.trim().isEmpty();
        if (hasEarlyBird) {
            try {
                LocalDate expiryDate = LocalDate.parse(expiry.trim());
                earlyBirdActive = !LocalDate.now().isAfter(expiryDate);
            } catch (Exception ignored) {}
        }

        //Build ticket price panel dynamically
        //Rows: VIP (if available), Standard/Early Bird, Speaker discount notice
        boolean hasVip = event.getVipQty() != null && !event.getVipQty().trim().equals("-") && !event.getVipQty().trim().equals("0");

        int rows = 1; // always show Standard
        if (hasVip) 
            rows++;
        if (hasEarlyBird) 
            rows++;
        boolean isSpeaker = "Speaker".equalsIgnoreCase(LoginRegisterSystem.getSavedRole());
        if (isSpeaker) 
            rows++;

        JPanel ticketPanel = new JPanel(new GridLayout(rows, 2, 10, 10));
        ticketPanel.setBorder(new TitledBorder("Ticket Categories"));

        //VIP row (only if VIP is enabled for this event)
        if (hasVip) {
            ticketPanel.add(new JLabel("VIP"));
            String vipDisplay = event.getVipPrice();
            if (isSpeaker) vipDisplay += "  (you pay: RM " + String.format("%.2f", parsePrice(event.getVipPrice()) * 0.80) + ")";
            ticketPanel.add(new JLabel(vipDisplay));
        }

        //Standard row — shows active price and label
        if (earlyBirdActive) {
            // Early Bird is active: show Early Bird price with expiry info
            ticketPanel.add(new JLabel("Standard seats (Early Bird until " + expiry + ")"));
            String ebDisplay = event.getCat1Price();
            if (isSpeaker) ebDisplay += "  (you pay: RM " + String.format("%.2f", parsePrice(event.getCat1Price()) * 0.80) + ")";
            JLabel ebLabel = new JLabel(ebDisplay);
            ebLabel.setForeground(new Color(0, 130, 0)); //green to highlight it's a deal
            ticketPanel.add(ebLabel);
        } else if (hasEarlyBird) {
            //Early Bird existed but has expired
            ticketPanel.add(new JLabel("Standard seats  (Early Bird ended " + expiry + ")"));
            String stdDisplay = event.getCat2Price();
            if (isSpeaker) stdDisplay += "  (you pay: RM " + String.format("%.2f", parsePrice(event.getCat2Price()) * 0.80) + ")";
            ticketPanel.add(new JLabel(stdDisplay));
        } else {
            //no Early Bird for this event
            ticketPanel.add(new JLabel("Standard seats"));
            String stdDisplay = event.getCat2Price();
            if (isSpeaker) stdDisplay += "  (you pay: RM " + String.format("%.2f", parsePrice(event.getCat2Price()) * 0.80) + ")";
            ticketPanel.add(new JLabel(stdDisplay));
        }

        //Speaker discount notice
        if (isSpeaker) {
            JLabel discount = new JLabel("* You have a 20% speaker discount on all tickets");
            discount.setForeground(new Color(0, 130, 0));
            discount.setFont(new Font("Arial", Font.BOLD, 13));
            ticketPanel.add(discount);
            ticketPanel.add(new JLabel(""));
        }

        main.add(ticketPanel);
        main.add(Box.createVerticalStrut(20));

        //book Now button — opens NoticeDialog
        JButton book = new JButton("Book Now");
        book.setAlignmentX(Component.CENTER_ALIGNMENT);
        book.setPreferredSize(new Dimension(180, 40));
        book.setFont(new Font("SansSerif", Font.BOLD, 14));
        book.setBackground(new Color(73, 60, 255));
        book.setForeground(Color.WHITE);
        book.setBorderPainted(false);
        book.setFocusPainted(false);
        //pass card and container so NoticeDialog can navigate to SeatSelectionPage
        book.addActionListener(e -> {
            try {
                new NoticeDialog(this, event, card, c);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        main.add(book);

        add(new JScrollPane(main));
        setVisible(true);
    
        }

    private JPanel createSection(String sectionTitle, String text) {
        try {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel label = new JLabel(sectionTitle);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(Color.WHITE);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(label, BorderLayout.NORTH);
        panel.add(area, BorderLayout.CENTER);
        return panel;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //parse a price string like "RM50.00" or "50.00" into a double
    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replace("RM", "").trim());
        } catch (Exception ex) {
            return 0.0;
        }
    }


    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
