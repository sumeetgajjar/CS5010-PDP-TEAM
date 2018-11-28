package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.stockdao.StockDAO;

abstract class AbstractWeightedInvestmentStrategy implements Strategy {
  protected final Date startDate;
  protected final Map<String, Double> stockWeights;

  protected AbstractWeightedInvestmentStrategy(Date startDate, Map<String, Double> stockWeights) {
    this.checkInvariantForStockWeights(Utils.requireNonNull(stockWeights));
    this.startDate = Utils.requireNonNull(startDate);
    this.stockWeights = stockWeights;
  }

  protected void checkInvariantForStockWeights(Map<String, Double> stockWeights) {
    double weightSum = stockWeights.values().stream().mapToDouble(Double::doubleValue).sum();
    if (!Utils.areTwoDoublesEqual(weightSum, 100.0, 0.001)) {
      throw new IllegalArgumentException("Weights do not sum up to 100");
    }
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest, StockDAO stockDAO) throws IllegalArgumentException, StrategyExecutionException {
    return null;
  }
}
