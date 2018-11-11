package freecell.model.rulechecker;

import java.util.List;

/**
 * RuleChecker represents a set of operations that helps validate a "move" in the game of free
 * cell.
 *
 * <p>These operations are supposed to be used by free cell models in order to validate whether a
 * card can be put into a destination pile and whether a card can be taken from a source pile.
 */
public interface RuleChecker<K> {
  /**
   * Validates if a card can be put into the destinationPile passed into it. The concrete rules are
   * model dependent.
   *
   * @param card            the card that needs to be moved
   * @param destinationPile the destination pile that it needs to be moved into
   * @return true if the card can be put into the destination pile, false otherwise
   */
  boolean canPutCardsInPile(K card, List<K> destinationPile);

  /**
   * Validates if a card can be taken out from the sourcePile. The concrete rules are model
   * dependent.
   *
   * <p>The most common operation is that one can get just the last card from any pile that is
   * the source of a move. A default implementation here helps make the code cleaner and avoid an
   * abstract class.
   *
   * @param cardIndex  the card index of the source sourcePile
   * @param sourcePile the source pile from which the card needs to be taken out
   * @return true if the card can be taken out of the source pile, false otherwise
   */
  default boolean canGetCardsFromThePile(int cardIndex, List<K> sourcePile) {
    // can only get last card and no other card
    return sourcePile.size() != 0 && sourcePile.size() - 1 == cardIndex;
  }
}
