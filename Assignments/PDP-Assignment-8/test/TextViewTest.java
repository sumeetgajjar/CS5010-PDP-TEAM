import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 5:13 PM, 11/14/18
 */
public class TextViewTest {

  @Test
  public void getInputFromViewWorks() throws IOException {
    Readable readable = new StringReader("line1\nline2\n");
    Appendable appendable = new StringBuffer();

    View view = new TextView(readable, appendable);
    Assert.assertEquals("line1", view.getInput());
    Assert.assertEquals("line2", view.getInput());
  }

  @Test
  public void displayTextOnViewWorks() throws IOException {
    Readable readable = new StringReader("");
    Appendable appendable = new StringBuffer();

    View view = new TextView(readable, appendable);
    view.display("line1");
    Assert.assertEquals("line1", appendable.toString());

    view.display("line2");
    Assert.assertEquals("line1line2", appendable.toString());
  }
}
