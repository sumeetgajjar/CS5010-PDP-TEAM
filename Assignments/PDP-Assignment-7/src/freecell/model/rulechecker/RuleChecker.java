package freecell.model.rulechecker;

import java.util.List;

/**
 * Created by gajjar.s, on 12:32 PM, 11/6/18
 */
public interface RuleChecker<K> {
  boolean canPutCardsInPile(K card, List<K> pile);

  boolean canGetCardsFromThePile(int cardIndex, List<K> pile);
}
