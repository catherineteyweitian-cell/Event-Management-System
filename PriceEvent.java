import java.awt.*;
import javax.swing.*;


//Step 2 of event create: set ticket prices and quantities
//rule : early bird qty <= standard qty <= VIP qty
//VIP card hidden when VIP is not enabled
//it inherits top bar from BaseEventPage
public class PriceEvent extends BaseEventPage {

    //fields kept as instance vars so Next button can read them
    private JTextField standardPriceField;
    private JTextField standardQtyField;
    private JTextField earlyPriceField;
    private JTextField earlyQtyField;
    private JTextField vipPriceField;
    private JTextField vipQtyField;
    private JTextField giftQtyField;

    public PriceEvent(CardLayout card, Container c) {
        
        super(card, c);
        setLayout(null);
        setBackground(new Color(243, 243, 246));
        refreshCards();
    
        }

    //called from CreateEvent when moving to this step
    public void refreshCards() {
        try {
        removeAll();

        //main page container
        JPanel page = new JPanel(null);
        page.setBounds(0, 0, screenWidth, screenHeight);
        page.setBackground(new Color(243, 243, 246));

        //top navigation bar
        JPanel top = buildTopByRole();
        page.add(top);

        //page title
        JLabel title = new JLabel("Step 2 : Set Ticket Price and Quantity");
        title.setBounds(60, 75, 700, 34);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(new Color(20, 30, 54));
        page.add(title);

        //check which ticket types should be shown
        boolean showEarly = CreateEventSystem.hasEarlyTicket();
        boolean showVip   = CreateEventSystem.hasVipTicket();   //only show if enabled
        boolean showGift  = CreateEventSystem.hasGiftTicket();

        //count visible cards to centre them
        int cardCount = 1 + (showEarly ? 1 : 0) + (showVip   ? 1 : 0) + (showGift  ? 1 : 0);

        int cardW = 240;
        int cardH = 220;
        int gap   = 24;
        int totalW = cardCount * cardW + (cardCount - 1) * gap;
        int startX = Math.max(40, (screenWidth - totalW) / 2);
        int cardY  = 130;
        int idx    = 0;

        //Standard card — always shown
        JPanel stdCard = buildTicketCard("Standard",  CreateEventSystem.getStandardPrice(), CreateEventSystem.getStandardQty(), "standard", showGift);
        stdCard.setBounds(startX + idx++ * (cardW + gap), cardY, cardW, cardH);
        page.add(stdCard);

        //Early Bird card — only when enabled
        if (showEarly) {
            JPanel earlyCard = buildTicketCard("Early Bird", CreateEventSystem.getEarlyPrice(), CreateEventSystem.getEarlyQty(), "early", showGift);
            earlyCard.setBounds(startX + idx++ * (cardW + gap), cardY, cardW, cardH);
            page.add(earlyCard);
        }

        //VIP card — hidden when VIP not enabled
        if (showVip) {
            JPanel vipCard = buildTicketCard("VIP", CreateEventSystem.getVipPrice(), CreateEventSystem.getVipQty(), "vip", showGift);
            vipCard.setBounds(startX + idx++ * (cardW + gap), cardY, cardW, cardH);
            page.add(vipCard);
        }

        //gift card — only when enabled
        if (showGift) {
            JPanel giftCard = buildTicketCard("Gift", "-", CreateEventSystem.getGiftQty(), "gift", showGift);
            giftCard.setBounds(startX + idx * (cardW + gap), cardY, cardW, cardH);
            page.add(giftCard);
        }

        //hint label for quantity rules 
        JLabel hint = new JLabel( "Note: Early Bird qty ≤ Standard qty  |  Standard qty ≤ VIP qty");
        hint.setBounds(60, cardY + cardH + 400, 700, 24);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 13));
        hint.setForeground(new Color(120, 120, 130));
        page.add(hint);

