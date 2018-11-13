package virtualgambling.model.stockexchange;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.exceptions.StockNotFoundException;

/**
 * Created by gajjar.s, on 9:47 PM, 11/12/18
 */
public class SimpleStockDataSource implements StockDataSource {

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockNotFoundException {
    return null;
  }
}
