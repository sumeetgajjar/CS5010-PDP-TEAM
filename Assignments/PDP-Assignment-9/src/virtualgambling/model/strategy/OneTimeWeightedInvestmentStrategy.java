package virtualgambling.model.strategy;

import java.util.Date;
import java.util.Map;

public class OneTimeWeightedInvestmentStrategy extends RecurringWeightedInvestmentStrategy {
  public OneTimeWeightedInvestmentStrategy(Date dateOfPurchase, Map<String, Double> stockWeights) {
    super(dateOfPurchase, stockWeights, 1, dateOfPurchase);
  }
}