        //back button to return to Step 1 (Create Event page)
        JButton backBtn = new JButton("← Back");
        backBtn.setBounds(60, cardY  + cardH + 400, 120, 38);
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            try {
                go("CreateEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        //add button to page
        page.add(backBtn);

        //next button to proceed to Step 3 (review page)
        JButton nextBtn = new JButton("Next →");
        nextBtn.setBounds(200, cardY + cardH + 400, 120, 38);
        nextBtn.setBackground(new Color(30, 160, 80));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setOpaque(true);
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        nextBtn.addActionListener(e -> {
            try {
            //read field values (use saved defaults if a field wasn't shown)
            String stdPrice = standardPriceField.getText().trim();
            String stdQty = standardQtyField.getText().trim();
            String ePriceStr = earlyPriceField  == null ? CreateEventSystem.getEarlyPrice() : earlyPriceField.getText().trim();
            String eQtyStr = earlyQtyField == null ? CreateEventSystem.getEarlyQty() : earlyQtyField.getText().trim();
            String vPriceStr = vipPriceField == null ? CreateEventSystem.getVipPrice() : vipPriceField.getText().trim();
            String vQtyStr = vipQtyField == null ? CreateEventSystem.getVipQty() : vipQtyField.getText().trim();
            String gQtyStr = giftQtyField == null ? "0" : giftQtyField.getText().trim();

            //save to SpeakerSystem — this validates format
            boolean ok = CreateEventSystem.saveStep2Pricing(stdPrice, stdQty, ePriceStr, eQtyStr, vPriceStr, vQtyStr, gQtyStr);
            if (!ok) 
                return;

            //price order check
            try {
                double stadandPrice = Double.parseDouble(stdPrice);
                if (showEarly) {
                    double earlyPrice = Double.parseDouble(ePriceStr);
                    if (earlyPrice > stadandPrice) {JOptionPane.showMessageDialog(null,"Early Bird price cannot exceed Standard price.","Price Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (showVip) {
                    double vipPrice = Double.parseDouble(vPriceStr);
                    if (stadandPrice > vipPrice) {
                        JOptionPane.showMessageDialog(null,"Standard price cannot exceed VIP price.","Price Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException ignored) {}

            //quantity order check 
            try {
                int standardQty = Integer.parseInt(stdQty);
                if (showEarly) {
                    int eQ = Integer.parseInt(eQtyStr);
                    if (eQ > standardQty) {
                        JOptionPane.showMessageDialog(null, "Early Bird quantity cannot exceed Standard quantity.", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (showVip) {
                    int vipQty = Integer.parseInt(vQtyStr);
                    if (standardQty > vipQty) {
                        JOptionPane.showMessageDialog(null, "Standard quantity cannot exceed VIP quantity.", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException ignored) {}

            //refresh detail page then navigate
            for (Component comp : c.getComponents()) {
                if (comp instanceof DetailEvent) {
                    ((DetailEvent) comp).refreshDetails();
                    break;
                }
            }
            go("DetailEvent");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        page.add(nextBtn);

        add(page);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build one ticket card with price and qty fields
    private JPanel buildTicketCard(String title, String price,String qty, String type, boolean showGift) {
         try {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220), 1));

        //accent strip at top
        JPanel accent = new JPanel();
        accent.setBounds(0, 0, 240, 6);
        accent.setBackground(typeColor(type));
        card.add(accent);

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLbl.setBounds(10, 16, 220, 26);
        card.add(titleLbl);

        //price row — gift has no price
        JLabel priceLbl = new JLabel("Price (RM):");
        priceLbl.setBounds(16, 60, 95, 24);
        card.add(priceLbl);

        JTextField priceField = new JTextField("gift".equals(type) ? "-" : price);
        priceField.setBounds(115, 58, 110, 28);
        priceField.setEditable(!"gift".equals(type));
        if ("gift".equals(type)) priceField.setBackground(new Color(235, 235, 235));
        card.add(priceField);

        //qty row
        JLabel qtyLbl = new JLabel("Quantity:");
        qtyLbl.setBounds(16, 106, 95, 24);
        card.add(qtyLbl);

        JTextField qtyField = new JTextField(qty);
        qtyField.setBounds(115, 104, 110, 28);
        card.add(qtyField);

        //wire up field references
        switch (type) {
            case "standard": 
            standardPriceField = priceField;
             standardQtyField = qtyField; 
             break;

            case "early":    
            earlyPriceField = priceField;
             earlyQtyField = qtyField;
              break;

            case "vip":      
            vipPriceField = priceField; 
            vipQtyField = qtyField; 
            break;

            case "gift":     
            giftQtyField = qtyField; 
            break;
        }

        return card;
    
} catch (Exception ex) {
ex.printStackTrace();
return null;
                                       }
                                    }

    //returns accent colour per ticket type.
    private Color typeColor(String type) {
        try {
        switch (type) {
            case "early":
                  return new Color(255, 165, 0);   // orange

            case "vip":
                    return new Color(200, 50, 50);   // red

            case "gift":
                   return new Color(30, 160, 80);   // green

            default:
                       return new Color(73, 60, 255);   // purple (standard)

        }
    
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
