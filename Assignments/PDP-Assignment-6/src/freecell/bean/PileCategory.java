package freecell.bean;

import java.util.List;

import freecell.model.PileType;
import util.Utils;

/**
 * Category for the three types of piles in a game of Freecell. <br> Open: This pile can hold only
 * one card. It is like a buffer to temporarily hold cards <br> Cascade: This is a pile of face-up
 * cards. A build within a cascade pile is a subset of cards that has monotonically decreasing
 * values and suits of alternating colors<br> Foundation: Initially empty, there are 4 foundation
 * piles, one for each suit. Each foundation pile collects cards in increasing order of value for
 * one suit (Ace being the lowest). <br> The goal of the game is to fill up all the foundation
 * piles
 */
public enum PileCategory {

  OPEN('O', PileType.OPEN) {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) {
      return pile.isEmpty();
    }
  },

  CASCADE('C', PileType.CASCADE) {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) {
      if (pile.isEmpty()) {
        return true;
      } else {
        Card lastCardInPile = Utils.getLast(pile);
        return (lastCardInPile.getSuit().getColor() != card.getSuit().getColor())
                && (lastCardInPile.getCardValue().getPriority() -
                card.getCardValue().getPriority() == 1);
      }
    }
  },

  FOUNDATION('F', PileType.FOUNDATION) {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) {
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
  private final PileType pileType;

  /**
   * Constructs a PileCategory Object with the given symbol and pileType.
   *
   * @param symbol   the given symbol
   * @param pileType the given pileType
   */
  PileCategory(char symbol, PileType pileType) {
    this.symbol = symbol;
    this.pileType = pileType;
  }

  /**
   * Returns true, if the given card can be added to the end of the given list, false otherwise.
   *
   * @param card the card to check
   * @param pile the list to check against given card
   * @return true, if the given card can be added to the end of the given list, false otherwise
   */
  public abstract boolean canPutCardInPile(Card card, List<Card> pile);

  /**
   * Returns the symbol of this PileCategory.
   *
   * @return the symbol of this PileCategory
   */
  public char getSymbol() {
    return symbol;
  }

  /**
   * Returns the pileType of this PileCategory.
   *
   * @return the pileType of this PileCategory
   */
  public PileType getPileType() {
    return pileType;
  }

  /**
   * Returns a PileCategory for the given PileType. It throws an {@link IllegalArgumentException} if
   * there is no PileCategory associated with the given PileType.
   *
   * @param pileType the given pileType
   * @return a PileCategory for the given PileType
   * @throws IllegalArgumentException if there is not PileCategory associated with the given
   *                                  PileType
   */
  public static PileCategory getPileCategory(PileType pileType) throws IllegalArgumentException {
    for (PileCategory pileCategory : PileCategory.values()) {
      if (pileCategory.getPileType().equals(pileType)) {
        return pileCategory;
      }
    }
    throw new IllegalArgumentException("Invalid input");
  }
}
