import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Random;
import javax.swing.JOptionPane;

//CreateEventSystem handles all the data for creating a new event
//it stores the event details in memory across the 3 steps,then save everything to event.txt when the organizer confirms
//it extends BaseSystemService to inherit isBlank() and validateInput()

public class CreateEventSystem extends BaseSystemService {
    private static final String eventFile = "event.txt";

    //temporary storage for event details before final submission
    private static String draftEventName = "";
    private static String draftIntroduction = "";
    private static String draftType = "";
    private static String draftEventDate = "";
    private static String draftEventTime = "";
    private static String draftVenue = "";
    private static int draftCapacity = 0;
    private static String draftEventId = "";
    private static String draftSpeakerUid = "";
    private static String draftImagePath = ""; //image path selected by company
    private static String earlyBirdExpiry = "-"; //Early Bird expiry date (YYYY-MM-DD) or "-"
    private static boolean earlyTicketEnabled = false;
    private static boolean vipTicketEnabled = false;
    private static boolean giftOfTicketEnabled = false;
    private static double StandardPrice = 0.0;
    private static int StandardQty = 0;
    private static double earlyPrice = 0.0;
    private static int earlyQty = 0;
    private static double vipPrice = 0.0;
    private static int vipQty = 0;
    private static int giftQty = 0;
    private static int total = 0;

