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
import java.util.Arrays;
import java.util.List;

import freecell.bean.Card;
import freecell.controller.FreecellController;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import util.TestUtils;
import util.Utils;

/**
 * Represents tests that are run on the <code>FreecellController</code> that implements
 * <code>IFreecellController</code>.
 */
public class FreecellControllerTest {

  private static String GAME_QUIT_STRING = "Game quit prematurely.";


  @Test
  public void constructingControllerWithNullParamsFails() {
    StringReader readable = new StringReader("C1 8 F1 q");
    StringBuffer appendable = new StringBuffer();

    try {
      new FreecellController(null, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals("", appendable.toString());

    try {
      new FreecellController(null, appendable);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals("", appendable.toString());

    try {
      new FreecellController(readable, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals("", appendable.toString());
  }

  @Test
  public void invalidModelOrDeckGivenToControllerFails() {
    StringReader readable = new StringReader("C1 8 F1 q");
    StringBuffer appendable = new StringBuffer();
    FreecellController freecellController = new FreecellController(readable, appendable);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

    Assert.assertEquals("", appendable.toString());
    try {
      freecellController.playGame(null, freecellOperations, false);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals("", appendable.toString());

    try {
      freecellController.playGame(freecellOperations.getDeck(), null, false);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals("", appendable.toString());
  }

  @Test
  public void passingDeckWith51CardsToController() {
    StringReader readable = new StringReader("C1 q");
    StringBuffer appendable = new StringBuffer();
    StringBuilder expectedOutput = new StringBuilder();

    FreecellController freecellController = new FreecellController(readable, appendable);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

    List<Card> deck = freecellOperations.getDeck();
    deck.remove(0);

    freecellController.playGame(deck, freecellOperations, false);
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append("Invalid move. Try again.");
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append(GAME_QUIT_STRING);
    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  //todo add and remove new line after game over and prematurely
  @Test
  public void quitWorksWithShuffleFalse() {
    for (String quitString : Arrays.asList("Q", "q")) {
      StringReader readable = new StringReader(quitString);
      StringBuffer appendable = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellController freecellController = new FreecellController(readable, appendable);
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

      List<Card> deck = TestUtils.getValidDeck();
      List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
      List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
      List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

      freecellController.playGame(deck, freecellOperations, false);
      expectedOutput.append(TestUtils.convertPilesToString(
              expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
      expectedOutput.append(System.lineSeparator());
      expectedOutput.append(GAME_QUIT_STRING);

      Assert.assertEquals(expectedOutput.toString(), appendable.toString());
    }
  }

  @Test
  public void quitWorksWithShuffleTrue() {
    for (String quitString : Arrays.asList("Q", "q")) {
      StringReader readable = new StringReader(quitString);
      StringBuffer appendable = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellController freecellController = new FreecellController(readable, appendable);
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

      List<Card> deck = TestUtils.getValidDeck();
      List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
      List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
      List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

      freecellController.playGame(deck, freecellOperations, true);
      expectedOutput.append(TestUtils.convertPilesToString(
              expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
      expectedOutput.append(System.lineSeparator());
      expectedOutput.append(GAME_QUIT_STRING);

      Assert.assertNotEquals(expectedOutput.toString(), appendable.toString());
    }
  }

  @Test
  public void readableFailureCauseIllegalStateException() throws IOException {
    BufferedReader readable =
            new BufferedReader(
                    new InputStreamReader(
                            new ByteArrayInputStream("C1 q".getBytes())));

    readable.close();

    StringBuffer appendable = new StringBuffer();

    FreecellController freecellController = new FreecellController(readable, appendable);
    Assert.assertEquals("", appendable.toString());

    try {
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation();
      List<Card> deck = freecellOperations.getDeck();

      freecellController.playGame(deck, freecellOperations, false);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("cannot read from readable", e.getMessage());
    }

    Assert.assertEquals("", appendable.toString());
  }

  @Test
  public void appendableFailureCauseIllegalStateException() throws IOException {
    StringReader readable = new StringReader("q");

    BufferedWriter appendable =
            new BufferedWriter(
                    new OutputStreamWriter(
                            new ByteArrayOutputStream()));

    appendable.close();

    FreecellController freecellController = new FreecellController(readable, appendable);
    Assert.assertEquals("", appendable.toString());

    try {
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation();
      List<Card> deck = freecellOperations.getDeck();

      freecellController.playGame(deck, freecellOperations, false);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("cannot write to appendable", e.getMessage());
    }

    Assert.assertEquals("", appendable.toString());
  }

  private FreecellOperations<Card> getFreecellOperation() {
    return FreecellMultiMoveModel.getBuilder().cascades(4).opens(4).build();
  }
}
