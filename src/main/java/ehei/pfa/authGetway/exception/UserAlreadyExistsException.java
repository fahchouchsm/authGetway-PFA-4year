package ehei.pfa.authGetway.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("The user already exist");
    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}