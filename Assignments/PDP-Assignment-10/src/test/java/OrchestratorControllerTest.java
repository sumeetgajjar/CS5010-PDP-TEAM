import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;

import util.Constants;
import util.TestUtils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.OrchestratorController;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * This class represents a Junit class to test OrchestratorController.
 */
public class OrchestratorControllerTest {

  @Test
  public void quitWorks() {
    for (String quit : Arrays.asList("q", "quit")) {
      Readable readable = new StringReader(quit);
      Appendable appendable = new StringBuffer();
      View view = new TextView(readable, appendable);
      Controller controller = new OrchestratorController(view);
      controller.run();

      String expectedOutput =
              TestUtils.getWelcomeMessage() + System.lineSeparator()
                      + TestUtils.getMenuMessageOfOrchestrator() +
                      System.lineSeparator();
      Assert.assertEquals(expectedOutput, appendable.toString());
    }
  }

  @Test
  public void invalidInputAsksForRetry() {
    Readable readable = new StringReader("asd q");
    Appendable appendable = new StringBuffer();
    View view = new TextView(readable, appendable);
    Controller controller = new OrchestratorController(view);
    controller.run();

    String expectedOutput =
            TestUtils.getWelcomeMessage() + System.lineSeparator()
                    + TestUtils.getMenuMessageOfOrchestrator()
                    + System.lineSeparator() + Constants.INVALID_CHOICE_MESSAGE
                    + System.lineSeparator()
                    + TestUtils.getMenuMessageOfOrchestrator() +
                    System.lineSeparator();
    Assert.assertEquals(expectedOutput, appendable.toString());
  }

  @Test
  public void dataSourceSelectionWorks() {
    for (int i : Arrays.asList(1, 2)) {
      Readable readable = new StringReader(i + " quit");
      Appendable appendable = new StringBuffer();
      View view = new TextView(readable, appendable);
      Controller controller = new OrchestratorController(view);
      controller.run();

      String expectedOutput =
              TestUtils.getWelcomeMessage() + System.lineSeparator()
                      + TestUtils.getMenuMessageOfOrchestrator()
                      + System.lineSeparator()
                      + TestUtils.getMenuMessageOfPersistableTradingController()
                      + System.lineSeparator();
      Assert.assertEquals(expectedOutput, appendable.toString());
    }
  }
}
