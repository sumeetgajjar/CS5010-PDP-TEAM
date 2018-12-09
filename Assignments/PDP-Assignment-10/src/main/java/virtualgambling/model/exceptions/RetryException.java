package virtualgambling.model.exceptions;

/**
 * {@link RetryException} represents a {@link RuntimeException} that fails to retry.
 */
public class RetryException extends RuntimeException {
  /**
   * Constructs a retry exception with the given message.
   *
   * @param message message of the exception
   */
  public RetryException(String message) {
    super(message);
  }
}
