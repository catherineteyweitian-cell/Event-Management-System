//UserRow represents one row of user data read from users.txt
//Private fields and getters = encapsulation
//toString() and equals() are overridden for polymorphism

public class UserRow {
    private final String name;
    private final String role;
    private final String email;
    private final String uid;
    private final String companyName;
    private final String idValue;

    public UserRow(String name, String role, String email, String uid, String companyName, String idValue) {
        
        this.name = name;
        this.role = role;
        this.email = email;
        this.uid = uid;
        this.companyName = companyName;
        this.idValue = idValue;
    
        }

    //simple display text for user rows
    @Override
    //return a readable summary of this user
    public String toString() {
        try {
        return "Name: " + fixed(name, 22)
                + " | Role: " + fixed(role, 8)
                + " | Email: " + fixed(email, 28)
                + " | UID: " + fixed(uid, 10)
                + " | Company: " + fixed(companyName, 20)
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

    public String getEmail() {
        try { return email; 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }}
    public String getUid() {
        try { 
            return uid; 
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

    //basic equals (compare by UID when possible)
    @Override
    //two users are equal if they have the same UID
    public boolean equals(Object object) {
        try {
        if (this == object)
             return true;
        if (!(object instanceof UserRow))
             return false;
        UserRow other = (UserRow) object;
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
        return safeTrim(name) + "|" + safeTrim(email) + "|" + safeTrim(role);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String safeTrim(String v) {
        try {
        return v == null ? "" : v.trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //basic try-catch guard
    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
