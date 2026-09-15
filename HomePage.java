import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

//HomePage is the event browsing page for users and speakers
//it reads all approved events from event.txt and shows them as cards when users click an event card to view details and book tickets
//it extend BaseEventPage

// homepage for showing all events
// user can see available concert tickets
public class HomePage extends BaseEventPage {

    private final CardLayout card;
    private final Container c;

    public HomePage(CardLayout card, Container c) {
        
        super(card,c);
        this.card = card;
        this.c = c;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
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
        add(createEvents(), BorderLayout.CENTER);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // create event display area
    // show all available events in grid
    private JScrollPane createEvents() {
        try {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Available Events");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        ArrayList<Event> events = loadEventsFromFile();

        JPanel grid;
        // if no event found
        if (events.isEmpty()) {
            grid = new JPanel(new FlowLayout());
            JLabel empty = new JLabel("No approved events available yet.");
            empty.setFont(new Font("Arial", Font.BOLD, 18));
            empty.setForeground(Color.GRAY);
            grid.add(empty);
        } else {
            int cols = Math.min(3, events.size());
            int rows = (int) Math.ceil(events.size() / (double) cols);
            grid = new JPanel(new GridLayout(rows, cols, 30, 30));
            for (Event e : events) {
                grid.add(new EventCard(e, card, c));
            }
        }

        container.add(title, BorderLayout.NORTH);
        container.add(grid, BorderLayout.CENTER);
        return new JScrollPane(container);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //load APPROVED events from event.txt and the same file companies write to
    private ArrayList<Event> loadEventsFromFile() {
        try {
        ArrayList<Event> list = new ArrayList<>();
        File file = new File("event.txt");
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;
                
                String[] p = line.split("\\|");
                if (p.length < 20) 
                    continue;

                // only approved events shown
                String status = p[19].trim();
                if (!"APPROVED".equalsIgnoreCase(status)) 
                    continue;

                String name = p[0].trim();
                String intro = p[1].trim();
                String date = p[13].trim();
                String time = p[14].trim();
                String venue = safe(p, 21);
                String eventId = p[18].trim();

                //read Early Bird expiry date from field index 23
                //"-" means no Early Bird, or field not present in older records
                String earlyBirdExpiry = safe(p, 23);
                String capacity = safe(p, 22);

                if ("-".equals(capacity)) 
                    capacity = "0";
                String vipQty = safe(p, 11);
                if ("-".equals(vipQty))
                     vipQty = "0";

                String imagePath = safe(p, 24);

                //map ticket tiers to Event price fields:
                //VIP (p10) → vipPrice
                //Early Bird (p8) → cat1Price  (same seats as standard, cheaper while expiry not passed)
                //Standard (p6)   → cat2Price
                String vipPrice  = formatPrice(safe(p, 10));
                String cat1Price = formatPrice(safe(p, 8));   //Early Bird price
                String cat2Price = formatPrice(safe(p, 6));   //Standard price
                String cat3Price = "-";
                String cat4Price = "-";
                String cat5Price = "-";

                //use uploaded image if provided; otherwise fall back to defaults
                String image = (imagePath != null && !imagePath.equals("-") && !imagePath.isEmpty())? imagePath : pickImage(p[2].trim(), list.size());

                list.add(new Event( name, date, time, venue, image, intro, vipPrice, cat1Price, cat2Price, cat3Price, cat4Price, cat5Price, eventId, earlyBirdExpiry, capacity, vipQty
                ));
            }
        
            } catch (IOException ignored) {}
        return list;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String safe(String[] arr, int index) {
        try {
        if (arr == null || index >= arr.length || arr[index] == null)
             return "-";

        String v = arr[index].trim();
        return v.isEmpty() ? "-" : v;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // format price with RM
    // ensure all prices look same
    private String formatPrice(String raw) {
        try {
        if (raw == null || raw.isEmpty() || "-".equals(raw)) 
            return "-";
        // already has RM or is just a number
        return raw.startsWith("RM") ? raw : "RM" + raw;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String pickImage(String type, int index) {
        try {
        //map to available event images
        String[] images = {"concert.jpg","conference.jpg","workshop.jpg"};
        if (type.toLowerCase().contains("concert")) 
            return images[0];

        if (type.toLowerCase().contains("conference")) 
            return images[1];

        if (type.toLowerCase().contains("workshop")) 
            return images[2];
        
        return images[index % images.length];
    
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
