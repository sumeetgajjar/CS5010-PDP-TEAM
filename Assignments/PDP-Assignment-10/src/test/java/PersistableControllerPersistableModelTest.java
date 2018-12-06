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

  @Test
  public void equalWeightsStrategyAfterCreatingItWorks_SaveAndLoad() {
    Readable readable = new StringReader("15 Files/controller-strategy1.json 1 2018-01-11 - 10 2 " +
            "AAPL GOOG 14 p1 Files/controller-strategy1.json 10000 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_FILE_SAVE_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SELECT_STRATEGY_TO_CREATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_SAVED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.LOAD_STRATEGY_FROM_FILE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_LOADED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("" +
            "Buy Date            Stocks              Quantity            Cost Price          " +
            "Current Value       Commission Percentage\n" +
            "2018-01-11          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-11          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-21          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-21          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-31          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-31          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-10          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-10          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-20          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-20          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-02          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-02          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-12          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-12          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-22          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-22          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-01          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-01          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-11          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-11          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-21          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-21          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-01          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-01          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-11          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-11          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-21          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-21          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-31          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-31          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-10          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-10          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-20          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-20          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-30          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-30          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-10          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-10          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-20          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-20          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-30          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-30          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-09          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-09          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-19          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-19          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-29          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-29          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-08          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-08          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-18          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-18          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-28          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-28          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-08          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-08          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-18          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-18          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-28          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-28          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-07          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-07          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-17          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-17          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-27          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-27          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "\n" +
            "Total Value:                                      $296,802.00\n" +
            "Total Cost (excluding commission):                $296,802.00\n" +
            "Total Cost (including commission):                $326,482.20\n" +
            "Profit:                                           ($29,680.20)");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void unequalWeightsStrategyAfterCreatingItWorks_SaveAndLoad() {
    Readable readable = new StringReader("15 Files/controller-strategy2.json 2 2018-01-11 - 10 2 " +
            "AAPL 80 GOOG 20 14 p1 Files/controller-strategy2.json 10000 10 5 p1 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_FILE_SAVE_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SELECT_STRATEGY_TO_CREATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_SAVED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.LOAD_STRATEGY_FROM_FILE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_LOADED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Buy Date            Stocks             " +
            " Quantity            Cost Price          Current Value       Commission Percentage\n" +
            "2018-01-11          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-11          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-21          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-21          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-31          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-31          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-10          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-10          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-20          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-20          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-02          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-02          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-12          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-12          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-22          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-22          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-01          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-01          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-11          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-11          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-21          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-21          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-01          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-01          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-11          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-11          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-21          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-21          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-31          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-31          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-10          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-10          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-20          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-20          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-30          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-30          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-10          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-10          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-20          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-20          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-30          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-30          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-09          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-09          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-19          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-19          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-29          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-29          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-08          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-08          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-18          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-18          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-28          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-28          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-08          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-08          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-18          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-18          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-28          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-28          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-07          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-07          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-17          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-17          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-27          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-27          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "\n" +
            "Total Value:                                      $329,703.00\n" +
            "Total Cost (excluding commission):                $329,703.00\n" +
            "Total Cost (including commission):                $362,673.30\n" +
            "Profit:                                           ($32,970.30)");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void equalWeightsStrategyAfterCreatingItWorksInExistingPortfolio_SaveAndLoad() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 0 15 " +
            "Files/controller-strategy1.json 1 2018-01-11 - 10 2" +
            " AAPL GOOG 14 p1 Files/controller-strategy1.json 10000 10 5 p1 quit");

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
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_FILE_SAVE_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SELECT_STRATEGY_TO_CREATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_SAVED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.LOAD_STRATEGY_FROM_FILE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_LOADED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("" +
            "Buy Date            Stocks              Quantity            Cost Price          " +
            "Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                10                  $30.00              $2," +
            "000.00           0.0%\n" +
            "2018-01-11          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-11          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-21          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-21          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-31          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-31          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-10          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-10          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-20          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-20          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-02          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-02          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-12          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-12          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-22          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-22          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-01          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-01          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-11          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-11          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-21          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-21          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-01          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-01          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-11          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-11          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-21          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-21          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-31          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-31          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-10          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-10          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-20          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-20          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-30          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-30          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-10          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-10          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-20          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-20          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-30          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-30          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-09          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-09          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-19          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-19          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-29          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-29          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-08          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-08          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-18          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-18          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-28          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-28          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-08          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-08          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-18          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-18          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-28          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-28          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-07          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-07          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-17          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-17          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-27          GOOG                454                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-27          AAPL                2                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "\n" +
            "Total Value:                                      $316,802.00\n" +
            "Total Cost (excluding commission):                $297,102.00\n" +
            "Total Cost (including commission):                $326,782.20\n" +
            "Profit:                                           ($9,980.20)");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void unequalWeightsStrategyAfterCreatingItWorksInExistingPortfolio_SaveAndLoad() {
    Readable readable = new StringReader("1 p1 7 AAPL p1 2018-10-30 10 0 15 " +
            "Files/controller-strategy2.json 2 2018-01-11 - 10 2 " +
            "AAPL 80 GOOG 20 14 p1 Files/controller-strategy2.json 10000 10 5 p1 quit");

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
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_FILE_SAVE_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.SELECT_STRATEGY_TO_CREATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.START_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.END_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.RECURRING_INTERVAL_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_COUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STOCK_PERCENTAGE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_SAVED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.LOAD_STRATEGY_FROM_FILE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_AMOUNT_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMISSION_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.STRATEGY_SUCCESSFULLY_LOADED_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Buy Date            Stocks             " +
            " Quantity            Cost Price          Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                10                  $30.00              $2," +
            "000.00           0.0%\n" +
            "2018-01-11          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-11          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-21          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-21          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-01-31          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-01-31          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-10          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-10          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-02-20          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-02-20          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-02          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-02          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-12          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-12          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-03-22          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-03-22          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-01          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-01          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-11          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-11          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-04-21          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-04-21          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-01          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-01          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-11          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-11          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-21          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-21          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-05-31          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-05-31          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-10          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-10          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-20          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-20          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-06-30          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-06-30          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-10          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-10          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-20          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-20          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-07-30          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-07-30          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-09          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-09          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-19          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-19          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-08-29          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-08-29          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-08          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-08          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-18          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-18          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-09-28          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-09-28          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-08          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-08          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-18          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-18          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-10-28          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-10-28          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-07          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-07          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-17          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-17          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "2018-11-27          GOOG                181                 $11.00              $11" +
            ".00              10.0%\n" +
            "2018-11-27          AAPL                4                   $2,000.00           $2," +
            "000.00           10.0%\n" +
            "\n" +
            "Total Value:                                      $349,703.00\n" +
            "Total Cost (excluding commission):                $330,003.00\n" +
            "Total Cost (including commission):                $362,973.30\n" +
            "Profit:                                           ($13,270.30)");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }
}
