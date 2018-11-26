package virtualgambling.controller.command;

import java.util.Map;

import util.Utils;
import virtualgambling.model.UserModel;
import virtualgambling.model.strategy.Strategy;
import virtualgambling.model.strategy.WeightedInvestmentStrategy;

/**
 * This class represents a Buy Share command with the enhancement that the each ticker can have an
 * associated weight and then given an amount to purchase, the ticker will be bought in the ratio of
 * it's weight/total weight. It implements the {@link Command} interface.
 */
public class BuySharesWeightedCommand implements Command {
  private final Map<String, Double> stockWeights;
  private final Strategy strategy;

  /**
   * Constructs a BuySharesWeightedCommand that take in a set of tickers and their associated
   * weights.
   *
   * <p>The constructor will throw an IllegalArgumentException if any of the parameters are null
   * or if the weights do not sum up to 1.
   *
   * @param stockWeights map of ticker to stocks
   */
  public BuySharesWeightedCommand(Map<String, Double> stockWeights)
          throws IllegalArgumentException {
    this.stockWeights = Utils.requireNonNull(stockWeights);
    this.strategy = new WeightedInvestmentStrategy(stockWeights);
  }

  @Override
  public void execute(UserModel userModel) {

  }
}
