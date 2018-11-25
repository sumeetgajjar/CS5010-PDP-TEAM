import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;

import util.TestUtils;
import util.Utils;
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
    Readable readable = new StringReader("create_portfolio p1\ncreate_portfolio p2\n"
            + "get_all_portfolios\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable,
                    appendable));
    controller.run();
    Assert.assertEquals(TestUtils.getWelcomeMessage() + "\np1\np2\n", appendable.toString());
  }

  @Test
  public void getCostBasisForPortfolio() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_cost_basis p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator()
            + "Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on 2018-10-30"
            + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(new BigDecimal("300"))
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void getPortfolioValueWorks() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_value p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable,
                    appendable));

    controller.run();

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator() + "Purchased 10 share"
            + "(s) of "
            + "'AAPL' at a rate of $30"
            + ".00 per stock on 2018-10-30" + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(new BigDecimal("100"))
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void emptyPortfolioCompositionWorks() {
    Readable readable = new StringReader("create_portfolio p1\nget_portfolio_composition p1\n"
            + "quit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getEmptyUserModel(),
            new TextView(readable, appendable));

    controller.run();
    String expected = TestUtils.getWelcomeMessage() + System.lineSeparator() + "Buy Date         " +
            "   Stocks              Quantity            Cost Price          Current Value\n" +
            "\n" +
            "Total Value:        $0.00\n" +
            "Total Cost:         $0.00\n" +
            "Profit:             $0.00\n";
    Assert.assertEquals(expected, appendable.toString());
  }

  @Test
  public void portfolioCompositionWorks() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 1"
            + "\nget_portfolio_composition p1\nquit");
    Appendable appendable = new StringBuffer();
    UserModel mockedUserModel = TestUtils.getMockedUserModel();
    Controller controller = new TradingController(mockedUserModel,
            new TextView(readable, appendable));

    controller.run();

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator() + "Purchased 1 share"
            + "(s) of "
            + "'AAPL' at a rate of $30.00 per stock on 2018-10-30"
            + System.lineSeparator()
            + mockedUserModel.getPortfolio("p1").toString()
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void getRemainingCapitalWorks() {
    Readable readable = new StringReader("get_remaining_capital\ncreate_portfolio p1\n"
            + "get_remaining_capital\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.run();

    String expected =
            TestUtils.getWelcomeMessage() + System.lineSeparator()
                    + Utils.getFormattedCurrencyNumberString(TestUtils.DEFAULT_USER_CAPITAL)
                    + System.lineSeparator()
                    + Utils.getFormattedCurrencyNumberString(TestUtils.DEFAULT_USER_CAPITAL)
                    + System.lineSeparator();
    Assert.assertEquals(expected, appendable.toString());
  }

  @Test
  public void commandNotFoundInformsUser() {
    Readable readable = new StringReader("anything_random\nget_remaining_capital\nrandom_hello"
            + "\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.run();

    String expected = TestUtils.getWelcomeMessage() + System.lineSeparator()
            + "Command not found, please try again" + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(TestUtils.DEFAULT_USER_CAPITAL)
            + System.lineSeparator() + "Command not found, please try again"
            + System.lineSeparator();
    Assert.assertEquals(expected,
            appendable.toString());
  }

  @Test
  public void invalidGetPortfolioValueCommandFails() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_value"
            + "\nget_portfolio_value p1"
            + "\nget_portfolio_value p1 2018-11-"
            + "\nget_portfolio_value p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable,
                    appendable));

    controller.run();
    String invalidCommand = "Incomplete Command, please enter valid parameters";

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator() + "Purchased 10 share"
            + "(s) of "
            + "'AAPL' at a rate of $30"
            + ".00 per stock on 2018-10-30" + System.lineSeparator()
            + invalidCommand + System.lineSeparator()
            + invalidCommand + System.lineSeparator()
            + "Invalid date format" + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(new BigDecimal("100"))
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void dateStringWithHourMinutesAndSecondsWorks() {
    Readable readable = new StringReader("create_portfolio p1\nget_portfolio_cost_basis p1"
            + " 2018-11-01:12:11:21\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(new BigDecimal("0"))
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void incompleteCostBasisForPortfolioAsksToRetry() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_cost_basis p1\nget_portfolio_cost_basis "
            + "2018-11-10\nget_portfolio_cost_basis p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator() + "Purchased 10 share"
            + "(s) of "
            + "'AAPL' at a rate of $30.00 per stock on "
            + "2018-10-30" + System.lineSeparator()
            + "Incomplete Command, please enter valid parameters" + System.lineSeparator()
            + "Incomplete Command, please enter valid parameters"
            + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(new BigDecimal("300"))
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void incompleteBuySharesForPortfolioAsksToRetry() {
    Readable readable = new StringReader("create_portfolio p1\n"
            + "buy_shares AAPL\n"
            + "buy_shares AAPL p1 2018\n"
            + "buy_shares AAPL 2018-1\n"
            + "buy_shares AAPL 2018-10-30\n"
            + "buy_shares AAPL p1 2018-10-30\n"
            + "buy_shares AAPL p1 2018-10-30 10\n"
            + "buy_shares AAPL p1 2018-10-30 asd\n"
            + "buy_shares APL p1 2018-10-30 10\n"
            + "get_portfolio_cost_basis p1 2018-11-01\n"
            + "quit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();
    String invalidCommand = "Incomplete Command, please enter valid parameters";

    String builder = TestUtils.getWelcomeMessage()
            + System.lineSeparator() + invalidCommand + System.lineSeparator()
            + "Invalid date format" + System.lineSeparator()
            + invalidCommand + System.lineSeparator()
            + invalidCommand + System.lineSeparator()
            + invalidCommand + System.lineSeparator()
            + "Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on "
            + "2018-10-30" + System.lineSeparator()
            + "Invalid quantity of shares" + System.lineSeparator()
            + "Stock Data not found" + System.lineSeparator()
            + Utils.getFormattedCurrencyNumberString(new BigDecimal("300"))
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void validCreatePortfolioInvalidParameters() {
    Readable readable = new StringReader("create_portfolio\ncreate_portfolio \nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.run();

    String expected = TestUtils.getWelcomeMessage() + System.lineSeparator()
            + "Incomplete Command, please enter valid parameters" + System.lineSeparator()
            + "Incomplete Command, please enter valid parameters"
            + System.lineSeparator();
    Assert.assertEquals(expected, appendable.toString());
  }

  @Test
  public void spaceInPortfolioNameLeadsToIgnoringOf2ndWord() {
    Readable readable = new StringReader("create_portfolio word1 word2\nget_all_portfolios\n"
            + "quit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.run();

    String expected = TestUtils.getWelcomeMessage()
            + System.lineSeparator() + "word1" + System.lineSeparator();
    Assert.assertEquals(expected, appendable.toString());
  }

  @Test
  public void portfolioCompositionWithoutValidPortfolioNameDoesNotWork() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 1"
            + "\nget_portfolio_composition\nget_portfolio_composition "
            + "p\nget_portfolio_composition p1\nquit");
    Appendable appendable = new StringBuffer();
    UserModel mockedUserModel = TestUtils.getMockedUserModel();
    Controller controller = new TradingController(mockedUserModel,
            new TextView(readable, appendable));

    controller.run();

    String builder = TestUtils.getWelcomeMessage() + System.lineSeparator() + "Purchased 1 share"
            + "(s) of "
            + "'AAPL' at a rate of $30.00 per stock on 2018-10-30"
            + System.lineSeparator()
            + "Incomplete Command, please enter valid parameters"
            + System.lineSeparator()
            + "Portfolio not found"
            + System.lineSeparator()
            + mockedUserModel.getPortfolio("p1").toString()
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
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


      Controller controller = new TradingController(TestUtils.getEmptyUserModel(),
              new TextView(readable, appendable));

      controller.run();
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("Cannot display data on view", e.getMessage());
    }
  }
}