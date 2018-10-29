package freecell.bean;

import java.util.Objects;

import util.Utils;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class Card implements Comparable<Card> {

  private final Suit suit;
  private final CardValue cardValue;

  public Card(Suit suit, CardValue cardValue) throws IllegalArgumentException {
    this.suit = Utils.requireNonNull(suit);
    this.cardValue = Utils.requireNonNull(cardValue);
  }

  public Suit getSuit() {
    return suit;
  }

  public CardValue getCardValue() {
    return cardValue;
  }

  public static Card parse(String cardString) {
    return new Card(null, null);
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
            getCardValue() == card.getCardValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSuit(), getCardValue());
  }
}
