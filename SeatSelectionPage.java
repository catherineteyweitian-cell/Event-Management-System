import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.*;

// Seat selection page
//seats generated based on capacity, starting from centre outwards
//VIP rows only shown when VIP tickets exist (qty > 0)
public class SeatSelectionPage extends BaseEventPage {

    private static final int MAX_PER_BOOKING = 10; // max tickets per booking
    private static final int SEATS_PER_ROW   = 30; // seats in each row
    private static final String[] ROW_LABELS = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ","BA","BB","BC","BD","BE","BF","BG","BH","BI","BJ","BK","BL","BM","BN","BO","BP","BQ","BR","BS","BT","BU","BV","BW","BX","BY","BZ","CA","CB","CC","CD","CE","CF","CG","CH","CI","CJ","CK","CL","CM","CN","CO","CP","CQ","CR","CS","CT","CU","CV","CW","CX","CY","CZ","DA","DB","DC","DD","DE","DF","DG","DH","DI","DJ","DK","DL","DM","DN","DO","DP","DQ","DR","DS","DT","DU","DV","DW","DX","DY","DZ","EA","EB","EC","ED","EE","EF","EG","EH","EI","EJ","EK","EL","EM","EN","EO","EP","EQ","ER","ES","ET","EU","EV","EW","EX","EY","EZ","FA","FB","FC","FD","FE","FF","FG","FH","FI","FJ","FK","FL","FM","FN","FO","FP","FQ","FR","FS","FT","FU","FV","FW","FX","FY","FZ"};

    private int maxSeats; // how many tickets the user wants
    private int selectedCount; // how many they have clicked
    private ArrayList<String> selectedList = new ArrayList<>();
    private HashSet<String>   soldSeats    = new HashSet<>();
    private Event  event;
    private JLabel qtyLabel;
    private boolean vipEnabled; 

    public SeatSelectionPage(CardLayout card, Container c) {
        
        super(card, c);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    
        }

