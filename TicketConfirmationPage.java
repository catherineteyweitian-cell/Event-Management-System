import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

//TicketConfirmationPage shows the booking confirmation to the user
//it displays the booking ID, event name, seats, and total paid when payment are finish

public class TicketConfirmationPage extends BaseEventPage {

    private final CardLayout card;
    private final Container c;

    public TicketConfirmationPage(CardLayout card, Container c) {
        super(card,c);
        this.card = card;
        this.c = c;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    
        }

    public void setVisible(boolean aFlag) {
        try {
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void refreshPage() {
        try {
        removeAll();
        add(buildTopByRole(), BorderLayout.NORTH);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //called from PaymentPage with all ticket data
    public void loadData(Event event, String buyerName, String buyerIc, String buyerPhone, ArrayList<String[]> ticketDetails) {
        try {
        buildUI(event, buyerName, buyerIc, buyerPhone, ticketDetails);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void buildUI(Event event, String buyerName, String buyerIc, String buyerPhone, ArrayList<String[]> ticketDetails) {
        try {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //add header
        add(buildTopByRole(), BorderLayout.NORTH);

        JLabel title = new JLabel("Booking Confirmed!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JTextArea text = new JTextArea();
        text.setFont(new Font("Arial", Font.PLAIN, 15));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setMargin(new Insets(15, 20, 15, 20));

        StringBuilder buildLine = new StringBuilder();

        //Event details
        buildLine.append("===== EVENT DETAILS =====\n");
        buildLine.append("Event   : ").append(event.getName()).append("\n");
        buildLine.append("Date    : ").append(event.getDate()).append("\n");
        buildLine.append("Time    : ").append(event.getTime()).append("\n");
        buildLine.append("Venue   : ").append(event.getVenue()).append("\n\n");

        //Buyer info 
        buildLine.append("===== BUYER INFO =====\n");
        buildLine.append("Name    : ").append(buyerName).append("\n");
        buildLine.append("IC      : ").append(buyerIc).append("\n");
        buildLine.append("Contact : ").append(buyerPhone).append("\n\n");

        // One ticket block per seat 
        buildLine.append("===== YOUR TICKETS =====\n");
        for (int i = 0; i < ticketDetails.size(); i++) {
            String[] detail = ticketDetails.get(i); // [name, ic, phone, seat]
            String seat     = detail[3];
            //use full tier label: VIP, EARLY BIRD, or STANDARD
            String category = getTicketTier(event, seat);
             String ticketId = generateTicketId();
             String gate     = generateGate();

             //save ticket so staff can verify it
            TicketIdData.addTicket(ticketId, event.getEventId(), event.getName(), seat, category, detail[0], detail[1], detail[2]);

            //if buyer is a Speaker, add event to their timetable (speaker_event.txt)
            if ("Speaker".equalsIgnoreCase(LoginRegisterSystem.getSavedRole())) {
                addToSpeakerTimetable(event.getEventId());
            }

            //save to my_tickets.txt for Profile page
            saveToMyTickets(ticketId, event, seat, category, detail[0], detail[1], detail[2]);

            buildLine.append("Ticket ").append(i + 1).append(":\n");
            buildLine.append("  Ticket ID : ").append(ticketId).append("\n");
            buildLine.append("  Seat      : ").append(seat).append("\n");
            buildLine.append("  Category  : ").append(category).append("\n");
            buildLine.append("  Holder    : ").append(detail[0]).append("\n");
            buildLine.append("  IC        : ").append(detail[1]).append("\n");
            buildLine.append("  Contact   : ").append(detail[2]).append("\n");
            buildLine.append("  Entrance  : ").append(gate).append("\n");
            buildLine.append("  Type      : E-Ticket\n\n");
        }

        buildLine.append("Confirmation has been sent to your email.\n");
        text.setText(buildLine.toString());

        //create center panel with title and text
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(title, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(text), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        //back to home button
        JButton home = new JButton("Back to Home");
        home.setFont(new Font("SansSerif", Font.BOLD, 14));
        home.setBackground(Color.RED);
        home.setForeground(Color.WHITE);
        home.setBorderPainted(false);
        home.setFocusPainted(false);
        home.addActionListener(e -> {
            try {
            String role = LoginRegisterSystem.getSavedRole();
            if ("Speaker".equalsIgnoreCase(role)) {
                card.show(c, "main_sp");
            } else {
                card.show(c, "main_u");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //view My Tickets button (goes to SettingProfile which shows tickets)
        JButton viewTickets = new JButton("View My Tickets");
        viewTickets.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewTickets.setBackground(new Color(73, 60, 255));
        viewTickets.setForeground(Color.WHITE);
        viewTickets.setBorderPainted(false);
        viewTickets.setFocusPainted(false);
        viewTickets.addActionListener(e -> {
            try {
                card.show(c, "SettingProfile");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.add(home);
        btnPanel.add(viewTickets);
        add(btnPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //save ticket info to my_tickets.txt — read by SettingProfile 
    private void saveToMyTickets(String ticketId, Event event, String seat,String category, String holderName, String holderIc, String holderPhone) {
        try {
        String userEmail = LoginRegisterSystem.getSavedEmail();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("my_tickets.txt", true))) {
            
            //format: email | ticketId | eventName | date | time | venue | seat | category | name | ic | phone
            String row = userEmail + "|" + ticketId + "|" + event.getName()  + "|" + event.getDate() + "|" + event.getTime() + "|" + event.getVenue() + "|" + seat + "|" + category + "|" + holderName + "|" + holderIc + "|" + holderPhone;
            bw.write(row);
            bw.newLine();
        
            } catch (IOException ignored) {

        }
    
      } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    //return the ticket tier: "VIP", "EARLY BIRD", or "STANDARD"
    //rows A-B are always VIP
    //all other rows use Early Bird price if today is within the expiry date, else Standard
    private String getTicketTier(Event event, String seat) {
        try {
        char row = seat.charAt(0);
        boolean vipEnabled = event != null && event.getVipPrice() != null && !event.getVipPrice().trim().isEmpty() && !"-".equals(event.getVipPrice().trim());
        if (vipEnabled && (row == 'A' || row == 'B'))
            return "VIP";

        String expiry = event.getEarlyBirdExpiry();
        if (expiry != null && !expiry.equals("-") && !expiry.trim().isEmpty()) {
            try {
                java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiry.trim());
                if (!java.time.LocalDate.now().isAfter(expiryDate)) {
                    return "EARLY BIRD";
                }
            } catch (Exception ignored) {

            }
        }
        return "STANDARD";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String generateTicketId() {
        try {
        while (true) {
            int random = (int)(Math.random() * 900000) + 100000;
            String id = "MYGR" + random;
            if (TicketIdData.getTicket(id) == null) {
                return id;
            }
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String generateGate() {
        try {
        String[] gates = {"Gate A", "Gate B", "Gate C", "Gate D"};
        return gates[(int)(Math.random() * gates.length)];
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //add speaker UID + event ID to speaker_event.txt so it shows in their timetable 
    private void addToSpeakerTimetable(String eventId) {
        try {
        String speakerUid = LoginRegisterSystem.getCurrentUserId();
        if (speakerUid == null || speakerUid.trim().isEmpty()) 
            return;
        
        if (eventId == null || eventId.trim().isEmpty()) 
            return;

        //check if link already exists to avoid duplicates
        java.io.File file = new java.io.File("speaker_event.txt");
        if (file.exists()) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2 && speakerUid.trim().equals(parts[0].trim()) && eventId.trim().equals(parts[1].trim())) {
                        return; //already linked, do nothing
                    }
                }
            
                } catch (IOException ignored) {

            }
        }

        //write new link row
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                new java.io.FileWriter("speaker_event.txt", true))) {
                    
            bw.write(speakerUid.trim() + "|" + eventId.trim());
            bw.newLine();
        
         } catch (IOException ignored) {

        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
