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
public class FreecellModelControllerTest {

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
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

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
    StringReader readable = new StringReader("C1");
    StringBuffer appendable = new StringBuffer();
    StringBuilder expectedOutput = new StringBuilder();

    FreecellController freecellController = new FreecellController(readable, appendable);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

    List<Card> deck = freecellOperations.getDeck();
    deck.remove(0);

    freecellController.playGame(deck, freecellOperations, false);
    expectedOutput.append(System.lineSeparator());
    expectedOutput.append(TestUtils.CANNOT_START_THE_GAME_STRING);
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
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

      List<Card> deck = TestUtils.getValidDeck();
      List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
      List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
      List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

      freecellController.playGame(deck, freecellOperations, false);
      expectedOutput.append(TestUtils.convertPilesToString(
              expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
      expectedOutput.append(System.lineSeparator());
      expectedOutput.append(TestUtils.GAME_QUIT_STRING);
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
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

      List<Card> deck = TestUtils.getValidDeck();
      List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
      List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
      List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

      freecellController.playGame(deck, freecellOperations, true);
      expectedOutput.append(TestUtils.convertPilesToString(
              expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
      expectedOutput.append(System.lineSeparator());
      expectedOutput.append(TestUtils.GAME_QUIT_STRING);
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

    FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);
    List<Card> deck = freecellOperations.getDeck();
    try {
      freecellController.playGame(deck, freecellOperations, false);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("cannot read from readable", e.getMessage());
    }

    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

    StringBuilder expectedOutput = new StringBuilder(TestUtils.convertPilesToString(
            expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles))
            .append(System.lineSeparator());

    Assert.assertEquals(expectedOutput.toString(), appendable.toString());
  }

  @Test
  public void appendableFailureCauseIllegalStateException() throws IOException {
    StringReader readable = new StringReader("q");

    ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    BufferedWriter appendable =
            new BufferedWriter(
                    new OutputStreamWriter(
                            outputBuffer));

    appendable.close();

    FreecellController freecellController = new FreecellController(readable, appendable);
    Assert.assertEquals("", outputBuffer.toString());

    try {
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);
      List<Card> deck = freecellOperations.getDeck();

      freecellController.playGame(deck, freecellOperations, false);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertEquals("cannot write to appendable", e.getMessage());
    }

    Assert.assertEquals("", outputBuffer.toString());
  }

  @Test
  public void anyInputAfterQMakesNoDifference() {

  }

  @Test
  public void singleMoveModelOtherThanLastCardIndexDoesNotWork() {

  }

  @Test
  public void simulateEntireGame() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int cardIndex = 13; cardIndex > 0; cardIndex--) {
      for (int pileIndex = 1; pileIndex <= 4; pileIndex++) {
        stringBuilder.append("C").append(pileIndex);
        stringBuilder.append(" ");
        stringBuilder.append(cardIndex);
        stringBuilder.append(" ");
        stringBuilder.append("O").append(pileIndex);
        stringBuilder.append(" ");
      }

      for (int pileIndex = 1; pileIndex <= 4; pileIndex++) {
        stringBuilder.append("O").append(pileIndex);
        stringBuilder.append(" ");
        stringBuilder.append(1);
        stringBuilder.append(" ");
        stringBuilder.append("F").append(pileIndex);
        stringBuilder.append(" ");
      }
    }

    StringReader actualInput = new StringReader(stringBuilder.toString().trim());
    StringBuffer actualOutput = new StringBuffer();
    StringBuilder expectedOutput = new StringBuilder();

    FreecellController freecellController = new FreecellController(actualInput, actualOutput);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

    List<Card> deck = TestUtils.getDeckWithAlterColorSuitAndSameCardValue();
    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

    freecellController.playGame(deck, freecellOperations, false);
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

