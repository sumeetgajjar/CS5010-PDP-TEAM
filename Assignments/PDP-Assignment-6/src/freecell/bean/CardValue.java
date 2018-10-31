package freecell.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;

/**
 * This enum represents all the values a card can have.
 */
public enum CardValue {
  ACE("A", 1),
  TWO("2", 2),
  THREE("3", 3),
  FOUR("4", 4),
  FIVE("5", 5),
  SIX("6", 6),
  SEVEN("7", 7),
  EIGHT("8", 8),
  NINE("9", 9),
  TEN("10", 10),
  JACK("J", 11),
  QUEEN("Q", 12),
  KING("K", 13);

  private final String symbol;

  /**
   * lower the number indicates lower priority.
   */
  private final int priority;

  /**
   * Constructs a CardValue object with given symbol and priority.
   *
   * @param symbol   the symbol of the value
   * @param priority the priority of the value
   */
  CardValue(String symbol, int priority) {
    this.symbol = symbol;
    this.priority = priority;
  }

  /**
   * Returns the symbol of this CardValue.
   *
   * @return the symbol of this CardValue
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Returns the priority of this CardValue.
   *
   * @return the priority of this CardValue
   */
  public int getPriority() {
    return priority;
  }

  /**
   * Returns a CardValue by parsing the given string. It throws a {@link IllegalArgumentException}
   * if the given string is null or non-parsable.
   *
   * @param cardValueString the string to parse
   * @return a CardValue by parsing the given string
   * @throws IllegalArgumentException if the given string is null or non-parsable.
   */
  public static CardValue parse(String cardValueString) throws IllegalArgumentException {
    Utils.requireNonNull(cardValueString);

    List<CardValue> cardValues = Arrays.stream(CardValue.values())
            .filter(cardValue -> cardValue.getSymbol().equals(cardValueString))
            .collect(Collectors.toList());
    try {
      return Utils.getFirst(cardValues);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }
}
