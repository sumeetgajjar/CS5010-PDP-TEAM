package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;
import freecell.bean.CardValue;
import util.Utils;

/**
 * FoundationPileRuleChecker represents rules that apply to a move when the Foundation pile is the
 * source or the destination of the move.
 */
public class FoundationPileRuleChecker implements RuleChecker<Card> {

  /**
   * Validates if a card can be put into the destinationPile passed into it.
   *
   * <p>There is no ordering of suits in foundation piles, when a valid move is made from any pile
   * to an empty foundation pile, that pile is now assigned to the suit entering it. If the
   * foundation pile is not empty, then the last card on the foundation pile should be of the same
   * suit with one less rank than the card that has been passed to it.
   *
   * @param card            the card that needs to be moved
   * @param destinationPile the destination pile that it needs to be moved into
   * @return true if the card can be put into the destination pile, false otherwise
   */
  @Override
  public boolean canPutCardsInPile(Card card, List<Card> destinationPile) {
    if (destinationPile.isEmpty()) {
      return card.getCardValue().equals(CardValue.ACE);
    } else {
      Card lastCardInPile = Utils.getLast(destinationPile);
      return (lastCardInPile.getSuit() == card.getSuit())
              && (card.getCardValue().getPriority() - lastCardInPile.getCardValue().getPriority()
              == 1);
    }
  }
}