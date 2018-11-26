package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.List;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;

public class RecurringWeightedInvestmentStrategy implements Strategy {
  private final Strategy strategy;

  public RecurringWeightedInvestmentStrategy(Strategy strategy) {
    this.strategy = Utils.requireNonNull(strategy);
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest) throws IllegalArgumentException {
    return null;
  }
}
