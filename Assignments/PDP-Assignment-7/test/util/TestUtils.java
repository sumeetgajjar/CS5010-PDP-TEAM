package util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.PileCategory;
import freecell.bean.Suit;

/**
 * Created by gajjar.s, on 10:42 PM, 11/6/18
 */
public class TestUtils {

  public static List<List<Card>> getListOfEmptyLists(int listSize) {
    List<List<Card>> expectedOpenPiles = new ArrayList<>(listSize);
    for (int i = 0; i < listSize; i++) {
      expectedOpenPiles.add(new LinkedList<>());
    }
    return expectedOpenPiles;
  }

  public static List<Card> getValidDeck() {
    List<Card> deck = new ArrayList<>(52);
    for (Suit suit : Suit.values()) {
      for (CardValue cardValue : CardValue.values()) {
        deck.add(new Card(suit, cardValue));
      }
    }
    return deck;
  }

  public static List<List<Card>> getCardsInCascadingPiles(int cascadePileCount,
                                                          List<Card> validDeck) {
    List<List<Card>> expectedCascadingPiles = getListOfEmptyLists(cascadePileCount);

    int i = 0;
    int j = 0;
    while (i < validDeck.size()) {
      expectedCascadingPiles.get(j).add(validDeck.get(i));
      j = (j + 1) % cascadePileCount;
      i++;
    }

    return expectedCascadingPiles;
  }

  public static String convertPilesToString(List<List<Card>> foundationPiles,
                                            List<List<Card>> openPiles,
                                            List<List<Card>> cascadePiles) {
    return pileToString(foundationPiles, PileCategory.FOUNDATION)
            + System.lineSeparator()
            + pileToString(openPiles, PileCategory.OPEN)
            + System.lineSeparator()
            + pileToString(cascadePiles, PileCategory.CASCADE);
  }

  public static String pileToString(List<List<Card>> piles, PileCategory pile) {
    List<String> listOfStrings = new ArrayList<>();
    for (List<Card> cards : piles) {
      StringBuilder lineString = new StringBuilder();
      for (Card card : cards) {
        lineString.append(" ").append(card).append(",");
      }
      listOfStrings.add(Utils.removeTheLastCharacterFrom(lineString.toString()));
    }

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < listOfStrings.size(); i++) {
      stringBuilder.append(pile.getSymbol());
      stringBuilder.append(i + 1).append(":");
      stringBuilder.append(listOfStrings.get(i));
      stringBuilder.append(System.lineSeparator());
    }
    return stringBuilder.toString().trim();
  }
}
