package freecell.bean;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class Card {
  private final Suit suit;
  private final Rank rank;

  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }
}
