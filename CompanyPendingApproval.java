import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//CompanyPendingApproval shows staff and speaker registration that are waiting for company approval or reject
//company can approve or reject the pending registration (speaker & staff) in this page

//page to show company pending approval list
public class CompanyPendingApproval extends BaseEventPage {
    private final CardLayout card;
    private final Container c;

    //constructor to set layout and load page
    public CompanyPendingApproval(CardLayout card, Container c) {
        super(card, c);
        this.card = card;
        this.c = c;
        setLayout(null);
        setBackground(Color.WHITE);
        refreshPage();
        setVisible(true);
        }
        
    //refresh page every time it becomes visible
    @Override
    public void setVisible(boolean aFlag) {
        try {
        if (aFlag) {
            refreshPage();
        }
        super.setVisible(aFlag);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //reload pending approval page and display all waiting users
    private void refreshPage() {
        try {
        removeAll();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;
        int screenHeight = screen.height;

        JPanel mainPage = new JPanel(null);
        mainPage.setBackground(new Color(243, 243, 246));
        mainPage.setBounds(0, 0, screenWidth, screenHeight);

        JPanel top = buildTopByRole();
        mainPage.add(top);

        //page title
        JLabel title = new JLabel("Pending Staff/Speaker Approval");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setBounds(80, 70, 600, 40);

        //get current company info
        String companyCid = LoginRegisterSystem.getCurrentUserId();
        String companyName = LoginRegisterSystem.getSavedUsername();

        //panel to list all pending users
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(243, 243, 246));

        //get pending users from system
        List<String[]> rows = LoginRegisterSystem.getPendingUsersForCompany(companyCid, companyName);
        //if no request, show empty message
        if (rows.isEmpty()) {
            JLabel empty = new JLabel("No pending request.");
            empty.setFont(new Font("SansSerif", Font.BOLD, 20));
            empty.setAlignmentX(LEFT_ALIGNMENT);
            listPanel.add(empty);
        } else {
            for (String[] part : rows) {  //loop each pending user data
                String pendingUid = part[4].trim();
                String role = part[3].trim();
                String username = part[2].trim();
                String profileId = part[7].trim();

                //create a label to show the pending speaker/staff role
                //show UID,username,role,company name,company id in the label
                JLabel item = new JLabel(new PendingUserRow(pendingUid, username, role, part[5].trim(), part[6].trim(), profileId).toString());
                item.setFont(new Font("Monospaced", Font.PLAIN, 13));
                item.setOpaque(true);// set opaque to show background color
                item.setBackground(Color.WHITE);
                //javax.swing.BorderFactory.createEmptyBorder(top, left, bottom, right) to set padding for the label
                item.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
                
                //create approve and reject button for each pending registration
                JButton approveBtn = new JButton("Approve");
            approveBtn.setBackground(new Color(73, 60, 255));
                approveBtn.setForeground(Color.WHITE);
                approveBtn.setBorderPainted(false);
                approveBtn.setFocusPainted(false);
                approveBtn.addActionListener(e -> {
                    try {
                    boolean ok = LoginRegisterSystem.approvePendingUser(pendingUid, companyCid, companyName);
                    if (ok) {
                        JOptionPane.showMessageDialog(null, "Approved.");
                        refreshPage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Approve failed.");
                    }
                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                //create reject button to decline pending user request
                JButton rejectBtn = new JButton("Reject");
            rejectBtn.setBackground(new Color(73, 60, 255));
                rejectBtn.setForeground(Color.WHITE);
                rejectBtn.setBorderPainted(false);
                rejectBtn.setFocusPainted(false);
                rejectBtn.addActionListener(e -> {  //reject user and refresh page after action
                    try {
                    boolean ok = LoginRegisterSystem.rejectPendingUser(pendingUid, companyCid, companyName);
                    if (ok) {
                        JOptionPane.showMessageDialog(null, "Rejected.");
                        refreshPage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Reject failed.");
                    }
                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                //create a panel to hold the approve and reject button, align them to the right of every row
                //FlowLayout(FlowLayout.RIGHT, 8, 0) to align buttons to the right with 8px horizontal gap and no vertical gap
                JPanel action = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                action.setOpaque(false);
                action.add(approveBtn);
                action.add(rejectBtn);

                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
                row.add(item, BorderLayout.CENTER);
                row.add(action, BorderLayout.EAST);

                listPanel.add(row);
                listPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        //add scroll panel to show all pending users list
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBounds(80, 130, screenWidth - 160, screenHeight - 250);
        scrollPane.setBorder(null);

        //create back button to return to main company page
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.RED);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setBounds(80, screenHeight - 100, 120, 40);
        backBtn.addActionListener(e -> {
            try {
                card.show(c, "mainCompany");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        mainPage.add(title);
        mainPage.add(scrollPane);
        mainPage.add(backBtn);
        add(mainPage);

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
