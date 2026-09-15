import java.awt.*;
import javax.swing.*;

// LoginFrame is the main window of the entire application.
// It uses CardLayout to hold all pages and switch between them.
// The main() method here is the entry point that starts the whole program.

public class LoginFrame extends JFrame {
    private Image bgImage;

    public LoginFrame() {
        
        setTitle("EventFlow");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //get the screen size to set the window to full-screen.
        int screenWidth = screen.width;
        int screenHeight = screen.height;

        //full-screen window
        setSize(screenWidth, screenHeight);
        setLocation(0, 0);//set the location of the window to the top left part of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Ensure the app exit when the window is closed
        Container c = getContentPane(); //get the content pane to add components
        CardLayout card = new CardLayout(); 
        c.setLayout(card);//set the layout of the content pane to CardLayout, which allows us to stack multiple pages and show only one at a time

        /*
        Container is the main area of the JFrame where add components
        By setting layout to CardLayout, it allow us to stack multiple pages on top of each other and show only one at a time
        Each page is added the container with a unique key, and can switch between them using card.show() method with the corresponding key
        
        CardLayout is allow us to stack multiple pages on top of each other and show only one at a time.
        We can switch between them using the card.show() method with the unique keys we assign to each panel when we add them to the container
        This way, we can manage all the different screens of app within a single JFrame without needing to open new windows
         */

        //entry page for email/password + role selection
        c.add(new RegisterFrame(card, c), "register");
        c.add(new RegisterUnified(card, c, "User"), "registerUser");
        c.add(new RegisterUnified(card, c, "Company"), "registerCompany");
        c.add(new RegisterUnified(card, c, "Staff"), "registerStaff");
        c.add(new RegisterUnified(card, c, "Speaker"), "registerSpeaker");
        c.add(new RegisterUnified(card, c, "Admin"), "registerAdmin");

        //main entry for all roles
        c.add(new MainFrame(card, c), "mainUser");
        c.add(new MainFrame(card, c), "mainSpeaker");
        c.add(new MainFrame(card, c), "mainStaff");
        c.add(new MainFrame(card, c), "mainCompany");
        c.add(new MainFrame(card, c), "mainAdmin");

        //Speaker pages
        c.add(new Calendar(card, c), "Calendar");
        c.add(new SpeakerNote(card, c), "speakerNote"); 
        
        //User and Speaker pages
        c.add(new SettingProfile(card, c), "SettingProfile");
        c.add(new Profile(card, c), "profile");
        c.add(new HomePage(card, c), "homePage");

        // Booking flow pages
        c.add(new SeatSelectionPage(card, c), "seatSelection");
        c.add(new CartPage(card, c), "cart");
        c.add(new CustomerDetailsPage(card, c), "customerDetails");
        c.add(new PaymentPage(card, c), "payment");
        c.add(new TicketConfirmationPage(card, c), "ticketConfirmation");

        //Staff pages
        c.add(new StaffVerify(card, c), "staffVerify");

        //Company pages
        c.add(new DashboardCompany(card, c), "dashboardCompany");
        c.add(new CreateEvent(card, c), "CreateEvent");
        c.add(new PriceEvent(card, c), "PriceEvent");
        c.add(new DetailEvent(card, c), "DetailEvent");
        c.add(new CompanyPendingApproval(card, c), "companyPending");

        //Admin pages
        c.add(new ManageEvent(card, c), "ManageEvent");
        c.add(new ViewAllUser(card, c), "ViewAllUser");
        c.add(new ViewEvent(card, c), "ViewEvent");
        c.add(new EventDetailPage(card, c), "eventDetailAdmin");
        c.add(new ReportAdmin(card, c), "reportAdmin");

        //Staff,Company,and Admin pages
        c.add(new ViewAllEvent(card, c), "ViewAllEvent");

        //background image for login screen
        bgImage = new ImageIcon("login.png").getImage();

        JPanel loginPage = new JPanel() {
            
            //Override is inherit from JFrame, and it can reduce the risk of typos and ensure that we are correct override the method from the parent class
            @Override
            protected void paintComponent(Graphics picture) { //Graphics is the class that allow us to draw on the componet
                try {
                super.paintComponent(picture);
                // Draw background image stretched to fit the window.
                picture.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        
            };

        JLabel loginLabel = new JLabel("<html>Welcome to EventFlow!<br>           Manage events, book tickets,<br>\t explore conferences all in one place!</html>"); 
        //HTML allows us to use easy format the text into multiple lines and add spacing for better visual appeal
        //HTML = <html> is beginning of HTML code, and </html> is the end. And, <br> is line break, &nbsp; is not breaking space for adding extra spaces.
        loginLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
        loginLabel.setForeground(Color.WHITE);

        // homepage for showing all events 
        // user can see available concert tickets
        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(750, 250, 550, 475);
        loginPanel.setOpaque(true); //make the panel opaque to show the background color.
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //padding around the content.
        loginPanel.setBackground(new Color(255, 255, 255, 200)); //set background color with Hexadecimal RGBA (red,gree,blue,alpha) where level (0-255)
        loginPanel.setLayout(null);
        loginPage.add(loginPanel);

        JLabel msgGmail = new JLabel("Gmail        :");
        msgGmail.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel msgPassword = new JLabel("Password :");
        msgPassword.setFont(new Font("SansSerif", Font.BOLD, 18));

        JTextField loginMail = new JTextField();
        JPasswordField loginPassword = new JPasswordField();


        JButton buttonLogin = new JButton("Login");
        buttonLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonLogin.setBackground(new Color(73, 60, 255));
        buttonLogin.setForeground(Color.WHITE);
        buttonLogin.setBorderPainted(false);
        buttonLogin.setFocusPainted(false);
        buttonLogin.addActionListener(e -> {
            try {
            // collect the data in loginFrame , then route to the specific role main key
            String email = loginMail.getText().trim();
            String password = new String(loginPassword.getPassword());
            if (LoginRegisterSystem.user_login(email, password, "", "")) {
                String role = LoginRegisterSystem.getSavedRole();
                if ("User".equals(role)) {
                    card.show(c, "mainUser");
                } else if ("Speaker".equals(role)) {
                    card.show(c, "mainSpeaker");
                } else if ("Company".equals(role)) {
                    card.show(c, "mainCompany");
                } else if ("Staff".equals(role)) {
                    card.show(c, "mainStaff");
                } else if ("Admin".equals(role)) {
                    card.show(c, "mainAdmin");
                }
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        JButton buttonRegister = new JButton("Register");
        buttonRegister.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonRegister.setBackground(new Color(30, 160, 80));
        buttonRegister.setForeground(Color.WHITE);
        buttonRegister.setBorderPainted(false);
        buttonRegister.setFocusPainted(false);
        //go to registration page
        buttonRegister.addActionListener(e -> {
            try {
                card.show(c, "register");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //absolute layout for simple in this screen 
        loginPage.setLayout(null);
        loginLabel.setBounds(575, 0, 900, 250);
        msgGmail.setBounds(10, 50, 100, 30);
        loginMail.setBounds(140, 50, 360, 40);
        msgPassword.setBounds(10, 110, 120, 30);
        loginPassword.setBounds(140,110, 360, 40);
        buttonLogin.setBounds(140, 180, 360, 40);
        buttonRegister.setBounds(140, 230, 360, 40);

        loginPage.setBackground(Color.WHITE);
        loginPage.add(loginLabel);
        loginPanel.add(msgGmail);
        loginPanel.add(msgPassword);
        loginPanel.add(loginMail);
        loginPanel.add(loginPassword);
        loginPanel.add(buttonLogin);
        loginPanel.add(buttonRegister);
        c.add(loginPage, "login");

        card.show(c, "login");
        setVisible(true); //show the window after all components are added and layout are set up
    
        }

    }
