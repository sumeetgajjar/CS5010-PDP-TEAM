import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import freecell.bean.Card;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import freecell.model.FreecellOperationsBuilder;
import freecell.model.PileType;
import util.TestUtils;
import util.Utils;

/**
 * Represents tests that are run on the <code>FreecellMultiMoveModel</code> that implements
 * <code>FreeCellOperations</code>.
 */
public class FreecellMultiMoveModelTest extends FreecellModelTest {
  //  @Override
  protected FreecellOperationsBuilder getFreecellOperationsBuilder() {
    return FreecellMultiMoveModel.getBuilder();
  }

  @Test
  public void moveMultipleCardFromCascadeToCascadeWorks() {
    int cascadePiles = 4;
    int openPiles = 5;

    for (int j = 12; j >= 0; j--) {
      FreecellOperations<Card> freecellOperations = getFreecellOperationsBuilder()
              .opens(openPiles)
              .cascades(cascadePiles)
              .build();

      List<Card> deck = TestUtils.getDeckForMultipleCardsMovementOnCascadePile();
      List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(cascadePiles,
              deck);
      freecellOperations.startGame(deck, false);

      List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
      Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles,
              Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
              freecellOperations.getGameState());

      boolean flag = true;
      //moving cards from cascade pile 0 and 1 to foundation piles.
      for (int i = 12; i >= 0; i--) {
        int sourceCascadePile1 = 0;
        int sourceCascadePile2 = 1;
        int destinationFoundationPile1 = 0;
        int destinationFoundationPile2 = 1;

        if (flag) {
          sourceCascadePile1 = 1;
          sourceCascadePile2 = 0;
        }

        Card cardFromSourceCascadePile1 = expectedCascadingPiles.get(sourceCascadePile1).remove(i);
        expectedFoundationPiles.get(destinationFoundationPile1).add(cardFromSourceCascadePile1);
        freecellOperations.move(PileType.CASCADE, sourceCascadePile1, i, PileType.FOUNDATION,
                destinationFoundationPile1);

        Card cardFromSourceCascadePile2 = expectedCascadingPiles.get(sourceCascadePile2).remove(i);
        expectedFoundationPiles.get(destinationFoundationPile2).add(cardFromSourceCascadePile2);
        freecellOperations.move(PileType.CASCADE, sourceCascadePile2, i, PileType.FOUNDATION,
                destinationFoundationPile2);
        flag = !flag;
      }
      //now cascade piles 0 and 1 are empty.

      //moving multiple cards from cascade pile 2 to 0
      List<Card> cascadePile2 = expectedCascadingPiles.get(2);
      while (j < cascadePile2.size()) {
        Card cardFromCascadePile = cascadePile2.remove(j);
        expectedCascadingPiles.get(0).add(cardFromCascadePile);
      }
      freecellOperations.move(PileType.CASCADE, 2, j, PileType.CASCADE, 0);

      Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles,
              Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
              freecellOperations.getGameState());
    }

  }

  @Test
  public void multipleCascadeCardMoveFailsSinceCardsAreNotOfAlternatingSuite() {
    int cascadePiles = 4;
    int openPiles = 5;

    FreecellOperations<Card> freecellOperations = getFreecellOperationsBuilder()
            .opens(openPiles)
            .cascades(cascadePiles)
            .build();

    List<Card> deck = TestUtils.getDeckWithAlterColorSuitAndSameCardValue();
    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(cascadePiles,
            deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

    freecellOperations.startGame(deck, false);
    moveCardsFromCascadePileToFoundationPile(freecellOperations, expectedCascadingPiles,
            expectedFoundationPiles);

    for (int i = 11; i >= 0; i--) {
      try {
        freecellOperations.move(PileType.CASCADE, 2, i, PileType.CASCADE, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }

      try {
        freecellOperations.move(PileType.CASCADE, 3, i, PileType.CASCADE, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
    }
  }

  @Test
  public void multipleCascadeCardMoveFailsSinceCardsAreNotInDecreasingOrder() {
    int cascadePiles = 4;
    int openPiles = 24;

    FreecellOperations<Card> freecellOperations = getFreecellOperationsBuilder()
            .opens(openPiles)
            .cascades(cascadePiles)
            .build();

    List<Card> deck = TestUtils.getDeckForMultipleCardsMovementOnCascadePile();
    Collections.reverse(deck);
    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(cascadePiles,
            deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPiles);

    freecellOperations.startGame(deck, false);

    Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), freecellOperations.getGameState());

    for (int i = 12; i >= 0; i--) {
      Card cardFromCascadePile = expectedCascadingPiles.get(3).remove(i);
      expectedOpenPiles.get(i).add(cardFromCascadePile);
      freecellOperations.move(PileType.CASCADE, 3, i, PileType.OPEN, i);
    }

    Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), freecellOperations.getGameState());

    for (int i = 0; i < 12; i++) {
      try {
        freecellOperations.move(PileType.CASCADE, 0, i, PileType.CASCADE, 3);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
    }

    Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), freecellOperations.getGameState());
  }

  @Test
  public void movingMultipleCascadeCardFailsDueToLessNumberOfEmptyPiles() {
    int cascadePiles = 4;
    int openPiles = 13;

    FreecellOperations<Card> freecellOperations = getFreecellOperationsBuilder()
            .opens(openPiles)
            .cascades(cascadePiles)
            .build();

    List<Card> deck = TestUtils.getDeckForMultipleCardsMovementOnCascadePile();
    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(cascadePiles,
            deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPiles);

    freecellOperations.startGame(deck, false);

    Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), freecellOperations.getGameState());

    //moving cards from cascade pile 3 to all open piles
    for (int i = 12; i >= 0; i--) {
      Card cardFromCascadePile = expectedCascadingPiles.get(3).remove(i);
      expectedOpenPiles.get(i).add(cardFromCascadePile);
      freecellOperations.move(PileType.CASCADE, 3, i, PileType.OPEN, i);
    }
    //cascade pile 3 is empty and all open piles are full

    Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), freecellOperations.getGameState());

    try {
      freecellOperations.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 3);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    Assert.assertEquals(TestUtils.convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), freecellOperations.getGameState());
  }

  private void moveCardsFromCascadePileToFoundationPile(
          FreecellOperations<Card> freecellOperations,
          List<List<Card>> expectedCascadingPiles,
          List<List<Card>> expectedFoundationPiles) {

    //moving cards from cascade pile 0 and 1 to foundation piles.
    for (int i = 12; i >= 0; i--) {
      Card cardFromSourceCascadePile1 = expectedCascadingPiles.get(0).remove(i);
      expectedFoundationPiles.get(0).add(cardFromSourceCascadePile1);
      freecellOperations.move(PileType.CASCADE, 0, i, PileType.FOUNDATION, 0);

      Card cardFromSourceCascadePile2 = expectedCascadingPiles.get(1).remove(i);
      expectedFoundationPiles.get(1).add(cardFromSourceCascadePile2);
      freecellOperations.move(PileType.CASCADE, 1, i, PileType.FOUNDATION, 1);
    }
    //now cascade piles 0 and 1 are empty.
  }

}
