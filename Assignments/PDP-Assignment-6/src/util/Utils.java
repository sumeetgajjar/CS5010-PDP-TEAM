package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.Suit;

/**
 * Represents utility functions that are independent of the state of the application.
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
      throw new IllegalArgumentException("Invalid input");
    }
    return input;
  }

  /**
   * Gets the last element from the list.
   *
   * @param list the list from which to get the last element from
   * @param <T>  the type of data in the list
   * @return the last element
   */
  public static <T> T getLast(List<T> list) throws IndexOutOfBoundsException {
    return list.get(list.size() - 1);
  }

  /**
   * Returns a list of empty lists.
   *
   * @param listSize number of empty list
   * @param <T>      type of the empty lists
   * @return a list of empty lists
   */
  public static <T> List<List<T>> getListOfEmptyLists(int listSize) {
    List<List<T>> expectedOpenPiles = new ArrayList<>(listSize);
    for (int i = 0; i < listSize; i++) {
      expectedOpenPiles.add(new ArrayList<>());
    }
    return expectedOpenPiles;
  }


  /**
   * Gets the first element from the list
   *
   * @param collect the list from which the first element is needed
   * @param <T>     the type of the data in the list
   * @return the first element in the list
   */
  public static <T> T getFirst(List<T> collect) {
    Utils.requireNonNull(collect);
    return collect.get(0);
  }

  /**
   * Returns a string minus it's last character
   *
   * @param string string from which last character is to be removed
   * @return string minus it's last character
   */
  public static String removeTheLastCharacterFrom(String string) {
    Utils.requireNonNull(string);
    if (string.isEmpty()) {
      return string;
    }
    return string.substring(0, string.length() - 1);
  }
}
