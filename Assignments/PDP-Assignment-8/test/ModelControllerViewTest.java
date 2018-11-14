import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import util.TestUtils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.SharePurchaseInfo;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.view.TextView;

/**
 * Created by gajjar.s, on 5:17 PM, 11/14/18
 */
public class ModelControllerViewTest {

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  @Test
  public void testingControllerSendsCorrectInputToModel() {
    Readable readable = new StringReader("create_portfolio p1\nget_portfolio_cost_basis p1 "
            + "2018-11-11\nget_portfolio_value p1 2018-11-11\nget_portfolio_composition "
            + "p1\nget_all_portfolios\nget_remaining_capital\nbuy_shares AAPL p1 2018-11-11 10\nq");

    Appendable appendable = new StringBuffer();
    StringBuilder log = new StringBuilder();

    int createPortfolioCode = RANDOM.nextInt();
    int getCostBasisOfPortfolioCode = RANDOM.nextInt();
    int getPortfolioValueCode = RANDOM.nextInt();
    int getPortfolioCompositionCode = RANDOM.nextInt();
    int getAllPortfolioNamesCode = RANDOM.nextInt();
    int getRemainingCapitalCode = RANDOM.nextInt();
    int buySharesCode = RANDOM.nextInt();
    StringBuilder expectedLog = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    Controller controller = new TradingController(
            new MockModel(log,
                    createPortfolioCode,
                    getCostBasisOfPortfolioCode,
                    getPortfolioValueCode,
                    getPortfolioCompositionCode,
                    getAllPortfolioNamesCode,
                    getRemainingCapitalCode,
                    buySharesCode),
            new TextView(readable, appendable));

    controller.run();

    expectedOutput.append(TestUtils.getWelcomeMessage()).append(System.lineSeparator())
            .append("$1.00").append(System.lineSeparator())
            .append("$2.00").append(System.lineSeparator())
            .append("3").append(System.lineSeparator())
            .append("4").append(System.lineSeparator())
            .append("$5.00").append(System.lineSeparator())
            .append("Purchased 11 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-11")
            .append(System.lineSeparator());


    expectedLog.append(createPortfolioCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getCostBasisOfPortfolioCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1").append("Sun Nov 11 00:00:00 EST 2018");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getPortfolioValueCode);
    expectedLog.append(System.lineSeparator());
    expectedLog.append("p1").append("Sun Nov 11 00:00:00 EST 2018");
    expectedLog.append(System.lineSeparator());

    expectedLog.append(getPortfolioCompositionCode);
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
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }


  private static class MockModel implements UserModel {

    private final StringBuilder log;
    private final int createPortfolioCode;
    private final int getCostBasisOfPortfolioCode;
    private final int getPortfolioValueCode;
    private final int getPortfolioCompositionCode;
    private final int getAllPortfolioNamesCode;
    private final int getRemainingCapitalCode;
    private final int buySharesCode;

    private MockModel(StringBuilder log, int createPortfolioCode, int getCostBasisOfPortfolioCode
            , int getPortfolioValueCode, int getPortfolioCompositionCode,
                      int getAllPortfolioNamesCode, int getRemainingCapitalCode,
                      int buySharesCode) {
      this.log = log;
      this.createPortfolioCode = createPortfolioCode;
      this.getCostBasisOfPortfolioCode = getCostBasisOfPortfolioCode;
      this.getPortfolioValueCode = getPortfolioValueCode;
      this.getPortfolioCompositionCode = getPortfolioCompositionCode;
      this.getAllPortfolioNamesCode = getAllPortfolioNamesCode;
      this.getRemainingCapitalCode = getRemainingCapitalCode;
      this.buySharesCode = buySharesCode;
    }

    @Override
    public void createPortfolio(String portfolioName) {
      this.log.append(createPortfolioCode);
      this.log.append(System.lineSeparator());
      this.log.append(portfolioName);
      this.log.append(System.lineSeparator());
    }

    @Override
    public BigDecimal getCostBasisOfPortfolio(String portfolioName, Date date) {
      this.log.append(getCostBasisOfPortfolioCode);
      this.log.append(System.lineSeparator());
      this.log.append(portfolioName).append(date);
      this.log.append(System.lineSeparator());
      return BigDecimal.ONE;
    }

    @Override
    public BigDecimal getPortfolioValue(String portfolioName, Date date) {
      this.log.append(getPortfolioValueCode);
      this.log.append(System.lineSeparator());
      this.log.append(portfolioName).append(date);
      this.log.append(System.lineSeparator());
      return new BigDecimal(2);
    }

    @Override
    public String getPortfolioComposition(String portfolioName) throws IllegalArgumentException {
      this.log.append(getPortfolioCompositionCode);
      this.log.append(System.lineSeparator());
      this.log.append(portfolioName);
      this.log.append(System.lineSeparator());
      return "3";
    }

    @Override
    public String getAllPortfolioNames() {
      this.log.append(getAllPortfolioNamesCode);
      this.log.append(System.lineSeparator());
      return "4";
    }

    @Override
    public BigDecimal getRemainingCapital() {
      this.log.append(getRemainingCapitalCode);
      this.log.append(System.lineSeparator());
      return new BigDecimal(5);
    }

    @Override
    public SharePurchaseInfo buyShares(String tickerName, String portfolioName, Date date,
                                       long quantity) throws IllegalArgumentException,
            StockDataNotFoundException, InsufficientCapitalException {
      this.log.append(buySharesCode);
      this.log.append(System.lineSeparator());
      this.log.append(tickerName).append(portfolioName).append(date).append(quantity);
      this.log.append(System.lineSeparator());
      return new SharePurchaseInfo(tickerName, BigDecimal.TEN, date, 11);
    }
  }
}
