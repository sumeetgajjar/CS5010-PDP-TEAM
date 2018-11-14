package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.exceptions.StockDataNotFoundException;

public interface StockExchange {
  BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException,
          IllegalArgumentException;
}
