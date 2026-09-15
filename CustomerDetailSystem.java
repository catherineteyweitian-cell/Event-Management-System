import javax.swing.JOptionPane;

// CustomerDetailSystem validates and stores the customer details
// entered on CustomerDetailsPage before the payment step.
// Extends BaseSystemService to inherit the isBlank() helper.

//validate buyer personal details before confirming booking
public class CustomerDetailSystem extends BaseSystemService {

    public static boolean validateBuyerDetails(String buyerName, String buyerIc, String buyerEmail, String buyerPhone, String confirmEmail) {
        try {
        //check empty fields
        if (buyerName.isEmpty() || buyerIc.isEmpty() || buyerEmail.isEmpty() || buyerPhone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in the buyer's name, IC, email, and phone number.");
            return false;
        }

        //check email confirmation
        if (!buyerEmail.equals(confirmEmail)) {
            JOptionPane.showMessageDialog(null, "Buyer's email does not match the confirmation email.");
            return false;
        }

        //basic email format check
        if (!buyerEmail.contains("@") || !buyerEmail.contains(".")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address for the buyer.");
            return false;
        }

        //block system go to next step when user/speaker enter word or least digit 
        if (!buyerPhone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid phone number (10-15 digits) for the buyer.");
            return false;
        }

        //IC must be 12 digits
        if (!buyerIc.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid IC number (12 digits) for the buyer.");
            return false;
        }

        //name length rules
        if (buyerName.length() < 3) {
            JOptionPane.showMessageDialog(null, "Buyer's name must be at least 3 characters long.");
            return false;
        }

        if (buyerName.length() > 50) {
            JOptionPane.showMessageDialog(null, "Buyer's name must be less than 50 characters long.");
            return false;
        }

        //email must contain exactly one @
        if (buyerEmail.chars().filter(ch -> ch == '@').count() != 1) {
            JOptionPane.showMessageDialog(null, "Buyer's email must contain exactly one '@' character.");
            return false;
        }

        //email cannot start/end with @
        if (buyerEmail.startsWith("@") || buyerEmail.endsWith("@")) {
            JOptionPane.showMessageDialog(null, "Buyer's email cannot start or end with '@'.");
            return false;
        }

        //email cannot start/end with .
        if (buyerEmail.startsWith(".") || buyerEmail.endsWith(".")) {
            JOptionPane.showMessageDialog(null, "Buyer's email cannot start or end with '.'.");
            return false;
        }

        //no double dots in email
        if (buyerEmail.contains("..")) {
            JOptionPane.showMessageDialog(null, "Buyer's email cannot contain consecutive '.' characters.");
            return false;
        }

        //no spaces in email
        if (buyerEmail.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Buyer's email cannot contain spaces.");
            return false;
        }

        //name cannot contain numbers
        if (buyerName.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(null, "Buyer's name cannot contain digits.");
            return false;
        }

        //name cannot contain special characters
        if (buyerName.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            JOptionPane.showMessageDialog(null, "Buyer's name cannot contain special characters.");
            return false;
        }

        return true;
    
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
