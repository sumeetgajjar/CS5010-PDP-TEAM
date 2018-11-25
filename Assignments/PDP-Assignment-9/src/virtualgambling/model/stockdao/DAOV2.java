package virtualgambling.model.stockdao;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 2:35 PM, 11/25/18
 */
public class DAOV2 implements StockDAO {

  @Override
  public SharePurchaseOrder createPurchaseOrder(String tickerName, long quantity, Date date,
                                                BigDecimal remainingCapital) throws IllegalStateException, IllegalArgumentException, StockDataNotFoundException {
    return null;
  }

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException,
          IllegalArgumentException {
    return null;
  }
}
