package virtualgambling.model.exceptions;

/**
 * This class represents a runtime exception which will be thrown when the API call limit of a
 * particular API is exceeded.
 */
public class APILimitExceededException extends RuntimeException {

  /**
   * Constructs an APILimitExceededException object with the given message.
   *
   * @param message the given message
   */
  public APILimitExceededException(String message) {
    super(message);
  }
}