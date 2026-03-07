package ehei.pfa.authGetway.security;

public class InvalidVerificationTokenException extends RuntimeException {
  public InvalidVerificationTokenException(String message) {
    super(message);
  }
  public InvalidVerificationTokenException() {
    super("Lien de vérification invalide ou expiré.");
  }
}
