package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;
import freecell.bean.CardValue;
import util.Utils;

/**
 * Created by gajjar.s, on 12:37 PM, 11/6/18
 */
public class FoundationPileRuleChecker extends AbstractRuleChecker<Card> {

  @Override
  public boolean canPutCardsInPile(Card card, List<Card> pile) {
    if (pile.isEmpty()) {
      return card.getCardValue().equals(CardValue.ACE);
    } else {
      Card lastCardInPile = Utils.getLast(pile);
      return (lastCardInPile.getSuit() == card.getSuit())
              && (card.getCardValue().getPriority() - lastCardInPile.getCardValue().getPriority()
              == 1);
    }
  }
}
