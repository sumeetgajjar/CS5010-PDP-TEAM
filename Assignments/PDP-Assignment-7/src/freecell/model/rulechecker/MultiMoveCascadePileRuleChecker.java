package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;
import util.Utils;

/**
 * MultiMoveCascadePileRuleChecker represents rules that apply to a move when the cascade pile is
 * the source or the destination of the move such that multiple cards can be moved from the source
 * pile.
 */
public class MultiMoveCascadePileRuleChecker extends SingleMoveCascadePileRuleChecker {
  private final long emptyCascadePileCount;
  private final long emptyOpenPileCount;

  /**
   * Constructs a <code>MultiMoveCascadePileRuleChecker</code> in terms of the emptyCascadePileCount
   * and emptyOpenPileCount since the maximum number of cards that can be moved at the same time is
   * bounded above by a function of the number of empty cascadePiles and open piles.
   *
   * @param emptyCascadePileCount the number of empty cascade piles
   * @param emptyOpenPileCount    the number of empty open piles
   */
  public MultiMoveCascadePileRuleChecker(long emptyCascadePileCount, long emptyOpenPileCount) {
    this.emptyCascadePileCount = emptyCascadePileCount;
    this.emptyOpenPileCount = emptyOpenPileCount;
  }

  /**
   * Checks if the all cards from the cardIndex in the source pile can be moved.
   *
   * <p>The following are the conditions for a valid move from a source pile:
   * <ul>
   * <li>The first condition is that they should form a valid build, i.e. they should be arranged
   * in alternating colors and consecutive, descending values in the cascade pile that they are
   * moving from.</li>
   * <li>the maximum number of cards that can be moved when there are N free open piles and K empty
   * cascade piles is (N+1) * 2^K, accordingly, if the number of cards to move is more than this, it
   * will amount to an invalid move.
   * </li>
   * </ul>
   *
   * @param cardIndex  the card index of the source sourcePile
   * @param sourcePile the source pile from which the card needs to be taken out
   */
  @Override
  public boolean canGetCardsFromThePile(int cardIndex, List<Card> sourcePile) {
    if (cardIndex < 0 || cardIndex >= sourcePile.size()) {
      return false;
    }

    if (!ifEnoughCascadeAndOpenPilesAreEmpty(sourcePile.size() - cardIndex)) {
      return false;
    }
    return doSourcePileCardsFormValidBuild(Utils.sliceList(sourcePile, cardIndex));
  }

  private boolean doSourcePileCardsFormValidBuild(List<Card> sliceList) {
    for (int currentCardIndex = 0; currentCardIndex < sliceList.size() - 1; currentCardIndex++) {
      Card currentCard = sliceList.get(currentCardIndex);
      Card nextCard = sliceList.get(currentCardIndex + 1);
      if (currentCard.getSuit().getColor() == nextCard.getSuit().getColor()) {
        return false;
      }

      if (currentCard.getCardValue().getPriority() - nextCard.getCardValue().getPriority() != 1) {
        return false;
      }
    }
    return true;
  }

  private boolean ifEnoughCascadeAndOpenPilesAreEmpty(int numberOfCardsToBeMoved) {
    return numberOfCardsToBeMoved <= (emptyOpenPileCount + 1) * Math.pow(2, emptyCascadePileCount);
  }
}
