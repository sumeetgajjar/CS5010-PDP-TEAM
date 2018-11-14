import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;

import util.TestUtils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.TradingController;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

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
    Assert.assertEquals("p1\np2", appendable.toString());
  }

  @Test
  public void getCostBasisForPortfolio() {
    Readable readable = new StringReader("create_portfolio p1\nbuy_shares AAPL p1 2018-10-30 10" +
            "\nget_portfolio_cost_basis p1 2018-11-01\nquit");
    Appendable appendable = new StringBuffer();
    Controller controller = new TradingController(TestUtils.getMockedUserModel(),
            new TextView(readable, appendable));

    controller.go();

    NumberFormat numberFormat = TestUtils.getCurrencyNumberFormatter();
    Assert.assertEquals(numberFormat.format(new BigDecimal("300")), appendable.toString());
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

    NumberFormat numberFormat = TestUtils.getCurrencyNumberFormatter();
    Assert.assertEquals(numberFormat.format(new BigDecimal("100")), appendable.toString());
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
    Assert.assertEquals(mockedUserModel.getPortfolioComposition("p1"),
            appendable.toString());
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