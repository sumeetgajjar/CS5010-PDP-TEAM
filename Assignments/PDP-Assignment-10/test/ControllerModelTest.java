import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import util.TestUtils;
import virtualgambling.controller.Constants;
import virtualgambling.controller.Controller;
import virtualgambling.model.UserModel;

/**
 * This class represents a Junit test class to test controller and model.
 */
public abstract class ControllerModelTest {

  protected UserModel userModel;

  protected abstract UserModel getUserModel();

  protected abstract Controller getController(Readable readable, Appendable appendable);

  protected abstract String getMenuStringOfController();

  @Test
  public void emptyPortfolioCompositionWorks() {
    Readable readable = new StringReader("1 p1 5 p1 quit");
    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator())
            .append(this.userModel.getPortfolio("p1").toString());
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void getRemainingCapitalWorks() {
    Readable readable = new StringReader("6 1 p1 6 q");
    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);
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
  public void invalidGetPortfolioValueCommandFails() {
    Readable readable = new StringReader("1 p1 4 p2 2018-11-01 quit");
    Appendable appendable = new StringBuffer();
    UserModel userModel = TestUtils.getEmptySimpleUserModel();
    Controller controller = getController(readable, appendable);

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
    Controller controller = getController(readable, appendable);

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
  public void spaceInPortfolioNameLeadsToIgnoringOf2ndWord() {
    Readable readable = new StringReader("1 word1 word2 2 quit");

    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);

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
    Controller controller = getController(readable, appendable);

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

      Controller controller = getController(readable, appendable);

      controller.run();
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("Cannot display data on view", e.getMessage());
    }
  }

  @Test
  public void closingReadableBeforeGivingToController() throws IOException {
    try {
      BufferedReader readable =
              new BufferedReader(
                      new InputStreamReader(
                              new ByteArrayInputStream("C1 q".getBytes())));

      readable.close();

      Appendable appendable = new StringBuffer();

      Controller controller = getController(readable, appendable);

      controller.run();
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("Cannot get data from view", e.getMessage());
    }
  }

  @Test
  public void commandNotFoundInformsUser() {
    Readable readable = new StringReader("asd q");
    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.COMMAND_NOT_FOUND_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void creatingPortfolioWorks() {
    Readable readable = new StringReader("1 p1 2 q");
    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);
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
  public void unparseableDateFails() {
    Readable readable = new StringReader("1 p1 3 p1 2018-11-00 2018-09-31 2018-11-01 q");
    Appendable appendable = new StringBuffer();
    Controller controller = getController(readable, appendable);
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator()).append(Constants.PORTFOLIO_NAME_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Unparseable date: \"2018-11-00\"");
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("Unparseable date: \"2018-09-31\"");
    expectedOutput.append(System.lineSeparator()).append(Constants.INVESTMENT_DATE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append("$0.00");
    expectedOutput.append(System.lineSeparator()).append(getMenuStringOfController());
    expectedOutput.append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }
}
