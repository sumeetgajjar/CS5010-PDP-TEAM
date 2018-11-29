package virtualgambling.model.exceptions;

public class AlphaVantageAPILimitExceeded extends RuntimeException {
  public AlphaVantageAPILimitExceeded(String message) {
    super(message);
  }
}
