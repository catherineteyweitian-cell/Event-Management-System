import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// EventCard is thumbnail card shown in the event browse grid and click it opens UserEventDetailPage
public class EventCard extends JPanel {

    private Event event;
    private CardLayout card;
    private Container c;

    //event card UI used to display event preview in listing pages
    public EventCard(Event event, CardLayout card, Container c) {
        
        this.event = event;
        this.card = card;
        this.c = c;

        //card layout setup
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 240));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        //event poster image
        JLabel poster = new JLabel();
        poster.setHorizontalAlignment(JLabel.CENTER);
        
        //load event image from file path
        ImageIcon icon = new ImageIcon(event.getImage());
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        poster.setIcon(new ImageIcon(img));

        //event name and date info below image
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        info.setBackground(Color.WHITE);

        JLabel title = new JLabel(event.getName());
        title.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel date = new JLabel(event.getDate() + " | " + event.getTime());
        date.setForeground(Color.GRAY);

        info.add(title);
        info.add(Box.createVerticalStrut(5));
        info.add(date);

        add(poster, BorderLayout.CENTER);
        add(info, BorderLayout.SOUTH);

        //Hover: blue border
        //click: open detail page
        addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
                try {
                setBorder(BorderFactory.createLineBorder(new Color(73, 60, 255), 2));
            
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void mouseExited(MouseEvent e) {
                try {
                setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            public void mouseClicked(MouseEvent e) {
                try {
                //rass card and container so booking flow can use CardLayout
                new UserEventDetailPage(event, card, c);
            
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        
            });
    
        }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
