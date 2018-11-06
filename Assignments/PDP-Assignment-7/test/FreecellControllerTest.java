import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import freecell.bean.Card;
import freecell.controller.FreecellController;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;

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

  @Test
  public void invalidModelOrDeckGivenToControllerFails() {
    StringReader stringReader = new StringReader("C1 8 F1 q");
    StringBuffer stringBuffer = new StringBuffer();
    FreecellController freecellController = new FreecellController(stringReader, stringBuffer);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

    try {
      freecellController.playGame(null, freecellOperations, false);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      freecellController.playGame(freecellOperations.getDeck(), null, false);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void passingDeckWith51CardsToController() {
    StringReader actualInput = new StringReader("C1 q");
    StringBuffer actualOutput = new StringBuffer();
    StringBuffer expectedOutput = new StringBuffer();

    FreecellController freecellController = new FreecellController(actualInput, actualOutput);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

    List<Card> deck = freecellOperations.getDeck();
    deck.remove(0);

    freecellController.playGame(deck, freecellOperations, false);
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append("Invalid move. Try again.");
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append("Game quit prematurely.");
    Assert.assertEquals(expectedOutput, actualOutput);
    expectedOutput.append(System.lineSeparator());
  }

  private FreecellOperations<Card> getFreecellOperation() {
    return FreecellMultiMoveModel.getBuilder().cascades(4).opens(4).build();
  }
}
