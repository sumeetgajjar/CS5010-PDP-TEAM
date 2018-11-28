package virtualgambling.controller.command;

import java.util.Date;
import java.util.Map;

import virtualgambling.model.UserModel;
import virtualgambling.model.strategy.OneTimeWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

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
   * @param dateOfPurchase the date of purchase for the stocks
   * @param stockWeights   map of ticker to stocks
   */
  public BuySharesWeightedCommand(Date dateOfPurchase, Map<String, Double> stockWeights)
          throws IllegalArgumentException {
    this.stockWeights = stockWeights;
    this.strategy = new OneTimeWeightedInvestmentStrategy(dateOfPurchase, stockWeights);
  }

  @Override
  public void execute(UserModel userModel) {

  }
}
