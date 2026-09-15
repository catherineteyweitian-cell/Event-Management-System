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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//ViewAllUser shows all registered users for the admin.Admin can view user details and delete accounts if needed
//Staff can see same company speaker and staff only

public class ViewAllUser extends BaseEventPage {

    private static final String USER_FILE = "users.txt";
    private final CardLayout card;
    private final Container c;

    public ViewAllUser(CardLayout card, Container c) {
        
        super(card, c);
        this.card = card;
        this.c = c;

        setLayout(null);
        setBackground(Color.WHITE);
        refreshEvents();
        setVisible(true);

    
        }

    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            refreshEvents();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refreshEvents() {
        try {
        removeAll();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;
        int screenHeight = screen.height;

        JPanel mainPage = new JPanel(null);
        mainPage.setBackground(new Color(243, 243, 246));
        mainPage.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top = buildTopByRole();
        String role = LoginRegisterSystem.getSavedRole();

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif",  Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.RED);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(700,700,130,30);
        backBtn.addActionListener(e -> {
            try {
                if("Admin".equalsIgnoreCase(role)) {
                   card.show(c, "ManageEvent");
                } else if ("Staff".equalsIgnoreCase(role)) {
                    card.show(c, "ViewAllEvent");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        if("Admin".equalsIgnoreCase(role)) {
        //show admin-specific options
        JLabel userTitle = new JLabel("All Users:");
        userTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        userTitle.setBounds(80, 150, 300, 40);

        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(new Color(243, 243, 246));

        List<String> userRows = loadAllUsers();
        if (userRows.isEmpty()) {
            JLabel userEmpty = new JLabel("No user");
            userEmpty.setFont(new Font("SansSerif",  Font.BOLD, 22));
            userEmpty.setForeground(new Color(120, 126, 142));
            userEmpty.setAlignmentX(Component.LEFT_ALIGNMENT);
            userListPanel.add(userEmpty);
        } else {
            for (String row : userRows) {
                String uid = extractUid(row);
                String targetRole = extractRole(row);
                JLabel userItem = new JLabel(row);
                userItem.setFont(new Font("Monospaced",  Font.PLAIN, 14));
                userItem.setOpaque(true);
                userItem.setBackground(Color.WHITE);
                userItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                userItem.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED);
                deleteButton.setForeground(Color.WHITE);
                deleteButton.setBorderPainted(false);
                deleteButton.setFocusPainted(false);
                deleteButton.addActionListener(e -> {
                    try {
                    if (uid == null || uid.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid user UID.");
                        return;
                    }
                    String currentUid = LoginRegisterSystem.getCurrentUserId();
                    if (uid.equals(currentUid)) {
                        JOptionPane.showMessageDialog(null, "You cannot delete your own admin account.");
                        return;
                    }
                    if ("Admin".equalsIgnoreCase(targetRole)) {
                        JOptionPane.showMessageDialog(null, "You cannot delete other admin account.");
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog( null, "Delete this user? UID: " + uid, "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                    boolean ok = ManageSystem.deleteUserByUid(uid,role);
                    if (ok) {
                        refreshEvents();
                    } else {
                        JOptionPane.showMessageDialog(null, "Delete failed.");
                    }
                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                JPanel userRowPanel = new JPanel(new BorderLayout());
                userRowPanel.setBackground(Color.WHITE);
                userRowPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
                userRowPanel.add(userItem, BorderLayout.CENTER);
                userRowPanel.add(deleteButton, BorderLayout.EAST);

                userListPanel.add(userRowPanel);
                userListPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        JScrollPane userScrollPane = new JScrollPane(userListPanel);
        userScrollPane.setBounds(80, 200, screenWidth - 160, screenHeight - 420);
        userScrollPane.setBorder(null);

        mainPage.add(top);
        mainPage.add(userTitle);
        mainPage.add(userScrollPane);
        mainPage.add(backBtn);
        add(mainPage);

        } else if ("Staff".equalsIgnoreCase(role)) {
        //show staff-specific options
        JPanel staffListPanel = new JPanel();
        staffListPanel.setLayout(new BoxLayout(staffListPanel, BoxLayout.Y_AXIS));
        staffListPanel.setBackground(new Color(243, 243, 246));

        JLabel staffTitle = new JLabel("All Staff and Speaker:");
        staffTitle.setFont(new Font("SansSerif",  Font.BOLD, 28));
        staffTitle.setBounds(80, 150, 300, 40);

        List<String> staffRows = loadStaffForCurrentCompany();
        if (staffRows.isEmpty()) {
            JLabel staffEmpty = new JLabel("No staff or speaker");
            staffEmpty.setFont(new Font("SansSerif",  Font.BOLD, 22));
            staffEmpty.setForeground(new Color(120, 126, 142));
            staffEmpty.setAlignmentX(Component.LEFT_ALIGNMENT);
            staffListPanel.add(staffEmpty);
        } else {
            for (String row : staffRows) {
                JLabel staffItem = new JLabel(row);
                staffItem.setFont(new Font("Monospaced",  Font.PLAIN, 14));
                staffItem.setOpaque(true);
                staffItem.setBackground(Color.WHITE);
                staffItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                staffItem.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                staffListPanel.add(staffItem);
                staffListPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }


        JScrollPane staffScrollPane = new JScrollPane(staffListPanel);
        staffScrollPane.setBounds(80, 200, screenWidth - 160, screenHeight - 520);
        staffScrollPane.setBorder(null);

        mainPage.add(top);
        mainPage.add(staffTitle);
        mainPage.add(backBtn);
        mainPage.add(staffScrollPane);
       add(mainPage);
        }
        revalidate();
        repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

        private List<String> loadAllUsers() {
            try {
        List<String> result = new ArrayList<>();

        File file = new File(USER_FILE);
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
                if (parts.length < 5) {
                    continue;
                }

                String email = parts[0].trim();
                String name = parts[2].trim();
                String role = parts[3].trim();
                String uid = parts[4].trim();
                String companyName = parts.length >= 6 ? parts[5].trim() : "-";
                String workOrSpeakerId = parts.length >= 7 ? parts[6].trim() : "-";

                String rowText = new UserRow(name, role, email, uid, companyName, workOrSpeakerId).toString();
                result.add(rowText);
            }
        
            } catch (IOException e) {
            return result;
        }

        //keep a simple role order in admin user list
        result.sort((a, b) -> Integer.compare(roleRank(extractRole(a)), roleRank(extractRole(b))));
        return result;
    
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

    private String extractUid(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | UID: ";
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

    private String extractRole(String rowText) {
        try {
        if (rowText == null) {
            return "";
        }
        String key = " | Role: ";
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

    private int roleRank(String role) {
        try {
        if ("User".equalsIgnoreCase(role)) {
            return 0;
        }
        if ("Speaker".equalsIgnoreCase(role)) {
            return 1;
        }
        if ("Staff".equalsIgnoreCase(role)) {
            return 2;
        }
        if ("Company".equalsIgnoreCase(role)) {
            return 3;
        }
        if ("Admin".equalsIgnoreCase(role)) {
            return 4;
        }
        return 5;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

        private List<String> loadStaffForCurrentCompany() {
        try {
        List<String> result = new ArrayList<>();
        String role = LoginRegisterSystem.getSavedRole();
        String companyCid = LoginRegisterSystem.getCompanyCidForCurrentUser();
        boolean filterByCompany = !"Admin".equalsIgnoreCase(role);
        if (filterByCompany && (companyCid == null || companyCid.trim().isEmpty())) {
            return result;
        }

        File file = new File(USER_FILE);
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
                if (parts.length < 5) {
                    continue;
                }

                String rowRole = parts[3].trim();
                if (!"Staff".equalsIgnoreCase(rowRole) && !"Speaker".equalsIgnoreCase(rowRole)) {
                    continue;
                }

                String staffName = parts[2].trim();
                String staffUid = parts[4].trim();
                String workId = parts.length >= 7 ? parts[6].trim() : "-";
                String rowCompanyCid = "";
                if ("Staff".equalsIgnoreCase(rowRole)) {
                    rowCompanyCid = parts.length >= 8 ? parts[7].trim() : "";
                } else if ("Speaker".equalsIgnoreCase(rowRole)) {
                    rowCompanyCid = parts.length >= 10 ? parts[9].trim() : "";
                }

                if (filterByCompany && !companyCid.trim().equals(rowCompanyCid)) {
                    continue;
                }

                result.add("Name: " + safe(staffName, 22) + " | Work ID: " + safe(workId, 14) + " | UID: " + safeText(staffUid));
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

    private String safe(String value, int width) {
        try {
            String v = safeText(value);
            if (v.length() > width) {
                if (width <= 1) {
                    v = v.substring(0, width);
                } else {
                    v = v.substring(0, width - 1) + "…";
                }
            }
            return String.format("%-" + width + "s", v);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String safeText(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return "-";
            }
            return value.trim();
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
