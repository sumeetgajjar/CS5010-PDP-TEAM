package virtualgambling.model.stockexchange;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.exceptions.StockNotFoundException;

/**
 * Created by gajjar.s, on 8:11 PM, 11/11/18
 */
public interface StockDataSource {

  BigDecimal getPrice(String tickerName, Date date) throws StockNotFoundException;
}
