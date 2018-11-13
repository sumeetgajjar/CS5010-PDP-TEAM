package virtualgambling.model.stockdatasource;

import java.util.Date;

import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.StockNotFoundException;

public interface StockExchange {
  Share getPrice(String tickerName, Date date) throws StockNotFoundException;

  void addShareData(Share share, Date date);
}
