package freecell.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;

public enum Suit {
  SPADES('♠', SuiteColor.BLACK),
  DIAMONDS('♦', SuiteColor.RED),
  CLUBS('♣', SuiteColor.BLACK),
  HEARTS('♥', SuiteColor.RED);

  private final char symbol;
  private final SuiteColor color;

  Suit(char symbol, SuiteColor color) {
    this.symbol = symbol;
    this.color = color;
  }

  public char getSymbol() {
    return symbol;
  }

  public SuiteColor getColor() {
    return color;
  }

  public static Suit parse(char symbol) {
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
