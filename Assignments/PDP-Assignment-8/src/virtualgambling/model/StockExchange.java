package virtualgambling.model;

import java.util.Date;

import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.StockPriceNotFoundException;

public interface StockExchange {
  Share getPrice(String tickerName, Date date) throws StockPriceNotFoundException;

  void addShareData(Share share, Date date);
}
