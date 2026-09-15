//ReportData is a data class that stores event report static
//it uses private fields with public getters and setters (encapsulation)
//toString() and equals() are overridden for polymorphism

public class ReportData {

    // private fields
    private int totalEvents;
    private int totalUsers;
    private int approvedEvents;
    private int pendingEvents;
    private int rejectedEvents;

    // Set the total number of events
    public void setTotalEvents(int totalEvents) {
        try {
        this.totalEvents = totalEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTotalUsers(int totalUsers) {
        try {
        this.totalUsers = totalUsers;
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setApprovedEvents(int approvedEvents) {
        try {
        this.approvedEvents = approvedEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPendingEvents(int pendingEvents) {
        try {
        this.pendingEvents = pendingEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setRejectedEvents(int rejectedEvents) {
        try {
        this.rejectedEvents = rejectedEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getTotalEvents() {
        try {
        return totalEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int getTotalUsers() {
        try {
        return totalUsers;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int getApprovedEvents() {
        try {
        return approvedEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int getPendingEvents() {
        try {
        return pendingEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public int getRejectedEvents() {
        try {
        return rejectedEvents;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    //return a readable string showing all report values
    public String toString() {
        try {
        return "ReportData{" + "totalEvents=" + totalEvents + ", totalUsers=" + totalUsers + ", approvedEvents=" + approvedEvents + ", pendingEvents=" + pendingEvents + ", rejectedEvents=" + rejectedEvents+ "}";
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    //compare two ReportData objects field by field
    public boolean equals(Object object) {
        // Check if same object
        if (this == object) 
            return true;
        //check if object is a ReportData
        if (!(object instanceof ReportData))
             return false;
        // Cast and compare each field
        ReportData other = (ReportData) object;
        return this.totalEvents == other.totalEvents && this.totalUsers == other.totalUsers && this.approvedEvents == other.approvedEvents && this.pendingEvents  == other.pendingEvents && this.rejectedEvents == other.rejectedEvents;
    }

    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
