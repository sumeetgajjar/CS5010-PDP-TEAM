import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;

import util.TestUtils;
import util.Utils;
import virtualgambling.controller.Constants;
import virtualgambling.controller.Controller;
import virtualgambling.controller.EnhancedTradingController;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

/**
 * The class represents a Junit test class.It contains all the tests for scenarios when
 * EnhancedTradingController is given EnhancedUserModel.
 */
public class EnhancedTradingControllerEnhancedModelTest extends ControllerModelTest {

  private EnhancedUserModel enhancedUserModel;

  @Before
  public void setUp() {
    this.enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    this.userModel = enhancedUserModel;
  }

  @Override
  protected UserModel getUserModel() {
    return userModel;
  }

  @Override
  protected Controller getController(Readable readable,
                                     Appendable appendable) {
    return new EnhancedTradingController(enhancedUserModel,
            new TextView(readable, appendable));
  }

  @Override
  protected String getMenuStringOfController() {
    return TestUtils.getMenuMessageOfEnhanceTradingController();
  }

  @Test
  public void invalidParamsGivenToEnhanceTradingControllerFails() {
    try {
      new EnhancedTradingController(null,
              new TextView(new StringReader(""), System.out));
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      new EnhancedTradingController(this.enhancedUserModel, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buySharesWithCommissionWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 10 3 p1 2018-11-01 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$330.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getCostBasisWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 10 " +
            "3 p1 2018-10-29 " +
            "3 p1 2018-10-30 " +
            "3 p1 2018-11-01 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$0.00");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$330.00");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$330.00");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getPortfolioValueBasisWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 10 " +
            "4 p1 2018-10-29 " +
            "4 p1 2018-10-30 " +
            "4 p1 2018-11-01 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$0.00");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$300.00");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$100.00");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getPortfolioCompositionWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void incompleteBuySharesForPortfolioAsksToRetry() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10 2018-10-30 a 10 a 10 3 p1 " +
            "2018-11-01 " +
            "quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Unparseable date: \"2018-10\"");
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("For input string: \"a\"");
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("For input string: \"a\"");
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$330.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithSameWeights() {
    Readable readable = new StringReader("1 p1 9 p1 2018-11-1 10000 2 AAPL GOOG 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());

    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithDifferentWeights() {
    Readable readable = new StringReader(
            "8 p1 2018-11-1 100000 11 FB 10 AMD 5 ASC 4 AAPL 15 MSFT 6 EBAY 11 GOOG 14 QCOM 10 " +
                    "MU " +
                    "7 NFLX 8 T 10 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    for (int i = 0; i < 11; i++) {
      expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
      expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    }
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 100 share(s) of 'MSFT' at a rate of $60.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 400 share(s) of 'NFLX' at a rate of $20.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 80 share(s) of 'ASC' at a rate of $50.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 1272 share(s) of 'GOOG' at a rate of $11.00 per stock on " +
                    "2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 1500 share(s) of 'AAPL' at a rate of $10.00 per stock on " +
                    "2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 1000 share(s) of 'T' at a rate of $10.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 137 share(s) of 'EBAY' at a rate of $80.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 111 share(s) of 'QCOM' at a rate of $90.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 71 share(s) of 'AMD' at a rate of $70.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 70 share(s) of 'MU' at a rate of $100.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 250 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithDifferentWeightsButStockEnteredMultipleTimes() {
    Readable readable = new StringReader(
            "8 p1 2018-11-1 100000 3 GOOG 10 FB 30 GOOG 70 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    for (int i = 0; i < 3; i++) {
      expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
      expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    }
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 6363 share(s) of 'GOOG' at a rate of $11.00 per stock on " +
                    "2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 750 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithSameWeightsWithRecurrentStrategy() {
    Readable readable = new StringReader("11 p1 2018-11-1 2018-11-4 1 10000 2 AAPL GOOG 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-02");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-02");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-03");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-03");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-04");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-04");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithDifferentWeightsWithRecurrentStrategy() {
    Readable readable = new StringReader("10 p1 2018-11-1 2018-11-4 1 10000 2 AAPL 40 GOOG 60 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 545 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 400 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 545 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-02");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-02");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 545 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-03");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-03");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 545 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-04");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-04");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }


  @Test
  public void buySharesWithInsufficientInvestmentAmountFails() {
    Readable readable = new StringReader(
            "8 p1 2018-11-1 10 5 FB 15 AAPL 25 GOOG 35 NFLX 15 T 10 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_EXECUTION_EXCEPTION_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithSameWeightsWithRecurrentStrategyWithoutEndDate() {
    Date todayDate = Utils.getTodayDate();
    Calendar calendar = Utils.getCalendarInstance();
    calendar.setTime(todayDate);
    int numberOfDays = 3;
    calendar.add(Calendar.DATE, -numberOfDays);
    Date dateBefore3Days = calendar.getTime();
    String startDateString = Utils.getDefaultFormattedDateStringFromDate(dateBefore3Days);

    Readable readable = new StringReader("11 p1 " + startDateString + " - 1 10000 2 AAPL GOOG 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    for (int i = 0; i < numberOfDays; i++) {
      Date date = calendar.getTime();
      String dateString = Utils.getDefaultFormattedDateStringFromDate(date);

      expectedOutput.append(System.lineSeparator())
              .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on " +
                      dateString);
      expectedOutput.append(System.lineSeparator())
              .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                      dateString);
      calendar.add(Calendar.DATE, 1);
    }

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithDifferentWeightsWithRecurrentStrategyWithoutDate() {
    Date todayDate = Utils.getTodayDate();
    Calendar calendar = Utils.getCalendarInstance();
    calendar.setTime(todayDate);
    int numberOfDays = 3;
    calendar.add(Calendar.DATE, -numberOfDays);
    Date dateBefore3Days = calendar.getTime();
    String startDateString = Utils.getDefaultFormattedDateStringFromDate(dateBefore3Days);

    Readable readable = new StringReader("10 p1 " + startDateString + " - 1 10000 2 AAPL 40 GOOG " +
            "60 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);

    for (int i = 0; i < numberOfDays; i++) {
      Date date = calendar.getTime();
      String dateString = Utils.getDefaultFormattedDateStringFromDate(date);

      expectedOutput.append(System.lineSeparator())
              .append("Purchased 545 share(s) of 'GOOG' at a rate of $11.00 per stock on " +
                      dateString);
      expectedOutput.append(System.lineSeparator())
              .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                      dateString);
      calendar.add(Calendar.DATE, 1);
    }

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySameSharesMultipleTimesUsingStrategyInSamePortfolioOnSameDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-01 10000 2 AAPL GOOG 10 " +
            "9 p1 2018-11-01 10000 2 AAPL GOOG 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySameSharesMultipleTimesUsingStrategyInDifferentPortfolioOnSameDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-01 10000 2 AAPL GOOG 10 " +
            "9 p2 2018-11-01 10000 2 AAPL GOOG 10 " +
            "5 p1 5 p2 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p2").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buyDifferentSharesMultipleTimesUsingStrategyInSamePortfolioOnSameDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-01 10000 2 AAPL GOOG 10 " +
            "9 p1 2018-11-01 10000 2 T FB 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'T' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 125 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buyDifferentSharesMultipleTimesUsingStrategyInDifferentPortfolioOnSameDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-01 10000 2 AAPL GOOG 10 " +
            "9 p2 2018-11-01 10000 2 T FB 10 " +
            "5 p1 5 p2 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'T' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 125 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());


    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p2").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySameSharesMultipleTimesUsingStrategyInSamePortfolioOnDifferentDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-1 10000 2 AAPL GOOG 10 " +
            "9 p1 2018-11-14 10000 2 AAPL GOOG 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on " +
                    "2018-11-14");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySameSharesMultipleTimesUsingStrategyInDifferentPortfolioOnDifferentDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-1 10000 2 AAPL GOOG 10 " +
            "9 p2 2018-11-14 10000 2 AAPL GOOG 10 " +
            "5 p1 5 p2 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 2 share(s) of 'AAPL' at a rate of $2,000.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p2").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buyDifferentSharesMultipleTimesUsingStrategyInSamePortfolioOnDifferentDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-1 10000 2 AAPL GOOG 10 " +
            "9 p1 2018-11-14 10000 2 T FB 10 " +
            "5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'T' at a rate of $10.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 125 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buyDifferentSharesMultipleTimesUsingStrategyInDifferentPortfolioOnDifferentDay() {
    Readable readable = new StringReader("1 p1 " +
            "9 p1 2018-11-1 10000 2 AAPL GOOG 10 " +
            "9 p2 2018-11-14 10000 2 T FB 10 " +
            "5 p1 5 p2 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 454 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 500 share(s) of 'T' at a rate of $10.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator())
            .append("Purchased 125 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-14");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p1").toString());


    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(this.enhancedUserModel.getPortfolio("p2").toString());

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }
}
