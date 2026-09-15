//PendingUserRow represents a pending staff or speaker registration
//Private fields and getters = encapsulation
//toString() and equals() are overridden for polymorphism

public class PendingUserRow {
    private final String uid;
    private final String name;
    private final String role;
    private final String companyCid;
    private final String companyName;
    private final String idValue;

    public PendingUserRow(String uid, String name, String role, String companyCid, String companyName, String idValue) {
        this.uid = uid;
        this.name = name;
        this.role = role;
        this.companyCid = companyCid;
        this.companyName = companyName;
        this.idValue = idValue;
        }

    //simple display text for pending approval rows
    @Override
    //return a readable summary of this pending user
    public String toString() {
        try {
        return "UID: " + fixed(uid, 10)
                + " | Name: " + fixed(name, 22)
                + " | Role: " + fixed(role, 8)
                + " | Company CID: " + fixed(companyCid, 10)
                + " | Company Name: " + fixed(companyName, 20)
                + " | ID: " + fixed(idValue, 14);
    
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

    public String getUid() {
        try { 
            return uid; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getName() {
        try { 
            return name; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getRole() {
        try { 
            return role; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCompanyCid() {
        try { 
            return companyCid; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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

    public String getIdValue() {
        try { 
            return idValue; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    //two pending users are equal if they have the same email
    public boolean equals(Object object) {
        try {
        if (this == object) 
            return true;

        if (!(object instanceof PendingUserRow))
             return false;

        PendingUserRow other = (PendingUserRow) object;
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
        String id = safeTrim(uid);
        if (!id.isEmpty() && !"-".equals(id)) {
            return id;
        }
        return safeTrim(name) + "|" + safeTrim(role) + "|" + safeTrim(companyCid);
    
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


    static {
        try {

        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
