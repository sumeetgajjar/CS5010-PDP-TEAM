import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import util.Constants;
import util.TestUtils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.PersistableTradingController;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

public class PersistableControllerPersistableModelTest extends EnhancedTradingControllerPersistableModelTest {
  private PersistableUserModel persistableUserModel;

  @Before
  public void setUp() {
    this.persistableUserModel = TestUtils.getEmptyPersistableUserModel();
    this.userModel = persistableUserModel;
    this.enhancedUserModel = persistableUserModel;
  }

  @Override
  protected UserModel getUserModel() {
    return userModel;
  }

  @Override
  protected String getMenuStringOfController() {
    return TestUtils.getMenuMessageOfPersistableTradingController();
  }

  @Override
  protected Controller getController(Readable readable,
                                     Appendable appendable) {
    return new PersistableTradingController(persistableUserModel, new TextView(readable,
            appendable));
  }

  @Test
  public void savingPortfolioAfterCreatingItWorks() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 0 13 p1 " +
            "Files/controller-p1.json quit");

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
    expectedOutput.append(System.lineSeparator()).append(Constants.ABSOLUTE_PATH_FILE_SAVE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_FILE_SAVE_SUCCESS);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void savingPortfolioBeforeCreatingItIsWrong() {
    Readable readable = new StringReader("13 p1 Files/controller-p1.json quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.ABSOLUTE_PATH_FILE_SAVE);
    expectedOutput.append(System.lineSeparator()).append("portfolio by the name 'p1' not found");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());

  }

  @Test
  public void loadingPortfolioIntoExistingPortfolioOverridesIt() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 0 5 p1 13 p1 " +
            "Files/controller-p1.json 7 GOOG p1 2018-10-30 10 0 5 p1 12 Files/controller-p1.json " +
            "5 p1 quit");

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
    expectedOutput.append(System.lineSeparator()).append("Buy Date            Stocks             " +
            " Quantity            Cost Price          Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                10                  $30.00              $2," +
            "000.00           0.0%\n" +
            "\n" +
            "Total Value:                                      $20,000.00\n" +
            "Total Cost (excluding commission):                $300.00\n" +
            "Total Cost (including commission):                $300.00\n" +
            "Profit:                                           $19,700.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.ABSOLUTE_PATH_FILE_SAVE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_FILE_SAVE_SUCCESS);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SHARE_QUANTITY_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append("Purchased 10 share(s) of 'GOOG' at a rate of $11.00 per stock on 2018-10-30");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Buy Date            Stocks             " +
            " Quantity            Cost Price          Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                10                  $30.00              $2," +
            "000.00           0.0%\n" +
            "2018-10-30          GOOG                10                  $11.00              $11" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $20,110.00\n" +
            "Total Cost (excluding commission):                $410.00\n" +
            "Total Cost (including commission):                $410.00\n" +
            "Profit:                                           $19,700.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.LOAD_PORTFOLIO_FROM_FILE);
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_SUCCESSFULLY_LOADED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Buy Date            Stocks             " +
            " Quantity            Cost Price          Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                10                  $30.00              $2," +
            "000.00           0.0%\n" +
            "\n" +
            "Total Value:                                      $20,000.00\n" +
            "Total Cost (excluding commission):                $300.00\n" +
            "Total Cost (including commission):                $300.00\n" +
            "Profit:                                           $19,700.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void loadingPortfolioWhereFileDoesNotExistDoesNotWork() {
    Readable readable = new StringReader("12 Files/FILE_NOT_EXISTS.json quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.LOAD_PORTFOLIO_FROM_FILE);
    expectedOutput.append(System.lineSeparator()).append("/Users/chintanshah/courses/cs5010/team" +
            "-projects/CS5010-PDP-TEAM/Assignments/PDP-Assignment-10/Files/FILE_NOT_EXISTS.json " +
            "(No such file or directory)");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }
}
