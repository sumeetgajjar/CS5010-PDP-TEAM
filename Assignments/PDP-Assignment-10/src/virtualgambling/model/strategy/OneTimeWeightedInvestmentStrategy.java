package virtualgambling.model.strategy;

import java.util.Date;
import java.util.Map;

/**
 * {@link OneTimeWeightedInvestmentStrategy} is a strategy to invest in a set of stocks that takes
 * ticker and weights and invest in them just one time on a given day.
 */
public class OneTimeWeightedInvestmentStrategy extends RecurringWeightedInvestmentStrategy {
  /**
   * Constructs a {@link OneTimeWeightedInvestmentStrategy} in terms of the date when the stocks
   * need to be purchased and the ticker and their weights.
   *
   * @param dateOfPurchase date of purchase
   * @param stockWeights   a map of ticker names to their percentages that represents investment
   *                       weights.
   * @throws IllegalArgumentException if any input is null or if the total of the weights in
   *                                  stockWeights do not sum to 100.
   */
  public OneTimeWeightedInvestmentStrategy(Date dateOfPurchase, Map<String, Double> stockWeights)
          throws IllegalArgumentException {
    super(dateOfPurchase, stockWeights, 1, dateOfPurchase);
  }
}
