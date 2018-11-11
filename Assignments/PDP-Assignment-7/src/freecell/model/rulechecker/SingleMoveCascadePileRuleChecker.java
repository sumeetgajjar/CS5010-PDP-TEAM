package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;
import util.Utils;

/**
 * SingleMoveCascadePileRuleChecker represents rules that apply to a move when the cascade pile is
 * the source or the destination of the move such that only a <b>single</b> card is allowed to move
 * from a cascade pile.
 */
public class SingleMoveCascadePileRuleChecker implements RuleChecker<Card> {

  /**
   * Validates if a card can be put into the destinationPile passed into it.
   *
   * <p>A move to an empty cascade pile is valid if it's empty. Additionally, a valid move is
   * also one such that the last card on the destination cascadePile is one rank above the card that
   * has been passed as input and of the opposite suit color.
   *
   * @param card            the card that needs to be moved
   * @param destinationPile the destination pile that it needs to be moved into
   */
  @Override
  public boolean canPutCardsInPile(Card card, List<Card> destinationPile) {
    if (destinationPile.isEmpty()) {
      return true;
    } else {
      Card lastCardInPile = Utils.getLast(destinationPile);
      return (lastCardInPile.getSuit().getColor() != card.getSuit().getColor())
              && (lastCardInPile.getCardValue().getPriority() - card.getCardValue().getPriority()
              == 1);
    }
  }
}
