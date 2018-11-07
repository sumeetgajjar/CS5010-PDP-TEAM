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
  private static final String GAME_OVER_STRING = "Game over.";
  private final String INVALID_SOURCE_PILE_MESSAGE = "Invalid input, please enter source pile " +
          "again.";


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
  public void passingDeckWith51CardsToControllerFails() {
    StringReader readable = new StringReader("C1 q");
    StringBuffer appendable = new StringBuffer();
    StringBuilder expectedOutput = new StringBuilder();

    FreecellController freecellController = new FreecellController(readable, appendable);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

    List<Card> deck = freecellOperations.getDeck();
    deck.remove(0);

    freecellController.playGame(deck, freecellOperations, false);
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append("Cannot start the game");
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append(GAME_QUIT_STRING);
    expectedOutput.append(System.lineSeparator());

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
      expectedOutput.append(System.lineSeparator());

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
      expectedOutput.append(System.lineSeparator());

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

  @Test
  public void simulateEntireGame() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int cardIndex = 13; cardIndex > 0; cardIndex--) {
      for (int pileIndex = 1; pileIndex <= 4; pileIndex++) {
        stringBuilder.append("C").append(pileIndex);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(cardIndex);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("O").append(pileIndex);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("O").append(pileIndex);
        stringBuilder.append(" ");
        stringBuilder.append(1);
        stringBuilder.append(" ");
        stringBuilder.append("F").append(pileIndex);
      }
    }

    StringReader actualInput = new StringReader(stringBuilder.toString());
    StringBuffer actualOutput = new StringBuffer();
    StringBuilder expectedOutput = new StringBuilder();

    FreecellController freecellController = new FreecellController(actualInput, actualOutput);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation();

    List<Card> deck = TestUtils.getDeckWithAlterColorSuitAndSameCardValue();
    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

    freecellController.playGame(deck, freecellOperations, true);
    expectedOutput.append(TestUtils.convertPilesToString(
            expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
    expectedOutput.append(System.lineSeparator());

    for (int cardIndex = 12; cardIndex >= 0; cardIndex--) {

      for (int pileIndex = 0; pileIndex < 4; pileIndex++) {
        Card removedCard = expectedCascadingPiles.get(pileIndex).remove(cardIndex);
        expectedOpenPiles.get(pileIndex).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
      }

      for (int pileIndex = 1; pileIndex < 4; pileIndex++) {
        Card removedCard = expectedOpenPiles.get(pileIndex).remove(0);
        expectedFoundationPiles.get(pileIndex).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
      }
    }

    expectedOutput.append(GAME_OVER_STRING);
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test
  public void prematureQuitWorks() {
    for (String quitString : Arrays.asList("Q", "q")) {
      StringReader readable = new StringReader("C1 12 O1 " + quitString);
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

      Card removedCard = expectedCascadingPiles.get(0).remove(12);
      expectedOpenPiles.get(0).add(removedCard);

      expectedOutput.append(TestUtils.convertPilesToString(
              expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
      expectedOutput.append(System.lineSeparator());
      expectedOutput.append(GAME_QUIT_STRING);
      expectedOutput.append(System.lineSeparator());

      Assert.assertEquals(expectedOutput.toString(), appendable.toString());
    }
  }

  @Test
  public void badSourcePileGivenToController() {
    for (String quitString : Arrays.asList("Q", "q")) {
      for (String badInput : Arrays.asList("1", "m", "@")) {
        StringReader readable = new StringReader(badInput + " C1 12 O1 " + quitString);
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

        expectedOutput.append(INVALID_SOURCE_PILE_MESSAGE);
        expectedOutput.append(System.lineSeparator());

        Card removedCard = expectedCascadingPiles.get(0).remove(12);
        expectedOpenPiles.get(0).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
        expectedOutput.append(GAME_QUIT_STRING);
        expectedOutput.append(System.lineSeparator());

        Assert.assertEquals(expectedOutput.toString(), appendable.toString());
      }
    }
  }

  private FreecellOperations<Card> getFreecellOperation() {
    return FreecellMultiMoveModel.getBuilder().cascades(4).opens(4).build();
  }
}
