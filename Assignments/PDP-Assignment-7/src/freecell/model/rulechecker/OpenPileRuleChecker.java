package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;

/**
 * Created by gajjar.s, on 12:37 PM, 11/6/18
 */
public class OpenPileRuleChecker extends AbstractRuleChecker<Card> {
  @Override
  public boolean canPutCardsInPile(Card card, List<Card> pile) {
    return pile.isEmpty();
  }
}
