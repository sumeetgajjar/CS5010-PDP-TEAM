package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;

/**
 * OpenPileRuleChecker represents rules that apply to a move when the Open pile is the source or the
 * destination of the move.
 */
public class OpenPileRuleChecker implements RuleChecker<Card> {

  /**
   * Validates if a card can be put into the destinationPile passed into it.
   *
   * <p>A move to an open pile is valid if it's empty.
   *
   * @param card            the card that needs to be moved
   * @param destinationPile the destination pile that it needs to be moved into
   * @return true if the card can be put into the destination pile, false otherwise
   */
  @Override
  public boolean canPutCardsInPile(Card card, List<Card> destinationPile) {
    return destinationPile.isEmpty();
  }
}
