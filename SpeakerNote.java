import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

//Speaker note page —speaker can write and view personal notes
//notes are saved to speaker_notes.txt, one line per note with timestamp
public class SpeakerNote extends BaseMainFrame {

    private static final String NOTE_FILE = "speaker_notes.txt";
    private static final Color PURPLE     = new Color(73, 60, 255);

    public SpeakerNote(CardLayout card, Container c) {
        super(card, c);
        initMainFrame();
        }

    @Override
    public void setVisible(boolean aFlag) {
        try {
        //refresh notes list every time page opens
        if (aFlag) initMainFrame();
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

        JPanel top = buildUserTopBar("home");

        main.add(top);

        JLabel titleLbl = new JLabel("My Notes");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLbl.setForeground(new Color(20, 30, 54));
        titleLbl.setBounds(60, 85, 300, 40);
        main.add(titleLbl);

        JLabel writeLbl = new JLabel("Write a new note:");
        writeLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        writeLbl.setBounds(60, 140, 200, 28);
        main.add(writeLbl);

        JTextArea noteInput = new JTextArea();
        noteInput.setLineWrap(true);
        noteInput.setWrapStyleWord(true);
        noteInput.setFont(new Font("SansSerif", Font.PLAIN, 15));
        noteInput.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JScrollPane inputScroll = new JScrollPane(noteInput);
        inputScroll.setBounds(60, 175, 700, 100);
        inputScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 214, 224)));
        main.add(inputScroll);

        JButton saveBtn = new JButton("Save Note");
        saveBtn.setBounds(60, 285, 130, 38);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setBackground(PURPLE);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        main.add(saveBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(205, 285, 100, 38);
        clearBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        clearBtn.setBackground(new Color(73, 60, 255));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBorderPainted(false);
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> {
            try {
                noteInput.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        main.add(clearBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(320, 285, 100, 38);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            try {
                go("SettingProfile");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        main.add(backBtn);

        JLabel savedLbl = new JLabel("Saved Notes:");
        savedLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        savedLbl.setBounds(60, 340, 200, 28);
        main.add(savedLbl);

        //notes list panel and rebuilt after each save
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(243, 243, 246));

        JScrollPane listScroll = new JScrollPane(listPanel);
        listScroll.setBounds(60, 375, screenWidth - 120, screenHeight - 430);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 214, 224)));
        main.add(listScroll);

        //load and display existing notes
        refreshNoteList(listPanel, listScroll);

        //save button action
        saveBtn.addActionListener(e -> {
            try {
            String text = noteInput.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Note cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saveNote(text);
            noteInput.setText(""); // clear input
            refreshNoteList(listPanel, listScroll);
            JOptionPane.showMessageDialog(null, "Note saved!");
        
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(main);
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //load notes from file and display each one as a card
    private void refreshNoteList(JPanel listPanel, JScrollPane scroll) {
        try {
        listPanel.removeAll();
        List<String> notes = loadNotes();

        if (notes.isEmpty()) {
            JLabel empty = new JLabel("  No notes yet. Write one above!");
            empty.setFont(new Font("SansSerif", Font.ITALIC, 14));
            empty.setForeground(new Color(150, 150, 160));
            empty.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));
            listPanel.add(empty);
        } else {
            //show newest first
            for (int i = notes.size() - 1; i >= 0; i--) {
                listPanel.add(buildNoteCard(notes.get(i)));
                listPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
        scroll.revalidate();
        scroll.repaint();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //build one note card widget
    private JPanel buildNoteCard(String rawLine) {
        try {
        // format stored: timestamp|||noteText
        String timestamp = "";
        String noteText  = rawLine;
        if (rawLine.contains("|||")) {
            String[] parts = rawLine.split("\\|\\|\\|", 2);
            timestamp = parts[0].trim();
            noteText  = parts.length > 1 ? parts[1].trim() : "";
        }

        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 214, 224)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setPreferredSize(new Dimension(700, 80));

        JLabel timeLbl = new JLabel(timestamp);
        timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        timeLbl.setForeground(new Color(150, 150, 160));
        timeLbl.setBounds(12, 8, 400, 18);
        card.add(timeLbl);

        JLabel textLbl = new JLabel("<html><body style='width:650px'>"  + noteText.replace("<", "&lt;") + "</body></html>");
        textLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLbl.setForeground(new Color(30, 30, 40));
        textLbl.setBounds(12, 28, 680, 44);
        card.add(textLbl);

        return card;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //save a note to speaker_notes.txt with timestamp
    private void saveNote(String text) {
        try {
        // Get current speaker's email/uid to tag the note
        String uid = LoginRegisterSystem.getCurrentUserId();
        if (uid == null) 
            uid = "unknown";

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        //format: uid | timestamp ||| note text
        String line = uid.trim() + " | " + timestamp + "|||" + text.trim();

        try (BufferedWriter w = new BufferedWriter(
                new FileWriter(NOTE_FILE, true))) {
                    
            w.write(line);
            w.newLine();
        
                    } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error saving note: " + e.getMessage());
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //load only notes belonging to the current speaker
    private List<String> loadNotes() {
        try {
        List<String> result = new ArrayList<>();
        String uid = LoginRegisterSystem.getCurrentUserId();
        if (uid == null) uid = "";

        File file = new File(NOTE_FILE);
        if (!file.exists()) return result;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                // Only show this speaker's notes
                if (line.startsWith(uid.trim())) {
                    // Strip the uid prefix, keep timestamp|||text
                    String withoutUid = line.substring(uid.trim().length()).trim();
                    if (withoutUid.startsWith("|")) {
                        withoutUid = withoutUid.substring(1).trim();
                    }
                    result.add(withoutUid);
                }
            }
        
            } catch (IOException ignored) {}
        return result;
    
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
