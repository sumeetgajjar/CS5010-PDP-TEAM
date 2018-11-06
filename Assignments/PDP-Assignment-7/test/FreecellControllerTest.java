import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

import freecell.controller.FreecellController;

/**
 * Represents tests that are run on the <code>FreecellController</code> that implements
 * <code>IFreecellController</code>.
 */
public class FreecellControllerTest {

  @Test
  public void constructingControllerWithNullParamsFails() {
    StringReader stringReader = new StringReader("C1 8 F1 q");
    StringBuffer stringBuffer = new StringBuffer();

    try {
      new FreecellController(null, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      new FreecellController(null, stringBuffer);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      new FreecellController(stringReader, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }
}
