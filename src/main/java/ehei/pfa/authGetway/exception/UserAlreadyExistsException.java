package ehei.pfa.authGetway.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("the user already exist");
    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}