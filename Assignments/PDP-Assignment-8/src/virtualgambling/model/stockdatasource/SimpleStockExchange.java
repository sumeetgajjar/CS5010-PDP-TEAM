package virtualgambling.model.stockdatasource;

import java.util.Date;

import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockexchange.StockDataSource;

/**
 * Created by gajjar.s, on 9:46 PM, 11/12/18
 */
public class SimpleStockExchange implements StockExchange {

  public SimpleStockExchange(StockDataSource stockDataSource) {

  }

  @Override
  public Share getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    return null;
  }
}
