import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;

// shows seat list, price breakdown, and checkout button

//cart page to show selected event and seats before payment
public class CartPage extends BaseEventPage {

    private Event event;
    private ArrayList<String> seats;
    private CardLayout card;
    private Container c;

    //constructor to set card layout and container
    public CartPage(CardLayout card, Container c) {
        
        super(card, c);
        this.card = card;
        this.c = c;
    
        }

    //call this every time we navigate here, passing fresh event + seat data
    public void loadData(Event event, ArrayList<String> seats) {
        try {
        this.event = event;
        this.seats = seats;
        buildUI();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build cart UI to show event info and selected seats
    private void buildUI() {
        try {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(buildTopByRole(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        //show event details at top section
        JPanel eventPanel = new JPanel();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBorder(BorderFactory.createTitledBorder("Event Details"));

        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        eventPanel.add(nameLabel);
        eventPanel.add(Box.createVerticalStrut(5));
        eventPanel.add(new JLabel(event.getVenue()));
        eventPanel.add(new JLabel(event.getDate() + " " + event.getTime()));
        content.add(eventPanel, BorderLayout.NORTH);

        //create seat table and calculate total price
        String[] columns = {"Seat", "Category", "Price"};
        Object[][] data = new Object[seats.size()][3];
        double subtotal = 0;

        for (int i = 0; i < seats.size(); i++) {
            String seat = seats.get(i);
            String tier = getTicketTier(event, seat); // VIP, EARLY BIRD, or STANDARD
            double price = getPrice(event, getCategory(event, seat));
            data[i][0] = seat;
            data[i][1] = tier;
            data[i][2] = String.format("RM %.2f", price);
            //data[][] is what gets shown in the table
            //first [] is row, second [] is column
            //String.format is used to format the price with RM ??.??
            subtotal += price;
        }

        //create table to display selected seats and add scroll bar
        JTable table = new JTable(data, columns);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        //calculate and show price breakdown on the right side
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBorder(BorderFactory.createTitledBorder("Price Details"));

        //extra charges and tax calculation
        double bookingFee = 4.50;
        double operationalFee = 10.80;
        double taxRate = 0.06; // 6% SST

        double tax = (subtotal + bookingFee + operationalFee) * taxRate;
        double total = subtotal + bookingFee + operationalFee + tax;
        double finalTotal = total;

        //add price breakdown details to right panel
        pricePanel.add(new JLabel("Subtotal"));
        pricePanel.add(new JLabel(String.format("RM %.2f", subtotal)));
        pricePanel.add(Box.createVerticalStrut(8));
        pricePanel.add(new JLabel("Booking Fee"));
        pricePanel.add(new JLabel("RM 4.50"));

        pricePanel.add(Box.createVerticalStrut(8));
        pricePanel.add(new JLabel("Operational Fee"));
        pricePanel.add(new JLabel("RM 10.80"));

        pricePanel.add(Box.createVerticalStrut(10));
        pricePanel.add(new JLabel("SST (6%)"));
        pricePanel.add(new JLabel(String.format("RM %.2f", tax)));
        pricePanel.add(Box.createVerticalStrut(10));

        //show total price in bold
        JLabel totalLabel = new JLabel("Total");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel totalValue = new JLabel(String.format("RM %.2f", total));
        totalValue.setFont(new Font("Arial", Font.BOLD, 14));

        pricePanel.add(totalLabel);
        pricePanel.add(totalValue);

        pricePanel.add(Box.createVerticalStrut(8));
        pricePanel.add(new JLabel("Total includes SST"));
        pricePanel.add(Box.createVerticalStrut(15));



        //save bookings then go to CustomerDetailsPage
        JButton checkout = new JButton("Proceed to Checkout");
        checkout.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkout.setBackground(new Color(73, 60, 255));
        checkout.setForeground(Color.WHITE);
        checkout.setBorderPainted(false);
        checkout.setFocusPainted(false);
        checkout.addActionListener(e -> {
            try {
            saveBookings(event, seats);
            // Pass event and total to CustomerDetailsPage
            for (Component comp : c.getComponents()) {
                if (comp instanceof CustomerDetailsPage) {
                    ((CustomerDetailsPage) comp).loadData(event, finalTotal, seats);
                    break;
                }
            }
            card.show(c, "customerDetails");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        pricePanel.add(checkout);

        //create back button and go back to seat selection page
        JButton back = new JButton("Back");
        back.setFont(new Font("SansSerif", Font.BOLD, 14));
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.addActionListener(e -> {
            try {
                card.show(c, "seatSelection");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        pricePanel.add(Box.createVerticalStrut(8)); //add back button to price panel
        pricePanel.add(back);

        //add panels to main content and refresh UI
        content.add(pricePanel, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Save each seat as a booking row in bookings.txt
    private void saveBookings(Event event, ArrayList<String> seats) {
        try {
        String userEmail = LoginRegisterSystem.getSavedEmail();

        //count how many VIP and Standard seats are being booking with company of event
        int vipCount = 0;
        int standardCount = 0;
        for (String seat : seats) {
            String category = getCategory(event, seat);
            if (category.equals("VIP")) {
                vipCount++;
            } else {
                standardCount++;
            }
        }

        //check ticket availability before confirming booking
        int[] available = getAvailableQty(event == null ? "" : event.getEventId());//read remaining qty from event.txt 
        if (available != null) {
            if (vipCount > available[1] || standardCount > available[0]) {
                JOptionPane.showMessageDialog(null, "Not enough tickets available.\n"+ "VIP left: " + available[1] + ", Standard left: " + available[0],"Not enough tickets", JOptionPane.WARNING_MESSAGE);
                //available[] is read from event.txt, 0 us standard qty, 1 is vip qty
                return;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("bookings.txt", true))) { // append confirmed booking
            
            //save each selected seat into booking file
            for (String seat : seats) {
                String tier = getTicketTier(event, seat); // VIP, EARLY BIRD, or STANDARD
                double price = getPrice(event, getCategory(event, seat));
                String row = event.getEventId() + " | " + userEmail + " | " + seat + " | " + tier + " | " + String.format("%.2f", price) + " | 1 | CONFIRMED";
                bw.write(row);
                bw.newLine();
            }
        
            } catch (IOException ignored) {
                JOptionPane.showMessageDialog(null, "Error occurred while saving booking.", "Error", JOptionPane.ERROR_MESSAGE);
                //if error while writing booking.txt,it will not save the booking into the file and no crash the app,so it will show message for error
            }
        
        //update event.txt to reduce available ticket quantities
        ManageSystem.reduceEventTicketQty(event.getEventId(), vipCount, standardCount);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //read current available ticket quantities from event.txt
    //returns [standardQty, vipQty] or null if not found
    private int[] getAvailableQty(String eventId) {
        try {
        if (eventId == null || eventId.trim().isEmpty()) {
            return null;
        }
        File file = new File("event.txt");
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader bufferedRead = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = bufferedRead.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                String[] p = line.split("\\s*\\|\\s*");
                if (p.length < 19) 
                    continue;

                String id = p[18].trim();
                if (!eventId.trim().equals(id)) 
                    continue;

                int standard = parseIntSafe(safe(p, 7), -1);
                int vip = parseIntSafe(safe(p, 11), -1);
                if (standard < 0 || vip < 0)
                     return null;
                return new int[] { standard, vip };
                //if eventId matches, read standard and vip qty from event.txt and return as int array
            }
        
            } catch (IOException ignored) {}
        return null;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private int parseIntSafe(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String safe(String[] arr, int idx) {
        try {
        //if is invalid index or null,return empty string to avoid each time check for null in caller code
        if (arr == null || idx < 0 || idx >= arr.length || arr[idx] == null) 
            return "";
        //return trimmed value in array at index
        return arr[idx].trim();
        
        } catch (Exception ex) {
            ex.printStackTrace();
            return null; //if case of any error,return null to call code handle it
        }
    }

    //Map VIP(Rows A-B) and Standard zones
    //determine ticket category from seat row letter,it only two physical zones: VIP (rows A-B) and STANDARD (everything else)
    //Early Bird is the same Standard seats but it have a time-limited discount.  if late expiry of early bird date, it will change back to standard price in getPrice() method
    String getCategory(Event event, String seat) {
        try {
        char row = seat.charAt(0);

        boolean vipEnabled = event != null && event.getVipPrice() != null && !event.getVipPrice().trim().isEmpty() && !"-".equals(event.getVipPrice().trim());
        if (vipEnabled && (row == 'A' || row == 'B')) 
            return "VIP";
        return "STANDARD"; // all other rows are standard seats
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }}

    //return the ticket tier label for a seat: "VIP", "EARLY BIRD", or "STANDARD".
    //this is what gets printed on the ticket and saved to files
    String getTicketTier(Event event, String seat) {
        try {
        char row = seat.charAt(0);
        boolean vipEnabled = event != null && event.getVipPrice() != null && !event.getVipPrice().trim().isEmpty() && !"-".equals(event.getVipPrice().trim());
        if (vipEnabled && (row == 'A' || row == 'B'))
             return "VIP";

        //check if Early Bird is still active today
        String expiry = event.getEarlyBirdExpiry();
        if (expiry != null && !expiry.equals("-") && !expiry.trim().isEmpty()) {
            try {
                java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiry.trim());
                if (!java.time.LocalDate.now().isAfter(expiryDate)) {
                    return "EARLY BIRD"; // within early bird period
                }
            } catch (Exception ignored) {}
        }
        return "STANDARD";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }}

    //calculate base price (VIP/Early bird/Standard)
    //Get price for a seat
    //VIP seats → always VIP price
    //all other seats:
    //if Early Bird is enabled AND today is on or before the expiry date → Early Bird price (cat1Price)
    //otherwise → Standard price (cat2Price)
    //speaker gets an extra 20% off any price.
    double getPrice(Event event, String category) {
        try {
            double price;
            if ("VIP".equals(category)) {
                // VIP seats always use VIP price
                if (event.getVipPrice() != null && !event.getVipPrice().trim().isEmpty()
                        && !"-".equals(event.getVipPrice().trim())) {
                    price = Double.parseDouble(event.getVipPrice().replace("RM", "").trim());
                } else {
                    price = 0.0;
                }//early bird fallback logic
            } else {
                //for all not a VIP seats: check if Early Bird pricing is still active
                String expiry = event.getEarlyBirdExpiry();
                boolean earlyBirdActive = false; // default to standard price

                if (expiry != null && !expiry.equals("-") && !expiry.trim().isEmpty()) {
                    try {
                        LocalDate expiryDate = LocalDate.parse(expiry.trim());
                        //Early Bird is active if today is on or before the expiry date
                        earlyBirdActive = !LocalDate.now().isAfter(expiryDate); // isAfter return true if today is after expiry,so we can get early bird active if isAfter return false
                    } catch (Exception ignored) {}
                }

                if (earlyBirdActive) {
                    //Early Bird price is stored
                    price = Double.parseDouble(event.getCat1Price().replace("RM", "").trim());
                } else {
                    //Standard price is stored
                    price = Double.parseDouble(event.getCat2Price().replace("RM", "").trim());
                }
            }
            // Speaker gets 20% discount on any price
            if ("Speaker".equalsIgnoreCase(LoginRegisterSystem.getSavedRole())) {
                price = price * 0.80;
            }
            return price; // user get normal price
        } catch (Exception ex) {
            return 0.0;
        }
    }

    static {
        try {
            //shows exception handling  in static block
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
