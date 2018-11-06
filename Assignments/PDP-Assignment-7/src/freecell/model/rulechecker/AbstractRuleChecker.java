package freecell.model.rulechecker;

import java.util.List;

/**
 * Created by gajjar.s, on 12:36 PM, 11/6/18
 */
public abstract class AbstractRuleChecker<K> implements RuleChecker<K> {

  @Override
  public boolean canGetCardFromThePile(int cardIndex, List<K> pile) {
    // can only get last card and no other card
    return pile.size() != 0 && pile.size() - 1 == cardIndex;
  }
}
