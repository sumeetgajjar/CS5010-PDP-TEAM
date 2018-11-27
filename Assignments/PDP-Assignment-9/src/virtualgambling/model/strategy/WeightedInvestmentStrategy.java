package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import virtualgambling.model.bean.SharePurchaseOrder;

public class WeightedInvestmentStrategy extends AbstractWeightedInvestmentStrategy {
  public WeightedInvestmentStrategy(Date dateOfPurchase, Map<String, Double> stockWeights) {
    super(dateOfPurchase, stockWeights);
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest) throws IllegalArgumentException {
    return null;
  }
}
