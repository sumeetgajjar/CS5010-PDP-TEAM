package virtualgambling.model.exceptions;

public class AlphaVantageAPILimitExceededException extends RuntimeException {
  public AlphaVantageAPILimitExceededException(String message) {
    super(message);
  }
}
