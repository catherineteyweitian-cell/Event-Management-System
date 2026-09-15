//TestEventFlow is a simple test class to launch the application.
//it wraps the app start in a try-catch for basic error handling.

public class TestEventFlow {
    //start the EventFlow application with error handling
    //this main method is opening the LoginFrame, which is the first screen users see when they start the application. It allows users to log in or register for an account.
    //if the system out any problems during start, they will be caught and printed to the console

    //recommended test credentials for admin register:
    //Admin 1st password: ADMIN00@T
    //Admin 2nd password: ADMIN159#357
    //Admin 3rd password: ADMIN123456

    public static void main(String[] args) {
        try {
            new LoginFrame();

}catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
            //for debugging purposes, print the stack trace to understand where the problem are coming from
        }
    }
}
