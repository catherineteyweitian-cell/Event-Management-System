import java.util.Locale;

//EventRow represents one row of event data read from event.txt
//it stores all the parsed fields as private strings (encapsulation)
//toString() formats the row for display
//equals() compares by eventId

public class EventRow {
    private final String eventName;
    private final String eventId;
    private final String date;
    private final String time;
    private final String eventType;
    private final double earlyPrice;
    private final int earlyQty;
    private final double standardPrice;
    private final int standardQty;
    private final double vipPrice;
    private final int vipQty;
    private final String companyName;
    private final String companyEmail;
    private final String companyId;
    private final String location;
    private final String status;
    private final String reason;

    public EventRow(String eventName,String eventId,String date,String time,String location,String eventType,double earlyPrice,int earlyQty,double standardPrice,int standardQty,double vipPrice,int vipQty,String companyName,String companyEmail,String companyId,String status,String reason) {
        
        this.eventName = eventName;
        this.eventId = eventId;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventType = eventType;
        this.earlyPrice = earlyPrice;
        this.earlyQty = earlyQty;
        this.standardPrice = standardPrice;
        this.standardQty = standardQty;
        this.vipPrice = vipPrice;
        this.vipQty = vipQty;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyId = companyId;
        this.status = status;
        this.reason = reason;
    
        }

    //simple and clear string output for UI display
    //call parent class's function
    @Override
    //return a readable summary of this event row
    public String toString() {
        try {
        String role = LoginRegisterSystem.getSavedRole();
        if (role == null) {
            role = "";
        }
        role = role.trim();

        String base = "Name: " + fixed(eventName, 24) + " | Event ID: " + fixed(eventId, 10) + " | Date: " + fixed(date, 10)  + " | Time: " + fixed(time, 5)  + " | Location: " + fixed(location, 18) + " | Type: " + fixed(eventType, 10) + " | Early RM " + fixed(earlyPrice, 6) + " x " + fixed(earlyQty, 4) + " | Standard RM " + fixed(standardPrice, 6) + " x " + fixed(standardQty, 4) + " | VIP RM " + fixed(vipPrice, 6) + " x " + fixed(vipQty, 4) + " | Company: " + fixed(companyName, 18);

        if ("User".equalsIgnoreCase(role)) {
            return base;
        }

        String companyBits = " | Email: " + fixed(companyEmail, 24) + " | CID: " + fixed(companyId, 10);

        if ("Speaker".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role)) {
            return base + companyBits;
        }

        String reviewBits = " | Status: " + fixed(status, 9) + " | Reason: " + fixed(reason, 24);

        if ("Company".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role)) {
            return base + companyBits + reviewBits;
        }

        return base + companyBits + reviewBits;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String fixed(String value, int width) {
        try {
            String displayText = safe(value);
            if (displayText.length() > width) {
                if (width <= 1) {
                    displayText = displayText.substring(0, width);
                } else {
                    displayText = displayText.substring(0, width - 1) + "…";
                }
            }
            return String.format("%-" + width + "s", displayText);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String fixed(double value, int width) {
        return fixed(String.format(Locale.US, "%.2f", value), width);
    }

    private String fixed(int value, int width) {
        return fixed(String.valueOf(value), width);
    }

    //avoid null text in display output
    private String safe(String value) {
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

    public String getEventName() {
        try { 
            return eventName; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getEventId() {
        try { 
            return eventId; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getDate() {
        try { 
            return date; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getTime() {
        try { 
            return time; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getEventType() {
        try { 
            return eventType; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public double getEarlyPrice() {
        try { 
            return earlyPrice; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public int getEarlyQty() {
        try { 
            return earlyQty; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public double getStandardPrice() {
        try { 
            return standardPrice; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public int getStandardQty() {
        try { 
            return standardQty; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public double getVipPrice() {
        try { 
            return vipPrice; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public int getVipQty() {
        try {
             return vipQty; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public String getCompanyName() {
        try { 
            return companyName; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCompanyEmail() {
        try {
            return companyEmail; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCompanyId() {
        try { 
            return companyId; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getLocation() {
        try { 
            return location; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        try { 
            return status; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getReason() {
        try { 
            return reason; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    //two event rows are equal if they have the same event ID
    public boolean equals(Object object) {
        try {
        if (this == object) 
            return true;

        if (!(object instanceof EventRow))
             return false;

        EventRow other = (EventRow) object;
        return buildKey().equals(other.buildKey());
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public int hashCode() {
        try {
        return buildKey().hashCode();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private String buildKey() {
        try {
        String id = safeTrim(eventId);
        if (!id.isEmpty() && !"-".equals(id)) {
            return id;
        }
        return safeTrim(eventName) + "|" + safeTrim(date) + "|" + safeTrim(time) + "|" + safeTrim(location);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String safeTrim(String value) {
        try {
        return value == null ? "" : value.trim();
     
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static double parsePriceOrZero(String value) {
        try {
            if (value == null) {
                return 0.0;
            }
            String cleaned = value.replace("RM", "").trim();
            if (cleaned.isEmpty() || "-".equals(cleaned)) {
                return 0.0;
            }
            return Double.parseDouble(cleaned);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public static int parseQtyOrZero(String value) {
        try {
            if (value == null) {
                return 0;
            }
            String cleaned = value.trim();
            if (cleaned.isEmpty() || "-".equals(cleaned)) {
                return 0;
            }
            return Integer.parseInt(cleaned);
        } catch (Exception ex) {
            return 0;
        }
    }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
