import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

//Profile is the edit profile page for speakers and user
//it load data from speakers.txt when speaker open
//saving updates both speakers.txt and the name in users.txt.

public class Profile extends BaseMainFrame {
    private final CardLayout card;
    private final Container c;
    private static final String SPEAKERS_FILE = "speakers.txt";

    // Fields kept as instance variables so we can pre-fill them
    private JTextField nameField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField contactField;
    private JTextField emailField;
    private JTextField hobbyField;
    private JTextField regionField;
    private JTextField occupationField;
    private JTextField educationBackgroundField;
    private JTextArea introductionArea;

    //profile page (edit user details)
    public Profile(CardLayout card, Container c) {
        
        super(card, c);
        this.card = card;
        this.c = c;
        setLayout(new BorderLayout());
    
        }

    //refresh page every time it becomes visible
    @Override
    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            initMainFrame(); //rebuild page fresh each time
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void buildMainPage() {
        try {
        setSize(screenWidth, screenHeight);
        setLayout(null);

        JPanel main = new JPanel(null);
        main.setBackground(new Color(243, 243, 246));
        main.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top = buildUserTopBar("profile");

        JLabel paLabel = new JLabel("Edit Profile");
        paLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        paLabel.setBounds(700, 92, 320, 44);

        JLabel nameLabel = makeLabel("Name:"); // call function to build JLabel (text)
        JLabel ageLabel = makeLabel("Age (eg:18):");
        JLabel genderLabel = makeLabel("Gender (Male/Female):");
        JLabel contactLabel = makeLabel("Contact Number:");
        JLabel emailLabel = makeLabel("Email:");
        JLabel hobbyLabel = makeLabel("Hobby:");
        JLabel regionLabel = makeLabel("Region:");
        JLabel occupationLabel = makeLabel("Occupation:");
        JLabel educationLabel = makeLabel("Education Background:");
        JLabel introductionLabel = makeLabel("Introduction:");

        nameField = makeField(); // call function to build JTextField
        ageField = makeField();
        genderField = makeField();
        contactField = makeField();
        emailField  = makeField();
        hobbyField = makeField();
        regionField = makeField();
        occupationField = makeField();
        educationBackgroundField = makeField();
        introductionArea = new JTextArea();

        introductionArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        introductionArea.setLineWrap(true);
        introductionArea.setWrapStyleWord(true);

        JScrollPane introductionScroll = new JScrollPane(introductionArea);
        introductionScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 214, 224)));

        //load saved profile data into fields
        loadExistingProfile();

        String currentEmail = LoginRegisterSystem.getSavedEmail();
        if (currentEmail != null && !currentEmail.trim().isEmpty()) {
            emailField.setText(currentEmail.trim());
            emailField.setEditable(false);
        }

        //save and cancel buttons for profile update
        JButton buttonSave = new JButton("Save");
        buttonSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        buttonSave.setBackground(new Color(73, 60, 255));
        buttonSave.setForeground(Color.WHITE);
        buttonSave.setBorderPainted(false);
        buttonSave.setFocusPainted(false);

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setFont(new Font("SansSerif", Font.BOLD, 16));
        buttonCancel.setBackground(Color.RED);
        buttonCancel.setForeground(Color.WHITE);
        buttonCancel.setBorderPainted(false);
        buttonCancel.setFocusPainted(false);

        buttonSave.addActionListener(e -> {
            try {
            //get all the data and save it
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String gender = genderField.getText().trim();
            String contact = contactField.getText().trim();
            String email = LoginRegisterSystem.getSavedEmail();
            String hobby = hobbyField.getText().trim();
            String region = regionField.getText().trim();
            String occupation = occupationField.getText().trim();
            String educationBackground = educationBackgroundField.getText().trim();
            String introduction = introductionArea.getText().trim();

            //if email get wrong or no have , it will show error message
            if (email == null || email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cannot find current login email.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //validate speaker/user input before saving
            if (SpeakerSystem.speakers(name, age, gender, contact, email, hobby, region, occupation, educationBackground, introduction)) {
                String role = LoginRegisterSystem.getSavedRole(); //get role from LoginRegister.getSavedRole()
                boolean saved = false;
                if ("Speaker".equalsIgnoreCase(role)) {
                    saved = SpeakerSystem.upsertSpeakerProfileByEmail(email, name, age, gender, contact, hobby, region, occupation, educationBackground, introduction);
                    if (saved) {
                        LoginRegisterSystem.updateSpeakerName(LoginRegisterSystem.getCurrentUserId(), name);
                    }
                    go("SettingProfile");
                } else if ("User".equalsIgnoreCase(role)) {
                    saved = LoginRegisterSystem.updateUserProfileByEmail(email, name, age, gender, contact, hobby, region, occupation, educationBackground);
                    if (saved) {
                        go("main_u");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update user profile.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Profile update is only supported for Speaker or User.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonCancel.addActionListener(e -> {
            try {
            go("SettingProfile");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        nameLabel.setBounds(470, 160, 220, 34);
        ageLabel.setBounds(470, 208, 220, 34);
        genderLabel.setBounds(470, 256, 220, 34);
        contactLabel.setBounds(470, 304, 220, 34);
        emailLabel.setBounds(470, 352, 220, 34);
        hobbyLabel.setBounds(470, 400, 220, 34);
        regionLabel.setBounds(470, 448, 220, 34);
        occupationLabel.setBounds(470, 496, 220, 34);
        educationLabel.setBounds(470, 544, 260, 34);
        introductionLabel.setBounds(470, 592, 220, 34);

        nameField.setBounds(710, 160, 360, 36);
        ageField.setBounds(710, 208, 360, 36);
        genderField.setBounds(710, 256, 360, 36);
        contactField.setBounds(710, 304, 360, 36);
        emailField.setBounds(710, 352, 360, 36);
        hobbyField.setBounds(710, 400, 360, 36);
        regionField.setBounds(710, 448, 360, 36);
        occupationField.setBounds(710, 496, 360, 36);
        educationBackgroundField.setBounds(710, 544, 360, 36);
        introductionScroll.setBounds(710, 592, 360, 90);

        buttonCancel.setBounds(710, 700, 150, 42);
        buttonSave.setBounds(920, 700, 150, 42);

        main.add(top);
        main.add(paLabel);
        main.add(nameLabel);        
        main.add(nameField);
        main.add(ageLabel);         
        main.add(ageField);
        main.add(genderLabel);      
        main.add(genderField);
        main.add(contactLabel);     
        main.add(contactField);
        main.add(emailLabel);       
        main.add(emailField);
        main.add(hobbyLabel);       
        main.add(hobbyField);
        main.add(regionLabel);      
        main.add(regionField);
        main.add(occupationLabel);  
        main.add(occupationField);
        main.add(educationLabel);   
        main.add(educationBackgroundField);
        main.add(introductionLabel);
        main.add(introductionScroll);
        main.add(buttonSave);
        main.add(buttonCancel);

        add(main, BorderLayout.CENTER);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }}

    //read speakers.txt and fill in the fields
    private void loadExistingProfile() {
        try {
        String role = LoginRegisterSystem.getSavedRole();
        if ("User".equalsIgnoreCase(role)) {
            String currentEmail = LoginRegisterSystem.getSavedEmail();
            String[] profile = LoginRegisterSystem.getUserProfileByEmail(currentEmail);
            if (profile == null) {
                return;
            }
            if (profile.length >= 1) 
                nameField.setText(profile[0]); // In computer, it start from 0

            if (profile.length >= 2) 
                ageField.setText(profile[1]);

            if (profile.length >= 3) 
                genderField.setText(profile[2]);

            if (profile.length >= 4) 
                contactField.setText(profile[3]);

            if (profile.length >= 5) 
                emailField.setText(profile[4]);

            if (profile.length >= 6) 
                hobbyField.setText(profile[5]);

            if (profile.length >= 7) 
                regionField.setText(profile[6]);

            if (profile.length >= 8) 
                occupationField.setText(profile[7]);

            if (profile.length >= 9) 
                educationBackgroundField.setText(profile[8]);
            return;
        }

        //read speaker file and find the latest matched record by email
        File file = new File(SPEAKERS_FILE);
        if (!file.exists()) 
            return;

        //get current logged-in user's email
        String currentEmail = LoginRegisterSystem.getSavedEmail();
        String lastMatchedLine = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (currentEmail != null && !currentEmail.trim().isEmpty()) {
                    String[] parts = line.split("\\s*\\|\\s*");
                    if (parts.length >= 5 && currentEmail.trim().equalsIgnoreCase(parts[4].trim())) {
                        lastMatchedLine = line;
                    }
                } else {
                    lastMatchedLine = line;
                }
            }
        
            } catch (IOException e) {
            return;
        }

        if (lastMatchedLine == null)
             return;

        String[] parts = lastMatchedLine.split("\\s*\\|\\s*");
        if (parts.length >= 1) 
            nameField.setText(parts[0].trim());

        if (parts.length >= 2) 
            ageField.setText(parts[1].trim());

        if (parts.length >= 3) 
            genderField.setText(parts[2].trim());

        if (parts.length >= 4) 
            contactField.setText(parts[3].trim());

        if (parts.length >= 5) 
            emailField.setText(parts[4].trim());

        if (parts.length >= 6) 
            hobbyField.setText(parts[5].trim());

        if (parts.length >= 7) 
            regionField.setText(parts[6].trim());

        if (parts.length >= 8) 
            occupationField.setText(parts[7].trim());

        if (parts.length >= 9) 
            educationBackgroundField.setText(parts[8].trim());

        if (parts.length >= 10)
            introductionArea.setText(parts[9].trim());
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //create a standard label
    private JLabel makeLabel(String text) {
        try {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 17));
        return lbl;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //create a standard text field
    private JTextField makeField() {
        try {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return field;
    
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
