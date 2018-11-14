import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;

import util.TestUtils;
import util.Utils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

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
    Assert.assertEquals(getWelcomeMessage() + "\np1\np2\n", appendable.toString());
  }

  @Test
  public void getCostBasisForPortfolio() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_cost_basis p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();

    String builder = getWelcomeMessage() + System.lineSeparator()
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

    String builder = getWelcomeMessage() + System.lineSeparator() + "Purchased 10 share(s) of "
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
    String expected = getWelcomeMessage() + System.lineSeparator() + "Buy Date            Stocks "
            + "             Cost Price          Current "
            + "Value\n" +
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

    String builder = getWelcomeMessage() + System.lineSeparator() + "Purchased 1 share(s) of "
            + "'AAPL' at a rate of $30.00 per stock on 2018-10-30"
            + System.lineSeparator()
            + mockedUserModel.getPortfolioComposition("p1")
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
            getWelcomeMessage() + System.lineSeparator() +
                    Utils.getFormattedCurrencyNumberString(TestUtils.DEFAULT_USER_CAPITAL) +
                    System.lineSeparator() +
                    Utils.getFormattedCurrencyNumberString(TestUtils.DEFAULT_USER_CAPITAL) +
                    System.lineSeparator();
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

    String expected = getWelcomeMessage() + System.lineSeparator() +
            "Command not found, please try again" + System.lineSeparator() +
            Utils.getFormattedCurrencyNumberString(TestUtils.DEFAULT_USER_CAPITAL) +
            System.lineSeparator() + "Command not found, please try again"
            + System.lineSeparator();
    Assert.assertEquals(expected,
            appendable.toString());
  }

  @Test
  public void invalidGetPortfolioValueCommandFails() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_value" +
            "\nget_portfolio_value p1" +
            "\nget_portfolio_value p1 2018-11-" +
            "\nget_portfolio_value p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable,
                    appendable));

    controller.run();
    String invalidCommand = "Invalid Command";

    String builder = getWelcomeMessage() + System.lineSeparator() + "Purchased 10 share(s) of " +
            "'AAPL' at a rate of $30" +
            ".00 per stock on 2018-10-30" + System.lineSeparator() +
            invalidCommand + System.lineSeparator() +
            invalidCommand + System.lineSeparator() +
            "Invalid date format" + System.lineSeparator() +
            Utils.getFormattedCurrencyNumberString(new BigDecimal("100")) +
            System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void incompleteCostBasisForPortfolioAsksToRetry() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10"
            + "\nget_portfolio_cost_basis p1\nget_portfolio_cost_basis " +
            "2018-11-10\nget_portfolio_cost_basis p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();

    String builder = getWelcomeMessage() + System.lineSeparator() + "Purchased 10 share(s) of " +
            "'AAPL' at a rate of $30.00 per stock on " +
            "2018-10-30" + System.lineSeparator() +
            "Invalid Command" + System.lineSeparator() + "Invalid Command"
            + System.lineSeparator() +
            Utils.getFormattedCurrencyNumberString(new BigDecimal("300")) +
            System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void incompleteBuySharesForPortfolioAsksToRetry() {
    Readable readable = new StringReader("create_portfolio p1\n" +
            "buy_shares AAPL\n" +
            "buy_shares AAPL p1 2018\n" +
            "buy_shares AAPL 2018-1\n" +
            "buy_shares AAPL 2018-10-30\n" +
            "buy_shares AAPL p1 2018-10-30\n" +
            "buy_shares AAPL p1 2018-10-30 10\n" +
            "get_portfolio_cost_basis p1 2018-11-01\n" +
            "quit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.run();
    String invalidCommand = "Invalid Command";

    String builder =
            getWelcomeMessage() + System.lineSeparator() + invalidCommand + System.lineSeparator()
                    + "Invalid date format" + System.lineSeparator() +
                    invalidCommand + System.lineSeparator() +
                    invalidCommand + System.lineSeparator() +
                    invalidCommand + System.lineSeparator() +
                    "Purchased 10 share(s) of 'AAPL' at a rate of $30.00 per stock on " +
                    "2018-10-30" + System.lineSeparator() +
                    Utils.getFormattedCurrencyNumberString(new BigDecimal("300")) +
                    System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  @Test
  public void validCreatePortfolioInvalidParameters() {
    Readable readable = new StringReader("create_portfolio\ncreate_portfolio \nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.run();

    String expected = getWelcomeMessage() + System.lineSeparator() +
            "Invalid Command" + System.lineSeparator() + "Invalid Command" + System.lineSeparator();
    Assert.assertEquals(expected, appendable.toString());
  }

  @Test
  public void spaceInPortfolioNameLeadsToIgnoringOf2ndWord() {
    Readable readable = new StringReader("create_portfolio word1 word2\nget_all_portfolios\n" +
            "quit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.run();

    String expected =
            getWelcomeMessage() + System.lineSeparator() + "word1" + System.lineSeparator();
    Assert.assertEquals(expected, appendable.toString());
  }

  @Test
  public void portfolioCompositionWithoutValidPortfolioNameDoesNotWork() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 1"
            + "\nget_portfolio_composition\nget_portfolio_composition " +
            "p\nget_portfolio_composition p1\nquit");
    Appendable appendable = new StringBuffer();
    UserModel mockedUserModel = TestUtils.getMockedUserModel();
    Controller controller = new TradingController(mockedUserModel,
            new TextView(readable, appendable));

    controller.run();

    String builder = getWelcomeMessage() + System.lineSeparator() + "Purchased 1 share(s) of "
            + "'AAPL' at a rate of $30.00 per stock on 2018-10-30"
            + System.lineSeparator()
            + "Invalid Command"
            + System.lineSeparator()
            + "Portfolio not found"
            + System.lineSeparator()
            + mockedUserModel.getPortfolioComposition("p1")
            + System.lineSeparator();
    Assert.assertEquals(builder, appendable.toString());
  }

  private String getWelcomeMessage() {
    return "Welcome to Virtual Stock Trading Application" + System.lineSeparator()
            + "You can use the following example commands" + System.lineSeparator()
            + "create_portfolio portfolioName (portfolioName should be one word): Create an empty"
            + " portfolio with name as portfolioName."
            + System.lineSeparator()
            + "get_all_portfolios: Gets a new line separated string of portfolio names."
            + System.lineSeparator()
            + "get_portfolio_cost_basis portfolioName date: Gets the cost basis of " +
            "portfolioName at the given date in this format --> yyyy-MM-dd."
            + System.lineSeparator()
            + "get_portfolio_value portfolioName date: Gets the value of portFolioName at the " +
            "given date in this format --> yyyy-MM-dd."
            + System.lineSeparator()
            + "get_portfolio_composition portfolioName: Gets the composition of portfolioName"
            + System.lineSeparator()
            + "get_remaining_capital: Gets your remaining in dollar amount"
            + System.lineSeparator()
            + "buy_shares tickerName portfolioName date quantity: Buys the stock with the given " +
            "ticker in portfolioName at a given date and the given quantity."
            + System.lineSeparator()
            + "The quantity must be positive and the date must be a working day not in the future.";
  }
}