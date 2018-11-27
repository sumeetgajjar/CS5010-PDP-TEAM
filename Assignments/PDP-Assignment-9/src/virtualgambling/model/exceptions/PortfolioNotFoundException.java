package virtualgambling.model.exceptions;

public class PortfolioNotFoundException extends RuntimeException {
  public PortfolioNotFoundException(String message) {
    super(message);
  }
}
