package virtualgambling.model.stockdatasource.strategy;

import java.math.BigDecimal;
import java.util.List;

import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * {@link Strategy} represents a strategy of investing in stocks given the amount to invest and
 * returns a list of {@link SharePurchaseOrder}.
 */
public interface Strategy {
  /**
   * Executes a strategy such that the amount to invest is passed as input and the strategy
   * generates a list of purchase orders.
   *
   * <p>It throws an {@link IllegalArgumentException} if the following cases occur:
   * <ul>
   * <li>amountToInvest is null</li>
   * <li>amountToInvest is negative</li>
   * </ul>
   *
   * @param amountToInvest amount to invest in dollars
   * @return list of {@link SharePurchaseOrder}.
   */
  List<SharePurchaseOrder> execute(BigDecimal amountToInvest) throws IllegalArgumentException;
}
