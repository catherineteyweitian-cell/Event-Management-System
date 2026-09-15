import java.io.*;
import java.util.ArrayList;
import java.util.List;

//ManageSystem handles all admin management operations
//it can update event status, delete users, and manage related data
//it extends BaseSystemService to inherit shared help methods

public class ManageSystem extends BaseSystemService {
    private static final String USER_FILE = "users.txt";
    private static final String EVENT_FILE = "event.txt";
    private static final String PENDING_USER_FILE = "pending_users.txt";
    private static String selectedEventId = "";

    public static List<String[]> loadAllUsersRaw() {
        try {
        List<String[]> rows = new ArrayList<>();
        File file = new File(USER_FILE);
        if (!file.exists()) return rows;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) rows.add(line.split("\\|"));
            }

            // show error if file cannot be read
            } catch (IOException e) {
                //show error if file cannot be read
                System.out.println("Error reading file: " + e.getMessage());
            }
        return rows;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean deleteUserByUid(String uid,String role) {
        try {
        File src = new File(USER_FILE);
        File tmp = new File(USER_FILE + ".tmp");
        boolean deleted = false;
        String deletedUserRole = "";
        String deletedCompanyCid = "";
        String deletedCompanyName = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmp))) {
                 

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;
                String[] parts = line.split("\\|");
                // delete target user (only admin can delete)
                if (parts.length >= 5 && uid.equals(parts[4].trim()) && role.equals("Admin")) {
                    deleted = true;
                    deletedUserRole = parts[3].trim();
                    deletedCompanyCid = parts[4].trim();
                    deletedCompanyName = parts[2].trim();
                    continue;
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        
                 } catch (IOException e) {
            return false;
        }

        if (!src.delete()) 
            return false;
        if (!tmp.renameTo(src)) 
            return false;

        // if deleted user is company
        // remove related data (events, staff, pending users)
        if (deleted && "Company".equalsIgnoreCase(deletedUserRole)) {
            //remove all staff/speaker accounts linked to this company
            deleteUsersByCompany(deletedCompanyCid, deletedCompanyName);
            deleteEventsByCompanyCid(deletedCompanyCid);
            //also remove pending staff/speaker requests for this company
            deletePendingUsersByCompanyCid(deletedCompanyCid);
        }

        return deleted;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // delete all events by company cid
    private static boolean deleteEventsByCompanyCid(String companyCid) {
        try {
        if (companyCid == null || companyCid.trim().isEmpty()) {
            return false;
        }

        File src = new File(EVENT_FILE);
        File tmp = new File(EVENT_FILE + ".tmp");
        if (!src.exists()) {
            return true; //no event file means nothing to clean
        }

        boolean touched = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                //current event format stores company CID at index 17
                if (parts.length >= 18 && companyCid.trim().equals(parts[17].trim())) {
                    touched = true;
                    continue;
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
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
        return touched;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // delete all staff and speaker accounts under a company
    // used when company account is removed
    private static boolean deleteUsersByCompany(String companyCid, String companyName) {
        try {
        // check valid input
        if ((companyCid == null || companyCid.trim().isEmpty()) && (companyName == null || companyName.trim().isEmpty())) {
            return false;
        }

        File src = new File(USER_FILE);
        File tmp = new File(USER_FILE + ".tmp2");
        if (!src.exists()) {
            return false;
        }

        boolean touched = false;
        try (BufferedReader br = new BufferedReader(new FileReader(src));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 5) {
                    String role = parts[3].trim();
                    // only check staff and speaker
                    if ("Staff".equalsIgnoreCase(role) || "Speaker".equalsIgnoreCase(role)) {
                        boolean match = false;
                        //new format stores company CID:
                        //Staff -> index 7, Speaker -> index 9
                        if ("Staff".equalsIgnoreCase(role) && parts.length >= 8 && companyCid != null && companyCid.trim().equals(parts[7].trim())) {
                            match = true;
                        }
                        if ("Speaker".equalsIgnoreCase(role) && parts.length >= 10 && companyCid != null && companyCid.trim().equals(parts[9].trim())) {
                            match = true;
                        }
                        //old format fallback: company name at index 5.
                        if (!match && parts.length >= 6 && companyName != null && companyName.trim().equalsIgnoreCase(parts[5].trim())) {
                            match = true;
                        }
                        if (match) {
                            touched = true;
                            continue;
                        }
                    }
                }
                bw.write(line);
                bw.newLine();
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
        return touched;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // delete pending users by company CID
    // remove staff/speaker requests under a company
    private static boolean deletePendingUsersByCompanyCid(String companyCid) {
        try {
        if (companyCid == null || companyCid.trim().isEmpty()) {
            return false;
        }

        File src = new File(PENDING_USER_FILE);
        File tmp = new File(PENDING_USER_FILE + ".tmp");
        if (!src.exists()) {
            return true; //no pending file means nothing to clean
        }

        boolean touched = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                //pending format stores company CID at index 5
                if (parts.length >= 6 && companyCid.trim().equals(parts[5].trim())) {
                    touched = true;
                    continue;
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
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
        return touched;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // delete event by event ID
    // remove event from event file
    public static boolean deleteEventByEventId(String eventId) {
        try {
        File src = new File(EVENT_FILE);
        File tmp = new File(EVENT_FILE + ".tmp");
        boolean deleted = false;

        if (eventId == null || eventId.trim().isEmpty()) 
            return false;

        if (!src.exists()) 
            return false;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmp))) {
                 

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 1) {
                    String fileEventId = parts.length >= 19 ? parts[18].trim() : parts[parts.length - 1].trim();
                    if (eventId.trim().equals(fileEventId)) {
                        deleted = true;
                        continue;
                    }
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        
                 } catch (IOException e) {
            return false;
        }

        if (!src.delete()) 
            return false;
        if (!tmp.renameTo(src)) 
            return false;
        return deleted;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    //change an event status to APPROVED or REJECTED in event.txt
    public static boolean updateEventStatusByEventId(String eventId, String status, String reason) {
        try {
        File src = new File(EVENT_FILE);
        File tmp = new File(EVENT_FILE + ".tmp");
        boolean updated = false;

        if (eventId == null || eventId.trim().isEmpty())
             return false;
        if (status == null || status.trim().isEmpty())
             return false;
        if (!src.exists())
             return false;

        String safeReason = (reason == null || reason.trim().isEmpty()) ? "-" : reason.trim();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(src));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 19) {
                    String fileEventId = parts[18].trim();
                    if (eventId.trim().equals(fileEventId)) {
                        StringBuilder rebuilt = new StringBuilder();
                        for (int i = 0; i < parts.length; i++) {
                            if (i > 0) rebuilt.append(" | ");
                            rebuilt.append(parts[i].trim());
                        }

                        String[] updatedParts = rebuilt.toString().split("\\s\\|\\s");
                        if (updatedParts.length >= 21) {
                            updatedParts[19] = status.trim();
                            updatedParts[20] = safeReason;
                        } else {
                            StringBuilder add = new StringBuilder();
                            for (int i = 0; i < updatedParts.length; i++) {
                                if (i > 0) add.append(" | ");
                                add.append(updatedParts[i]);
                            }
                            add.append(" | ").append(status.trim()).append(" | ").append(safeReason);
                            bufferedWriter.write(add.toString());
                            bufferedWriter.newLine();
                            updated = true;
                            continue;
                        }

                        StringBuilder out = new StringBuilder();
                        for (int i = 0; i < updatedParts.length; i++) {
                            if (i > 0) out.append(" | ");
                            out.append(updatedParts[i]);
                        }
                        bufferedWriter.write(out.toString());
                        bufferedWriter.newLine();
                        updated = true;
                        continue;
                    }
                }

                bufferedWriter.write(line);
                bufferedWriter.newLine();
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

    public static void setSelectedEventId(String eventId) {
        try {
        selectedEventId = eventId == null ? "" : eventId.trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getSelectedEventId() {
        try {
        return selectedEventId;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String[] findEventByEventId(String eventId) {
        try {
        if (eventId == null || eventId.trim().isEmpty()) return null;
        File file = new File(EVENT_FILE);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;
                String[] parts = line.split("\\|");
                if (parts.length < 1) 
                    continue;
                String fileEventId = parts.length >= 19 ? parts[18].trim() : parts[parts.length - 1].trim();
                if (eventId.trim().equals(fileEventId)) {
                    return parts;
                }
            }
        
            } catch (IOException e) {
                //show error if file cannot be read
                System.out.println("Error reading file: " + e.getMessage());
            }

        return null;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


        //reduce available ticket quantities after customer purchase
    public static boolean reduceEventTicketQty(String eventId, int vipReduction, int standardReduction) {
        try {
        if (eventId == null || eventId.trim().isEmpty()) 
            return false;
        if (vipReduction < 0 || standardReduction < 0) 
            return false;
        File src = new File(EVENT_FILE);
        File tmp = new File(EVENT_FILE + ".tmp");
        if (!src.exists()) 
            return false;
        boolean updated = false;
        try (BufferedReader br = new BufferedReader(new FileReader(src));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tmp))) {
                 
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                //use consistent delimiter with spaces: " | "
                String[] parts = line.split("\\s\\|\\s");
                if (parts.length < 19) { bw.write(line); bw.newLine(); 
                    continue;
                 }
                String fileEventId = parts[18].trim();
                if (eventId.trim().equals(fileEventId)) {
                    try {
                        //p[7] = StandardQty, p[11] = VIPQty
                        int currentStandard = Integer.parseInt(parts[7].trim());
                        int currentVip = Integer.parseInt(parts[11].trim());
                        //subtract purchased quantities, but don't go below 0
                        currentStandard = Math.max(0, currentStandard - standardReduction);
                        currentVip = Math.max(0, currentVip - vipReduction);
                        parts[7] = String.valueOf(currentStandard);
                        parts[11] = String.valueOf(currentVip);
                        updated = true;
                    } catch (NumberFormatException ignored) {

                    }
                    //rebuild the line with consistent delimiter
                    StringBuilder rebuilt = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        if (i > 0) rebuilt.append(" | ");
                        rebuilt.append(parts[i].trim());
                    }
                    bw.write(rebuilt.toString()); bw.newLine(); 
                    continue;
                }
                bw.write(line); bw.newLine();
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

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}

