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
}
