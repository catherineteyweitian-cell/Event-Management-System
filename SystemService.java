//Interface that all service classes must follow
//defines the contract for loading and saving data
public interface SystemService {

    //every service must be able to validate input data
    //returns true if the data is valid, false otherwise
    boolean validateInput(String input);

    //every service must be able to describe itself
    //used for logging and debugging
    String getServiceName();
}
