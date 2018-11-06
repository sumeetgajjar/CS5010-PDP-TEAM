package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;
import util.Utils;

/**
 * Created by gajjar.s, on 12:37 PM, 11/6/18
 */
public class SingleMoveCascadePileRuleChecker extends AbstractRuleChecker<Card> {

  @Override
  public boolean canPutCardInPile(Card card, List<Card> pile) {
    if (pile.isEmpty()) {
      return true;
    } else {
      Card lastCardInPile = Utils.getLast(pile);
      return (lastCardInPile.getSuit().getColor() != card.getSuit().getColor())
              && (lastCardInPile.getCardValue().getPriority() - card.getCardValue().getPriority()
              == 1);
    }
  }
}
