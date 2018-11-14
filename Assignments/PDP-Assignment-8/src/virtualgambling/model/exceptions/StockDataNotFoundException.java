package virtualgambling.model.exceptions;

/**
 * This class represents a runtime exception which will be thrown if Data for the queried stock is
 * not available with the source.
 */
public class StockDataNotFoundException extends RuntimeException {

  /**
   * Constructs a object of {@link StockDataNotFoundException} with the given message.
   *
   * @param message the message
   */
  public StockDataNotFoundException(String message) {
    super(message);
  }
}
