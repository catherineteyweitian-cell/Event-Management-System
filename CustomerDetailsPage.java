import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

//customer details page for entering buyer information before payment
public class CustomerDetailsPage extends BaseEventPage {

    private CardLayout card;
    private Container c;
    private Event event;
    private double totalAmount;
    private ArrayList<String> seats;

    public CustomerDetailsPage(CardLayout card, Container c) {
        super(card, c);
        this.card = card;
        this.c = c;
        }

    //called from CartPage to pass event data before showing this page
    public void loadData(Event event, double totalAmount, ArrayList<String> seats) {
        try {
        this.event = event;
        this.totalAmount = totalAmount;
        this.seats = seats;
        buildUI();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build customer details UI for each selected seat
    private void buildUI() {
        try {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = buildTopByRole();
        add(top, BorderLayout.NORTH);

        //page title
        JLabel title = new JLabel("Buyer & Ticket Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        //main form container
        JPanel allForms = new JPanel();
        allForms.setLayout(new BoxLayout(allForms, BoxLayout.Y_AXIS));
        allForms.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        //one set of fields per seat
        JTextField[] nameFields = new JTextField[seats.size()];
        JTextField[] icFields = new JTextField[seats.size()]; //IC
        JTextField[] emailFields = new JTextField[seats.size()];
        JTextField[] confirmEmails = new JTextField[seats.size()];
        JTextField[] phoneFields = new JTextField[seats.size()];

        //create input form for each selected seat (each ticket has its own buyer details)
        for (int i = 0; i < seats.size(); i++) {
            String seat = seats.get(i);

            //title for each ticket section
            JLabel header = new JLabel("Ticket " + (i + 1) + " — Seat: " + seat);
            header.setFont(new Font("Arial", Font.BOLD, 16));
            header.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
            allForms.add(header);

            //create input fields for this ticket
            nameFields[i] = new JTextField();
            icFields[i] = new JTextField();
            emailFields[i] = new JTextField();
            confirmEmails[i] = new JTextField();
            phoneFields[i]  = new JTextField();

            //create input fields for this ticket
            allForms.add(makeRow("Full Name:",nameFields[i]));
            allForms.add(makeRow("IC Number:",icFields[i]));
            allForms.add(makeRow("Email Address:",emailFields[i]));
            allForms.add(makeRow("Confirm Email:",confirmEmails[i]));
            allForms.add(makeRow("Phone Number:",phoneFields[i]));
        }

        //wrap title and form into center area with scroll
        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setBackground(Color.WHITE);
        centerWrap.add(title, BorderLayout.NORTH);
        centerWrap.add(new JScrollPane(allForms), BorderLayout.CENTER);
        add(centerWrap, BorderLayout.CENTER);

        //bottom panel with agree checkbox, total, and buttons
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        //terms checkbox (no refund policy)
        JCheckBox agree = new JCheckBox("<html>No refund or exchange once booking is confirmed.</html>");

        //show total payment amount
        JLabel total = new JLabel(String.format("Total: RM %.2f", totalAmount));
        total.setFont(new Font("Arial", Font.BOLD, 16));

        //button to confirm all buyer details before proceeding to payment
        JButton confirm = new JButton("Confirm Details");
        confirm.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirm.setBackground(new Color(73, 60, 255));
        confirm.setForeground(Color.WHITE);
        confirm.setBorderPainted(false);
        confirm.setFocusPainted(false);
        confirm.addActionListener(e -> {
            try {
            //validate every ticket's fields
            for (int i = 0; i < seats.size(); i++) {
                if (nameFields[i].getText().isEmpty() || icFields[i].getText().isEmpty() || emailFields[i].getText().isEmpty() || confirmEmails[i].getText().isEmpty() || phoneFields[i].getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ticket " + (i+1) + ": Please fill in all fields.");
                    return;
                }
                //email confirmation check
                if (!emailFields[i].getText().equals(confirmEmails[i].getText())) {
                    JOptionPane.showMessageDialog(null,
                        "Ticket " + (i+1) + ": Email addresses do not match.");
                    return;
                }
            }
            //must agree to policy before continuing
            if (!agree.isSelected()) {
                JOptionPane.showMessageDialog(null, "Please agree to the policy.");
                return;
            }


            //collect buyer name and IC from the first ticket (main purchaser)
            String buyerName = nameFields[0].getText().trim();
            String buyerIc   = icFields[0].getText().trim();
            String buyerEmail = emailFields[0].getText().trim();
            String buyerPhone = phoneFields[0].getText().trim();
            if (CustomerDetailSystem.validateBuyerDetails(buyerName, buyerIc, buyerEmail, buyerPhone, confirmEmails[0].getText().trim()) == false) {
                return;
            }

            //build per-ticket detail list for confirmation page
            ArrayList<String[]> ticketDetails = new ArrayList<>();
            for (int i = 0; i < seats.size(); i++) {
                //each entry: [name, ic, phone, seat]
                ticketDetails.add(new String[]{
                    nameFields[i].getText().trim(),
                    icFields[i].getText().trim(),
                    phoneFields[i].getText().trim(),
                    seats.get(i)
                });
            }

            //pass all data to PaymentPage
            for (Component comp : c.getComponents()) {
                if (comp instanceof PaymentPage) {
                    ((PaymentPage) comp).loadData(event, totalAmount, buyerName, buyerIc, buyerPhone, ticketDetails);
                    break;
                }
            }
            card.show(c, "payment");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //back button to return to cart page
        JButton back = new JButton("Back");
        back.setFont(new Font("SansSerif", Font.BOLD, 14));
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.addActionListener(e -> {
            try {
                card.show(c, "cart");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //add checkbox, total price, and buttons into bottom section
        bottom.add(agree);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(total);
        bottom.add(Box.createVerticalStrut(10));
        bottom.add(confirm);
        bottom.add(Box.createVerticalStrut(5));
        bottom.add(back);

        //place bottom panel at the bottom of page
        add(bottom, BorderLayout.SOUTH);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //make a label + field row panel
    private JPanel makeRow(String labelText, JTextField field) {
        try {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(160, 30));
        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    
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