      for (int pileIndex = 0; pileIndex < 4; pileIndex++) {
        Card removedCard = expectedOpenPiles.get(pileIndex).remove(0);
        expectedFoundationPiles.get(pileIndex).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
      }
    }

    expectedOutput.append(TestUtils.GAME_OVER_STRING);
    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test
  public void prematureQuitWorks() {
    for (String quitString : Arrays.asList("Q", "q")) {
      StringReader readable = new StringReader("C1 13 O1 " + quitString);
      StringBuffer appendable = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellController freecellController = new FreecellController(readable, appendable);
      FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

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
      expectedOutput.append(TestUtils.GAME_QUIT_STRING);
      expectedOutput.append(System.lineSeparator());

      Assert.assertEquals(expectedOutput.toString(), appendable.toString());
    }
  }

  @Test
  public void badSourcePileGivenToController() {
    for (String quitString : Arrays.asList("Q", "q")) {
      // tests for bad inputs for the source pile
      for (String badInput : getBadInputStrings()) {
        StringReader readable = new StringReader(badInput + " C1 13 O1 " + quitString);
        StringBuffer appendable = new StringBuffer();

        StringBuilder expectedOutput = new StringBuilder();

        FreecellController freecellController = new FreecellController(readable, appendable);
        FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

        List<Card> deck = TestUtils.getValidDeck();
        List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

        freecellController.playGame(deck, freecellOperations, false);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());

        expectedOutput.append(TestUtils.INVALID_SOURCE_PILE_MESSAGE);
        expectedOutput.append(System.lineSeparator());

        Card removedCard = expectedCascadingPiles.get(0).remove(12);
        expectedOpenPiles.get(0).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
        expectedOutput.append(TestUtils.GAME_QUIT_STRING);
        expectedOutput.append(System.lineSeparator());

        Assert.assertEquals(expectedOutput.toString(), appendable.toString());
      }
    }
  }

  @Test
  public void badDestinationPileGivenToController() {
    for (String quitString : Arrays.asList("Q", "q")) {
      // tests for bad inputs for the destination pile
      for (String badInput : getBadInputStrings()) {
        StringReader readable = new StringReader("C1 13 " + badInput + " O1 " + quitString);
        StringBuffer appendable = new StringBuffer();

        StringBuilder expectedOutput = new StringBuilder();

        FreecellController freecellController = new FreecellController(readable, appendable);
        FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

        List<Card> deck = TestUtils.getValidDeck();
        List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

        freecellController.playGame(deck, freecellOperations, false);

        // tests for initial game state
        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());

        expectedOutput.append(TestUtils.INVALID_DESTINATION_PILE_MESSAGE);
        expectedOutput.append(System.lineSeparator());

        Card removedCard = expectedCascadingPiles.get(0).remove(12);
        expectedOpenPiles.get(0).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
        expectedOutput.append(TestUtils.GAME_QUIT_STRING);
        expectedOutput.append(System.lineSeparator());
        Assert.assertEquals(expectedOutput.toString(), appendable.toString());
      }
    }
  }

  @Test
  public void badCardIndexGivenToController() {
    for (String quitString : Arrays.asList("Q", "q")) {
      // tests for bad inputs for the card index
      for (String badInput : getBadInputStrings()) {
        StringReader readable = new StringReader("C1 " + badInput + " 13 O1 " + quitString);
        StringBuffer appendable = new StringBuffer();

        StringBuilder expectedOutput = new StringBuilder();

        FreecellController freecellController = new FreecellController(readable, appendable);
        FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

        List<Card> deck = TestUtils.getValidDeck();
        List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

        freecellController.playGame(deck, freecellOperations, false);

        // tests for initial game state
        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());

        expectedOutput.append(TestUtils.INVALID_CARD_INDEX_MESSAGE);
        expectedOutput.append(System.lineSeparator());

        Card removedCard = expectedCascadingPiles.get(0).remove(12);
        expectedOpenPiles.get(0).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
        expectedOutput.append(TestUtils.GAME_QUIT_STRING);
        expectedOutput.append(System.lineSeparator());
        Assert.assertEquals(expectedOutput.toString(), appendable.toString());
      }
    }
  }

  @Test
  public void invalidMoveMadeByController() {
    for (String quitString : Arrays.asList("Q", "q")) {
      // tests for bad inputs for the card index
      for (String badMoves : Arrays.asList("C5 13 F1", "C4 14 F1", "C4 13 F5", "C5 14 F5", "O5 13" +
              " F1", "O4 14 F1", "O4 13 F5", "O5 14 F5", "F5 13 F1", "F4 14 F1", "F4 13 F5", "F5 " +
              "14 F1")) {
        StringReader readable = new StringReader(badMoves + " C1 13 O1 " + quitString);
        StringBuffer appendable = new StringBuffer();

        StringBuilder expectedOutput = new StringBuilder();

        FreecellController freecellController = new FreecellController(readable, appendable);
        FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 4);

        List<Card> deck = TestUtils.getValidDeck();
        List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

        freecellController.playGame(deck, freecellOperations, false);

        // tests for initial game state
        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());

        expectedOutput.append(TestUtils.INVALID_MOVE_MESSAGE_STRING + ": Invalid input");
        expectedOutput.append(System.lineSeparator());

        Card removedCard = expectedCascadingPiles.get(0).remove(12);
        expectedOpenPiles.get(0).add(removedCard);

        expectedOutput.append(TestUtils.convertPilesToString(
                expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
        expectedOutput.append(System.lineSeparator());
        expectedOutput.append(TestUtils.GAME_QUIT_STRING);
        expectedOutput.append(System.lineSeparator());

        Assert.assertEquals(expectedOutput.toString(), appendable.toString());
      }
    }
  }

  protected FreecellOperations<Card> getFreecellOperation(int cascadeCount, int openCount) {
    return FreecellMultiMoveModel.getBuilder().cascades(cascadeCount).opens(openCount).build();
  }

  private static List<String> getBadInputStrings() {
    // todo: check if space fails
    return Arrays.asList("-1", "0", "123851293872198374616293", "123.1", "m",
            "@", "c", "f", "o", "c1", "f1", "o1", "C-1", "C0", "Cm", "C@",
            "C123851293872198374616293",
            "C111F1", "O-1", "O0", "Om", "O@", "O123851293872198374616293", "O111F1", "F-1", "F0",
            "Fm",
            "F@",
            "F123851293872198374616293", "F111F1", "C1.1", "F1.1", "O1.1", "C@",
            "C123851293872198374616293.1", "O123851293872198374616293.1", "O111F1.1",
            "F123851293872198374616293.1"
    );
  }
}
