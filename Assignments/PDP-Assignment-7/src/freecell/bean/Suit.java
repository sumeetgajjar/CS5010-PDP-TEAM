package freecell.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;

/**
 * Represents the suit of the card defined in terms of it's symbol and it's color.
 */
public enum Suit {
  CLUBS('♣', SuitColor.BLACK),
  DIAMONDS('♦', SuitColor.RED),
  HEARTS('♥', SuitColor.RED),
  SPADES('♠', SuitColor.BLACK);

  private final char symbol;
  private final SuitColor color;

  /**
   * Constructs a suit from it's symbol represented as a character and it's color represented as a
   * SuitColor.
   *
   * @param symbol character representation of the suit symbol
   * @param color  color of the card represented as <code>SuitColor</code>
   */
  Suit(char symbol, SuitColor color) {
    this.symbol = symbol;
    this.color = color;
  }

  /**
   * Gets the symbol of the Suit.
   *
   * @return the symbol of the suit
   */
  public char getSymbol() {
    return symbol;
  }

  /**
   * Gets the color of the Suit represented as a SuitColor.
   *
   * @return the color of the suit
   */
  public SuitColor getColor() {
    return color;
  }

  /**
   * Parses a given character into the appropriate Suit if it's a valid symbol.
   *
   * @param symbol the character representation of the suit
   * @return Suit parsed from the character representation of it's symbol
   * @throws IllegalArgumentException if the symbol passed is not one of the valid suit symbols
   */
  public static Suit parse(char symbol) throws IllegalArgumentException {
    List<Suit> suits = Arrays.stream(Suit.values())
            .filter(cardValue -> cardValue.getSymbol() == symbol)
            .collect(Collectors.toList());
    try {
      return Utils.getFirst(suits);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }
}
