import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import javax.swing.*;

//Step 1: Create event and set the inform
//it inherits top bar from BaseEventPage
public class CreateEvent extends BaseEventPage {

    public CreateEvent(CardLayout card, Container c) {
        super(card, c);
        setLayout(null);
        setBackground(new Color(243, 243, 246));
        buildPage();
        }

    //build the Step 1 form with all input fields
    private void buildPage() {
        try {
        removeAll();

        JPanel page = new JPanel(null);
        page.setBounds(0, 0, screenWidth, screenHeight);
        page.setBackground(new Color(243, 243, 246));

        JPanel top = buildTopBar();
        page.add(top);

        //page title
        JLabel step1 = new JLabel("Step 1 : Create an Event");
        step1.setBounds(60, 75, 600, 34);
        step1.setFont(new Font("SansSerif", Font.BOLD, 30));
        step1.setForeground(new Color(20, 30, 54));
        page.add(step1);

        //event name input
        //left column: name, intro, speaker
        JLabel nameLbl = new JLabel("*Event Name:");
        nameLbl.setBounds(60, 325, 160, 28);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextField nameField = new JTextField();
        nameField.setBounds(220, 325, 480, 32);

        //event introduction input
        JLabel introLbl = new JLabel("*Introduction:");
        introLbl.setBounds(60, 370, 160, 28);
        introLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextArea introArea = new JTextArea();
        introArea.setFont(new Font("SansSerif",Font.PLAIN,16));
        introArea.setLineWrap(true);
        introArea.setWrapStyleWord(true);

        JScrollPane introScroll = new JScrollPane(introArea);
        introScroll.setBounds(220, 370, 480, 100);

        //speaker UID input
        JLabel speakerLbl = new JLabel("Speaker UID:");
        speakerLbl.setBounds(60, 475, 160, 28);
        speakerLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextField speakerField = new JTextField();
        speakerField.setBounds(220, 475, 480, 32);

        //image upload section
        JLabel imgLbl = new JLabel("Event Image:");
        imgLbl.setBounds(60, 525, 160, 28);
        imgLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel imgPathLbl = new JLabel("No image selected");
        imgPathLbl.setBounds(220, 525, 340, 28);
        imgPathLbl.setForeground(new Color(120, 120, 130));

        //store selected image path
        final String[] selectedImagePath = {""};

        JButton imgBtn = new JButton("Choose Image");
        imgBtn.setBounds(570, 525, 130, 32);
        imgBtn.setBackground(new Color(73, 60, 255));
        imgBtn.setForeground(Color.WHITE);
        imgBtn.setFocusPainted(false);
        imgBtn.setBorderPainted(false);
        imgBtn.setOpaque(true);
        imgBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        imgBtn.addActionListener(e -> {  //open file chooser to select image
            try {
            JFileChooser chooser = new JFileChooser();
            // Only allow image files
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File chosen = chooser.getSelectedFile();
                selectedImagePath[0] = chosen.getAbsolutePath();
                // Show short filename only
                imgPathLbl.setText(chosen.getName());
                imgPathLbl.setForeground(new Color(20, 30, 54));
            }
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //right column: date, time, venue, capacity, type, options
        JLabel dateLbl = new JLabel("*Event Date (YYYY-MM-DD):");
        dateLbl.setBounds(760, 325, 220, 28);
        dateLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextField dateField = new JTextField();
        dateField.setBounds(980, 325, 300, 32);

        //event time input
        JLabel timeLbl = new JLabel("*Event Time (HH:mm):");
        timeLbl.setBounds(760, 370, 200, 28);
        timeLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextField timeField = new JTextField();
        timeField.setBounds(980, 370, 300, 32);

        //venue input
        JLabel venueLbl = new JLabel("*Venue:");
        venueLbl.setBounds(760, 415, 200, 28);
        venueLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextField venueField = new JTextField();
        venueField.setBounds(980, 415, 300, 32);
        
        //capacity input
        JLabel capLbl = new JLabel("*Capacity:");
        capLbl.setBounds(760, 460, 200, 28);
        capLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextField capField = new JTextField();
        capField.setBounds(980, 460, 300, 32);

        //event type selection
        JLabel typeLbl = new JLabel("*Type of Event:");
        typeLbl.setBounds(760, 505, 200, 28);
        typeLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        String[] types = {"Please select", "Workshop", "Conference", "Concert"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(980, 505, 300, 32);

        //Ticket options
        JLabel optionLbl = new JLabel("Options:");
        optionLbl.setBounds(760, 550, 200, 28);
        optionLbl.setFont(new Font("SansSerif", Font.BOLD, 15));

        JCheckBox earlyChk = new JCheckBox("Early Bird Ticket");
        earlyChk.setBounds(760, 585, 180, 28);
        earlyChk.setBackground(new Color(243, 243, 246));

        JCheckBox vipChk = new JCheckBox("VIP Ticket");
        vipChk.setBounds(760, 610, 200, 28);
        vipChk.setBackground(new Color(243, 243, 246));

        JCheckBox giftChk = new JCheckBox("Gift for Attendees");
        giftChk.setBounds(760, 635, 200, 28);
        giftChk.setBackground(new Color(243, 243, 246));

        //Early Bird expiry — only shown when early bird is checked
        JLabel expiryLbl = new JLabel("Early Bird Expiry (YYYY-MM-DD):");
        expiryLbl.setBounds(975, 550, 260, 28);
        expiryLbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        //hide by default until checkbox is selected
        JTextField expiryField = new JTextField();
        expiryField.setBounds(975, 585, 200, 32);
        expiryLbl.setVisible(false);
        expiryField.setVisible(false);

        //show or hide expiry when early bird checkbox have been clicked
        earlyChk.addActionListener(e -> {
            try {
            expiryLbl.setVisible(earlyChk.isSelected());
            expiryField.setVisible(earlyChk.isSelected());
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create back button and go to company dashboard page when clicked
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(60, 700, 120, 38);
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setOpaque(true);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            try {
                go("dashboard_company");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //create next button to go to next step of event creation
        JButton nextBtn = new JButton("Next →");
        nextBtn.setBounds(950, 700, 120, 38);
        nextBtn.setBackground(new Color(30, 160, 80));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.setBorderPainted(false);
        nextBtn.setOpaque(true);
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        //when next button is clicked, validate event input before moving to next step
        nextBtn.addActionListener(e -> {
            try {
            String name = nameField.getText().trim();
            String intro = introArea.getText().trim();
            String type = typeBox.getSelectedItem().toString();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String venue = venueField.getText().trim();
            String capacity = capField.getText().trim();

            //event date cannot be in the past
            try {
                LocalDate eventDate = LocalDate.parse(date);
                if (eventDate.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(null,"Event date cannot be in the past.", "Invalid Date",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                //CreateEventSystem.event_create will catch format errors below
            }

            //set ticket options before validation
            CreateEventSystem.event_option(earlyChk.isSelected(), vipChk.isSelected(), giftChk.isSelected());

            // Validate early bird expiry if needed
            if (earlyChk.isSelected()) {
                String expiry = expiryField.getText().trim();
                if (expiry.isEmpty()) {
                    JOptionPane.showMessageDialog(null,"Please enter the Early Bird expiry date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            //validate all required fields
            boolean ok = CreateEventSystem.event_create(name, intro, type, date, time, venue, capacity);
            if (!ok) 
                return;

            //validate early bird expiry date
            boolean expiryOk = CreateEventSystem.setEarlyBirdExpiry(earlyChk.isSelected() ? expiryField.getText().trim() : "-");
            if (!expiryOk) 
                return;

            //save speaker UID if provided
            String speakerUid = speakerField.getText().trim();
            CreateEventSystem.setDraftSpeakerUid(speakerUid.isEmpty() ? "" : speakerUid);

            //save image path if selected
            if (!selectedImagePath[0].isEmpty()) {
                CreateEventSystem.setDraftImagePath(selectedImagePath[0]);
            }

            //refresh pricing page before showing it
            for (Component comp : c.getComponents()) {
                if (comp instanceof PriceEvent) {
                    ((PriceEvent) comp).refreshCards();
                    break;
                }
            }
            go("PriceEvent");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        page.add(nameLbl);
        page.add(nameField);
        page.add(introLbl);      
        page.add(introScroll);
        page.add(speakerLbl);    
        page.add(speakerField);
        page.add(imgLbl);        
        page.add(imgPathLbl);   
        page.add(imgBtn);
        page.add(dateLbl);      
        page.add(dateField);
        page.add(timeLbl);       
        page.add(timeField);
        page.add(venueLbl);      
        page.add(venueField);
        page.add(capLbl);        
        page.add(capField);
        page.add(typeLbl);       
        page.add(typeBox);
        page.add(optionLbl);
        page.add(earlyChk);      
        page.add(vipChk);       
        page.add(giftChk);
        page.add(expiryLbl);     
        page.add(expiryField);
        page.add(backBtn);       
        page.add(nextBtn);

        add(page);
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
