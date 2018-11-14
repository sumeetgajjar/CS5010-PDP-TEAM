package virtualgambling.model.exceptions;

/**
 * Created by gajjar.s, on 10:24 PM, 11/11/18
 */
public class StockDataNotFoundException extends RuntimeException {

  public StockDataNotFoundException() {
  }

  public StockDataNotFoundException(String message) {
    super(message);
  }
}
