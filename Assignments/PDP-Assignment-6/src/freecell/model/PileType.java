package freecell.model;

import java.util.List;

import freecell.bean.Card;
import freecell.bean.CardValue;
import util.Utils;

/**
 * Type for the three types of piles in a game of Freecell. <br> Open: This pile can hold only one
 * card. It is like a buffer to temporarily hold cards <br> Cascade: This is a pile of face-up
 * cards. A build within a cascade pile is a subset of cards that has monotonically decreasing
 * values and suits of alternating colors<br> Foundation: Initially empty, there are 4 foundation
 * piles, one for each suit. Each foundation pile collects cards in increasing order of value for
 * one suit (Ace being the lowest). <br> The goal of the game is to fill up all the foundation
 * piles
 */
public enum PileType {
  OPEN('O') {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) throws IllegalArgumentException {
      return pile.isEmpty();
    }
  },
  CASCADE('C') {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) throws IllegalArgumentException {
      if (pile.isEmpty()) {
        return true;
      } else {
        Card lastCardInPile = Utils.getLast(pile);
        return (lastCardInPile.getSuit().getColor() != card.getSuit().getColor()) &&
                lastCardInPile.getCardValue().getPriority() - card.getCardValue().getPriority() == 1;
      }
    }
  },
  FOUNDATION('F') {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) throws IllegalArgumentException {
      if (pile.isEmpty()) {
        return card.getCardValue().equals(CardValue.ACE);
      } else {
        Card lastCardInPile = Utils.getLast(pile);
        return (lastCardInPile.getSuit() == card.getSuit()) &&
                card.getCardValue().getPriority() - lastCardInPile.getCardValue().getPriority() == 1;
      }
    }
  };

  private final char symbol;

  PileType(char symbol) {
    this.symbol = symbol;
  }

  public abstract boolean canPutCardInPile(Card card, List<Card> pile) throws IllegalArgumentException;

  public char getSymbol() {
    return symbol;
  }
}
