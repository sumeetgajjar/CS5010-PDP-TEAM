import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

import util.TestUtils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.EnhancedTradingController;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * The class represents a Junit class to test the trading Controller.
 */
public class EnhanceTradingControllerTest {

  @Test
  public void createPortfolioWorks() {
    Readable readable = new StringReader("1 1 asd q");
    Appendable appendable = new StringBuffer();
    View view = new TextView(readable, appendable);
    Controller controller = new EnhancedTradingController(TestUtils.getEmptyEnhancedUserModel(),
            view);
    controller.run();

    StringBuilder expectedOutput = new StringBuilder(TestUtils.getWelcomeMessage1());
    expectedOutput.append(System.lineSeparator()).append(TestUtils.getMenuMessageOfOrchestrator());
    expectedOutput.append(System.lineSeparator())
            .append(TestUtils.getMenuMessageOfEnhanceTradingController());


    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }
}
