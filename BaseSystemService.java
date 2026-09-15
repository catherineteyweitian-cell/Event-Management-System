//abstract base class for all service classes
//implements SystemService and provides shared helper methods that all service subclasses can reuse.
public abstract class BaseSystemService implements SystemService {

    //checks if a string is blank or null and used by all subclasses for input
    protected static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    //do the door for validateInput from SystemService interface
    // returns false if the input is blank
    @Override
    public boolean validateInput(String input) {
        return !isBlank(input);
    }

    //subclasses override this to return their own name
    //default returns the class name
    @Override
    public String getServiceName() {
        return getClass().getSimpleName();
    }
}
