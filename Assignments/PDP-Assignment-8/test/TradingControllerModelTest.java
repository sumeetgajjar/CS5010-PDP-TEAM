import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;

import util.TestUtils;
import util.Utils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

// todo write buy shares share purchase info output in stringbuffer
public class TradingControllerModelTest {
  @Test
  public void creatingPortfolioWorks() {
    Readable readable = new StringReader("create_portfolio p1\ncreate_portfolio p2\n" +
            "get_all_portfolios\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable,
                    appendable));
    controller.go();
    Assert.assertEquals("p1\np2\n", appendable.toString());
  }

  @Test
  public void getCostBasisForPortfolio() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10" +
            "\nget_portfolio_cost_basis p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.go();

    NumberFormat numberFormat = Utils.getCurrencyNumberFormatter();

    StringBuilder builder = new StringBuilder();
    builder.append("Purchased 10 share(s) of 'AAPL' at a rate of 30 per stock on 2018-10-30");
    builder.append(System.lineSeparator());
    builder.append(numberFormat.format(new BigDecimal("300")));
    builder.append(System.lineSeparator());

    Assert.assertEquals(builder.toString(), appendable.toString());
  }

  @Test
  public void getPortfolioValueWorks() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10" +
            "\nget_portfolio_value p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable,
                    appendable));

    controller.go();

    StringBuilder builder = new StringBuilder("Purchased 10 share(s) of 'AAPL' at a rate of 30 " +
            "per stock on 2018-10-30");
    builder.append(System.lineSeparator());
    builder.append(Utils.getFormattedCurrencyNumberString(new BigDecimal("100")));
    builder.append(System.lineSeparator());

    Assert.assertEquals(builder.toString(), appendable.toString());
  }

  @Test
  public void emptyPortfolioCompositionWorks() {
    Readable readable = new StringReader("create_portfolio p1\nget_portfolio_composition p1\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.go();
    Assert.assertEquals("", appendable.toString());
  }

  @Test
  public void portfolioCompositionWorks() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 1" +
            "\nget_portfolio_composition p1\nquit");
    Appendable appendable = new StringBuffer();
    UserModel mockedUserModel = TestUtils.getMockedUserModel();
    Controller controller = new TradingController(mockedUserModel,
            new TextView(readable, appendable));

    controller.go();

    StringBuilder builder = new StringBuilder("Purchased 1 share(s) of 'AAPL' at a rate of 30 per" +
            " stock on 2018-10-30");
    builder.append(System.lineSeparator());
    builder.append(mockedUserModel.getPortfolioComposition("p1"));
    builder.append(System.lineSeparator());

    Assert.assertEquals(builder.toString(), appendable.toString());
  }

  @Test
  public void getRemainingCapitalWorks() {
    Readable readable = new StringReader("get_remaining_capital\ncreate_portfolio p1\n" +
            "get_remaining_capital\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(), new TextView(
            readable, appendable));
    controller.go();
    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL.toPlainString(), appendable.toString());
  }


}