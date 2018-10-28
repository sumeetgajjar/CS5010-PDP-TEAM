package freecell.bean;

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
}
