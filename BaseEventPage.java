import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//base class for all multi step event creation pages
//it provide share the top navigation bar and button helpers for all role and it can reduce the repeated code in each page and make the code clean and easy to maintain
//it extends BaseScreen(inheritance)
public class BaseEventPage extends BaseScreen {

    protected BaseEventPage(CardLayout card, Container c) {
        super(card, c);
        }

    //build the shared top bar for event pages (company role only)
    protected JPanel buildTopBar() {
        try {
        JPanel top = new JPanel(null);
        top.setBackground(Color.WHITE);
        top.setBounds(0, 0, screenWidth, 70);
        top.setPreferredSize(new Dimension(screenWidth, 70));

        //logo and brand
        //logo image  ||| try resources first, if not found then read logo.png from the run folder
        JLabel logo = new JLabel();
        try {
            ImageIcon rawIcon = null;
            java.net.URL url = getClass().getResource("/logo.png");
            if (url != null) {
                rawIcon = new ImageIcon(url);
            } else {
                java.io.File f = new java.io.File("logo.png");
                if (f.exists()) {
                    rawIcon = new ImageIcon("logo.png");
                }
            }
            if (rawIcon != null) {
                Image scaled = rawIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(scaled));
            } else {
                logo.setText("[]"); //show "[]" when no image
                logo.setForeground(new Color(73, 60, 255));
                logo.setFont(new Font("SansSerif", Font.BOLD, 22));
            }
        } catch (Exception ex) {
            //show "[]" when something goes wrong
            logo.setText("[]");
            logo.setForeground(new Color(73, 60, 255));
            logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        }
        logo.setBounds(34, 10, 50, 50);

        //creates system title (EventFlow) label
        //sets style and position
        JLabel brand = new JLabel("EventFlow");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(new Color(20, 30, 54));
        brand.setBounds(74, 18, 180, 28);

        int logoutX = screenWidth - 140;
        int dividerX = logoutX - 15;
        int dashboardX = dividerX - 130;
        int eventX = dashboardX - 110;
        int homeX = eventX - 105;

        //createTopButton is a helper method to create styled buttons with consistent and it come by BaseScreen,
        //and it can reduce the repeated code for creating buttons in each page and make the code clean and easy to maintain
        //createTopButton parameters: text, isBold, foregroundColor, xPosition, width
        JButton homeBtn = createTopButton("Home", true, new Color(73, 60, 255), homeX, 90);
        homeBtn.addActionListener(e -> {
            try {
                go("mainCompany");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton eventBtn = createTopButton("Events", false, new Color(28, 33, 44), eventX, 100);
        eventBtn.addActionListener(e -> {
            try {
                go("ViewAllEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton dashBtn = createTopButton("Dashboard", false, new Color(96, 108, 130), dashboardX, 120);
        dashBtn.addActionListener(e -> {
            try {
                go("dashboardCompany");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //log out button
        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
        logOutBtn.setOpaque(true);
        logOutBtn.setBounds(logoutX, 17, 110, 36);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.addActionListener(e -> {
            try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Confirm to log out? ", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
            //confirm the user want to log out before go back to LoginFrame, and it will not loss the data if user click log out
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            //if user click yes, then go back to the login page
                go("login");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //createNavDivider is the divider line between dashboard and log out button
        //it can make the top bar look more organize and clear
        //it come by BaseScreen, and it reduce repeared code for create divider
        JPanel divider = createNavDivider(dividerX);

        top.add(logo);
        top.add(brand);
        top.add(homeBtn);
        top.add(eventBtn);
        top.add(divider);
        top.add(dashBtn);
        top.add(logOutBtn);
        return top;
        //this return top panel with all the buttons and logo,
        //and it can using by all the event pages for company role to maintain a consistent navigation experience across the app
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
            //this return null is somethings get wrong when build the top bar, it can protect the app from crash and it will print the error message for debugging
        }
    }

    //share style button builder used by child pages
    protected JButton createTopButton(String text, boolean bold, Color foreground, int x, int width) {
        try {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", bold ? Font.BOLD : Font.PLAIN, 14));

        //"?" is a operator that return value based on the condition before it
        //":" is a operator that check true or false and return value based to the condition
        //if bold is true, then return Font.BOLD
        //if false, return Font.PLAIN. 
        //it can make the code more concise and clear

        button.setBackground(Color.WHITE);
        button.setForeground(foreground);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBounds(x, 16, width, 36);
        
        return button;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //shared top bar for user/speaker pages
    //active can be: "home", "profile", "browse"
    protected JPanel buildUserTopBar(String active) {
        try {
        JPanel top = new JPanel(null);
        top.setBackground(Color.WHITE);
        top.setBounds(0, 0, screenWidth, 70);
        top.setPreferredSize(new Dimension(screenWidth, 70));

        //logo image ||| try resources first, if not found then read logo.png from the run folder
        JLabel logo = new JLabel();
        try {
            ImageIcon rawIcon = null;//set icon and try to load from resources first
            java.net.URL url = getClass().getResource("/logo.png");
            if (url != null) {
                rawIcon = new ImageIcon(url);//lost from resource folder
            } else {
                java.io.File f = new java.io.File("logo.png");
                if (f.exists()) {
                    rawIcon = new ImageIcon("logo.png");
                }
            }
            if (rawIcon != null) {
                Image scaled = rawIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(scaled));
            } else {
                logo.setText("[]"); //show "[]" when no image
                logo.setForeground(new Color(73, 60, 255));
                logo.setFont(new Font("SansSerif", Font.BOLD, 22));
            }
        } catch (Exception ex) {
            //show "[]" when something goes wrong
            logo.setText("[]");
            logo.setForeground(new Color(73, 60, 255));
            logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        }
        logo.setBounds(34, 18, 30, 30);

        //creates system title (EventFlow)
        JLabel brand = new JLabel("EventFlow");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(new Color(20, 30, 54));
        brand.setBounds(84, 20, 180, 30);

        int navX = screenWidth - 500;

        JButton homeBtn = createTopButton("Home", true, new Color(73, 60, 255), navX, 90);
        homeBtn.addActionListener(e -> {
            try {
            String role = LoginRegisterSystem.getSavedRole();
            if ("Speaker".equalsIgnoreCase(role)) {
                go("mainSpeaker");
            } else {
                go("mainUser");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //creates profile button and handles click to open profile page
        JButton profileBtn = createTopButton("Profile", false, new Color(90, 90, 110), navX + 90, 170);
        profileBtn.addActionListener(e -> {
            try {
                go("SettingProfile");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel divider = new JPanel();
        divider.setBackground(new Color(231, 232, 238));
        divider.setBounds(navX + 275, 18, 1, 34);

        JButton browseBtn = createTopButton("Browse Events", false, new Color(90, 90, 110), navX + 285, 200);
        browseBtn.addActionListener(e -> {
            try {
                go("homePage");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        top.add(logo);
        top.add(brand);
        top.add(homeBtn);
        top.add(profileBtn);
        top.add(divider);
        top.add(browseBtn);
        return top;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //shared top bar for staff pages
    protected JPanel buildStaffTopBar() {
        try {
        JPanel top = new JPanel(null);
        top.setBackground(Color.WHITE);
        top.setBounds(0, 0, screenWidth, 70);
        top.setPreferredSize(new Dimension(screenWidth, 70));

        //logo image ||| try resources first, if not found then read logo.png from the run folder
        JLabel logo = new JLabel();
        try {
            ImageIcon rawIcon = null;//set icon and try to load from resources first
            java.net.URL url = getClass().getResource("/logo.png");
            if (url != null) {
                rawIcon = new ImageIcon(url);//load from resource folder
            } else {
                java.io.File f = new java.io.File("logo.png");//try to read from local file system if resource not found
                if (f.exists()) {
                    rawIcon = new ImageIcon("logo.png");
                }
            }
            //put the image to the label if load successful
            if (rawIcon != null) {
                //resize the image to 18x18 with smooth scaling
                Image scaled = rawIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(scaled));
            } else {
                logo.setText("[]"); //show "[]" when no image
                logo.setForeground(new Color(73, 60, 255));
                logo.setFont(new Font("SansSerif", Font.BOLD, 22));
            }
        } catch (Exception ex) {
            //show "[]" when something goes wrong
            logo.setText("[]");
            logo.setForeground(new Color(73, 60, 255));
            logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        }
        logo.setBounds(34, 18, 30, 30);

        JLabel brand = new JLabel("EventFlow");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(new Color(20, 30, 54));
        brand.setBounds(84, 20, 180, 30);

        int logoutX = screenWidth - 140;
        int dividerX = logoutX - 15;
        int enterX = dividerX - 130;
        int eventX = enterX - 110;
        int homeX = eventX - 105;

        //creates home button and handles click to go main page
        JButton homeBtn = createTopButton("Home", true, new Color(73, 60, 255), homeX, 90);
        homeBtn.addActionListener(e -> {
            try {
                go("mainStaff");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //creates event button and enter ticket ID button, handles navigation when clicked
        JButton eventBtn = createTopButton("Events", false, new Color(28, 33, 44), eventX, 100);
        eventBtn.addActionListener(e -> {
            try {
                go("ViewAllEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //creates enter ticket ID button and navigates to staff verification page when clicked
        JButton enterBtn = createTopButton("Enter Ticket ID", false, new Color(96, 108, 130), enterX, 120);
        enterBtn.addActionListener(e -> {
            try {
                go("staffVerify");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //log out button 
        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
        logOutBtn.setOpaque(true);
        logOutBtn.setBounds(logoutX, 17, 110, 36);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.addActionListener(e -> {
            try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Confirm to log out? ", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                go("login");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create top menu bar and add all buttons and logo
        //return menu bar, if error then return null
        JPanel divider = createNavDivider(dividerX);

        top.add(logo);
        top.add(brand);
        top.add(homeBtn);
        top.add(eventBtn);
        top.add(divider);
        top.add(enterBtn);
        top.add(logOutBtn);

        return top;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //share top bar for admin pages
    protected JPanel buildAdminTopBar() {
        try {
        JPanel top = new JPanel(null);
        top.setBackground(Color.WHITE);
        top.setBounds(0, 0, screenWidth, 70);
        top.setPreferredSize(new Dimension(screenWidth, 70));

        //Logo image (beginner way): try resources first, if not found then read logo.png from the run folder
        JLabel logo = new JLabel();
        try {
            ImageIcon rawIcon = null;
            java.net.URL url = getClass().getResource("/logo.png");
            if (url != null) {
                rawIcon = new ImageIcon(url);
            } else {
                java.io.File f = new java.io.File("logo.png");
                if (f.exists()) {
                    rawIcon = new ImageIcon("logo.png");
                }
            }
            if (rawIcon != null) {
                Image scaled = rawIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(scaled));
            } else {
                logo.setText("[]"); //show "[]" when no image
                logo.setForeground(new Color(73, 60, 255));
                logo.setFont(new Font("SansSerif", Font.BOLD, 22));
            }
        } catch (Exception ex) {
            //show "[]" when something goes wrong
            logo.setText("[]");
            logo.setForeground(new Color(73, 60, 255));
            logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        }
        logo.setBounds(34, 18, 30, 30);

        JLabel brand = new JLabel("EventFlow");
        brand.setFont(new Font("SansSerif", Font.BOLD, 22));
        brand.setForeground(new Color(20, 30, 54));
        brand.setBounds(84, 20, 180, 30);

        int logoutX = screenWidth - 140;
        int dividerX = logoutX - 15;
        int manageX = dividerX - 130;
        int eventX = manageX - 110;
        int homeX = eventX - 105;

        //create home button and go to admin main page when clicked
        JButton topHome = createTopButton("Home", true, new Color(73, 60, 255), homeX, 90);
        topHome.addActionListener(e -> {
            try {
                go("mainAdmin");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create report button and go to report page when clicked
        JButton topReport = createTopButton("Report", false, new Color(90, 90, 110), eventX, 100);
        topReport.addActionListener(e -> {
            try {
                go("reportAdmin");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create manage button and go to manage event page when clicked
        JButton topManage = createTopButton("Manage", false, new Color(90, 90, 110), manageX, 120);
        topManage.addActionListener(e -> {
            try {
                go("ManageEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create logout button and confirm before going back to login page
        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
        logOutBtn.setOpaque(true);
        logOutBtn.setBounds(logoutX, 17, 110, 36);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.addActionListener(e -> {
            try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Confirm to log out? ", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                go("login");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create divider and add all items to top bar, then return panel
        JPanel divider = new JPanel();
        divider.setBackground(new Color(231, 232, 238));
        divider.setBounds(dividerX, 18, 1, 34);

        top.add(logo);
        top.add(brand);
        top.add(topHome);
        top.add(topReport);
        top.add(topManage);
        top.add(divider);
        top.add(logOutBtn);
        return top;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //pick a top bar based on current role
    protected JPanel buildTopByRole() {
        try {
        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) 
            role = "";
        if ("Admin".equalsIgnoreCase(role)) {
            return buildAdminTopBar();
        }
        if ("Staff".equalsIgnoreCase(role)) {
            return buildStaffTopBar();
        }
        if ("Company".equalsIgnoreCase(role)) {
            return buildTopBar();
        }
        return buildUserTopBar("home");
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

        //create a divider line to make top bar more clean
        protected JPanel createNavDivider(int x) { 
        //this is create the divider,so it can make the top bar look like more organize and clear
        try {
        JPanel divider = new JPanel();
        divider.setBackground(new Color(231, 232, 238));
        divider.setBounds(x, 18, 1, 34);
        return divider;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
