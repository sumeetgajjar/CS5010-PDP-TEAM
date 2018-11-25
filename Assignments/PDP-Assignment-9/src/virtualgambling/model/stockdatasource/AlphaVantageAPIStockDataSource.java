package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 1:08 PM, 11/25/18
 */
public class AlphaVantageAPIStockDataSource implements StockDataSource {

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    return null;
  }
}
