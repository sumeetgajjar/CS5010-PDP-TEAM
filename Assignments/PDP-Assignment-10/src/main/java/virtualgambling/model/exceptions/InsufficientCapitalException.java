package virtualgambling.model.exceptions;

/**
 * This class represents a runtime exception which will be thrown when a user is trying to purchase
 * some Shares but does have sufficient funds to buy the shares.
 */
public class InsufficientCapitalException extends RuntimeException {

  /**
   * Constructs a Object of {@link InsufficientCapitalException} with the given message.
   *
   * @param message the message
   */
  public InsufficientCapitalException(String message) {
    super(message);
  }
}
