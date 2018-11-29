import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import util.TestUtils;
import util.Utils;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
import virtualgambling.model.stockdatasource.StockDataSource;

public class AlphaVantageAPIStockDataSourceTest {

  private StockDataSource dataSource;

  @Before
  public void setUp() {
    dataSource = AlphaVantageAPIStockDataSource.getInstance();
  }

  @Test
  public void getPriceOnHolidayOfValidStockGetsNextAvailableDay() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 22); // thanksgiving goes to friday
    BigDecimal closePriceOn23Nov = dataSource.getPrice("AAPL", calendar.getTime());
    Assert.assertEquals(new BigDecimal("172.29"), closePriceOn23Nov.stripTrailingZeros());

    calendar.set(2018, Calendar.NOVEMBER, 24); // saturday goes to monday
    BigDecimal closePriceOn26Nov = dataSource.getPrice("AAPL", calendar.getTime());
    Assert.assertEquals(new BigDecimal("174.62"), closePriceOn26Nov.stripTrailingZeros());
  }

  @Test(expected = RuntimeException.class)
  public void aFutureDayFails() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2118, Calendar.NOVEMBER, 22);
    dataSource.getPrice("AAPL", calendar.getTime());
  }

  @Test
  public void priceReturnedIsClosingPrice() {
    Date validDateForTrading = TestUtils.getValidDateForTrading();
    BigDecimal aaplPrice = dataSource.getPrice("AAPL", validDateForTrading);
    BigDecimal expectedAAPLClosingPrice = new BigDecimal("222.2200");
    Assert.assertEquals(expectedAAPLClosingPrice, aaplPrice);
  }
}