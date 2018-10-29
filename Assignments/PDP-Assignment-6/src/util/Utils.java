package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.Suit;

/**
 * Represents utility functions that are independent of the state of the application
 */
public class Utils {
  /**
   * Generates a shuffled list of all suits and all ranks of each suit.
   *
   * @return shuffled list of 52 Card(s)
   */
  public static List<Card> getShuffledDeck() {
    List<Card> deck = new ArrayList<>(52);
    for (Suit suit : Suit.values()) {
      for (CardValue cardValue : CardValue.values()) {
        deck.add(new Card(suit, cardValue));
      }
    }
    Collections.shuffle(deck);
    return deck;
  }

  /**
   * Enforces that inputs are non null.
   *
   * @param input input to be checked for non-nullability
   * @param <T>   type of input
   * @return input as it is if input is not null
   * @throws IllegalArgumentException if input is null
   */
  public static <T> T requireNonNull(T input) throws IllegalArgumentException {
    if (Objects.isNull(input)) {
      throw new IllegalArgumentException("Input was null");
    }
    return input;
  }
}
