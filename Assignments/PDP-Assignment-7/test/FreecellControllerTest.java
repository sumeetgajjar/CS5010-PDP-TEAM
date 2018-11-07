import org.junit.Assert;
import org.junit.Test;

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
    expectedOutput.append(GAME_QUIT_STRING);
    Assert.assertEquals(expectedOutput, actualOutput);
  }

  //todo add and remove new line after game over and prematurely
  @Test
  public void quitWorksWithShuffleFalse() {
    for (String quitString : Arrays.asList("Q", "q")) {
      StringReader actualInput = new StringReader(quitString);
      StringBuffer actualOutput = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellController freecellController = new FreecellController(actualInput, actualOutput);
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

      Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
    }
  }

  @Test
  public void quitWorksWithShuffleTrue() {
    for (String quitString : Arrays.asList("Q", "q")) {
      StringReader actualInput = new StringReader(quitString);
      StringBuffer actualOutput = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellController freecellController = new FreecellController(actualInput, actualOutput);
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

      Assert.assertNotEquals(expectedOutput.toString(), actualOutput.toString());
    }
  }

  private FreecellOperations<Card> getFreecellOperation() {
    return FreecellMultiMoveModel.getBuilder().cascades(4).opens(4).build();
  }
}
