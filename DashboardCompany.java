import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//DashboardCompany is the organizer dashboard page.
//it shows ticket sales stats, a bar chart, and a donut chart for the company's own events using data from bookings.txt

public class DashboardCompany extends BaseEventPage {
    //UI colors + data files for dashboard
    private static final Color BG = new Color(246, 247, 251);
    private static final Color WHITE = Color.WHITE;
    private static final Color BORDER = new Color(230, 232, 238);
    private static final Color TEXT_DARK = new Color(20, 30, 54);
    private static final Color TEXT_MID = new Color(83, 95, 122);
    private static final Color TEXT_LIGHT = new Color(145, 154, 175);
    private static final Color PURPLE = new Color(79, 66, 232);
    private static final Color BLUE = new Color(28, 134, 240);
    private static final Color GREEN = new Color(17, 188, 157);
    private static final Color ORANGE = new Color(246, 178, 39);
    private static final String EVENT_FILE = "event.txt";
    private static final String BOOKING_FILE = "bookings.txt";

    private final CardLayout card;
    private final Container c;

    //company dashboard constructor (setup screen and load UI)
    public DashboardCompany(CardLayout card, Container c) {
        super(card, c);
        this.card = card;
        this.c = c;

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screen.width, screen.height);
        setLayout(null);
        setBackground(BG);
        rebuild();
    
        }

    //refresh dashboard every time page is shown
    @Override
    public void setVisible(boolean aFlag) {
        try {
        //refresh data whenever user opens this page
        if (aFlag) {
            rebuild();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //rebuild dashboard so numbers are up to date
    private void rebuild() {
        try {
        removeAll();

        //screen size for absolute-position layout
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;
        int screenHeight = screen.height;

        //load latest metrics before drawing widgets
        DashboardStats stats = loadDashboardStats();

        //main dashboard containe
        JPanel page = new JPanel(null);
        page.setBackground(BG);
        page.setBounds(0, 0, screenWidth, screenHeight);
        page.add(buildTopByRole());

        JLabel title = new JLabel("Organizer Dashboard");
        title.setFont(new Font("SansSerif",  Font.BOLD, 44));
        title.setForeground(TEXT_DARK);
        title.setBounds(150, 110, 600, 60);
        page.add(title);

        //top action button
        JButton createEventBtn = new JButton("+ Create New Event");
        createEventBtn.setBounds(screenWidth - 330, 120, 180, 40);
        createEventBtn.setFont(new Font("SansSerif",  Font.BOLD, 14));
        createEventBtn.setForeground(WHITE);
        createEventBtn.setBackground(PURPLE);
        createEventBtn.setFocusPainted(false);
        createEventBtn.setBorderPainted(false);
        createEventBtn.addActionListener(e -> {
            try {
                card.show(c, "CreateEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        page.add(createEventBtn);

        int left = 150;
        int topCardsY = 190;
        int cardW = 285;
        int cardH = 165;
        int gap = 20;

        // Summary cards row.
        JPanel card1 = buildStatCard("Total Revenue",String.format(Locale.US, "RM %.2f", stats.totalRevenue),"Based on bookings.txt","$",new Color(219, 244, 227),new Color(18, 168, 84),new Color(18, 168, 84));
        card1.setBounds(left, topCardsY, cardW, cardH);
        page.add(card1);

        JPanel card2 = buildStatCard("Total Attendees",String.valueOf(stats.totalAttendees),"Paid/confirmed bookings","A",new Color(224, 239, 255),BLUE,BLUE);
        card2.setBounds(left + cardW + gap, topCardsY, cardW, cardH);
        page.add(card2);

        JPanel card3 = buildStatCard("Events Managed",String.valueOf(stats.eventsManaged),"Owned by this organizer","E",new Color(240, 228, 255),PURPLE,TEXT_LIGHT);
        card3.setBounds(left + (cardW + gap) * 2, topCardsY, cardW, cardH);
        page.add(card3);

        JPanel card4 = buildStatCard("Avg Occupancy",String.format(Locale.US, "%.1f%%", stats.avgOccupancy),"Attendees / total capacity","O",new Color(255, 239, 218),new Color(246, 114, 11),TEXT_LIGHT);
        card4.setBounds(left + (cardW + gap) * 3, topCardsY, cardW, cardH);
        page.add(card4);

        //left chart: show ticket sales for each event
        RoundedPanel barCard = new RoundedPanel(18, WHITE, BORDER);
        barCard.setLayout(null);
        barCard.setBounds(left, 375, 595, 420);

        JLabel barTitle = new JLabel("Ticket Sales by Event");
        barTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        barTitle.setForeground(TEXT_DARK);
        barTitle.setBounds(25, 20, 450, 40);
        barCard.add(barTitle);

        BarChartPanel barChart = new BarChartPanel(stats.eventLabels, stats.eventSalesByTier);
        barChart.setBounds(25, 80, 545, 310);
        barCard.add(barChart);
        page.add(barCard);

        //right chart: show attendee distribution
        RoundedPanel donutCard = new RoundedPanel(18, WHITE, BORDER);
        donutCard.setLayout(null);
        donutCard.setBounds(left + 625, 375, 595, 420);

        JLabel donutTitle = new JLabel("Attendees by Category");
        donutTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        donutTitle.setForeground(TEXT_DARK);
        donutTitle.setBounds(25, 20, 420, 40);
        donutCard.add(donutTitle);

        DonutChartPanel donutChart = new DonutChartPanel(stats.categoryCounts);
        donutChart.setBounds(25, 80, 545, 310);
        donutCard.add(donutChart);
        page.add(donutCard);

        //add everything to page and refresh
        add(page);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }}

    //load and calculate all dashboard numbers
    private DashboardStats loadDashboardStats() {
        try {
        DashboardStats stats = new DashboardStats();
        //current logged-in company UID
        String ownerId = LoginRegisterSystem.getCurrentUserId();
        if (isBlank(ownerId)) {
            return stats;
        }

        //events that belong to current company only
        Map<String, EventMeta> ownedEvents = loadOwnedEvents(ownerId);
        stats.eventsManaged = ownedEvents.size();
        if (ownedEvents.isEmpty()) {
            return stats;
        }

        //capacity denominator for occupancy
        for (EventMeta e : ownedEvents.values()) {
            stats.totalCapacity += Math.max(0, e.capacity);
        }

        //revenue + attendee + chart aggregations from bookings
        applyBookingStats(stats, ownedEvents);
        if (stats.totalCapacity > 0) {
            stats.avgOccupancy = (stats.totalAttendees * 100.0) / stats.totalCapacity;
        }
        //convert map data into arrays used by chart panel
        stats.rebuildChartData(ownedEvents);
        return stats;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //read company-owned events from event.txt
    private Map<String, EventMeta> loadOwnedEvents(String ownerId) {
        try {
        //read event.txt and keep only current organizer's events
        Map<String, EventMeta> result = new LinkedHashMap<>();
        File file = new File(EVENT_FILE);
        if (!file.exists()) 
            return result;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] p = line.split("\\|");
                if (p.length < 19) 
                    continue;

                String fileOwnerId = safe(p, 17);
                // Skip other companies' events.
                if (!ownerId.equals(fileOwnerId))
                     continue;

                String eventId = safe(p, 18);
                if (isBlank(eventId))
                     continue;

                EventMeta eventMeta = new EventMeta();
                eventMeta.eventId = eventId;
                eventMeta.eventName = safe(p, 0);
                eventMeta.eventType = safe(p, 2);
                eventMeta.standardPrice = parseDouble(safe(p, 6));
                eventMeta.earlyPrice = parseDouble(safe(p, 8));
                eventMeta.vipPrice = parseDouble(safe(p, 10));
                //new files use capacity field; old files fallback to ticket qty sum
                eventMeta.capacity = parseInt(safe(p, 22),
                        parseInt(safe(p, 7), 0) + parseInt(safe(p, 9), 0) + parseInt(safe(p, 11), 0));
                //use eventId as unique key
                result.put(eventId, eventMeta);
            }
        
            } catch (IOException ignored) {
            return result;
        }
        return result;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    //sum booking data into totals and chart buckets
    private void applyBookingStats(DashboardStats stats, Map<String, EventMeta> ownedEvents) {
        try {
        //read bookings.txt and aggregate revenue + attendee counts
        File file = new File(BOOKING_FILE);
        if (!file.exists()) 
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())  //skip empty line
                    continue;
                // Parse one booking row.
                BookingRecord record = parseBookingRecord(line.split("\\|"), ownedEvents);
                if (record == null || !record.active) 
                    continue;

                EventMeta event = ownedEvents.get(record.eventId);
                if (event == null)  //skip if event not found
                    continue;

                //core numeric totals
                stats.totalAttendees += record.qty;
                stats.totalRevenue += record.totalAmount;
                //chart data buckets
                stats.addTierSales(record.eventId, record.ticketTier, record.qty);
                stats.addCategoryCount(event.eventType, record.qty);
            }
        
            } catch (IOException ignored) {
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //parse booking row with tolerant rules (for different teammate formats)
    private BookingRecord parseBookingRecord(String[] parts, Map<String, EventMeta> ownedEvents) {
        try {
        if (parts == null || parts.length == 0) 
            return null;

        BookingRecord record = new BookingRecord();
        record.active = true;
        record.qty = 1;
        record.ticketTier = "STANDARD";

        //preferred: strict format "eventId | email | seat | tier | amount | qty | status"
        if (parts.length >= 7) {
            String eventId = safe(parts, 0);
            if (!isBlank(eventId)) record.eventId = eventId;

            String tier = safe(parts, 3).toUpperCase(Locale.ROOT);
            if (tier.contains("EARLY")) 
                record.ticketTier = "EARLY";

            else if (tier.contains("VIP")) 
                record.ticketTier = "VIP";

            else if (tier.contains("STANDARD")) 
                record.ticketTier = "STANDARD";

            int qty = parseInt(safe(parts, 5), 1);
            record.qty = Math.max(1, qty);

            String status = safe(parts, 6).toUpperCase(Locale.ROOT);
            if (status.contains("CANCEL") || status.contains("REFUND")) 
                record.active = false;

            double price = parseDouble(safe(parts, 4));
            if (price > 0) 
                record.totalAmount = Math.max(0, price * record.qty);
        }

        //detect useful values from each token
        for (String raw : parts) {
            String token = raw == null ? "" : raw.trim();

            if (token.isEmpty()) 
                continue;
            String upper = token.toUpperCase(Locale.ROOT);

            if (ownedEvents.containsKey(token)) 
                record.eventId = token;

            if (upper.contains("EARLY")) 
                record.ticketTier = "EARLY";

            if (upper.contains("STANDARD")) 
                record.ticketTier = "STANDARD";

            if (upper.contains("VIP")) 
                record.ticketTier = "VIP";

            if (upper.contains("CANCEL") || upper.contains("REFUND")) 
                record.active = false;

            //avoid treating event IDs as qty; only accept small integers
            if (isInteger(token) && record.qty == 1) {
                int value = parseInt(token, 1);
                if (value >= 1 && value <= 500) 
                    record.qty = value;
            }

            if (isDecimal(token) && record.totalAmount == 0) 
                record.totalAmount = Math.max(0, parseDouble(token));

        }

        //event id is required to match booking to this company event
        if (isBlank(record.eventId)) 
            return null;

        EventMeta event = ownedEvents.get(record.eventId);

        //ignore bookings that do not belong to current company events
        if (event == null)
             return null;

        //if amount missing, estimate by ticket tier price
        if (record.totalAmount <= 0) {
            double unitPrice = event.standardPrice;
            if ("EARLY".equals(record.ticketTier))
                 unitPrice = event.earlyPrice;

            if ("VIP".equals(record.ticketTier))
                 unitPrice = event.vipPrice;
            record.totalAmount = Math.max(0, unitPrice * record.qty);
        }
        return record;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //create one summary card (title, value, hint text)
    private JPanel buildStatCard(String label, String value, String subText, String iconText, Color iconBg, Color iconColor, Color subTextColor) {
        try {
        //reusable card UI for main statistics
        RoundedPanel card = new RoundedPanel(16, WHITE, BORDER);
        card.setLayout(null);

        //title label (e.g. Total Revenue)
        JLabel Label = new JLabel(label);
        Label.setBounds(25, 30, 220, 25);
        Label.setForeground(TEXT_MID);
        Label.setFont(new Font("SansSerif",  Font.BOLD, 15));

        //icon background (small rounded box)
        RoundedPanel iconHolder = new RoundedPanel(10, iconBg, iconBg);
        iconHolder.setBounds(220, 25, 36, 36);
        iconHolder.setLayout(new BorderLayout());
        
        //icon text (symbol inside box)
        JLabel icon = new JLabel(iconText, SwingConstants.CENTER);
        icon.setForeground(iconColor);
        icon.setFont(new Font("SansSerif",  Font.BOLD, 18));
        iconHolder.add(icon, BorderLayout.CENTER);

        //main value (big number)
        JLabel Value = new JLabel(value);
        Value.setBounds(25, 74, 245, 40);
        Value.setForeground(TEXT_DARK);
        Value.setFont(new Font("SansSerif",  Font.BOLD, 28));

        JLabel SubText = new JLabel(subText);
        SubText.setBounds(25, 120, 250, 25);
        SubText.setForeground(subTextColor);
        SubText.setFont(new Font("SansSerif",  Font.BOLD, 13));

        card.add(Label);
        card.add(iconHolder);
        card.add(Value);
        card.add(SubText);
        return card;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //custom rounded panel with optinal border
    static class RoundedPanel extends JPanel {
        //shared rounded card look
        private final int radius;
        private final Color fill;
        private final Color stroke;

        RoundedPanel(int radius, Color fill, Color stroke) {
            
            this.radius = radius;
            this.fill = fill;
            this.stroke = stroke;
            setOpaque(false);
        
            }

        @Override
        protected void paintComponent(Graphics graphics) {
            try {
            //draw rounded rectangle background + border
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(fill);
            graphics2D.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            graphics2D.setColor(stroke);
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            graphics2D.dispose();
            super.paintComponent(graphics);
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    static class BarChartPanel extends JPanel {
        //custom stacked bar chart
        private final String[] labels;
        private final int[][] tierSales;
        private final Color[] tierColors = new Color[] {BLUE, GREEN, ORANGE};

        BarChartPanel(String[] labels, int[][] tierSales) {
            
            this.labels = labels == null ? new String[0] : labels;
            this.tierSales = tierSales == null ? new int[0][0] : tierSales;
            setOpaque(false);
        
            }

        @Override
        protected void paintComponent(Graphics graphics) {
            try {
            //use Graphics2D for better rendering
            super.paintComponent(graphics);
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int baseY = h - 45;
            //X-axis
            graphics2D.setColor(new Color(220, 223, 230));
            graphics2D.drawLine(20, baseY, w - 20, baseY);

            //empty state when no sales data
            if (labels.length == 0 || tierSales.length == 0) {
                graphics2D.setColor(TEXT_LIGHT);
                graphics2D.setFont(new Font("SansSerif",  Font.BOLD, 15));
                graphics2D.drawString("No booking data yet", 180, 150);
                graphics2D.dispose();
                return;
            }

            int max = 1;
            for (int[] row : tierSales) {
                int sum = 0;
                for (int v : row) sum += Math.max(0, v);
                //highest bar controls scale
                max = Math.max(max, sum);
            }

            //width for each event block
            int slotW = Math.max(70, (w - 60) / labels.length);
            int barW = Math.min(42, slotW - 20);

            for (int i = 0; i < labels.length; i++) {
                int x = 30 + i * slotW + (slotW - barW) / 2;
                int y = baseY;
                //draw EARLY/STANDARD/VIP segments as stacked blocks
                for (int t = 0; t < 3; t++) {
                    int value = (i < tierSales.length && t < tierSales[i].length) ? Math.max(0, tierSales[i][t]) : 0;
                    int bh = (int) ((baseY - 25) * (value / (double) max));
                    if (bh <= 0) 
                        continue;
                    graphics2D.setColor(tierColors[t]);
                    graphics2D.fillRoundRect(x, y - bh, barW, bh, 10, 10);
                    y -= bh;
                }
                //draw short label below each stacked bar
                graphics2D.setColor(TEXT_MID);
                graphics2D.setFont(new Font("SansSerif",  Font.PLAIN, 11));
                String label = labels[i] == null ? "" : labels[i];
                if (label.length() > 12) label = label.substring(0, 12) + "...";
                graphics2D.drawString(label, x - 8, baseY + 16);
            }
            graphics2D.dispose();
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //render dount chart
    static class DonutChartPanel extends JPanel {
        //custom donut chart for event categories
        private final int[] values;
        private final Color[] colors = new Color[] {BLUE, GREEN, ORANGE};
        private final String[] names = new String[] {"Conference", "Concert", "Workshop"};

        DonutChartPanel(int[] values) {
            
            this.values = values == null ? new int[] {0, 0, 0} : values;
            setOpaque(false);
        
            }

        @Override
        protected void paintComponent(Graphics graphics) {
            try {
            super.paintComponent(graphics);
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int total = 0;
            for (int v : values) total += Math.max(0, v);
            //empty state when no category counts.
            if (total <= 0) {
                graphics2D.setColor(TEXT_LIGHT);
                graphics2D.setFont(new Font("SansSerif",  Font.BOLD, 15));
                graphics2D.drawString("No attendee category data yet", 145, 150);
                graphics2D.dispose();
                return;
            }

            int size = Math.min(getWidth(), getHeight()) - 90;
            int x = 40;
            int y = 10;
            int start = 90;
            //draw category
            for (int i = 0; i < values.length && i < colors.length; i++) {
                int arc = (int) Math.round(values[i] * 360.0 / total);
                graphics2D.setColor(colors[i]);
                graphics2D.fillArc(x, y, size, size, start, -arc);
                start -= arc;
            }
            //inner white circle creates donut effect
            graphics2D.setColor(WHITE);
            graphics2D.fillOval(x + size / 4, y + size / 4, size / 2, size / 2);

            int ly = y + size + 20;
            //small legend
            for (int i = 0; i < names.length; i++) {
                graphics2D.setColor(colors[i]);
                graphics2D.fillRoundRect(x + i * 160, ly, 14, 14, 4, 4);
                graphics2D.setColor(TEXT_MID);
                graphics2D.setFont(new Font("SansSerif",  Font.PLAIN, 12));
                graphics2D.drawString(names[i] + " (" + values[i] + ")", x + 20 + i * 160, ly + 12);
            }
            graphics2D.dispose();
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    static class EventMeta {
        //event fields needed for dashboard calculation
        String eventId;
        String eventName;
        String eventType;
        int capacity;
        double earlyPrice;
        double standardPrice;
        double vipPrice;
    }

    static class BookingRecord {
        //parsed booking row used for aggregation
        String eventId;
        String ticketTier;
        int qty;
        double totalAmount;
        boolean active;
    }

    static class DashboardStats {
        //final numbers and chart data shown on screen
        double totalRevenue;
        int totalAttendees;
        int totalCapacity;
        int eventsManaged;
        double avgOccupancy;

        //data structures used for dashboard statistics and charts
    
        //store ticket sales per event (key = eventId, value = [standard, early, vip])
        final Map<String, int[]> tierSales = new LinkedHashMap<>();
        //store attendee counts by category (e.g. Workshop, Conference, Concert)
        final int[] categoryCounts = new int[] {0, 0, 0};
        //labels for bar chart (event names)
        String[] eventLabels = new String[0];
        //2D array for bar chart data (rows = events, columns = ticket tiers)
        int[][] eventSalesByTier = new int[0][0];

        void addTierSales(String eventId, String tier, int qty) {
            try {
            //[0]=EARLY, [1]=STANDARD, [2]=VIP
            int[] sales = tierSales.computeIfAbsent(eventId, k -> new int[] {0, 0, 0});
            if ("EARLY".equalsIgnoreCase(tier)) sales[0] += qty;
            else if ("VIP".equalsIgnoreCase(tier)) sales[2] += qty;
            else sales[1] += qty;
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        void addCategoryCount(String eventType, int qty) {
            try {
            //group attendee counts by 3 categories shown in donut chart
            String t = eventType == null ? "" : eventType.trim().toLowerCase(Locale.ROOT);
            if (t.contains("conference")) categoryCounts[0] += qty;
            else if (t.contains("concert")) categoryCounts[1] += qty;
            else categoryCounts[2] += qty;
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //convert tierSales map into arrays for chart rendering
        void rebuildChartData(Map<String, EventMeta> ownedEvents) {
            try { //if no data, reset chart arrays
            if (tierSales.isEmpty()) {
                eventLabels = new String[0];
                eventSalesByTier = new int[0][0];
                return;
            }
            //preserve insertion order so chart label order is stable
            List<String> labels = new ArrayList<>();
            List<int[]> values = new ArrayList<>();
            //loop through each event's sales data
            for (Map.Entry<String, int[]> entry : tierSales.entrySet()) {
                EventMeta e = ownedEvents.get(entry.getKey());
                String label = (e != null && !isBlank(e.eventName)) ? e.eventName : entry.getKey();  //use event name if available, otherwise fallback to eventId
                labels.add(label);
                values.add(entry.getValue());
            }
            eventLabels = labels.toArray(new String[0]);
            eventSalesByTier = values.toArray(new int[0][0]);
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //safe read for split array fields
    private static String safe(String[] arr, int index) {
        try {
        if (arr == null || index < 0 || index >= arr.length || arr[index] == null) return "";
        return arr[index].trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //parse int with fallback value
    private static int parseInt(String text, int fallback) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception ex) {
            return fallback;
        }
    }

    //parse decimal with fallback 0
    private static double parseDouble(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (Exception ex) {
            return 0.0;
        }
    }

    //check if token looks like integer
    private static boolean isInteger(String text) {
        try {
        if (isBlank(text)) 
            return false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (i == 0 && (ch == '-' || ch == '+')) 
                continue;
            if (!Character.isDigit(ch)) 
                return false;
        }
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //check if token looks like decimal number
    private static boolean isDecimal(String text) {
        try {
        if (isBlank(text)) 
            return false;

        boolean dot = false;
        for (int i = 0; i < text.length(); i++) {
            char chare = text.charAt(i);
            if (i == 0 && (chare == '-' || chare == '+')) 
                continue;
            
            if (chare == '.') {
                if (dot) 
                    return false;

                dot = true;
                continue;
            }
            if (!Character.isDigit(chare)) 
                return false;
        }
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