    //validate input and keep it in memory for next steps.
    public static boolean event_create(String event_Name,String Introduction,String type,String eventDate,String eventTime,String venue,String capacity) {
                try {
        if (isBlank(event_Name) || isBlank(Introduction) || "Please select".equals(type) || isBlank(eventDate) || isBlank(eventTime) || isBlank(venue) || isBlank(capacity)) {
            JOptionPane.showMessageDialog(null, "All required fields (*) must be filled!");
            return false;
        }
        if (!isValidDate(eventDate.trim())) {
            JOptionPane.showMessageDialog(null, "Date format must be yyyy-MM-dd.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //event date cannot be in the past
        try {
            LocalDate parsed = LocalDate.parse(eventDate.trim());
            if (parsed.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(null,"Event date cannot be in the past.","Invalid Date", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception ignored) {}
        if (!isValidTime(eventTime.trim())) {
            JOptionPane.showMessageDialog(null, "Time format must be HH:mm.");
            return false;
        }
        //event time cannot be in the past when date is today
        try {
            LocalDate dateEvent = LocalDate.parse(eventDate.trim());
            if (dateEvent.equals(LocalDate.now())) {
                LocalTime timeEvent = LocalTime.parse(eventTime.trim());
                if (timeEvent.isBefore(LocalTime.now())) {
                    JOptionPane.showMessageDialog(null,"Event time cannot be in the past for today.","Invalid Time", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (Exception ignored) {}
        if (!isValidPositiveInteger(capacity.trim())) {
            JOptionPane.showMessageDialog(null, "Capacity must be a positive integer.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //save event step 1 data into draft (temporary storage)
        draftEventName = event_Name.trim();
        draftIntroduction = Introduction.trim();
        draftType = type;
        draftEventDate = eventDate.trim();
        draftEventTime = eventTime.trim();
        draftVenue = venue.trim();
        draftCapacity = Integer.parseInt(capacity.trim());
        draftEventId = randomEventId();
        draftImagePath = "";
        return true;
    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

    //save selected ticket options from UI
    public static boolean event_option(boolean earlyTicket, boolean vipTicket, boolean giftOfTicket) {
        try {
        earlyTicketEnabled = earlyTicket;
        vipTicketEnabled = vipTicket;
        giftOfTicketEnabled = giftOfTicket;
        //reset prices and quantities to defaults when ticket options change
        StandardPrice = 0.0;
        StandardQty = 0;
        earlyPrice = 0.0;
        earlyQty = 0;
        vipPrice = 0.0;
        vipQty = 0;
        giftQty = 0;
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //final step: write event record into event.txt
    public static boolean event_save_all() {
        try {
        if (isBlank(draftEventName) || isBlank(draftIntroduction) || isBlank(draftType) || isBlank(draftVenue) || draftCapacity <= 0) {
            JOptionPane.showMessageDialog(null, "Event draft is incomplete.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validateTicketCapacityMatch()) {
            return false;
        }

        //get data from LoginRegisterSystem's function
        String ownerId = LoginRegisterSystem.getCurrentUserId();
        String ownemail = LoginRegisterSystem.getSavedEmail();
        String ownname = LoginRegisterSystem.getSavedUsername();

        //if ownerId and ownemail is empty , it will show error message
        if (isBlank(ownerId) && isBlank(ownemail)) {
            JOptionPane.showMessageDialog(null, "Cannot find current account.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(eventFile, true))) {
            
            String safeImage = isBlank(draftImagePath) ? "-" : draftImagePath.trim();
            String data = draftEventName + " | " + draftIntroduction + " | " + draftType + " | " + earlyTicketEnabled + " | " + vipTicketEnabled + " | " + giftOfTicketEnabled + " | " + formatPrice(StandardPrice) + " | " + StandardQty + " | " + (earlyTicketEnabled ? formatPrice(earlyPrice) : "-") + " | " + (earlyTicketEnabled ? earlyQty : "-") + " | " + (vipTicketEnabled ? formatPrice(vipPrice) : "-") + " | " + (vipTicketEnabled ? vipQty : "-") + " | " + (giftOfTicketEnabled ? giftQty : "-") + " | " + draftEventDate + " | " + draftEventTime + " | "  + ownemail + " | " + ownname + " | " + ownerId + " | " + draftEventId + " | PENDING | -" + " | " + draftVenue + " | " + draftCapacity + " | " + earlyBirdExpiry + " | " + safeImage;
            writer.write(data);
            writer.newLine();
        
            } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving event: " + e.getMessage());
            return false;
        }
        JOptionPane.showMessageDialog(null, "Event created successfully");
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //check if early bird ticket is enabled
    public static boolean hasEarlyTicket() {
        try {
        return earlyTicketEnabled;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //check if VIP ticket is enabled
    public static boolean hasVipTicket() {
        try {
        return vipTicketEnabled;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //check if gift ticket option is enabled
    public static boolean hasGiftTicket() {
        try {
        return giftOfTicketEnabled;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //validate and save Step 2 pricing and quantity data
    public static boolean saveStep2Pricing( String StandardPriceInput, String StandardQtyInput, String earlyPriceInput, String earlyQtyInput, String vipPriceInput, String vipQtyInput,String giftQtyInput) {
        try {
        
        //validate standard ticket price (must > 0)
        if (!isValidNumber(StandardPriceInput) || Double.parseDouble(StandardPriceInput.trim()) <= 0) {
            JOptionPane.showMessageDialog(null, "Standard price must be a number greater than 0.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //validate standard ticket quantity (must > 0)
        if (!isValidPositiveInteger(StandardQtyInput)) {
            JOptionPane.showMessageDialog(null, "Standard quantity must be a positive integer (> 0).","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //convert standard ticket price and quantity from input
        double nextStandardPrice = Double.parseDouble(StandardPriceInput.trim());
        int nextStandardQty = Integer.parseInt(StandardQtyInput.trim());

        //default early bird values
        double nextEarlyPrice = 0.0;
        int nextEarlyQty = 0;
        if (earlyTicketEnabled) {  //validate early bird ticket only if it is enabled
            if (!isValidNumber(earlyPriceInput) || Double.parseDouble(earlyPriceInput.trim()) <= 0) {
                JOptionPane.showMessageDialog(null, "Early Bird price must be a number greater than 0.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidPositiveInteger(earlyQtyInput)) {
                JOptionPane.showMessageDialog(null, "Early Bird quantity must be a positive integer (> 0).","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            nextEarlyPrice = Double.parseDouble(earlyPriceInput.trim());
            nextEarlyQty = Integer.parseInt(earlyQtyInput.trim());
        }

        //default VIP ticket values
        double nextVipPrice = 0.0;
        int nextVipQty = 0;
        //validate VIP ticket only if enabled
        if (vipTicketEnabled) {
            if (!isValidNumber(vipPriceInput) || Double.parseDouble(vipPriceInput.trim()) <= 0) {
                JOptionPane.showMessageDialog(null, "VIP price must be a number greater than 0.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidPositiveInteger(vipQtyInput)) {
                JOptionPane.showMessageDialog(null, "VIP quantity must be a positive integer (> 0).","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            nextVipPrice = Double.parseDouble(vipPriceInput.trim());
            nextVipQty = Integer.parseInt(vipQtyInput.trim());
        }

        //validate gift ticket quantity if enabled
        int nextGiftQty = 0;
        if (giftOfTicketEnabled) {
            if(giftQtyInput.equals("0")){
                JOptionPane.showMessageDialog(null, "Gift qty must be a number greater than 0.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidInteger(giftQtyInput)) {
                JOptionPane.showMessageDialog(null, "Gift qty format is invalid.","Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            nextGiftQty = Integer.parseInt(giftQtyInput.trim());
        }

        //check if event capacity is set from step 1
        if (draftCapacity <= 0) {
            JOptionPane.showMessageDialog(null, "Please complete Step 1 (capacity) before setting ticket quantities.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int sumQty = nextStandardQty + nextEarlyQty + nextVipQty;
        if (sumQty != draftCapacity) {
            JOptionPane.showMessageDialog(null, "All ticket quantities (Standard/Early/VIP/Gift) must sum to capacity (" + draftCapacity + "). Current sum: " + sumQty + ".","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //check gift ticket logic
        if(nextGiftQty < draftCapacity){
            JOptionPane.showMessageDialog(null,"Gift cannot less than total capacity.","Error",JOptionPane.ERROR_MESSAGE);
        }


        StandardPrice = nextStandardPrice;
        StandardQty = nextStandardQty;
        earlyPrice = nextEarlyPrice;
        earlyQty = nextEarlyQty;
        vipPrice = nextVipPrice;
        vipQty = nextVipQty;
        giftQty = nextGiftQty;

        return true;
        
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String getStandardPrice() {
        try {
        return formatPrice(StandardPrice);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getStandardQty() {
        try {
        return String.valueOf(StandardQty);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getEarlyPrice() {
        try {
        return earlyTicketEnabled ? formatPrice(earlyPrice) : "-";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getEarlyQty() {
        try {
        return earlyTicketEnabled ? String.valueOf(earlyQty) : "-";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getVipPrice() {
        try {
        return vipTicketEnabled ? formatPrice(vipPrice) : "-";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getVipQty() {
        try {
        return vipTicketEnabled ? String.valueOf(vipQty) : "-";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getGiftQty() {
        try {
        return giftOfTicketEnabled ? String.valueOf(giftQty) : "-";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftEventName() {
        try {
        return draftEventName;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftIntroduction() {
        try {
        return draftIntroduction;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftType() {
        try {
        return draftType;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftEventDate() {
        try {
        return draftEventDate;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftEventTime() {
        try {
        return draftEventTime;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftVenue() {
        try {
        return draftVenue;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftCapacity() {
        try {
        return String.valueOf(draftCapacity);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String getDraftEventId() {
        try {
        return draftEventId;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //store speaker UID for linking after save
    public static void setDraftSpeakerUid(String speakerUid) {
        try {
        draftSpeakerUid = speakerUid == null ? "" : speakerUid.trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getDraftSpeakerUid() {
        try {
        return draftSpeakerUid;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }    

    //save the image file path chose by the company on CreateEvent page
    public static void setDraftImagePath(String path) {
        try {
        draftImagePath = path == null ? "" : path.trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getDraftImagePath() {
        try {
        return draftImagePath;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



    //enter the wrong things, it will block system go to the next step
    private static boolean isValidNumber(String value) {
        try {
        if (isBlank(value)) {
            return false;
        }
        try {
            double numValue = Double.parseDouble(value.trim());
            return numValue >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean isValidInteger(String value) {
        try {
        if (isBlank(value)) {
            return false;
        }
        try {
            int numValue = Integer.parseInt(value.trim());
            return numValue >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //if enter negative integer ,it will block system go to next step
    private static boolean isValidPositiveInteger(String value) {
        try {
        if (isBlank(value)) {
            return false;
        }
        try {
            int numValue = Integer.parseInt(value.trim());
            return numValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //if format wrong,it will block the system go to the next step
    private static boolean isValidDate(String value) {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(value, dateFormat);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    //can entry the 24-hour format only
    private static boolean isValidTime(String value) {
        try {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
            LocalTime.parse(value, timeFormat);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    //create Event ID
    private static String randomEventId() {
        try {
        int min = 1000000;
        int max = 999999999;
        Random rng = new Random();
        int randomInt = rng.nextInt((max - min) + 1) + min;
        return String.valueOf(randomInt);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //format price to 2 decimal places (USD style)
    private static String formatPrice(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    //check if total ticket quantity matches event capacity
    private static boolean validateTicketCapacityMatch() {
        if (draftCapacity <= 0) {  //capacity must be set first
            JOptionPane.showMessageDialog(null, "Capacity is missing or invalid.");
            return false;
        }
        //sum all ticket quantities based on enabled options
        int sum = StandardQty + (earlyTicketEnabled ? earlyQty : 0) + (vipTicketEnabled ? vipQty : 0) + (giftOfTicketEnabled ? giftQty : 0);
        //check if total tickets match capacity
        if (sum != draftCapacity) {
            JOptionPane.showMessageDialog(null, "All ticket quantities (Standard/Early/VIP/Gift) must sum to capacity (" + draftCapacity + "). Current sum: " + sum + ".","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //validate gift ticket rule
        if (giftQty != draftCapacity || giftQty < draftCapacity){
            JOptionPane.showMessageDialog(null,"Gift quantities must be same or mote than capacity.","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    //simple helper to calculate capacity from tickets
    public static int calculateCapacity(int standardQty, int vipQty) {
        return standardQty + vipQty;
    }

    //store the Early Bird expiry date.
    //returns false and shows an error if the date is invalid or is after the event date.
    public static boolean setEarlyBirdExpiry(String expiry) {
        try {
        if (expiry == null || expiry.trim().isEmpty() || "-".equals(expiry.trim())) {
            earlyBirdExpiry = "-";
            return true;
        }
        String value = expiry.trim();
        if (!isValidDate(value)) {
            JOptionPane.showMessageDialog(null,"Early Bird expiry date format must be YYYY-MM-DD.","Invalid Date", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        LocalDate expiryDate = LocalDate.parse(value);
        //Early Bird must end on or before the event date — it makes no sense after.
        if (isValidDate(draftEventDate)) {
            LocalDate eventDate = LocalDate.parse(draftEventDate.trim());
            if (expiryDate.isAfter(eventDate)) {
                JOptionPane.showMessageDialog(null,"Early Bird expiry date cannot be after the event date (" + draftEventDate + ").","Invalid Date", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        //Early Bird expiry should also not be in the past — no point offering it.
        if (expiryDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null,"Early Bird expiry date cannot be in the past.","Invalid Date", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        earlyBirdExpiry = value;
        return true;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String getEarlyBirdExpiry() {
        try {
        return earlyBirdExpiry;
    
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
