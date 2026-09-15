import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// BaseMainFrame extends BaseScreen. This is inheritance
//it provides the shared top navigation bar and helper methods that all main pages (admin, speaker, company) can reuse.

public class BaseMainFrame extends BaseScreen {
    protected BaseMainFrame() {
        super();
        //subclass(BaseMainFrame) will call parent class(BaseScreen) and use the function for parent class. It can reduce repeat code and easy to use go() function to switch page 
        }

    protected BaseMainFrame(CardLayout card, Container c) {
        super(card, c);
        }

    //child pages override this to build their own UI
    protected void buildMainPage() {
        try {
    //default main page is blank with top bar, child pages will override this method to add content below the top bar
        } catch (Exception ex) {
            ex.printStackTrace(); //"ex" is mean exception for catch any error that might be happen in the try block, and it will print out the error for debugging
        }
    }

    //shared refresh entry point for main frames,and child class cannot override it
    //final = cannot be change for the other child class
    protected final void initMainFrame() {
        try {
        removeAll();//clear all existing content (including top bar) before rebuilding
        buildMainPage();//build the main page content, when in child classes
        revalidate();//refresh the Layout after add new things
        repaint();//refresh the screen after add new things

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //create the white top bar on every main page
    protected JPanel createTopBar() {
        try {
        JPanel top = new JPanel(null);
        top.setBackground(Color.WHITE);
        top.setBounds(0, 0, screenWidth, 70);
        return top;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //add the EventFlow logo and brand name to the top bar
    protected void addBrand(JPanel top) {
        try {
        //Logo image  ||  try resources first, if not found then read logo.png from the run folder
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
                logo.setText("[]"); //fallback when no image
                logo.setForeground(new Color(73, 60, 255));
                logo.setFont(new Font("SansSerif",  Font.BOLD, 22));
            }
        } catch (Exception ex) {
            //fallback when something goes wrong
            logo.setText("[]");
            logo.setForeground(new Color(73, 60, 255));
            logo.setFont(new Font("SansSerif",  Font.BOLD, 22));
        }
        logo.setBounds(34, 10, 50, 50);

        JLabel brand = new JLabel("EventFlow");
        brand.setFont(new Font("SansSerif",  Font.BOLD, 22));
        brand.setForeground(new Color(20, 30, 54));
        brand.setBounds(84, 20, 180, 30);

        top.add(logo);
        top.add(brand);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //calculate the starting X for screen width
    protected int navStartX() {
        try {
        return screenWidth - 500;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    //create a style navigation button for the top bar
    protected JButton createTopButton(String text, boolean bold, Color fg, int x, int width) {
        try {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif",  bold ? Font.BOLD : Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(fg);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBounds(x, 16, width, 36);

        return button;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //create divider line for top bar
    protected JPanel createNavDivider(int x) {
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

    //shared top bar for user/speaker pages
    //active can be: "home", "profile", "browse"
    protected JPanel buildUserTopBar(String active) {
        try {
        JPanel top = createTopBar();
        addBrand(top);

        int navX = navStartX();
        JButton homeBtn = createTopButton("Home", true, new Color(73, 60, 255), navX, 90);
        homeBtn.addActionListener(e -> {
            try {
            String role = LoginRegisterSystem.getSavedRole();
            if ("Speaker".equalsIgnoreCase(role)) {
                go("mainSpeaker"); //"go()" is the function for the parent class(BaseScreen) and call it to switch the page when click the button
            } else {
                go("mainUser");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create profile button and go to profile page when clicked
        JButton profileBtn = createTopButton("Profile", false, new Color(90, 90, 110), navX + 90, 170);
        profileBtn.addActionListener(e -> {
            try {
                go("SettingProfile");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create divider and browse events button, then go to home page when clicked
        JPanel divider = createNavDivider(navX + 275);

        JButton browseBtn = createTopButton("Browse Events", false, new Color(90, 90, 110), navX + 285, 200);
        browseBtn.addActionListener(e -> {
            try {
                go("homePage");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

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

    //create staff top bar and set button positions for navigation
    protected JPanel buildStaffTopBar() {
        try {
        JPanel top = createTopBar();
        addBrand(top);//same top bar as user/speaker, but with different navigation buttons for staff
        int logoutX = screenWidth - 140;
        int dividerX = logoutX - 15;
        int enterX = dividerX - 130;
        int eventX = enterX - 110;
        int homeX = eventX - 105;

        //create home button and go to staff main page when clicked
        JButton homeBtn = createTopButton("Home", true, new Color(73, 60, 255), homeX, 90);
        homeBtn.addActionListener(e -> {
            try {
                go("mainStaff");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create events button and go to event list page when clicked
        JButton eventBtn = createTopButton("Events", false, new Color(28, 33, 44), eventX, 100);
        eventBtn.addActionListener(e -> {
            try {
                go("ViewAllEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create enter ticket id button and go to staff verify page when clicked
        JButton enterBtn = createTopButton("Enter Ticket ID", false, new Color(96, 108, 130), enterX, 120);
        enterBtn.addActionListener(e -> {
            try {
                go("staffVerify");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create logout button and ask user confirmation before going to login page
        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBounds(logoutX, 17, 110, 36);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
        logOutBtn.addActionListener(e -> {
            try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Confirm to log out? ", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                go("login");
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }});

        JPanel divider = createNavDivider(dividerX);

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

    //create company top bar and set navigation button positions
    protected JPanel buildCompanyTopBar() {
        try {
        JPanel top = createTopBar();
        addBrand(top);
        //navigation buttons
        int logoutX = screenWidth - 140;
        int dividerX = logoutX - 15;
        int dashboardX = dividerX - 130;
        int eventX = dashboardX - 110;
        int homeX = eventX - 105;

        //create home button and go to company main page when clicked
        JButton homeBtn = createTopButton("Home", true, new Color(73, 60, 255), homeX, 90);
        homeBtn.addActionListener(e -> {
            try {
                go("mainCompany");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create events button and go to event list page when clicked
        JButton eventBtn = createTopButton("Events", false, new Color(28, 33, 44), eventX, 100);
        eventBtn.addActionListener(e -> {
            try {
                go("ViewAllEvent");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create dashboard button and go to company dashboard page when clicked
        JButton dashBtn = createTopButton("Dashboard", false, new Color(96, 108, 130), dashboardX, 120);
        dashBtn.addActionListener(e -> {
            try {
                go("dashboardCompany");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBounds(logoutX, 17, 110, 36);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
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

        JPanel divider = createNavDivider(dividerX);

        top.add(homeBtn);
        top.add(eventBtn);
        top.add(divider);
        top.add(dashBtn);
        top.add(logOutBtn);
        return top;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //create admin top bar and set navigation positions
    protected JPanel buildAdminTopBar() {
        try {
        JPanel top = createTopBar();
        addBrand(top);

        int logoutX = screenWidth - 140;
        int dividerX = logoutX - 15;
        int manageX = dividerX - 130;
        int reportX = manageX - 110;
        int homeX = reportX - 105;

        //create home button and go to admin main page when clicked
        JButton topHome = createTopButton("Home", true, new Color(73, 60, 255), homeX, 90);
        topHome.addActionListener(e -> {
            try {
                go("mainAdmin");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create report button and go to admin report page when clicked
        JButton topReport = createTopButton("Report", false, new Color(90, 90, 110), reportX, 100);
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

        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.setBounds(logoutX, 17, 110, 36);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.setBackground(Color.RED);
        logOutBtn.setFocusPainted(false);
        logOutBtn.setBorderPainted(false);
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

        JPanel divider = createNavDivider(dividerX);
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

}
