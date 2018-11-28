import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import util.TestUtils;
import virtualgambling.controller.Constants;
import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

/**
 * The class represents a Junit class to test Controller and Model.
 */
public class TradingControllerModelTest {

  @Test
  public void creatingPortfolioWorks() {
    Readable readable = new StringReader("1 p1 2 q");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(),
            new TextView(readable, appendable));
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append("p1");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getCostBasisForPortfolio() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 3 p1 2018-11-01 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(),
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$300.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getPortfolioValueWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 4 p1 2018-11-01 quit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(),
            new TextView(readable,
                    appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$100.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void emptyPortfolioCompositionWorks() {
    Readable readable = new StringReader("1 p1 5 p1 quit");
    Appendable appendable = new StringBuffer();
    UserModel userModel = TestUtils.getEmptySimpleUserModel();
    Controller controller = new TradingController(userModel,
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(userModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void portfolioCompositionWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 5 p1 quit");
    Appendable appendable = new StringBuffer();
    UserModel mockedUserModel = getUserModel();
    Controller controller = new TradingController(mockedUserModel,
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(mockedUserModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getRemainingCapitalWorks() {
    Readable readable = new StringReader("6 1 p1 6 q");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(), new TextView(
            readable, appendable));
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append("$10,000,000.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append("$10,000,000.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void commandNotFoundInformsUser() {
    Readable readable = new StringReader("asd q");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(), new TextView(
            readable, appendable));
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMAND_NOT_FOUND_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void invalidGetPortfolioValueCommandFails() {
    Readable readable = new StringReader("1 p1 4 p2 2018-11-01 quit");
    Appendable appendable = new StringBuffer();
    UserModel userModel = TestUtils.getEmptySimpleUserModel();
    Controller controller = new TradingController(userModel,
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("portfolio by the name 'p2' not found");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void dateStringWithHourMinutesAndSecondsWorks() {
    Readable readable = new StringReader("1 p1 3 p1 2018-11-01:12:11:21 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(),
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$0.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void incompleteBuySharesForPortfolioAsksToRetry() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10 2018-10-30 a 10 3 p1 2018-11-01 " +
            "quit");

    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(),
            new TextView(readable, appendable));

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
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$300.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void spaceInPortfolioNameLeadsToIgnoringOf2ndWord() {
    Readable readable = new StringReader("1 word1 word2 2 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(getUserModel(),
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMAND_NOT_FOUND_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append("word1");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void portfolioCompositionWithoutValidPortfolioNameDoesNotWork() {
    Readable readable = new StringReader("1 p1 5 p2 quit");
    Appendable appendable = new StringBuffer();
    UserModel userModel = TestUtils.getEmptySimpleUserModel();
    Controller controller = new TradingController(userModel,
            new TextView(readable, appendable));

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("portfolio by the name 'p2' not found");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void closingAppendableBeforeGivingToController() throws IOException {
    try {
      Readable readable = new StringReader("quit\n");

      ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
      BufferedWriter appendable =
              new BufferedWriter(
                      new OutputStreamWriter(
                              outputBuffer));

      appendable.close();


      Controller controller = new TradingController(TestUtils.getEmptySimpleUserModel(),
              new TextView(readable, appendable));

      controller.run();
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("Cannot display data on view", e.getMessage());
    }
  }

  protected UserModel getUserModel() {
    return TestUtils.getMockedUserModel();
  }

  private String getMenuStringOfController() {
    return TestUtils.getMenuStringOfTradingController();
  }
}