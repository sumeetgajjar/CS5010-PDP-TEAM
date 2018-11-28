import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;

import util.TestUtils;
import virtualgambling.controller.Constants;
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

      StringBuilder expectedOutput = new StringBuilder(TestUtils.getWelcomeMessage1());
      expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuMessageOfOrchestrator());
      expectedOutput.append(System.lineSeparator());
    }
  }

  @Test
  public void invalidInputAsksForRetry() {
    Readable readable = new StringReader("asd q");
    Appendable appendable = new StringBuffer();
    View view = new TextView(readable, appendable);
    Controller controller = new OrchestratorController(view);
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(TestUtils.getWelcomeMessage1());
    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuMessageOfOrchestrator());
    expectedOutput.append(System.lineSeparator()).append(Constants.INVALID_CHOICE_MESSAGE);
    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuMessageOfOrchestrator());
    expectedOutput.append(System.lineSeparator());
  }

  @Test
  public void dataSourceSelectionWorks() {
    for (int i : Arrays.asList(1, 2)) {
      Readable readable = new StringReader(i + " quit");
      Appendable appendable = new StringBuffer();
      View view = new TextView(readable, appendable);
      Controller controller = new OrchestratorController(view);
      controller.run();

      StringBuilder expectedOutput = new StringBuilder(TestUtils.getWelcomeMessage1());
      expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuMessageOfOrchestrator());
      expectedOutput.append(System.lineSeparator())
              .append(TestUtils.getMenuMessageOfEnhanceTradingController());
      expectedOutput.append(System.lineSeparator());
    }
  }
}
