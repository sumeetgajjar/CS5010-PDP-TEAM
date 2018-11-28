package virtualgambling.model.strategy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.stockdao.StockDAO;

public class WeightedInvestmentStrategy extends AbstractWeightedInvestmentStrategy {
  public WeightedInvestmentStrategy(Date dateOfPurchase, Map<String, Double> stockWeights) {
    super(dateOfPurchase, stockWeights);
  }

  @Override
  public List<SharePurchaseOrder> execute(BigDecimal amountToInvest, StockDAO stockDAO) throws IllegalArgumentException, StrategyExecutionException {
    return this.stockWeights.entrySet().stream().map(tickerAndAmount -> {
      String tickerName = tickerAndAmount.getKey();
      Double stockWeight = tickerAndAmount.getValue();
      BigDecimal price = stockDAO.getPrice(tickerName, startDate);
      BigDecimal amountAvailableForThisStock = amountToInvest.multiply(BigDecimal.valueOf(
              stockWeight
              ).divide(
              BigDecimal.valueOf(100), BigDecimal.ROUND_DOWN)
      );
      long quantity =
              amountAvailableForThisStock.divide(price, BigDecimal.ROUND_DOWN).longValueExact();
      return stockDAO.createPurchaseOrder(tickerName, quantity, this.startDate);
    })
            .collect(Collectors.toList());
  }
}
