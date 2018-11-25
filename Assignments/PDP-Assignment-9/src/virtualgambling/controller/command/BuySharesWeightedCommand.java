package virtualgambling.controller.command;

import java.util.List;

import util.Utils;
import virtualgambling.model.UserModel;

/**
 * This class represents a Buy Share command with the enhancement that the each ticker can have an
 * associated weight and then given an amount to purchase, the ticker will be bought in the ratio of
 * it's weight/total weight. It implements the {@link Command} interface.
 */
public class BuySharesWeightedCommand implements Command {
  private final List<String> tickerNames;
  private final List<Double> weights;

  /**
   * Constructs a BuySharesWeightedCommand that take in a set of tickers and their associated
   * weights.
   *
   * <p>The constructor will throw an IllegalArgumentException if any of the parameters are null
   * or if the weights do not sum up to 1.
   *
   * @param tickerNames list of ticker names
   * @param weights     list of weights for each ticker
   */
  public BuySharesWeightedCommand(List<String> tickerNames, List<Double> weights)
          throws IllegalArgumentException {
    this.tickerNames = Utils.requireNonNull(tickerNames);
    this.weights = Utils.requireNonNull(weights);
    this.checkTotalsInvariant(weights);
  }

  private void checkTotalsInvariant(List<Double> weights) {
    double sum = weights.stream().mapToDouble(Double::doubleValue).sum();
    if (sum != 1.0) {
      throw new IllegalArgumentException("weights do not sum to 1");
    }
  }

  @Override
  public void execute(UserModel userModel) {

  }
}
