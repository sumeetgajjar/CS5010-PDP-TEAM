package util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.SimpleStockExchange;
import virtualgambling.model.stockdatasource.StockExchange;
import virtualgambling.model.stockexchange.StockDataSource;

public class TestUtils {
  public static final BigDecimal DEFAULT_USER_CAPITAL = new BigDecimal("10000000");

  public static UserModel getMockedUserModel() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1);
    return new MockUserModel(calendar.getTime());
  }

  public static class MockUserModel extends SimpleUserModel implements UserModel {

    private final StockExchange stockExchange;
    private Date mockedTodayDate;

    private MockUserModel(StockExchange stockExchange) {
      super(stockExchange);
      this.stockExchange = stockExchange;
    }

    MockUserModel(Date mockedTodayDate) throws IllegalArgumentException {
      this(new SimpleStockExchange(new MockDataSource()));
      this.mockedTodayDate = Utils.requireNonNull(mockedTodayDate);
    }

    @Override
    public String getPortfolioComposition(String portfolioName) throws IllegalArgumentException {
      return TestUtils.getPortfolioComposition(
              Collections.singletonList(new Share("AAPL", new BigDecimal("30"))),
              Collections.singletonList(new BigDecimal("30")),
              Collections.singletonList(new BigDecimal("30")),
              Collections.singletonList(this.mockedTodayDate),
              new BigDecimal("30"),
              new BigDecimal("30")
      );
    }

  }

  public static class MockDataSource implements StockDataSource {

    @Override
    public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
      if (tickerName.equals("AAPL")) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
        Date day3 = calendar.getTime();
        if (date.equals(day3)) {
          return BigDecimal.TEN;
        }

        calendar.add(Calendar.DATE, -1);
        Date day2 = calendar.getTime();
        if (date.equals(day2)) {
          return new BigDecimal(20);
        }

        calendar.add(Calendar.DATE, -2);
        Date day1 = calendar.getTime();
        if (date.equals(day1)) {
          return new BigDecimal(30);
        }

      } else if (tickerName.equals("GOOG")) {
        return new BigDecimal("11");
      }

      throw new StockDataNotFoundException();
    }
  }

  private static String getPortfolioComposition(List<Share> shares, List<BigDecimal> currentValue,
                                                List<BigDecimal> costPrice, List<Date> buyDates,
                                                BigDecimal totalCost, BigDecimal totalValue) {
    StringBuilder composition = new StringBuilder();
    composition.append("Buy Date\tStocks\tCost Price\tCurrent Value");
    for (int i = 0; i < shares.size(); i++) {
      composition.append(buyDates.get(i));
      composition.append("\t");
      composition.append(shares.get(i));
      composition.append("\t");
      composition.append(costPrice.get(i));
      composition.append("\t");
      composition.append(currentValue.get(i));
      composition.append(System.lineSeparator());
    }

    composition.append("Total Value:\t");
    composition.append(Utils.getCurrencyNumberFormatter().format(totalCost));
    composition.append(System.lineSeparator());

    composition.append("Total Cost:\t");
    composition.append(Utils.getCurrencyNumberFormatter().format(totalValue));
    composition.append(System.lineSeparator());

    composition.append("Profit:\t");
    composition.append(Utils.getCurrencyNumberFormatter().format(totalValue.subtract(totalCost)));
    return composition.toString();
  }
}
