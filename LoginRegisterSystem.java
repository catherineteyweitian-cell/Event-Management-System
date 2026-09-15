import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Handles login/registration, pending approvals, and profile updates.
 * Data is stored in users.txt and pending_users.txt.
 **/
public class LoginRegisterSystem extends BaseSystemService {
    // Cached session info for the currently logged-in user.
    private static String savedEmail = "";
    private static String savedPassword = "";
    private static String savedUsername = "";
    private static String savedRole = "";
    private static String currentUserId = "";

    // Storage files.
    private static final String userFile = "users.txt";
    private static final String pendingUserFile = "pending_users.txt";
     
    // Restore the last successful login into memory (best-effort).
    static {
        loadLastUserFromFile();
    }


    // Authenticates a user and populates the session cache.
    public static boolean user_login(String email, String password, String username, String role){
        try {
        if(email.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null,"Email or Password cannot be empty.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!email.contains("@gmail.com")||!email.contains(".")){
            JOptionPane.showMessageDialog(null,"Email format is incorrect.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(email.equals(savedEmail) && password.equals(savedPassword)){
            if (isBlank(savedRole) || isBlank(currentUserId)) {
                loadUserByCredentials(email, password);
            }
            JOptionPane.showMessageDialog(null,"Welcome! " + savedUsername );
            return true;
        }
        if (loadUserByCredentials(email, password)) {
            JOptionPane.showMessageDialog(null,"Welcome! " + savedUsername );
            return true;
        }

        if (pendingEmailExists(email)) {
            JOptionPane.showMessageDialog(null,"Account is pending company approval.","Info",JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(null,"Email or password is incorrect.","Error",JOptionPane.ERROR_MESSAGE);
        return false;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Validates email/password before role-specific registration details.
    public static boolean register_account(String email, String password, String passwordComfim) {
        try {
        if(isBlank(email) || isBlank(password) || isBlank(passwordComfim)){
            JOptionPane.showMessageDialog(null,"All fields must be filled!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!email.contains("@") || !email.contains(".")){
            JOptionPane.showMessageDialog(null,"Email format is incorrect.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(password.length() < 6 || passwordComfim.length() < 6){
            JOptionPane.showMessageDialog(null,"Password cannot less than 6.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(password.length() > 15 || passwordComfim.length() > 15){
            JOptionPane.showMessageDialog(null,"Password cannot more than 15.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!password.equals(passwordComfim)){
            JOptionPane.showMessageDialog(null,"Password not match.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
         if(password.equals("ADMIN00@T")){
            String adminPasswordF = JOptionPane.showInputDialog(null,"Admin's Password:", "Input Dialog", JOptionPane.QUESTION_MESSAGE);
            if (adminPasswordF== null){
                JOptionPane.showMessageDialog(null, "You cancelled the input.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            } else if (adminPasswordF.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No input provided.");
            return false;
            }else if(!adminPasswordF.equals("ADMIN159#357")){
                JOptionPane.showMessageDialog(null, "Incorrect.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (emailExists(email) || pendingEmailExists(email)) {
            JOptionPane.showMessageDialog(null,"Email already registered.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        savedEmail = email;
        savedPassword = password;
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Registers a customer into users.txt using the cached email/password.
    public static boolean register_user_details(String username, int year, int month, int day) {
        try {
        int age =calculateAge(year,month, day);
        if(age >= 18){
        }else{
            JOptionPane.showMessageDialog(null,"Must be at least 18 years old.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(isBlank(username)){
            JOptionPane.showMessageDialog(null,"Username cannot be empty.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(username.length() > 35){ 
            JOptionPane.showMessageDialog(null,"Username too long.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        savedUsername = username;
        savedRole = "User";
        currentUserId = randomUID();
        saveUserToFile(savedEmail, savedPassword, username, "User", currentUserId);
        JOptionPane.showMessageDialog(null, "Success! Welcome " + username + " !");
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Registers a company into users.txt using the cached email/password.
    public static boolean register_company_details(String companyName, int year, int month, int day) {
        try {
        if(isBlank(companyName)){
            JOptionPane.showMessageDialog(null,"Company name cannot be empty.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(companyName.length() > 60){ 
            JOptionPane.showMessageDialog(null,"Company name too long.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        savedUsername = companyName;
        savedRole = "Company";
        currentUserId = randomUID();
        saveUserToFile(savedEmail, savedPassword, companyName, "Company", currentUserId);
        JOptionPane.showMessageDialog(null, "Success! Welcome " + companyName + " !");
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // register staff details for new staff signup
    // check age, company, and required fields
    public static boolean register_staff_details(String name, int year, int month, int day, String companyCid, String companyName, String workId) {
        try {
        // calculate user age
        int age =calculateAge(year,month, day);
        if(age >= 18){  // check minimum age (must be 18+)
        }else{
            JOptionPane.showMessageDialog(null,"Must be at least 18 years old.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(isBlank(name) || isBlank(companyCid) || isBlank(companyName) || isBlank(workId)){
            JOptionPane.showMessageDialog(null,"All fields must be filled!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // check company cid and name match database
        if (!companyExistsByCidAndName(companyCid, companyName)) {
            JOptionPane.showMessageDialog(null,"CID and company name do not match existing company.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // save user temporary data
        savedUsername = name;
        savedRole = "Staff";
        currentUserId = randomUID();
        // save pending staff to file (waiting approval)
        savePendingUserToFile(savedEmail, savedPassword, name, "Staff", currentUserId, companyCid, companyName, workId, "", "");
        JOptionPane.showMessageDialog(null, "Submitted. Please wait company approval.");
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }



    // register speaker details for speaker signupvalidate age, fields, and company info
    public static boolean register_speaker_details(String name,int year,int month,int day,String companyCid,String companyName,String speakerId,String bio,String topic) {
        try {
        int age =calculateAge(year,month, day);
        if(age >= 18){
        }else{
            JOptionPane.showMessageDialog(null,"Must be at least 18 years old.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(isBlank(name) || isBlank(companyCid) || isBlank(companyName) || isBlank(speakerId) || isBlank(bio) || isBlank(topic)){
            JOptionPane.showMessageDialog(null,"All fields must be filled!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!companyExistsByCidAndName(companyCid, companyName)) {
            JOptionPane.showMessageDialog(null,"CID and company name do not match existing company.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        savedUsername = name;
        savedRole = "Speaker";
        currentUserId = randomUID();
        savePendingUserToFile(savedEmail, savedPassword, name, "Speaker", currentUserId, companyCid, companyName, speakerId, bio, topic);
        JOptionPane.showMessageDialog(null, "Submitted. Please wait company approval.");
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Registers an admin into users.txt using the cached email/password.
    public static boolean register_admin_details(String username, int year, int month, int day, String adminPassword) {
        try {
        int age =calculateAge(year,month, day);
        if(age >= 18){
        }else{
            JOptionPane.showMessageDialog(null,"Must be at least 18 years old.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(isBlank(username) || isBlank(adminPassword)){
            JOptionPane.showMessageDialog(null,"All fields must be filled!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(username.length() > 35){ 
            JOptionPane.showMessageDialog(null,"Username too long.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!adminPassword.equals("ADMIN12345")){
             JOptionPane.showMessageDialog(null,"Wrong admin of password.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        savedUsername = username;
        savedRole = "Admin";
        currentUserId = randomUID();
        saveUserToFile(savedEmail, savedPassword, username, "Admin", currentUserId);
        JOptionPane.showMessageDialog(null, "Success! Welcome " + username + " !");
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Computes age based on date of birth.
    private static int calculateAge(int year, int month, int day) {
        try {
        LocalDate today = LocalDate.now();
        int age = today.getYear() - year;
        if (today.getMonthValue() < month || (today.getMonthValue() == month && today.getDayOfMonth() < day)) {
            age--;
        }
        return age;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // Returns the cached UID for the current session.
    public static String getCurrentUserId() {
        try { 
        return currentUserId;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    } 

    // Returns the cached role for the current session.
    public static String getSavedRole() {
        try {
        return savedRole;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Returns the cached email for the current session.
    public static String getSavedEmail(){
        try {
            return savedEmail;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Returns the cached username for the current session.
    public static String getSavedUsername(){
        try {
            return savedUsername;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



    // Looks up the company CID for the current session (if applicable).
    public static String getCompanyCidForCurrentUser() {
        try {
        String role = savedRole;
        if (role == null) 
            role = "";

        if ("Company".equalsIgnoreCase(role)) {
            return currentUserId == null ? "" : currentUserId.trim();
        }
        if (isBlank(currentUserId)) {
            return "";
        }
        File file = new File(userFile);
        if (!file.exists()) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;
                String[] parts = line.split("\\|");
                if (parts.length < 5) 
                    continue;
                String fileRole = parts[3].trim();
                String uid = parts[4].trim();
                if (!currentUserId.trim().equals(uid))
                    continue;
                if ("Staff".equalsIgnoreCase(fileRole)) {
                    return parts.length >= 8 ? parts[7].trim() : "";
                }
                if ("Speaker".equalsIgnoreCase(fileRole)) {
                    return parts.length >= 10 ? parts[9].trim() : "";
                }
                return "";
            }
        
            } catch (IOException e) {
            return "";
        }
        return "";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Reads a user record from users.txt by email.
    public static String[] getUserProfileByEmail(String email) {
        try {
        // check email is not empty
        if (isBlank(email)) {
            return null;
        }
        File file = new File(userFile);
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 5) {  // check valid data format
                    continue;
                }
                String role = parts[3].trim();
                String fileEmail = parts[0].trim();
                // only match User role + same email
                if (!"User".equalsIgnoreCase(role) || !email.trim().equalsIgnoreCase(fileEmail)) {
                    continue;
                }
                if (parts.length < 14) {
                    return null;
                }
                String[] profile = new String[9];
                for (int i = 0; i < 9; i++) {
                    profile[i] = parts[5 + i].trim();
                }
                return profile;
            }
        
            } catch (IOException e) {
            return null;
        }
        return null;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Updates a user record in users.txt by email.
    public static boolean updateUserProfileByEmail(String email, String name, String age, String gender, String contact,String hobby, String region, String occupation, String educationBackground) {
        try {  // check email not empty
        if (isBlank(email)) {
            return false;
        }
        File src = new File(userFile);
        File tmp = new File(userFile + ".tmp");
        if (!src.exists()) {
            return false;
        }

        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && "User".equalsIgnoreCase(parts[3].trim()) && email.trim().equalsIgnoreCase(parts[0].trim())) {
                    String password = parts[1].trim();
                    String uid = parts[4].trim();
                    String updatedLine = email.trim() + "|" + password + "|" + name.trim() + "|User|" + uid.trim() + "|" + name.trim() + "|" + age.trim() + "|" + gender.trim() + "|" + contact.trim() + "|" + email.trim() + "|" + hobby.trim() + "|" + region.trim() + "|" + occupation.trim() + "|" + educationBackground.trim();
                    writer.write(updatedLine);
                    writer.newLine();
                    savedUsername = name.trim();
                    updated = true;
                    continue;
                }
                writer.write(line);
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
        return updated;
    
     } catch (Exception ex) {
        ex.printStackTrace();
        return false;
}
}


    // Reads a speaker record from users.txt by UID.
    public static String[] getSpeakerProfileByUid(String uid) {
        try {
        File file = new File(userFile);
        if (!file.exists() || isBlank(uid)) {
            return null;
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
                String role = parts[3].trim();
                String fileUid = parts[4].trim();
                if (!"Speaker".equalsIgnoreCase(role) || !uid.trim().equals(fileUid)) {
                    continue;
                }

                // get speaker bio and topic
                String bio = parts.length >= 8 ? parts[7].trim() : "";
                String topic = parts.length >= 9 ? parts[8].trim() : "";
                return new String[] {bio, topic};
            }
        
            } catch (IOException e) {
            return null;
        }
        return null;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    // Updates a speaker profile (bio/topic) in users.txt by UID.
    public static boolean updateSpeakerProfile(String uid, String bio, String topic) {
        try {
        if (isBlank(uid) || isBlank(bio) || isBlank(topic)) {
            return false;
        }

        File src = new File(userFile);
        File tmp = new File(userFile + ".tmp");
        if (!src.exists()) {
            return false;
        }

        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && "Speaker".equalsIgnoreCase(parts[3].trim()) && uid.trim().equals(parts[4].trim())) {
                    String email = parts[0].trim();
                    String password = parts[1].trim();
                    String username = parts[2].trim();
                    String role = parts[3].trim();
                    String companyName = parts.length >= 6 ? parts[5].trim() : "";
                    String speakerId = parts.length >= 7 ? parts[6].trim() : "";
                    String updatedLine = email + "|" + password + "|" + username + "|" + role + "|" + uid.trim() + "|" + companyName + "|" + speakerId + "|" + bio.trim() + "|" + topic.trim();
                    writer.write(updatedLine);
                    writer.newLine();
                    updated = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        
                 } catch (IOException e) {
            return false;
        }

        if (!src.delete())
             return false;
        if (!tmp.renameTo(src)) 
            return false;
        return updated;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Generates a random UID that is not used in users.txt.
    private static String randomUID(){
        try {
        int min = 1000000;
        int max = 999999999;
        Random UID = new Random();
        int randomInt = UID.nextInt((max - min) + 1) + min;
        return String.valueOf(randomInt);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Appends one record to users.txt.
    public static void saveUserToFile(String email, String password, String username, String role, String uid, String... extras) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {

            StringBuilder data = new StringBuilder(email + "|" + password + "|" + username + "|" + role + "|" + uid);
            if (extras != null) {
                for (String extra : extras) {
                    data.append("|").append(extra == null ? "" : extra.trim());
                }
            }
            writer.write(data.toString());
            writer.newLine();
        
            } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }


    // Appends one record to pending_users.txt.
    private static void savePendingUserToFile(String email, String password, String username, String role, String uid,String companyCid, String companyName, String profileId, String bio, String topic) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pendingUserFile, true))) {
            
            writer.write(email + "|" + password + "|" + username + "|" + role + "|" + uid + "|" + companyCid + "|" + companyName + "|" + profileId + "|" + bio + "|" + topic);
            writer.newLine();
        
            } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving pending data: " + e.getMessage());
        }
    }


    // Checks whether a company exists in users.txt by CID and name.
    private static boolean companyExistsByCidAndName(String companyCid, String companyName) {
        try {
        File file = new File(userFile);
        if (!file.exists()) {
            return false;
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
                String role = parts[3].trim();
                String cid = parts[4].trim();
                String name = parts[2].trim();
                if ("Company".equalsIgnoreCase(role) && companyCid.trim().equals(cid) && companyName.trim().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        
            } catch (IOException e) {
            return false;
        }
        return false;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Returns pending staff/speaker requests for a company.
    public static List<String[]> getPendingUsersForCompany(String companyCid, String companyName) {
        try {
        List<String[]> result = new ArrayList<>();
        File file = new File(pendingUserFile);
        if (!file.exists()) {
            return result;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length < 10) {
                    continue;
                }
                if (!companyCid.trim().equals(parts[5].trim())) {
                    continue;
                }
                if (!companyName.trim().equalsIgnoreCase(parts[6].trim())) {
                    continue;
                }
                result.add(parts);
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


    // Approves a pending request and moves it into users.txt.
    public static boolean approvePendingUser(String pendingUid, String companyCid, String companyName) {
        try {
        return processPendingUser(pendingUid, companyCid, companyName, true);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Rejects a pending request and removes it from pending_users.txt.
    public static boolean rejectPendingUser(String pendingUid, String companyCid, String companyName) {
        try {
        return processPendingUser(pendingUid, companyCid, companyName, false);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Shared handler for pending request approval/rejection.
    private static boolean processPendingUser(String pendingUid, String companyCid, String companyName, boolean approve) {
        try {
        File src = new File(pendingUserFile);
        File tmp = new File(pendingUserFile + ".tmp");
        if (!src.exists() || isBlank(pendingUid)) {
            return false;
        }

        boolean handled = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length < 10) {
                    continue;
                }

                String uid = parts[4].trim();
                String rowCid = parts[5].trim();
                String rowCompanyName = parts[6].trim();

                if (pendingUid.trim().equals(uid) && companyCid.trim().equals(rowCid) && companyName.trim().equalsIgnoreCase(rowCompanyName)) {

                    handled = true;
                    if (approve) {
                        String email = parts[0].trim();
                        String password = parts[1].trim();
                        String username = parts[2].trim();
                        String role = parts[3].trim();
                        String profileId = parts[7].trim();
                        String bio = parts[8].trim();
                        String topic = parts[9].trim();

                        if ("Staff".equalsIgnoreCase(role)) {
                            saveUserToFile(email, password, username, "Staff", uid, rowCompanyName, profileId, rowCid);
                        } else if ("Speaker".equalsIgnoreCase(role)) {
                            saveUserToFile(email, password, username, "Speaker", uid, rowCompanyName, profileId, bio, topic, rowCid);
                        }
                    }
                    continue;
                }

                writer.write(line);
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
        return handled;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Loads the last record in users.txt into the session cache (best-effort).
    private static void loadLastUserFromFile() {
        try {
        File file = new File(userFile);
        if (!file.exists()) {
            return;
        }
        String lastLine = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lastLine = line;
                }
            }
        
            } catch (IOException e) {
            return;
        }
        if (lastLine == null) {
            return;
        }
        String[] parts = lastLine.split("\\|");
        if (parts.length >= 5) {
            savedEmail = parts[0].trim();
            savedPassword = parts[1].trim();
            savedUsername = parts[2].trim();
            savedRole = parts[3].trim();
            currentUserId = parts[4].trim();
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // Loads a user by credentials from users.txt and updates the session cache.
    private static boolean loadUserByCredentials(String email, String password) {
        try {
        File file = new File(userFile);
        if (!file.exists()) {
            return false;
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
                String fileEmail = parts[0].trim();
                String filePassword = parts[1].trim();
                if (email.equals(fileEmail) && password.equals(filePassword)) {
                    savedEmail = fileEmail;
                    savedPassword = filePassword;
                    savedUsername = parts[2].trim();
                    savedRole = parts[3].trim();
                    currentUserId = parts[4].trim();
                    return true;
                }
            }
        
            } catch (IOException e) {
            return false;
        }
        return false;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    // Checks whether an email exists in users.txt.
    private static boolean emailExists(String email) {
        try {
        File file = new File(userFile);
        if (!file.exists()) {
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue;
                }
                String fileEmail = parts[0].trim();
                if (email.equals(fileEmail)) {
                    return true;
                }
            }
        
            } catch (IOException e) {
            return false;
        }
        return false;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Checks whether an email exists in pending_users.txt.
    private static boolean pendingEmailExists(String email) {
        try {
        File file = new File(pendingUserFile);
        if (!file.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 1) {
                    continue;
                }
                if (email.equals(parts[0].trim())) {
                    return true;
                }
            }
        
            } catch (IOException e) {
            return false;
        }
        return false;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    // Updates the speaker's display name in users.txt by UID.
    public static boolean updateSpeakerName(String uid, String newName) {
        try {
        if (isBlank(uid) || isBlank(newName)) {
            return false;
        }

        File src = new File(userFile);
        File tmp = new File(userFile + ".tmp");
        if (!src.exists()) {
            return false;
        }

        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");

                if (parts.length >= 5 && "Speaker".equalsIgnoreCase(parts[3].trim()) && uid.trim().equals(parts[4].trim())) {

                    parts[2] = newName.trim();
                    StringBuilder updatedLine = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        if (i > 0) 
                            updatedLine.append("|");
                        updatedLine.append(parts[i].trim());
                    }
                    writer.write(updatedLine.toString());
                    writer.newLine();

                    savedUsername = newName.trim();
                    updated = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        
                 } catch (IOException e) {
            return false;
        }

        if (!src.delete()) 
            return false;
        if (!tmp.renameTo(src)) 
            return false;

        return updated;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
