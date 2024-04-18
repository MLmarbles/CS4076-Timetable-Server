package application;

public class IncorrectActionException extends Exception {
    private String message;

    // Constructor with no parameters
    public IncorrectActionException() {
        this.message = "Incorrect action, please try again!";
    }

    // Constructor with a single parameter of type String
    public IncorrectActionException(String message) {
        this.message = message;
    }

    // Method to get the exception message
    public String IncorrectActionMessage() {
        return this.message;
    }
}
