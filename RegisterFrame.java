import java.awt.*;
import javax.swing.*;

//RegisterFrame is the role selection page for new registrations
//New users choose their role (User, Speaker, Staff, Company,admin) before being sent to the registration form

public class RegisterFrame extends BaseScreen {
        private Image BgImage;
    public RegisterFrame(CardLayout card, Container c) {
        
        //initialize register selection page with full screen background
        super(card, c);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;
        int screenHeight = screen.height;
        setSize(screenWidth, screenHeight);//Window size
        this.setLayout(null);

        c.add(new RegisterUnified(card, c, "User"), "registerUser");
        c.add(new RegisterUnified(card, c, "Company"), "registerCompany");
        c.add(new RegisterUnified(card, c, "Staff"), "registerStaff");
        c.add(new RegisterUnified(card, c, "Speaker"), "registerSpeaker");
        c.add(new RegisterUnified(card, c, "Admin"), "registerAdmin");

        //Register_page for all user
        //background image
        BgImage = new ImageIcon("register.png").getImage();

        JPanel registerPage=new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                super.paintComponent(g);
                //draw background image stretched to fit the window
                g.drawImage(BgImage, 0, 0, getWidth(), getHeight(), this);
            
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        
            };

        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(new Font("Times New Roman",  Font.BOLD, 45));
        registerLabel.setForeground(Color.WHITE);

        JLabel msgGmailReg = new JLabel("Gmail:");
        msgGmailReg.setFont(new Font("SansSerif",  Font.BOLD,18));

        JLabel msgPasswordReg = new JLabel("Password:");
        msgPasswordReg.setFont(new Font("SansSerif",  Font.BOLD, 18));

        JLabel msgPasswordRegComfim = new JLabel("Confirm Password:");
        msgPasswordRegComfim.setFont(new Font("SansSerif",  Font.BOLD, 18));

        JLabel msgIdentity = new JLabel("Identity:");
        msgIdentity.setFont(new Font("SansSerif",  Font.BOLD, 18));

        String [] identity ={"Please select","User","Company","Staff","Speaker"};
        JComboBox identityUser = new JComboBox<>(identity);


        //next button (go to next step)
        JButton buttonNext =new JButton("Next");
        buttonNext.setFont(new Font("SansSerif",  Font.BOLD, 14));
        buttonNext.setForeground(Color.WHITE);
        buttonNext.setBackground(new Color(30, 160, 80));
        buttonNext.setBorderPainted(false);
        buttonNext.setFocusPainted(false);


        //back button (return to login page)
        JButton buttonBack = new JButton("Back");
        buttonBack.setFont(new Font("SansSerif",  Font.BOLD, 14));
        buttonBack.setForeground(Color.WHITE);
        buttonBack.setBackground(Color.RED);
        buttonBack.setBorderPainted(false);
        buttonBack.setFocusPainted(false);

        JTextField registerEmail = new JTextField();
        JPasswordField registerPassword = new JPasswordField();
        JPasswordField registerPasswordComfim = new JPasswordField();

        registerPage.setLayout(null);

        JPanel registerPanel = new JPanel(null);
        registerPanel.setOpaque(true);
        registerPanel.setLayout(null);
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //padding around the content
        registerPanel.setBackground(new Color(255, 255, 255, 200)); 
        registerPanel.setBounds(750,175,550,475);

        registerLabel.setBounds(575, 45, 200, 60);
        msgGmailReg.setBounds(10, 50, 80, 30);
        registerEmail.setBounds(180, 50, 360, 40);
        msgPasswordReg.setBounds(10, 110, 120, 30);
        registerPassword.setBounds(180, 110, 360, 40);
        msgPasswordRegComfim.setBounds(10, 170, 170, 30);
        registerPasswordComfim.setBounds(180, 170, 360, 40);
        msgIdentity.setBounds(10, 220, 80, 30);
        identityUser.setBounds(180, 220, 360, 40);
        buttonNext.setBounds(180, 280, 360, 40);
        buttonBack.setBounds(180, 330, 360, 40);

        registerPage.add(registerLabel);
        registerPanel.add(msgGmailReg);
        registerPanel.add(registerEmail);
        registerPanel.add(msgPasswordReg);
        registerPanel.add(registerPassword);
        registerPanel.add(msgPasswordRegComfim);
        registerPanel.add(registerPasswordComfim);
        registerPanel.add(msgIdentity);
        registerPanel.add(identityUser);
        registerPanel.add(buttonNext);
        registerPanel.add(buttonBack);

        registerPage.add(registerPanel);

        registerPage.setBackground(Color.WHITE);
        registerPage.setBounds(0, 0, screenWidth, screenHeight);

        buttonNext.addActionListener(e->{
            try {
            String email = registerEmail.getText().trim();
            String password = new String(registerPassword.getPassword());
            String passwordComfim = new String(registerPasswordComfim.getPassword());
            String role = identityUser.getSelectedItem().toString();

            //hidden admin entry: keep dropdown on "Please select" and use admin password
            if ("Please select".equals(role)) {
                if ("ADMIN00@T".equals(password) && "ADMIN00@T".equals(passwordComfim)) {
                    role = "Admin";
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an identity.","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            boolean ok = LoginRegisterSystem.register_account(email, password, passwordComfim);
            if (!ok) {
                return;
            }

            if (role.equals("User")) {
                card.show(c, "registerUser");
            } else if (role.equals("Company")) {
                card.show(c, "registerCompany");
            } else if (role.equals("Staff")) {
                card.show(c, "registerStaff");
            } else if(role.equals("Speaker")){
                card.show(c,"registerSpeaker");
            } else if (role.equals("Admin")) {
                card.show(c, "registerAdmin");
            }

        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonBack.addActionListener(e->{
            try {
            card.show(c,"login");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });     

        this.add(registerPage);
    
        }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
