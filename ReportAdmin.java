import java.awt.*;
import java.io.*;
import javax.swing.*;

//ReportAdmin is the admin report page
//it reads event.txt and users.txt, counts totals using ReportData,then displays summary cards and a breakdown table

//admin report page (system analytics dashboard)
public class ReportAdmin extends BaseMainFrame {

    private static final String EVENT_FILE = "event.txt";
    private static final String USER_FILE  = "users.txt";

    public ReportAdmin(CardLayout card, Container c) {
        
        super(card, c);
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

        JPanel top = buildAdminTopBar();

        JLabel pageTitle = new JLabel("System Report");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        pageTitle.setForeground(new Color(20, 30, 54));
        pageTitle.setBounds(60, 90, 400, 45);

        //load data using ReportData (getter/setter) 
        ReportData report = loadReportData();

        //stat cards row
        int cardW = 220;
        int cardH = 130;
        int gap   = 24;
        int startX = 60;
        int cardY  = 160;

        JPanel card1 = buildCard("Total Events", String.valueOf(report.getTotalEvents()),   new Color(220, 235, 255), new Color(73, 60, 255));
        JPanel card2 = buildCard("Approved", String.valueOf(report.getApprovedEvents()), new Color(210, 245, 220), new Color(30, 160, 80));
        JPanel card3 = buildCard("Pending", String.valueOf(report.getPendingEvents()),  new Color(255, 240, 210), new Color(200, 120, 0));
        JPanel card4 = buildCard("Rejected",        String.valueOf(report.getRejectedEvents()), new Color(255, 220, 220), new Color(200, 50, 50));
        JPanel card5 = buildCard("Total Users",     String.valueOf(report.getTotalUsers()),    new Color(230, 220, 255), new Color(100, 60, 200));

        card1.setBounds(startX, cardY, cardW, cardH);
        card2.setBounds(startX + (cardW + gap), cardY, cardW, cardH);
        card3.setBounds(startX + (cardW + gap) * 2, cardY, cardW, cardH);
        card4.setBounds(startX + (cardW + gap) * 3, cardY, cardW, cardH);
        card5.setBounds(startX + (cardW + gap) * 4, cardY, cardW, cardH);

        JLabel tableTitle = new JLabel("Event Status Breakdown");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        tableTitle.setForeground(new Color(20, 30, 54));
        tableTitle.setBounds(60, 320, 400, 30);

        //table rows: label + value
        String[][] rows = {
            { "Total Events",    String.valueOf(report.getTotalEvents())   },
            { "Approved Events", String.valueOf(report.getApprovedEvents()) },
            { "Pending Events",  String.valueOf(report.getPendingEvents())  },
            { "Rejected Events", String.valueOf(report.getRejectedEvents()) },
            { "Total Users",     String.valueOf(report.getTotalUsers())    },
        };

        int rowY = 365;
        for (String[] row : rows) {
            JPanel rowPanel = buildTableRow(row[0], row[1]);
            rowPanel.setBounds(60, rowY, 600, 44);
            main.add(rowPanel);
            rowY += 52;
        }

        //back button (navigate to admin main page)
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.RED);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(60, rowY + 20, 120, 38);
        //button action: go back to admin home
        backBtn.addActionListener(e -> {
            try {
                go("main_admin");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //add all dashboard components to main panel
        main.add(top);
        main.add(pageTitle);
        main.add(card1);
        main.add(card2);
        main.add(card3);
        main.add(card4);
        main.add(card5);
        main.add(tableTitle);
        main.add(backBtn);
        add(main);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }}

    //read files and fill ReportData using setters
    private ReportData loadReportData() {
        try {
        ReportData report = new ReportData();

        // Count events from event.txt
        int total = 0, approved = 0, pending = 0, rejected = 0;
        File eventFile = new File(EVENT_FILE);
        if (eventFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) 
                        continue;
                    String[] parts = line.split("\\|");
                    if (parts.length < 20) 
                        continue;
                    total++;

                    String status = parts[19].trim();
                    if (status.equalsIgnoreCase("APPROVED"))
                               approved++;
                    else if (status.equalsIgnoreCase("PENDING"))
                           pending++;
                    else if (status.equalsIgnoreCase("REJECTED"))
                          rejected++;
                }
            
                } catch (IOException ignored) {

            }
        }

        //use SETTERS to store data
        report.setTotalEvents(total);
        report.setApprovedEvents(approved);
        report.setPendingEvents(pending);
        report.setRejectedEvents(rejected);

        //count users from users.txt
        int totalUsers = 0;
        File userFile = new File(USER_FILE);
        if (userFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) totalUsers++;
                }
            
                } catch (IOException ignored) {}
        }

        //use SETTER to store user count
        report.setTotalUsers(totalUsers);

        return report;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //build a summary stat card
    private JPanel buildCard(String label, String value, Color bg, Color accent) {
        try {
        JPanel card = new JPanel(null);
        card.setBackground(bg);
        card.setBorder(BorderFactory.createLineBorder(accent, 2));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("SansSerif", Font.BOLD, 38));
        valLabel.setForeground(accent);
        valLabel.setBounds(0, 20, 220, 50);

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblLabel.setForeground(new Color(60, 60, 60));
        lblLabel.setBounds(0, 75, 220, 30);

        card.add(valLabel);
        card.add(lblLabel);
        return card;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // build one table row (label | value)
    private JPanel buildTableRow(String label, String value) {
        try {
        JPanel row = new JPanel(null);
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel labelOnTable = new JLabel(label);
        labelOnTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelOnTable.setForeground(new Color(60, 60, 60));
        labelOnTable.setBounds(16, 10, 300, 24);

        JLabel valueOnTable = new JLabel(value);
        valueOnTable.setFont(new Font("SansSerif", Font.BOLD, 16));
        valueOnTable.setForeground(new Color(20, 30, 54));
        valueOnTable.setBounds(320, 10, 200, 24);

        row.add(labelOnTable);
        row.add(valueOnTable);
        return row;
    
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
