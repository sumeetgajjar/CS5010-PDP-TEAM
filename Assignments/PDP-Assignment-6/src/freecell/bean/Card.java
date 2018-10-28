package freecell.bean;

import java.util.Objects;

import util.Utils;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class Card implements Comparable<Card> {
  private final Suit suit;
  private final Rank rank;

  public Card(Suit suit, Rank rank) throws IllegalArgumentException {
    this.suit = Utils.requireNonNull(suit);
    this.rank = Utils.requireNonNull(rank);
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  @Override
  public int compareTo(Card o) {
    //todo
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Card)) return false;
    Card card = (Card) o;
    return getSuit() == card.getSuit() &&
            getRank() == card.getRank();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSuit(), getRank());
  }
}
