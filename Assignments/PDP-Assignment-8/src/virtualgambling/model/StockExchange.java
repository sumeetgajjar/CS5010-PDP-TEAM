package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.exceptions.StockPriceNotFoundException;

/**
 * Created by gajjar.s, on 8:11 PM, 11/11/18
 */
public interface StockExchange {

  BigDecimal getPrice(String tickerName, Date date) throws StockPriceNotFoundException;
}
