import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;


// SpeakerSystem handles speaker profile data.
// It validates profile fields and saves them to speakers.txt.
// Also manages speaker introductions stored in users.txt.

public class SpeakerSystem {
    private static final String SPEAKERS_FILE = "speakers.txt";
    private static String savedName = "";
    private static String savedAge = "";
    private static String savedGender = "";
    private static String savedContact = "";
    private static String savedEmail = "";
    private static String savedhobby = "";
    private static String savedRegion = "";
    private static String savedOccupation = "";
    private static String savededucationBackground = "";
    private static String savedIntroduction = "";

    public static boolean speakers (String name, String age, String gender, String contact, String email, String hobby, String region, String occupation, String educationBackground, String introduction) {
        try {
        if(name.isEmpty() || age.isEmpty() || gender.isEmpty() || contact.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(null, "Name, age, gender, Contact Number, and Email cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!email.contains("@gmail.com") || !email.contains(".")){
            JOptionPane.showMessageDialog(null, "Email format is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female")){
            JOptionPane.showMessageDialog(null, "Gender format is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        savedName = name;
        savedAge = age;
        savedGender = gender;
        savedContact = contact;
        savedEmail = email;
        savedhobby = hobby;
        savedRegion = region;
        savedOccupation = occupation;
        savededucationBackground = educationBackground;
        savedIntroduction = introduction;
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //write the speaker profile data to speakers.txt
    public static void saveSpeakerToFile(String name, String age, String gender, String contact, String email, String hobby, String region, String occupation, String educationBackground, String introduction){ //save speaker
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(SPEAKERS_FILE, true))) {
         //open writer
            String data = name + " | " + age + " | " + gender + " | " + contact + " | " + email + " | " + hobby + " | " + region + " | " + occupation + " | " + educationBackground + " | " + introduction; //build record
            writer.write(data); //write profile
            writer.newLine();
            JOptionPane.showMessageDialog(null,"Saved successfully!");
    
        }
    catch (IOException e){ //catch error
            JOptionPane.showMessageDialog(null,"Error saving profile: " + e.getMessage()); //show error
    }
    }

    public static boolean upsertSpeakerProfileByEmail(String currentEmail, String name, String age, String gender, String contact, String hobby, String region, String occupation, String educationBackground, String introduction) {
    try {
        if (isBlank(currentEmail)) {
            return false;
        }

        File src = new File(SPEAKERS_FILE);
        if (!src.exists()) {
            saveSpeakerToFile(name, age, gender, contact, currentEmail, hobby, region, occupation, educationBackground, introduction);
            return true;
        }

        File tmp = new File(SPEAKERS_FILE + ".tmp");
        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length >= 5 && currentEmail.trim().equalsIgnoreCase(parts[4].trim())) {
                    String data = name + " | " + age + " | " + gender + " | " + contact + " | " + currentEmail + " | " + hobby + " | " + region + " | " + occupation + " | " + educationBackground + " | " + introduction;
                    writer.write(data);
                    writer.newLine();
                    updated = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }

            if (!updated) {
                String data = name + " | " + age + " | " + gender + " | " + contact + " | " + currentEmail + " | " + hobby + " | " + region + " | " + occupation + " | " + educationBackground + " | " + introduction;
                writer.write(data);
                writer.newLine();
            }
        
                 } catch (IOException e) {
            return false;
        }

        if (!src.delete()) {
            return false;
        }
        if (!tmp.renameTo(src)) {
            return false;
        }
        return true;
    
     } catch (Exception ex) {
        ex.printStackTrace();
            return false;
         }
        }

    //checks if a string is blank or null.
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String getSpeakerIntroductionByEmail(String email) {
        try {
        if (isBlank(email)) {
            return "";
        }
        File file = new File(SPEAKERS_FILE);
        if (!file.exists()) {
            return "";
        }
        String lastMatchedLine = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length >= 5 && email.trim().equalsIgnoreCase(parts[4].trim())) {
                    lastMatchedLine = line;
                }
            }
        
            } catch (IOException e) {
            return "";
        }
        if (lastMatchedLine == null) {
            return "";
        }
        String[] parts = lastMatchedLine.split("\\s*\\|\\s*");
        if (parts.length >= 10) {
            return parts[9].trim();
        }
        return "";
    
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
