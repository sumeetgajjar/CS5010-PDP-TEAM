package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.PileCategory;
import freecell.bean.Suit;

/**
 * This class represents set of util functions which can be used by any class for testing purposes.
 */
public class TestUtils {

  public static final String INVALID_DESTINATION_PILE_MESSAGE =
          "Invalid input, please enter destination pile again.";

  public static final String INVALID_CARD_INDEX_MESSAGE =
          "Invalid input, please enter card index again.";

  public static final String INVALID_MOVE_MESSAGE_STRING = "Invalid move, please try again";
  public static final String GAME_OVER_STRING = "Game over.";

  public static final String INVALID_SOURCE_PILE_MESSAGE =
          "Invalid input, please enter source pile again.";

  public static final String CANNOT_START_THE_GAME_STRING = "Cannot start the game";

  public static final String GAME_QUIT_STRING = "Game quit prematurely.";

  private static List<List<Card>> getListOfEmptyLists(int listSize) {
    List<List<Card>> expectedOpenPiles = new ArrayList<>(listSize);
    for (int i = 0; i < listSize; i++) {
      expectedOpenPiles.add(new LinkedList<>());
    }
    return expectedOpenPiles;
  }

  /**
   * Returns a deck with 52 cards. The deck is arranged such that all cards of one suit are together
   * with increasing order of their priority.
   *
   * @return a deck with 52 cards
   */
  public static List<Card> getValidDeck() {
    List<Card> deck = new ArrayList<>(52);
    for (Suit suit : Suit.values()) {
      for (CardValue cardValue : CardValue.values()) {
        deck.add(new Card(suit, cardValue));
      }
    }
    return deck;
  }

  /**
   * Returns a list of cascading piles.Distributes the deck among the cascade pile in round robin
   * fashion.
   *
   * @param cascadePileCount the number of cascade piles
   * @param validDeck        the deck
   * @return a list of cascading piles
   */
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

  /**
   * Returns a string representation of given foundation, open and cascade piles.
   *
   * @param foundationPiles the given foundation Piles
   * @param openPiles       the given open piles
   * @param cascadePiles    the given cascade piles
   * @return a string representation of given foundation, open and cascade piles
   */
  public static String convertPilesToString(List<List<Card>> foundationPiles,
                                            List<List<Card>> openPiles,
                                            List<List<Card>> cascadePiles) {
    return pileToString(foundationPiles, PileCategory.FOUNDATION)
            + System.lineSeparator()
            + pileToString(openPiles, PileCategory.OPEN)
            + System.lineSeparator()
            + pileToString(cascadePiles, PileCategory.CASCADE);
  }

  /**
   * Returns a string representation of the given piles of cards.
   *
   * @param piles        the piles of card
   * @param pileCategory the pile category
   * @return a string representation of the given pile of cards
   */
  private static String pileToString(List<List<Card>> piles, PileCategory pileCategory) {
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
      stringBuilder.append(pileCategory.getSymbol());
      stringBuilder.append(i + 1).append(":");
      stringBuilder.append(listOfStrings.get(i));
      stringBuilder.append(System.lineSeparator());
    }
    return stringBuilder.toString().trim();
  }

  /**
   * Returns a deck with 52 cards. The deck is arranged such that cards of all suits of a particular
   * value are together with decreasing order of their priority.
   *
   * @return a deck with 52 cards
   */
  public static List<Card> getDeckWithAlterColorSuitAndSameCardValue() {
    List<Card> deck = new ArrayList<>(52);
    List<CardValue> cardValues = Arrays.stream(CardValue.values())
            .sorted(Comparator.comparingInt(CardValue::getPriority).reversed())
            .collect(Collectors.toList());

    for (CardValue cardValue : cardValues) {
      deck.add(new Card(Suit.SPADES, cardValue));
      deck.add(new Card(Suit.DIAMONDS, cardValue));
      deck.add(new Card(Suit.CLUBS, cardValue));
      deck.add(new Card(Suit.HEARTS, cardValue));
    }
    return deck;
  }

  /**
   * Returns a deck with 52 cards. The deck is arranged such that multiple card movement is possible
   * on a cascade pile.
   *
   * @return a deck with 52 cards
   */
  public static List<Card> getDeckForMultipleCardsMovementOnCascadePile() {
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
