package virtualgambling.model;

public class PortfolioNotFoundException extends RuntimeException {
  public PortfolioNotFoundException(String message) {
    super(message);
  }
}
