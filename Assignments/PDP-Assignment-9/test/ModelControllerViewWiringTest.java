import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import util.TestUtils;
import virtualgambling.controller.Constants;
import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.view.TextView;

/**
 * The class represents a Junit class to test wiring of Model controller and view in isolation.
 */
public class ModelControllerViewWiringTest {

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  @Test
  public void testingModelControllerViewWiring() {
    Readable readable = new StringReader("1 p1 3 p1 2018-11-11 4 p1 2018-11-11 5 p1 2 6 7 AAPL p1" +
            " 2018-11-11 10 q");
    Appendable appendable = new StringBuffer();
    StringBuilder log = new StringBuilder();

    int createPortfolioCode = RANDOM.nextInt();
    int getAllPortfolioNamesCode = RANDOM.nextInt();
    int getRemainingCapitalCode = RANDOM.nextInt();
    int buySharesCode = RANDOM.nextInt();
    int getPortfolioCode = RANDOM.nextInt();
    StringBuilder expectedLog = new StringBuilder();

    MockModel userModel = new MockModel(log,
            createPortfolioCode,
            getAllPortfolioNamesCode,
            getRemainingCapitalCode,
            buySharesCode, getPortfolioCode);
    Controller controller = new TradingController(
            userModel,
            new TextView(readable, appendable));

    controller.run();


    expectedLog.append(createPortfolioCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getPortfolioCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getPortfolioCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getPortfolioCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getAllPortfolioNamesCode);
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getRemainingCapitalCode);
    expectedLog.append(System.lineSeparator());

    expectedLog.append(buySharesCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("AAPL").append("p1").append("Sun Nov 11 00:00:00 EST 2018").append("10");
    expectedLog.append(System.lineSeparator());

    Assert.assertEquals(expectedLog.toString(), log.toString());

    StringBuilder expectedOutput = new StringBuilder(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$0.00");

    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$0.00");

    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(userModel.getPortfolio("p1").toString());

    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator());

    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator()).append("$5.00");

    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 11 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-11");
    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuStringOfTradingController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  private static class MockModel implements UserModel {

    private final StringBuilder log;
    private final int createPortfolioCode;
    private final int getAllPortfolioNamesCode;
    private final int getRemainingCapitalCode;
    private final int buySharesCode;
    private final int getPortfolioCode;

    private MockModel(StringBuilder log, int createPortfolioCode,
                      int getAllPortfolioNamesCode, int getRemainingCapitalCode,
                      int buySharesCode, int getPortfolioCode) {
      this.log = log;
      this.createPortfolioCode = createPortfolioCode;
      this.getAllPortfolioNamesCode = getAllPortfolioNamesCode;
      this.getRemainingCapitalCode = getRemainingCapitalCode;
      this.buySharesCode = buySharesCode;
      this.getPortfolioCode = getPortfolioCode;
    }

    @Override
    public void createPortfolio(String portfolioName) {
      this.log.append(createPortfolioCode);
      this.log.append(System.lineSeparator());
      this.log.append(portfolioName);
      this.log.append(System.lineSeparator());
    }

    @Override
    public Portfolio getPortfolio(String portfolioName) throws PortfolioNotFoundException {
      this.log.append(getPortfolioCode);
      this.log.append(System.lineSeparator());
      this.log.append(portfolioName);
      this.log.append(System.lineSeparator());
      return new Portfolio("random", new SimpleStockDAO(new SimpleStockDataSource()),
              Collections.emptyList());
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
      this.log.append(getAllPortfolioNamesCode);
      this.log.append(System.lineSeparator());
      return Collections.emptyList();
    }

    @Override
    public BigDecimal getRemainingCapital() {
      this.log.append(getRemainingCapitalCode);
      this.log.append(System.lineSeparator());
      return new BigDecimal(5);
    }

    @Override
    public SharePurchaseOrder buyShares(String tickerName, String portfolioName, Date date,
                                        long quantity) throws IllegalArgumentException,
            StockDataNotFoundException, InsufficientCapitalException {
      this.log.append(buySharesCode);
      this.log.append(System.lineSeparator());
      this.log.append(tickerName).append(portfolioName).append(date).append(quantity);
      this.log.append(System.lineSeparator());
      return new SharePurchaseOrder(tickerName, BigDecimal.TEN, date, 11);
    }
  }
}
