import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

//MainFrame is the home dashboard page for all roles:
//User, Speaker, Staff, Company, and Admin
//it shows different content depending on the logged-in role

public class MainFrame extends BaseMainFrame {
    private static final String EVENT_FILE = "event.txt";
    private final CardLayout card;
    private final Container c;

    public MainFrame(CardLayout card, Container c) {
        
        super(card, c);
        this.card = card;
        this.c = c;
        initMainFrame();
    
        }

    @Override
    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            initMainFrame();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }}

    @Override
    // Build the correct dashboard based on the user's role
    protected void buildMainPage() {
        try {
        setSize(screenWidth, screenHeight);
        setLayout(null);

        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) role = "";

        // Single main page that adapts layout/buttons based on role.
        JPanel mainPage = new JPanel(null);
        mainPage.setBackground(new Color(243, 243, 246));
        mainPage.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top;
        if ("Admin".equalsIgnoreCase(role)) {
            top = buildAdminTopBar();
        } else if ("Staff".equalsIgnoreCase(role)) {
            top = buildStaffTopBar();
        } else if ("Company".equalsIgnoreCase(role)) {
            top = buildCompanyTopBar();
        } else {
            top = buildUserTopBar("home");
        }

        JPanel hero = buildHero(role);
        JPanel featured = buildFeatured(role);

        mainPage.add(top);
        mainPage.add(hero);
        if (featured != null) {
            mainPage.add(featured);
        }

        add(mainPage);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel buildHero(String role) {
        try {
        //hero block text + primary action varies by role
        JPanel hero = new JPanel(null);
        hero.setBackground(new Color(58, 45, 200));
        hero.setBounds(0, 70, screenWidth, 420);

        // main title
        JLabel heroTitle = new JLabel("Experience Events Like Never Before");
        heroTitle.setFont(new Font("SansSerif", Font.BOLD, 64));
        heroTitle.setForeground(new Color(240, 244, 255));
        heroTitle.setHorizontalAlignment(SwingConstants.CENTER);
        heroTitle.setBounds(60, 92, screenWidth - 120, 78);

        // subtitle text
        JLabel heroSub = new JLabel("Discover, book, and manage conferences, concerts, and workshops with EventFlow.");
        heroSub.setFont(new Font("SansSerif", Font.PLAIN, 22));
        heroSub.setForeground(new Color(230, 230, 245));
        heroSub.setHorizontalAlignment(SwingConstants.CENTER);
        heroSub.setBounds(120, 210, screenWidth - 240, 64);

        // main button action
        JButton heroBtn = new JButton("Browse All Event !");
        heroBtn.setFocusPainted(false);
        heroBtn.setBorderPainted(false);
        heroBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        heroBtn.setBackground(Color.WHITE);
        heroBtn.setForeground(new Color(58, 45, 200));
        heroBtn.setBounds((screenWidth - 280) / 2, 300, 280, 52);
        
        if ("Staff".equalsIgnoreCase(role)) {
            heroSub.setText("Work with EventFlow!");
            heroBtn.setText("Enter The Ticket ID!");
            heroBtn.addActionListener(e -> {
                try {
                    go("staffVerify");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else if ("Company".equalsIgnoreCase(role)) {
            heroSub.setText("Earn money with EventFlow!");
            heroBtn.setText("Create New Event!");
            heroBtn.addActionListener(e -> {
                try {
                    card.show(c, "CreateEvent");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else if ("Admin".equalsIgnoreCase(role)) {
            heroTitle.setText("Manage All Events Like Never Avoid");
            heroSub.setText("Manage with EventFlow!");
            heroBtn.setText("Manage All Event!");
            heroBtn.addActionListener(e -> {
                try {
                    go("ManageEvent");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            heroBtn.addActionListener(e -> {
                try {
                    go("homePage");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        // add all hero components
        hero.add(heroTitle);
        hero.add(heroSub);
        hero.add(heroBtn);
        return hero;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private JPanel buildFeatured(String role) {
        try {

        JPanel featured = new JPanel(null);
        featured.setBackground(new Color(243, 243, 246));
        featured.setBounds(0, 490, screenWidth, Math.max(300, screenHeight - 490));

        // icon label
        JLabel featuredIcon = new JLabel("*");
        featuredIcon.setFont(new Font("SansSerif", Font.BOLD, 22));
        featuredIcon.setForeground(new Color(237, 170, 0));
        featuredIcon.setBounds(35, 36, 24, 24);

        // section title
        JLabel featuredTitle = new JLabel("Featured Events");
        featuredTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        featuredTitle.setForeground(new Color(8, 27, 59));
        featuredTitle.setBounds(70, 32, 300, 36);

        // view all button
        JButton featuredViewAll = new JButton("View all  ->");
        featuredViewAll.setFont(new Font("SansSerif", Font.BOLD, 14));
        featuredViewAll.setForeground(new Color(73, 60, 255));
        featuredViewAll.setBackground(new Color(243, 243, 246));
        featuredViewAll.setBorderPainted(false);
        featuredViewAll.setFocusPainted(false);
        featuredViewAll.setBounds(screenWidth - 165, 34, 130, 30);
        if ("Company".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role)) {
            featuredViewAll.addActionListener(e -> {
                try {
                    card.show(c, "ViewAllEvent");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            featuredViewAll.addActionListener(e -> {
                try {
                    go("homePage");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(243, 243, 246));

        //company sees its own events; others see approved events.
        List<String> rows = "Company".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role) ? loadEventsForCurrentAccount() : loadEventsForPreview();
        // if no events found
        if (rows.isEmpty()) {
            JLabel empty = new JLabel("No event");
            empty.setFont(new Font("SansSerif", Font.BOLD, 22));
            empty.setForeground(new Color(120, 126, 142));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(empty);
        } else {
            // display each event row
            for (String row : rows) {
                JLabel item = new JLabel(row);
                item.setFont(new Font("Monospaced", Font.PLAIN, 13));
                item.setOpaque(true);
                item.setBackground(Color.WHITE);
                item.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                item.setAlignmentX(Component.LEFT_ALIGNMENT);
                listPanel.add(item);
                listPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(35, 85, screenWidth - 70, Math.max(180, screenHeight - 600));
        scrollPane.setBorder(null);

        featured.add(featuredIcon);
        featured.add(featuredTitle);
        featured.add(featuredViewAll);
        featured.add(scrollPane);
        return featured;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }}

    // only show APPROVED events
    private List<String> loadEventsForPreview() {
        try {
        List<String> result = new ArrayList<>();
        File file = new File(EVENT_FILE);
        if (!file.exists()) {
            return result;
        }

        // read event file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 22) {
                    continue;
                }
                String status = parts[19].trim();
                if (!"APPROVED".equalsIgnoreCase(status)) {
                    continue;
                }

                String eventName = parts[0].trim();
                String date = parts[13].trim();
                String time = parts[14].trim();
                String location = parts[21].trim();
                String eventType = parts[2].trim();
                String eventId = parts.length >= 19 ? parts[18].trim() : "-";
                String reason = parts.length >= 21 ? parts[20].trim() : "-";
                String earlyPrice = parts[8].trim();
                String earlyQty = parts[9].trim();
                String standardPrice = parts[6].trim();
                String standardQty = parts[7].trim();
                String vipPrice = parts[10].trim();
                String vipQty = parts[11].trim();
                String companyName = parts[16].trim();
                String companyEmail = parts[15].trim();
                String companyId = parts[17].trim();
                String rowText = new EventRow(eventName, eventId, date, time, location, eventType,
                        EventRow.parsePriceOrZero(earlyPrice), EventRow.parseQtyOrZero(earlyQty),
                        EventRow.parsePriceOrZero(standardPrice), EventRow.parseQtyOrZero(standardQty),
                        EventRow.parsePriceOrZero(vipPrice), EventRow.parseQtyOrZero(vipQty),
                        companyName, companyEmail, companyId, status, reason).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }
        return result;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

        //load the data for event.txt by staff,company and admin
    private List<String> loadEventsForCurrentAccount() {
        try {
        List<String> result = new ArrayList<>();
        String role = LoginRegisterSystem.getSavedRole();
        String currentAccount = LoginRegisterSystem.getCurrentUserId();
        String companyCid = LoginRegisterSystem.getCompanyCidForCurrentUser();
        boolean filterByCompany = !"Admin".equalsIgnoreCase(role);

        if (currentAccount == null || currentAccount.trim().isEmpty()) {
            return result;
        }

        File file = new File(EVENT_FILE);
        if (!file.exists()) {
            return result;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 18) {
                    continue;
                }
                String ownerId = parts[17].trim();

                String status = parts.length >= 20 ? parts[19].trim() : "PENDING";
                if ("Admin".equalsIgnoreCase(role)) {
                    // Admin sees all statuses
                } else if ("Company".equalsIgnoreCase(role)) {
                if (filterByCompany) {
                    if (companyCid == null || companyCid.trim().isEmpty()) {
                        continue;
                    }
                    if (!companyCid.trim().equals(ownerId)) {
                        continue;
                    }
                } else {
                    if (!currentAccount.trim().equals(ownerId)) {
                        continue;
                    }
                }
                    // Company sees approved + pending + rejected
                } else if ("Staff".equalsIgnoreCase(role)) {
                if (filterByCompany) {
                    if (companyCid == null || companyCid.trim().isEmpty()) {
                        continue;
                    }
                    if (!companyCid.trim().equals(ownerId)) {
                        continue;
                    }
                } else {
                    if (!currentAccount.trim().equals(ownerId)) {
                        continue;
                    }
                }
                    // Staff sees approved only
                     if (!"APPROVED".equalsIgnoreCase(status)) {
                        continue;
                    }
                } else {
                    // Others see approved only
                     if (!"APPROVED".equalsIgnoreCase(status)) {
                        continue;
                    }
                }

                if (!"APPROVED".equalsIgnoreCase(status)) {
                    continue;
                }

                String eventName = parts[0].trim();
                String date = parts[13].trim();
                String time = parts[14].trim();
                String eventType = parts[2].trim();
                String eventId = parts.length >= 19 ? parts[18].trim() : "-";
                String reason = parts.length >= 21 ? parts[20].trim() : "-";
                String location = parts.length >= 22 ? parts[21].trim() : "-";
                String earlyPrice = parts[8].trim();
                String earlyQty = parts[9].trim();
                String standardPrice = parts[6].trim();
                String standardQty = parts[7].trim();
                String vipPrice = parts[10].trim();
                String vipQty = parts[11].trim();
                String companyEmail = parts[15].trim();
                String companyName = parts[16].trim();
                String companyId = parts[17].trim();

                String rowText = new EventRow(eventName, eventId, date, time, location, eventType,
                        EventRow.parsePriceOrZero(earlyPrice), EventRow.parseQtyOrZero(earlyQty),
                        EventRow.parsePriceOrZero(standardPrice), EventRow.parseQtyOrZero(standardQty),
                        EventRow.parsePriceOrZero(vipPrice), EventRow.parseQtyOrZero(vipQty),
                        companyName, companyEmail, companyId, status, reason).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }
        // Keep pending items on top, then approved, then rejected
        if ("Admin".equalsIgnoreCase(role) || "Company".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role)) {
            result.sort((a, b) -> Integer.compare(statusRank(extractStatus(a)), statusRank(extractStatus(b))));
        }
        return result;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private String extractStatus(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | Status: ";
        int start = rowText.indexOf(key);
        if (start < 0) {
            return "";
        }
        start += key.length();
        int end = rowText.indexOf(" | ", start);
        if (end < 0) {
            end = rowText.length();
        }
        return rowText.substring(start, end).trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private int statusRank(String status) {
            try {
        if ("PENDING".equalsIgnoreCase(status)) {
            return 0;
        }
        if ("APPROVED".equalsIgnoreCase(status)) {
            return 1;
        }
        if ("REJECTED".equalsIgnoreCase(status)) {
            return 2;
        }
        return 3;
    
            } catch (Exception ex) {
                ex.printStackTrace();
                return 0;
            }
        }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