    //called by NoticeDialog before showing this page
    public void loadData(Event event, int quantity) {
        try {
        this.event         = event;
        this.maxSeats      = quantity;
        this.selectedCount = 0;
        this.selectedList  = new ArrayList<>();
        this.soldSeats     = new HashSet<>();

        //only show VIP rows when VIP qty is set
        String vipQty = event != null ? event.getVipQty() : null;
        this.vipEnabled = vipQty != null && !vipQty.trim().isEmpty() && !"-".equals(vipQty.trim()) && !vipQty.trim().equals("0");

        loadSoldSeats();
        buildUI();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build the full page with header, stage, seat grid, and controls
    private void buildUI() {
        try {
        removeAll();
        setLayout(new BorderLayout());

        //top nav bar
        add(buildTopByRole(), BorderLayout.NORTH);

        //stage label
        JLabel stage = new JLabel("◀  S T A G E  ▶", SwingConstants.CENTER);
        stage.setFont(new Font("SansSerif", Font.BOLD, 20));
        stage.setOpaque(true);
        stage.setBackground(new Color(50, 50, 60));
        stage.setForeground(Color.WHITE);
        stage.setPreferredSize(new Dimension(720, 44));
        stage.setMaximumSize(new Dimension(720, 44));
        stage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel seatArea = new JPanel();
        seatArea.setLayout(new BoxLayout(seatArea, BoxLayout.Y_AXIS));
        seatArea.setBackground(new Color(245, 247, 250));
        seatArea.add(stage);

        //generate seats based on capacity
        int capacity = parseCapacity();
        int totalRows = (int) Math.ceil((double) capacity / SEATS_PER_ROW);
        totalRows = Math.min(totalRows, ROW_LABELS.length);

        //VIP = first 2 rows (if enabled), rest = standard
        int vipRows = vipEnabled ? Math.min(2, totalRows) : 0;

        int remaining = capacity;
        for (int r = 0; r < totalRows; r++) {
            int seatsThisRow = Math.min(SEATS_PER_ROW, remaining);
            remaining -= seatsThisRow;
            Color rowColor = r < vipRows ? new Color(200, 60, 60) : new Color(70, 130, 180);// VIP - Red : Std  = blue
            seatArea.add(buildRow(ROW_LABELS[r], seatsThisRow, rowColor));
        }

        JScrollPane seatScroll = new JScrollPane(seatArea);
        seatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        seatScroll.setBorder(null);

        JPanel content = new JPanel(new BorderLayout());
        content.add(buildLegend(), BorderLayout.WEST);
        content.add(seatScroll, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
        add(buildSouthBar(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //seat numbers generated from left to right
    //position order is left -> right, numbering is sequential 1, 2, 3, ...
    private int[] seatNumbersByPosition(int seats) {
        try {
        int[] byPos = new int[seats];
        for (int i = 0; i < seats; i++) {
            byPos[i] = i + 1;
        }
        return byPos;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //build one row of seat buttons
    private JPanel buildRow(String rowLetter, int seats, Color baseColor) {
        try {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        row.setBackground(new Color(245, 247, 250));

        //row label on the left
        JLabel lbl = new JLabel(rowLetter);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setPreferredSize(new Dimension(20, 22));
        row.add(lbl);

        int[] seatByPos = seatNumbersByPosition(seats);
        for (int i = 0; i < seatByPos.length; i++) {
            String seatId = rowLetter + seatByPos[i];
            JButton btn   = new JButton(seatId);
            btn.setPreferredSize(new Dimension(38, 26));
            btn.setFont(new Font("SansSerif", Font.PLAIN, 9));
            btn.setFocusPainted(false);

            if (soldSeats.contains(seatId)) {
                //sold — grey out and disable
                btn.setBackground(Color.DARK_GRAY);
                btn.setForeground(Color.WHITE);
                btn.setEnabled(false);
            } else {
                btn.setBackground(baseColor);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                //hover highlight
                btn.addMouseListener(new MouseAdapter() {
                    
                    public void mouseEntered(MouseEvent e) {
                        try {
                        if (btn.isEnabled())
                            btn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                    
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }}
                    public void mouseExited(MouseEvent e) {
                        try {
                        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                
                    });

                btn.addActionListener(e -> {
                    try {
                        toggleSeat(btn, seatId, baseColor);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            row.add(btn);
        }
        return row;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //toggle seat selected
    private void toggleSeat(JButton btn, String seatId, Color baseColor) {
        try {
        if (btn.getBackground() == Color.GRAY) {
            // Deselect
            btn.setBackground(baseColor);
            btn.setForeground(Color.WHITE);
            selectedCount--;
            selectedList.remove(seatId);
        } else {
            //select if under limit
            if (selectedCount < maxSeats) {
                btn.setBackground(Color.GRAY);
                btn.setForeground(Color.WHITE);
                selectedCount++;
                selectedList.add(seatId);
            } else {
                JOptionPane.showMessageDialog(null,
                    "You can only select " + maxSeats + " seat(s).");
            }
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //below bar: quantity controls and navigation buttons
    private JPanel buildSouthBar() {
        try {
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        south.setBackground(Color.WHITE);

        JLabel qtyText = new JLabel("Quantity:");
        qtyText.setFont(new Font("SansSerif", Font.BOLD, 14));

        //minus button (decrease quantity)
        JButton minus = new JButton("-");
        minus.setFont(new Font("SansSerif", Font.BOLD, 16));
        minus.setPreferredSize(new Dimension(45, 32));
        minus.setBackground(Color.RED);
        minus.setForeground(Color.WHITE);
        minus.setBorderPainted(false);
        minus.setFocusPainted(false);

        //current quantity display
        qtyLabel = new JLabel(String.valueOf(maxSeats));
        qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        qtyLabel.setPreferredSize(new Dimension(30, 32));

        JLabel maxHint = new JLabel("Max " + MAX_PER_BOOKING);
        maxHint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        maxHint.setForeground(new Color(200, 40, 40));

        //plus button (increase quantity
        JButton plus = new JButton("+");
        plus.setFont(new Font("SansSerif", Font.BOLD, 16));
        plus.setPreferredSize(new Dimension(45, 32));
        plus.setBackground(new Color(73, 60, 255));
        plus.setForeground(Color.WHITE);
        plus.setBorderPainted(false);
        plus.setFocusPainted(false);

        //back button (return to home page)
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            try {
                go("homePage");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //proceed button (go to checkout)
        JButton proceedBtn = new JButton("Proceed to Checkout");
        proceedBtn.setBackground(new Color(73, 60, 255));
        proceedBtn.setForeground(Color.WHITE);
        proceedBtn.setBorderPainted(false);
        proceedBtn.setFocusPainted(false);

        minus.addActionListener(e -> {
            try {
            if (maxSeats > 1) {
                if (selectedCount >= maxSeats) {
                    JOptionPane.showMessageDialog(null,
                        "Deselect a seat before reducing quantity.");
                    return;
                }
                maxSeats--;
                qtyLabel.setText(String.valueOf(maxSeats));
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        plus.addActionListener(e -> {
            try {
            if (maxSeats < MAX_PER_BOOKING) {
                maxSeats++;
                qtyLabel.setText(String.valueOf(maxSeats));
            } else {
                JOptionPane.showMessageDialog(null,
                    "Maximum " + MAX_PER_BOOKING + " tickets per booking.");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        proceedBtn.addActionListener(e -> {
            try {
            if (selectedList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select at least one seat.");
                return;
            }
            if (selectedCount != maxSeats) {
                JOptionPane.showMessageDialog(null,
                    "Please select exactly " + maxSeats + " seat(s).");
                return;
            }
            //pass data to cart
            for (Component comp : c.getComponents()) {
                if (comp instanceof CartPage) {
                    ((CartPage) comp).loadData(event, selectedList);
                    break;
                }
            }
            go("cart");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        south.add(qtyText);
        south.add(minus);
        south.add(qtyLabel);
        south.add(maxHint);
        south.add(plus);
        south.add(backBtn);
        south.add(proceedBtn);
        return south;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //legend panel on the left side
    private JPanel buildLegend() {
        try {
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
        legend.setBackground(new Color(245, 247, 250));
        legend.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //only show VIP legend when VIP is enabled
        if (vipEnabled) {
            legend.add(makeLegendRow(new Color(200, 60, 60), "VIP"));
        }
        legend.add(makeLegendRow(new Color(70, 130, 180), "Standard / Early Bird"));
        legend.add(makeLegendRow(Color.GRAY,"Selected"));
        legend.add(makeLegendRow(Color.DARK_GRAY, "Sold"));
        return legend;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private JPanel makeLegendRow(Color color, String text) {
        try {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        row.setBackground(new Color(245, 247, 250));
        JLabel box = new JLabel("  ");
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(20, 20));
        row.add(box);
        row.add(new JLabel(text));
        return row;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //read capacity from the event object, fallback to 0
    private int parseCapacity() {
        try {
        if (event == null) 
            return 0;
        try {
            return event.getCapacity();
        } catch (Exception ex) {
            return 0;
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    //load sold seats from bookings.txt for this event
    private void loadSoldSeats() {
        try {
        if (event == null || event.getEventId() == null) 
            return;
        String eventId = event.getEventId().trim();
        java.io.File file = new java.io.File("bookings.txt");
        if (!file.exists()) 
            return;

        try (java.io.BufferedReader r =new java.io.BufferedReader(new java.io.FileReader(file))) {
                    
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                String[] p = line.split("\\s*\\|\\s*");

                if (p.length < 7) 
                    continue;
                if (!eventId.equals(p[0].trim())) 
                    continue;
                String status = p[6].trim().toUpperCase();
                if (status.contains("CANCEL") || status.contains("REFUND")) 
                    continue;

                String seat = p[2].trim();
                if (!seat.isEmpty()) soldSeats.add(seat);
            }
        
        } catch (java.io.IOException ignored) {}
    
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
