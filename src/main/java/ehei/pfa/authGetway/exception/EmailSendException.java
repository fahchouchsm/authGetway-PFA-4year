package ehei.pfa.authGetway.exception;

public class EmailSendException extends RuntimeException {
  public EmailSendException(String message) {
    super(message);
  }

  public EmailSendException() {
    super("Failed to send email");
  }
}
