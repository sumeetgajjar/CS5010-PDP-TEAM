package virtualgambling.model.stockdatasource;

import java.util.Date;

import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.StockDataNotFoundException;

public interface StockExchange {
  Share getPrice(String tickerName, Date date) throws StockDataNotFoundException;
}
