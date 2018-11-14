package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.bean.SharePurchaseInfo;
import virtualgambling.model.exceptions.StockDataNotFoundException;

public interface StockExchange {
  SharePurchaseInfo buyShares(String tickerName, long quantity, Date date,
                              BigDecimal remainingCapital);

  BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException;
}
