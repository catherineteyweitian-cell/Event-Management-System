import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

//Payment page is user/speaker select payment method and pay
//now a JPanel (CardLayout) instead of a JFrame popup
public class PaymentPage extends BaseEventPage {

    private CardLayout card;
    private Container c;
    private Event event;
    private double totalAmount;
    private String buyerName;
    private String buyerIc;
    private String buyerPhone;
    // ticket details list
    // each ticket: [name, ic, phone, seat]
    private ArrayList<String[]> ticketDetails; //each entry: [name, ic, phone, seat]

    public PaymentPage(CardLayout card, Container c) {
        super(card, c);
        this.card = card;
        this.c = c;
        }

    //called from CustomerDetailsPage
    public void loadData(Event event, double totalAmount, String buyerName, String buyerIc, String buyerPhone, ArrayList<String[]> ticketDetails) {
         try {
            this.event = event;
            this.totalAmount = totalAmount;
            this.buyerName = buyerName;
            this.buyerIc = buyerIc;
            this.buyerPhone = buyerPhone;
            this.ticketDetails = ticketDetails;
            buildUI();
    
        } catch (Exception ex) {
            ex.printStackTrace();
}
}

    // build payment UI
    private void buildUI() {
        try {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top bar (Requirement 1)
        add(buildTopByRole(), BorderLayout.NORTH);

        JLabel title = new JLabel("Payment Method", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(20, 30, 54));
        title.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // payment options
        JRadioButton tng = new JRadioButton("Touch 'n Go eWallet");
        JRadioButton boost  = new JRadioButton("Boost");
        JRadioButton wechat = new JRadioButton("WeChat Pay");
        JRadioButton bank   = new JRadioButton("Bank Transfer");
        JRadioButton credit = new JRadioButton("Credit Card");
        JRadioButton alipay = new JRadioButton("Alipay");
        
        tng.setFont(new Font("SansSerif", Font.PLAIN, 17));
        boost.setFont(new Font("SansSerif", Font.PLAIN, 17));
        wechat.setFont(new Font("SansSerif", Font.PLAIN, 17));
        bank.setFont(new Font("SansSerif", Font.PLAIN, 17));
        credit.setFont(new Font("SansSerif", Font.PLAIN, 17));
        alipay.setFont(new Font("SansSerif", Font.PLAIN, 17));

        ButtonGroup group = new ButtonGroup();
        group.add(tng); group.add(boost); group.add(wechat);
        group.add(bank); group.add(credit); group.add(alipay);

        JLabel total = new JLabel(String.format("Total Amount: RM %.2f", totalAmount));
        total.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel taxNote = new JLabel("Includes SST (6%)");
        taxNote.setFont(new Font("Arial", Font.PLAIN, 14));

        // pay button
        JButton pay = new JButton("Pay Now");
        pay.setFont(new Font("SansSerif", Font.BOLD, 16));
        pay.setBackground(new Color(73, 60, 255));
        pay.setForeground(Color.WHITE);
        pay.setBorderPainted(false);
        pay.setFocusPainted(false);
        pay.addActionListener(e -> {
            try {
            //check a payment method is selected
            if (!tng.isSelected() && !boost.isSelected() && !wechat.isSelected() && !bank.isSelected() && !credit.isSelected() && !alipay.isSelected()) {
                JOptionPane.showMessageDialog(null, "Please select a payment method.");
                return;
            }

            JOptionPane.showMessageDialog(null, "Payment Successful!");

            // go to confirmation page
            for (Component comp : c.getComponents()) {
                if (comp instanceof TicketConfirmationPage) {
                    ((TicketConfirmationPage) comp).loadData(event, buyerName, buyerIc, buyerPhone, ticketDetails);
                    break;
                }
            }
            card.show(c, "ticketConfirmation");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton back = new JButton("Back");
        back.setFont(new Font("SansSerif", Font.BOLD, 16));
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        pay.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        pay.setMaximumSize(new Dimension(260, 40));
        back.setMaximumSize(new Dimension(260, 40));
        back.addActionListener(e -> {
            try {
                card.show(c, "customerDetails");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JLabel select = new JLabel("Select Payment Method:");
        select.setFont(new Font("SansSerif", Font.BOLD, 17));
        select.setForeground(new Color(90, 90, 110));
        select.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(select);
        center.add(Box.createVerticalStrut(10));
        
        tng.setAlignmentX(Component.LEFT_ALIGNMENT);
        boost.setAlignmentX(Component.LEFT_ALIGNMENT);
        wechat.setAlignmentX(Component.LEFT_ALIGNMENT);
        bank.setAlignmentX(Component.LEFT_ALIGNMENT);
        credit.setAlignmentX(Component.LEFT_ALIGNMENT);
        alipay.setAlignmentX(Component.LEFT_ALIGNMENT);

        center.add(tng); center.add(boost); center.add(wechat);
        center.add(bank); center.add(credit); center.add(alipay);
        center.add(Box.createVerticalStrut(20));
        center.add(total);
        center.add(Box.createVerticalStrut(4));
        center.add(taxNote);
        center.add(Box.createVerticalStrut(20));
        center.add(pay);
        center.add(Box.createVerticalStrut(8));
        center.add(back);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setBackground(Color.WHITE);
        centerWrap.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerWrap.add(title, BorderLayout.NORTH);
        centerWrap.add(center, BorderLayout.CENTER);
        add(centerWrap, BorderLayout.CENTER);
        revalidate();
        repaint();
    
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
