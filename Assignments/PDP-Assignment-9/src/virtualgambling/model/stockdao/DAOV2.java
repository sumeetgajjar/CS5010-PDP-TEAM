package virtualgambling.model.stockdao;

import java.util.Calendar;
import java.util.Date;

import util.Utils;
import virtualgambling.model.stockdatasource.StockDataSource;

/**
 * Created by gajjar.s, on 2:35 PM, 11/25/18
 */
public class DAOV2 extends SimpleStockDAO {

  /**
   * Constructs a stockDAO given any implementation of the stock data source.
   *
   * @param stockDataSource the stock data source object
   */
  public DAOV2(StockDataSource stockDataSource) {
    super(stockDataSource);
  }

  @Override
  protected Date getValidDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    while (Utils.isNonWorkingDayOfTheWeek(date)) {
      calendar.add(Calendar.DATE, 1);
      date = calendar.getTime();
    }
    return date;
  }
}
