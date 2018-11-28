import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import util.TestUtils;
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
    Readable readable = new StringReader("1 p1 9 p1 2018-11-1 10000 2 AAPL GOOG 10 quit");

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
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void buySharesWithDifferentWeights() {
    Readable readable = new StringReader(
            "1 p1 8 p1 2018-11-1 10000 5 FB 15 AAPL 25 GOOG 35 NFLX 15 T 10 10 quit");

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
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 50 share(s) of 'NFLX' at a rate of $20.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 272 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 200 share(s) of 'AAPL' at a rate of $10.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 100 share(s) of 'T' at a rate of $10.00 per stock on 2018-11-01");
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 25 share(s) of 'FB' at a rate of $40.00 per stock on 2018-11-01");

    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }
}
