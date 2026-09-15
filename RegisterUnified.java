import java.awt.*;
import java.time.Year;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//RegisterUnified handles registration for all roles in one class
//it shows different input fields depending on the role chosen
//it extends BaseRegisterPanel to reuse shared form layout helpers

public class RegisterUnified extends BaseRegisterPanel {
    private Image BgImage;

    public RegisterUnified(CardLayout card, Container c, String role) {
        super(card, c);

        //get screen size
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth  = screen.width;
        int screenHeight = screen.height;
        setSize(screenWidth, screenHeight);
        setLayout(null);

        //background image — same as RegisterFrame
        BgImage = new ImageIcon("register.png").getImage();

        //background panel with image — same as RegisterFrame
        JPanel registerPage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                    //draw background image stretched to fill the window
                    g.drawImage(BgImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        registerPage.setLayout(null);
        registerPage.setBounds(0, 0, screenWidth, screenHeight);

        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setBounds(575, 45, 300, 60);
        registerPage.add(registerLabel);

        //same size and position as RegisterFrame
        JPanel registerPanel = new JPanel(null);
        registerPanel.setOpaque(true);
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        registerPanel.setBackground(new Color(255, 255, 255, 200));
        registerPanel.setBounds(750, 175, 550, 475);

        //layout constants inside the panel
        //same x positions as RegisterFrame fields
        int labelX = 10;   // label x
        int fieldX = 180;  // field x
        int fieldW = 360;  // field width
        int fieldH = 40;   // field height
        int rowGap = 60;   // space between rows
        int y      = 50;   // starting y inside panel

        //Declare all possible fields 
        JTextField nameField = null;
        JTextField companyField = null;
        JTextField companyCidField = null;
        JTextField workIdField = null;
        JTextField bioField = null;
        JTextField topicField = null;
        JPasswordField adminPassField  = null;

        //date combo boxes
        JComboBox<Integer> yearBox  = null;
        JComboBox<Integer> monthBox = null;
        JComboBox<Integer> dayBox   = null;


        if (role.equals("User")) {

            // Username row
            registerPanel.add(makeLabel("Username:", labelX, y, 160));
            nameField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(nameField);
            y += rowGap;

            // Date of birth row
            registerPanel.add(makeLabel("Date of Birth:", labelX, y, 160));
            yearBox  = makeYearBox (fieldX,       y, 110, fieldH);
            monthBox = makeMonthBox(fieldX + 120,  y, 110, fieldH);
            dayBox   = makeDayBox  (fieldX + 240,  y, 110, fieldH);
            registerPanel.add(yearBox);
            registerPanel.add(monthBox);
            registerPanel.add(dayBox);
            y += rowGap;

        } else if (role.equals("Company")) {

            // Company name row
            registerPanel.add(makeLabel("Company Name:", labelX, y, 160));
            companyField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(companyField);
            y += rowGap;

            // Company creation date row
            registerPanel.add(makeLabel("Date Created:", labelX, y, 160));
            yearBox  = makeYearBox (fieldX,       y, 110, fieldH);
            monthBox = makeMonthBox(fieldX + 120,  y, 110, fieldH);
            dayBox   = makeDayBox  (fieldX + 240,  y, 110, fieldH);
            registerPanel.add(yearBox);
            registerPanel.add(monthBox);
            registerPanel.add(dayBox);
            y += rowGap;

        } else if (role.equals("Staff")) {

            // Name row
            registerPanel.add(makeLabel("Your Name:", labelX, y, 160));
            nameField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(nameField);
            y += rowGap;

            // Date of birth row
            registerPanel.add(makeLabel("Date of Birth:", labelX, y, 160));
            yearBox  = makeYearBox (fieldX,       y, 110, fieldH);
            monthBox = makeMonthBox(fieldX + 120,  y, 110, fieldH);
            dayBox   = makeDayBox  (fieldX + 240,  y, 110, fieldH);
            registerPanel.add(yearBox);
            registerPanel.add(monthBox);
            registerPanel.add(dayBox);
            y += rowGap;

            // Company CID row
            registerPanel.add(makeLabel("Company CID:", labelX, y, 160));
            companyCidField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(companyCidField);
            y += rowGap;

            // Company name row
            registerPanel.add(makeLabel("Company Name:", labelX, y, 160));
            companyField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(companyField);
            y += rowGap;

            // Work ID row
            registerPanel.add(makeLabel("Work ID:", labelX, y, 160));
            workIdField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(workIdField);
            y += rowGap;

        } else if (role.equals("Speaker")) {

            // Name row
            registerPanel.add(makeLabel("Your Name:", labelX, y, 160));
            nameField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(nameField);
            y += rowGap;

            // Date of birth row
            registerPanel.add(makeLabel("Date of Birth:", labelX, y, 160));
            yearBox  = makeYearBox (fieldX,       y, 110, fieldH);
            monthBox = makeMonthBox(fieldX + 120,  y, 110, fieldH);
            dayBox   = makeDayBox  (fieldX + 240,  y, 110, fieldH);
            registerPanel.add(yearBox);
            registerPanel.add(monthBox);
            registerPanel.add(dayBox);
            y += rowGap;

            // Company CID row
            registerPanel.add(makeLabel("Company CID:", labelX, y, 160));
            companyCidField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(companyCidField);
            y += rowGap;

            // Company name row
            registerPanel.add(makeLabel("Company Name:", labelX, y, 160));
            companyField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(companyField);
            y += rowGap;

            // Speaker ID row
            registerPanel.add(makeLabel("Speaker ID:", labelX, y, 160));
            workIdField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(workIdField);
            y += rowGap;

            // Bio row
            registerPanel.add(makeLabel("Bio:", labelX, y, 160));
            bioField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(bioField);
            y += rowGap;

            // Session topic row
            registerPanel.add(makeLabel("Session Topic:", labelX, y, 160));
            topicField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(topicField);
            y += rowGap;

        } else if (role.equals("Admin")) {

            //username row
            registerPanel.add(makeLabel("Username:", labelX, y, 160));
            nameField = makeField(fieldX, y, fieldW, fieldH);
            registerPanel.add(nameField);
            y += rowGap;

            //date of birth row
            registerPanel.add(makeLabel("Date of Birth:", labelX, y, 160));
            yearBox  = makeYearBox (fieldX,       y, 110, fieldH);
            monthBox = makeMonthBox(fieldX + 120,  y, 110, fieldH);
            dayBox   = makeDayBox  (fieldX + 240,  y, 110, fieldH);
            registerPanel.add(yearBox);
            registerPanel.add(monthBox);
            registerPanel.add(dayBox);
            y += rowGap;

            //Admin password row
            registerPanel.add(makeLabel("Admin Password:", labelX, y, 160));
            adminPassField = new JPasswordField();
            adminPassField.setFont(new Font("SansSerif", Font.PLAIN, 16));
            adminPassField.setBounds(fieldX, y, fieldW, fieldH);
            registerPanel.add(adminPassField);
            y += rowGap;
        }

        //resize the white panel to fit all fields 
        //keep same width and x as RegisterFrame, only height changes
        registerPanel.setBounds(750, 175, 550, y + 120);

        //same style as RegisterFrame 
        JButton doneBtn = new JButton("Done");
        doneBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        doneBtn.setForeground(Color.WHITE);
        doneBtn.setBackground(new Color(30, 160, 80));
        doneBtn.setBorderPainted(false);
        doneBtn.setFocusPainted(false);
        doneBtn.setBounds(fieldX, y, fieldW, 40);
        registerPanel.add(doneBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.RED);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(fieldX, y + 50, fieldW, 40);
        registerPanel.add(backBtn);

        registerPage.add(registerPanel);

        //final copies needed for action listeners
        //Java requires variables used in lambdas to be effectively final
        final JTextField fName  = nameField;
        final JTextField fCompany = companyField;
        final JTextField fCid = companyCidField;
        final JTextField fWorkId = workIdField;
        final JTextField fBio = bioField;
        final JTextField fTopic = topicField;
        final JPasswordField fAdminPass = adminPassField;
        final JComboBox<Integer> fYear = yearBox;
        final JComboBox<Integer> fMonth = monthBox;
        final JComboBox<Integer> fDay = dayBox;

        //submit registration for this role 
        doneBtn.addActionListener(e -> {
            try {
                boolean ok = false;

                // Read date values (use 0 if date boxes were not shown)
                int year = (fYear  != null) ? (Integer) fYear.getSelectedItem()  : 0;
                int month = (fMonth != null) ? (Integer) fMonth.getSelectedItem() : 0;
                int day = (fDay   != null) ? (Integer) fDay.getSelectedItem()   : 0;

                if (role.equals("User")) {
                    //register a new User
                    ok = LoginRegisterSystem.register_user_details( getText(fName), year, month, day);

                } else if (role.equals("Company")) {
                    //register a new Company
                    ok = LoginRegisterSystem.register_company_details( getText(fCompany), year, month, day);

                } else if (role.equals("Staff")) {
                    //register a new Staff member
                    ok = LoginRegisterSystem.register_staff_details( getText(fName), year, month, day, getText(fCid), getText(fCompany), getText(fWorkId));

                } else if (role.equals("Speaker")) {
                    //register a new Speaker
                    ok = LoginRegisterSystem.register_speaker_details( getText(fName), year, month, day, getText(fCid), getText(fCompany), getText(fWorkId), getText(fBio), getText(fTopic));

                } else if (role.equals("Admin")) {
                    //register a new Admin
                    String adminPass = (fAdminPass != null) ? new String(fAdminPass.getPassword()).trim() : "";
                    ok = LoginRegisterSystem.register_admin_details( getText(fName), year, month, day, adminPass);
                }

                //when registration success,it will go back to login page
                if (ok) {
                    card.show(c, "login");
                }

            } catch (Exception ex) {
                //show error if something goes wrong
                ex.printStackTrace();
            }
        });

        //go back to role selection page
        backBtn.addActionListener(e -> {
            try {
                card.show(c, "register");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.add(registerPage);
    }

    private String getText(JTextField field) {
        if (field == null) 
            return "";
        return field.getText().trim();
    }

    // create a bold label — same font as RegisterFrame labels
    private JLabel makeLabel(String text, int x, int y, int w) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setBounds(x, y, w, 30);
        return label;
    }

    //create a standard text field
    private JTextField makeField(int x, int y, int w, int h) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBounds(x, y, w, h);
        return field;
    }

    //year combo box from 1900 to current year
    private JComboBox<Integer> makeYearBox(int x, int y, int w, int h) {
        JComboBox<Integer> box = new JComboBox<>();
        int currentYear = Year.now().getValue();
        for (int yr = 1900; yr <= currentYear; yr++) {
            box.addItem(yr);
        }
        box.setBounds(x, y, w, h);
        return box;
    }

    //month combo box 1 to 12
    private JComboBox<Integer> makeMonthBox(int x, int y, int w, int h) {
        JComboBox<Integer> box = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            box.addItem(m);
        }
        box.setBounds(x, y, w, h);
        return box;
    }

    //day combo box 1 to 31
    private JComboBox<Integer> makeDayBox(int x, int y, int w, int h) {
        JComboBox<Integer> box = new JComboBox<>();
        for (int d = 1; d <= 31; d++) {
            box.addItem(d);
        }
        box.setBounds(x, y, w, h);
        return box;
    }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
