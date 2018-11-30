package virtualgambling.model.exceptions;

/**
 * {@link StrategyExecutionException} represents an exception that is thrown when a strategy cannot
 * successfully be executed.
 */
public class StrategyExecutionException extends RuntimeException {
  /**
   * Constructs a {@link StrategyExecutionException} in terms of the error message.
   *
   * @param message error message
   */
  public StrategyExecutionException(String message) {
    super(message);
  }
}
