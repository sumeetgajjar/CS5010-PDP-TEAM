package freecell.model;

import java.util.List;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import freecell.model.rulechecker.MultiMoveCascadePileRuleChecker;
import util.Utils;

/**
 * Represents a FreeCellMultiMoveModel that can do everything that a FreeCellModel can do and
 * additionally it can move multiple cards from a source cascade pile to a destination cascade
 * pile.
 *
 * <p>A multi-card move is basically several single-card moves, using free open piles and empty
 * cascade piles as “intermediate slots”.
 *
 * <p>The following are the conditions for a valid move from a source pile to a destination cascade
 * pile:
 * <ul>
 * <li>The first condition is that they should form a valid build, i.e. they should be arranged in
 * alternating colors and consecutive, descending values in the cascade pile that they are moving
 * from.</li>
 * <li>The second condition is the same for any move to a cascade pile: these cards should form a
 * build with the last card in the destination cascade pile.</li>
 * <li>the maximum number of cards that can be moved when there are N free open piles and K empty
 * cascade piles is (N+1) * 2^K, accordingly, if the number of cards to move is more than this, it
 * will amount to an invalid move.
 * </li>
 * </ul>
 */
public class FreecellMultiMoveModel extends AbstractFreecellModel {

  private FreecellMultiMoveModel(int numberOfCascadePile, int numberOfOpenPile) {
    super(numberOfCascadePile, numberOfOpenPile);
  }

  /**
   * Gets a new instance of the <code>FreeCellOperationsBuilder</code>.
   *
   * @return instance of the <code>FreeCellOperationsBuilder</code>
   */
  public static FreecellOperationsBuilder getBuilder() {
    return new FreecellModelBuilder();
  }

  @Override
  protected MultiMoveCascadePileRuleChecker getCascadePileRuleChecker() {
    long emptyCascadePileCount =
            this.getPiles(PileCategory.CASCADE).stream().filter(List::isEmpty).count();
    long emptyOpenPileCount =
            this.getPiles(PileCategory.OPEN).stream().filter(List::isEmpty).count();
    return new MultiMoveCascadePileRuleChecker(emptyCascadePileCount, emptyOpenPileCount);
  }

  @Override
  protected void commitMove(List<Card> sourcePile, int cardIndex, List<Card> destinationPile) {
    List<Card> cardsInSourcePile = Utils.sliceList(sourcePile, cardIndex);
    destinationPile.addAll(cardsInSourcePile);
    while (cardIndex < sourcePile.size()) {
      sourcePile.remove(sourcePile.size() - 1);
    }
  }

  public static class FreecellModelBuilder extends AbstractFreecellOperationsBuilder {
    @Override
    protected FreecellOperations<Card> getFreeCellOperationsInstance() {
      return new FreecellMultiMoveModel(numberOfCascadePile, numberOfOpenPile);
    }
  }
}
