//Event is a data class that holds all the details of one event
//It uses private fields and public getters (encapsulation) and equals() is overridden to compare two events by their event ID

public class Event {

    private String name;
    private String date;
    private String time;
    private String venue;
    private String image;
    private String ticketSaleInfo;
    private String eventId;
    private String vipPrice;
    private String vipQty;      //VIP ticket quantity (for seat map logic)
    private int capacity;       //total venue capacity (for seat generation)
    private String cat1Price;   //Early Bird price
    private String cat2Price;   //Standard price
    private double cat3Price;
    private double cat4Price;
    private double cat5Price;
    private String earlyBirdExpiry; //date when Early Bird pricing ends, "-" if none

    //original constructor kept for backward compatibility
    public Event(String name, String date, String time, String venue, String image, String ticketSaleInfo, String vipPrice, String cat1Price, String cat2Price, String cat3Price, String cat4Price, String cat5Price) {
                     
        this(name, date, time, venue, image, ticketSaleInfo, vipPrice, cat1Price, cat2Price, cat3Price, cat4Price, cat5Price, "", "-", "0", "0");
    
                     }

    public Event(String name, String date, String time, String venue, String image, String ticketSaleInfo,String vipPrice, String cat1Price, String cat2Price, String cat3Price, String cat4Price, String cat5Price, String eventId) {
                     
        this(name, date, time, venue, image, ticketSaleInfo, vipPrice, cat1Price, cat2Price, cat3Price, cat4Price, cat5Price, eventId, "-", "0", "0");
    
                     }

    public Event(String name, String date, String time, String venue, String image, String ticketSaleInfo, String vipPrice, String cat1Price, String cat2Price, String cat3Price, String cat4Price, String cat5Price, String eventId, String earlyBirdExpiry) {
                     
        this(name, date, time, venue, image, ticketSaleInfo, vipPrice, cat1Price, cat2Price, cat3Price, cat4Price, cat5Price, eventId, earlyBirdExpiry, "0", "0");
    
                     }

    //full constructor with capacity and vipQty
    public Event(String name, String date, String time, String venue, String image, String ticketSaleInfo, String vipPrice, String cat1Price, String cat2Price, String cat3Price, String cat4Price, String cat5Price, String eventId, String earlyBirdExpiry, String capacity, String vipQty) {
                      
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.image = image;
        this.ticketSaleInfo = ticketSaleInfo;
        this.vipPrice = vipPrice;
        this.cat1Price = cat1Price;
        this.cat2Price = cat2Price;
        this.cat3Price = EventRow.parsePriceOrZero(cat3Price);
        this.cat4Price = EventRow.parsePriceOrZero(cat4Price);
        this.cat5Price = EventRow.parsePriceOrZero(cat5Price);
        this.eventId = eventId;
        this.earlyBirdExpiry = (earlyBirdExpiry == null || earlyBirdExpiry.trim().isEmpty()) ? "-" : earlyBirdExpiry.trim();
        this.capacity = parseIntOrZero(capacity);
        this.vipQty   = (vipQty   == null || vipQty.trim().isEmpty())   ? "0" : vipQty.trim();
     
    }

    public String getName() {
        try { 
            return name; 

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
    public String getVenue() {
        try { 
            return venue; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public String getImage() {
        try { 
            return image; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public String getTicketSaleInfo() {
        try { 
            return ticketSaleInfo; 
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
    public String getVipPrice() {
        try { 
            return vipPrice; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public String getVipQty() {
        try { 
            return vipQty; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public int getCapacity() {
        try {
            return capacity;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    public String getCat1Price() {
        try { 
            return cat1Price; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public String getCat2Price() {
        try { 
            return cat2Price; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }}
    public double getCat3Price() {
        try {
            return cat3Price;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    public double getCat4Price() {
        try {
            return cat4Price;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    public double getCat5Price() {
        try {
            return cat5Price;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }
    public String getEarlyBirdExpiry() {
        try { 
            return earlyBirdExpiry; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    //two events are equal if they have the same event ID
    public boolean equals(Object object) {
        try {
        if (this == object) 
            return true;

        if (!(object instanceof Event)) 
            return false;

        Event other = (Event) object;
        String key1 = buildKey();
        String key2 = other.buildKey();
        return key1.equals(key2);
    
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

    //build a simple unique key for equality check
    private String buildKey() {
        try {
        String id = safeTrim(eventId);
        if (!id.isEmpty() && !"-".equals(id)) {
            return id;
        }
        return safeTrim(name) + "|" + safeTrim(date) + "|" + safeTrim(time) + "|" + safeTrim(venue);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String safeTrim(String Value) {
        try {
        return Value == null ? "" : Value.trim();
     
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static int parseIntOrZero(String value) {
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
