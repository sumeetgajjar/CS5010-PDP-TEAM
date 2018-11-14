package virtualgambling.model.exceptions;

/**
 * Created by gajjar.s, on 12:07 PM, 11/14/18
 */
public class InsufficientCapitalException extends RuntimeException {

  public InsufficientCapitalException(String message) {
    super(message);
  }
}
