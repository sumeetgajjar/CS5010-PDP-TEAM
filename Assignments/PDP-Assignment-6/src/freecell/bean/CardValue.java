package freecell.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;

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

  CardValue(String symbol, int priority) {
    this.symbol = symbol;
    this.priority = priority;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getPriority() {
    return priority;
  }

  public static CardValue parse(String symbol) throws IllegalArgumentException {
    List<CardValue> cardValues = Arrays.stream(CardValue.values())
            .filter(cardValue -> cardValue.getSymbol().equals(symbol))
            .collect(Collectors.toList());
    try {
      return Utils.getFirst(cardValues);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }
}
