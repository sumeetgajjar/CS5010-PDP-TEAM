package virtualgambling.model.exceptions;

/**
 * This class represents a runtime exception which will be thrown if a portfolio queried is not
 * present in the UserModel.
 */
public class PortfolioNotFoundException extends RuntimeException {

  /**
   * Constructs an Object of PortfolioNotFoundException with the given message.
   *
   * @param message the given message
   */
  public PortfolioNotFoundException(String message) {
    super(message);
  }
}
