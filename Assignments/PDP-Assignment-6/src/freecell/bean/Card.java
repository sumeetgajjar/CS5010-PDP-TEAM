package freecell.bean;

import java.util.Objects;

import util.Utils;

/**
 * This class represents a Card. A {@link Card} has a {@link Suit} and {@link CardValue} associated
 * with it.
 */
public class Card {

  private final Suit suit;
  private final CardValue cardValue;

  /**
   * Constructs a Card object with the given suit and cardValue.
   *
   * @param suit      the suit of the card
   * @param cardValue the value for the card
   * @throws IllegalArgumentException if the given params are null
   */
  public Card(Suit suit, CardValue cardValue) throws IllegalArgumentException {
    this.suit = Utils.requireNonNull(suit);
    this.cardValue = Utils.requireNonNull(cardValue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Card)) {
      return false;
    }

    Card card = (Card) o;
    return this.getSuit() == card.getSuit() &&
            this.getCardValue() == card.getCardValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getSuit(), this.getCardValue());
  }

  /**
   * Returns the string representation of the card. The string representation will be in following
   * format: "[value of the card][suit symbol]". If a card is Ace of Diamonds, then its string
   * representation will be "A♦". If a card is Ten of Diamonds, then its string representation will
   * be "10♦".
   *
   * @return the string representation of the card
   */
  @Override
  public String toString() {
    return this.getCardValue().getSymbol() + this.getSuit().getSymbol();
  }

  /**
   * Returns the suit of this card.
   *
   * @return the suit of this card
   */
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * Returns the value of this card.
   *
   * @return the value of this card
   */
  public CardValue getCardValue() {
    return this.cardValue;
  }

  /**
   * Returns a card by parsing the given string. It throws an {@link IllegalArgumentException} if
   * the given param is null or given string is non-parsable.
   *
   * @param cardString the string to parse
   * @return a card by parsing the given string
   * @throws IllegalArgumentException if the given param is null or given string is non-parsable.
   */
  public static Card parse(String cardString) throws IllegalArgumentException {
    Utils.requireNonNull(cardString);
    cardString = cardString.trim();
    if (cardString.length() > 3 || cardString.length() < 2) {
      throw new IllegalArgumentException("Invalid string");
    }
    char suitSymbol = cardString.charAt(cardString.length() - 1);
    String cardValueString = Utils.removeTheLastCharacterFrom(cardString);
    return new Card(Suit.parse(suitSymbol), CardValue.parse(cardValueString));
  }
}
