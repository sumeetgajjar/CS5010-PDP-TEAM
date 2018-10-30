package freecell.bean;

import java.util.List;

import freecell.model.PileType;
import util.Utils;

public enum PileCategory {

  OPEN('O', PileType.OPEN) {
    @Override
    public boolean canPutCardInPile(Card card, List<Card> pile) throws IllegalArgumentException {
      return pile.isEmpty();
    }
  },

  CASCADE('C', PileType.CASCADE) {
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

  FOUNDATION('F', PileType.FOUNDATION) {
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
  private final PileType pileType;

  PileCategory(char symbol, PileType pileType) {
    this.symbol = symbol;
    this.pileType = pileType;
  }

  public abstract boolean canPutCardInPile(Card card, List<Card> pile) throws IllegalArgumentException;

  public char getSymbol() {
    return symbol;
  }

  public PileType getPileType() {
    return pileType;
  }

  public static PileCategory getPileCategory(PileType pileType) {
    for (PileCategory pileCategory : PileCategory.values()) {
      if (pileCategory.getPileType().equals(pileType)) {
        return pileCategory;
      }
    }
    throw new IllegalArgumentException("PileCategory not found");
  }
}
