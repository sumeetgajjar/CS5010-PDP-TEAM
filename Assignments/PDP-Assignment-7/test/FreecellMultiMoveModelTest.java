import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.Suit;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import freecell.model.FreecellOperationsBuilder;
import freecell.model.PileType;
import util.Utils;

/**
 * Represents tests that are run on the <code>FreecellMultiMoveModel</code> that implements
 * <code>FreeCellOperations</code>.
 */
public class FreecellMultiMoveModelTest extends FreecellModelTest {

  @Override
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

      List<Card> deck = getDeckForMultipleCardsMovementOnCascadePile();
      List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePiles, deck);
      freecellOperations.startGame(deck, false);

      List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
      Assert.assertEquals(convertPilesToString(expectedCascadingPiles,
              Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
              freecellOperations.getGameState());

      boolean flag = true;
      //moving cards from cascade pile 0 and 1 to foundation piles.
      for (int i = 11; i >= 0; i--) {
        int sourceCascadePile1 = 0;
        int sourceCascadePile2 = 1;
        int destinationFoundationPile1 = 0;
        int destinationFoundationPile2 = 1;

        if (flag) {
          sourceCascadePile1 = 1;
          sourceCascadePile2 = 0;
          destinationFoundationPile1 = 1;
          destinationFoundationPile2 = 0;
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
      for (int i = j; i < 13; i++) {
        Card cardFromCascadePile = expectedCascadingPiles.get(2).remove(i);
        expectedCascadingPiles.get(0).add(cardFromCascadePile);
      }
      freecellOperations.move(PileType.CASCADE, 2, j, PileType.CASCADE, 0);

      Assert.assertEquals(convertPilesToString(expectedCascadingPiles,
              Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
              freecellOperations.getGameState());
    }

  }

  @Test
  public void multipleCascadeCardMoveFailsSinceCardAreNotOfAlternatingSuite() {
    int cascadePiles = 4;
    int openPiles = 5;

    FreecellOperations<Card> freecellOperations = getFreecellOperationsBuilder()
            .opens(openPiles)
            .cascades(cascadePiles)
            .build();

    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();
    List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePiles, deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

    freecellOperations.startGame(deck, false);

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

    for (int i = 11; i >= 0; i--) {
      try {
        freecellOperations.move(PileType.CASCADE, 2, i, PileType.CASCADE, 0);
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }

      try {
        freecellOperations.move(PileType.CASCADE, 3, i, PileType.CASCADE, 0);
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
    }
  }

  private static List<Card> getDeckForMultipleCardsMovementOnCascadePile() {
    List<Card> deck = new ArrayList<>(52);
    List<CardValue> cardValues = Arrays.stream(CardValue.values())
            .sorted(Comparator.comparingInt(CardValue::getPriority).reversed())
            .collect(Collectors.toList());

    boolean flag = true;
    for (CardValue cardValue : cardValues) {

      if (flag) {
        deck.add(new Card(Suit.SPADES, cardValue));
        deck.add(new Card(Suit.DIAMONDS, cardValue));
        deck.add(new Card(Suit.CLUBS, cardValue));
        deck.add(new Card(Suit.HEARTS, cardValue));
      } else {
        deck.add(new Card(Suit.DIAMONDS, cardValue));
        deck.add(new Card(Suit.SPADES, cardValue));
        deck.add(new Card(Suit.HEARTS, cardValue));
        deck.add(new Card(Suit.CLUBS, cardValue));
      }
      flag = !flag;
    }
    return deck;
  }
}
