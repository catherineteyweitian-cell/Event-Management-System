import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

//TicketIdData stores ticket records in memory and in tickets.txt
//each ticket has an ID, event name, seat, and category
//toString() and equals() are overridden for polymorphism

 public class TicketIdData {
 
     private static final String TICKET_FILE = "tickets.txt";
     private static final String EVENT_FILE = "event.txt";
     private static HashMap<String, String[]> tickets = new HashMap<>();

     // Normalized ticket record indexes (value array length = 8)
     // 0 eventId, 1 eventName, 2 seat, 3 category, 4 holderName, 5 holderIc, 6 holderPhone, 7 companyCid
     private static final int IDX_EVENT_ID = 0;
     private static final int IDX_EVENT_NAME = 1;
     private static final int IDX_SEAT = 2;
     private static final int IDX_CATEGORY = 3;
     private static final int IDX_HOLDER_NAME = 4;
     private static final int IDX_HOLDER_IC = 5;
     private static final int IDX_HOLDER_PHONE = 6;
     private static final int IDX_COMPANY_CID = 7;

     //load all tickets from file into the in-memory map.
     private static void loadFromFile() {
         try {
         File file = new File(TICKET_FILE);
        if (!file.exists()) 
            return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
             String line;
             while ((line = reader.readLine()) != null) {
                 if (line.trim().isEmpty()) continue;
                 //old format: ticketId|eventName|seat|category
                 //new format: ticketId|eventId|eventName|seat|category|holderName|holderIc|holderPhone|companyCid
                 String[] parts = line.split("\\|", -1);
                 if (parts.length >= 4) {
                     String ticketId = safe(parts, 0);
                     if (ticketId.isEmpty()) 
                         continue;

                     String[] normalized = normalizeFromFileParts(parts);
                     tickets.put(ticketId, normalized);
                 }
             }
         
             } catch (IOException e) {
            //show error if ticket file cannot be read
            System.out.println("Error reading ticket file: " + e.getMessage());
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
     }

     //save a new ticket to memory and to tickets.txt
     public static void addTicket(String id, String eventId, String eventName, String seat, String category, String holderName, String holderIc, String holderPhone) {
         try {
         String companyCid = resolveCompanyCid(eventId, eventName);
         tickets.put(id, new String[]{
             safe(eventId), safe(eventName), safe(seat), safe(category), safe(holderName), safe(holderIc), safe(holderPhone), safe(companyCid)
         });
         // write to file so staff can verify tickets after restart.
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKET_FILE, true))) {
             
             writer.write(id + "|" + safe(eventId) + "|" + safe(eventName) + "|" + safe(seat) + "|" + safe(category) + "|" + safe(holderName) + "|" + safe(holderIc) + "|" + safe(holderPhone) + "|" + safe(companyCid));
             writer.newLine();
         
             } catch (IOException e) {
             //show error if ticket file cannot be saved
            System.out.println("Error saving ticket: " + e.getMessage());
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

     //find a ticket by its ID (loads from file if not in memory)
     public static String[] getTicket(String id) {
         try {
         //check memory first
         //load from file if not found
         if (!tickets.containsKey(id)) {
             loadFromFile();
         }
         String[] ticket = tickets.get(id);
         if (ticket == null) 
             return null;

         // Ensure normalized format (older in-memory entries)
         if (ticket.length < 8) {
             ticket = normalizeFromLegacyMemory(ticket);
             tickets.put(id, ticket);
         }

         // Best-effort resolve company CID if missing
         if (isBlank(ticket[IDX_COMPANY_CID])) {
             String companyCid = resolveCompanyCid(ticket[IDX_EVENT_ID], ticket[IDX_EVENT_NAME]);
             if (!isBlank(companyCid)) {
                 ticket[IDX_COMPANY_CID] = companyCid.trim();
                 tickets.put(id, ticket);
             }
         }

         return ticket;
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return null;
         }
     }

     // Returns true only if ticket is owned by the provided company CID.
     public static boolean isTicketOwnedByCompany(String[] ticket, String companyCid) {
         try {
         if (ticket == null || ticket.length < 8) 
             return false;
         if (isBlank(companyCid)) 
             return false;
         String owner = ticket[IDX_COMPANY_CID];
         if (isBlank(owner)) 
             return false;
         return companyCid.trim().equals(owner.trim());
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return false;
         }
     }

     public static int getTicketCount() {
         try {
         return tickets.size();
     
         } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    //check if two TicketIdData objects are equal
    @Override
    //compare two TicketIdData objects
    public boolean equals(Object object) {
        //Step 1: check if it is the same object
        if (this == object) {
            return true;
        }
        //Step 2: check if obj is a TicketIdData
        if (!(object instanceof TicketIdData)) {
            return false;
        }
        //Step 3: same class = equal (no instance fields to compare)
        return true;
    }

    //show ticket info as a string
    @Override
    //show the total ticket count as a string
     public String toString() {
         return "TicketIdData{ticketCount=" + tickets.size() + "}";
     }

     private static String[] normalizeFromFileParts(String[] parts) {
         try {
         // new format (>= 9): id|eventId|eventName|seat|category|holderName|holderIc|holderPhone|companyCid
         if (parts.length >= 9) {
             return new String[]{
                 safe(parts, 1), safe(parts, 2), safe(parts, 3), safe(parts, 4),
                 safe(parts, 5), safe(parts, 6), safe(parts, 7), safe(parts, 8)
             };
         }

         // old format (>= 4): id|eventName|seat|category
         String eventName = safe(parts, 1);
         String eventId = "-";
         String seat = safe(parts, 2);
         String category = safe(parts, 3);
         String companyCid = resolveCompanyCid(eventId, eventName);
         return new String[]{
             eventId, eventName, seat, category,
             "-", "-", "-", safe(companyCid)
         };
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return new String[]{"-", "-", "-", "-", "-", "-", "-", ""};
         }
     }

     private static String[] normalizeFromLegacyMemory(String[] legacy) {
         try {
         // legacy in-memory format was [eventName, seat, category]
         String eventName = legacy.length >= 1 ? safe(legacy[0]) : "-";
         String seat = legacy.length >= 2 ? safe(legacy[1]) : "-";
         String category = legacy.length >= 3 ? safe(legacy[2]) : "-";
         String companyCid = resolveCompanyCid("-", eventName);
         return new String[]{"-", eventName, seat, category, "-", "-", "-", safe(companyCid)};
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return new String[]{"-", "-", "-", "-", "-", "-", "-", ""};
         }
     }

     private static String resolveCompanyCid(String eventId, String eventName) {
         try {
         File file = new File(EVENT_FILE);
         if (!file.exists()) {
             return "";
         }

         String id = safe(eventId);
         String name = safe(eventName);

         int nameMatches = 0;
         String nameMatchOwner = "";

         try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
             
             String line;
             while ((line = reader.readLine()) != null) {
                 if (line.trim().isEmpty()) 
                     continue;
                 String[] parts = line.split("\\|", -1);
                 if (parts.length < 18) 
                     continue;

                 String ownerCid = safe(parts, 17);
                 String fileEventName = safe(parts, 0);
                 String fileEventId = parts.length >= 19 ? safe(parts, 18) : "-";

                 if (!isBlank(id) && !"-".equals(id) && id.trim().equals(fileEventId.trim())) {
                     return ownerCid;
                 }

                 if (!isBlank(name) && name.trim().equals(fileEventName.trim())) {
                     nameMatches++;
                     nameMatchOwner = ownerCid;
                     if (nameMatches > 1) {
                         // ambiguous name, do not guess
                         nameMatchOwner = "";
                     }
                 }
             }
         
         } catch (IOException e) {
             return "";
         }

         return nameMatchOwner;
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return "";
         }
     }

     private static String safe(String[] parts, int idx) {
         try {
         if (parts == null || idx < 0 || idx >= parts.length) 
             return "";
         return safe(parts[idx]);
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return "";
         }
     }

     private static String safe(String value) {
         try {
         if (value == null) 
             return "";
         return value.trim();
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return "";
         }
     }

     private static boolean isBlank(String v) {
         try {
         return v == null || v.trim().isEmpty();
     
         } catch (Exception ex) {
             ex.printStackTrace();
             return true;
         }
     }

     static {
         try {
         } catch (Exception ex) {
             System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
