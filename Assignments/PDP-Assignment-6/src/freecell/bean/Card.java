package freecell.bean;

import java.util.Objects;

import util.Utils;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class Card {

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
    cardString = cardString.trim();
    if (cardString.length() > 3 || cardString.length() < 2) {
      throw new IllegalArgumentException("Invalid string");
    }
    char suitSymbol = cardString.charAt(cardString.length() - 1);
    String cardValueString = cardString.substring(0, cardString.length() - 1);
    return new Card(Suit.parse(suitSymbol), CardValue.parse(cardValueString));
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

  @Override
  public String toString() {
    return this.getCardValue().getSymbol() + this.getSuit().getSymbol();
  }
}
