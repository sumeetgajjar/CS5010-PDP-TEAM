package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;

public class WeightedInvestmentStrategy implements Strategy {
  private final Map<String, Double> stockWeights;

  public WeightedInvestmentStrategy(Map<String, Double> stockWeights) {
    this.checkInvariantForStockWeights(Utils.requireNonNull(stockWeights));
    this.stockWeights = stockWeights;
  }

  private void checkInvariantForStockWeights(Map<String, Double> stockWeights) {
    double weightSum = stockWeights.values().stream().mapToDouble(Double::doubleValue).sum();
    if (!Utils.areTwoDoublesEqual(weightSum, 1.0, 0.001)) {
      throw new IllegalArgumentException("Weights do not sum up to 1");
    }
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest) throws IllegalArgumentException {
    return null;
  }
}
