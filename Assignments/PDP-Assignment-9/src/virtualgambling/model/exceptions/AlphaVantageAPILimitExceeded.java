package virtualgambling.model.exceptions;

/**
 * This class represents a runtime exception which will be thrown if the API limit is exceeded.
 */
public class AlphaVantageAPILimitExceeded extends RuntimeException {

  /**
   * Constructs a AlphaVantageAPILimitExceeded with the given message.
   *
   * @param message the given message
   */
  public AlphaVantageAPILimitExceeded(String message) {
    super(message);
  }
}
