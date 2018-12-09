package virtualgambling.model.exceptions;

/**
 * {@link PersistenceException} represents an exception thrown when persistence is not possible.
 */
public class PersistenceException extends RuntimeException {
  /**
   * Constructs a {@link PersistenceException} in terms of a String message.
   *
   * @param message the message to be attached to this exception
   */
  public PersistenceException(String message) {
    super(message);
  }
}
