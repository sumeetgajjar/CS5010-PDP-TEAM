package freecell.model;

import java.util.List;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import freecell.model.rulechecker.MultiMoveCascadePileRuleChecker;
import freecell.model.rulechecker.RuleChecker;
import util.Utils;

/**
 * Created by gajjar.s, on 6:58 PM, 11/3/18
 */
public class FreecellMultiMoveModel extends AbstractFreecellModel {

  private FreecellMultiMoveModel(int numberOfCascadePile, int numberOfOpenPile) {
    super(numberOfCascadePile, numberOfOpenPile);
  }

  public static FreecellOperationsBuilder getBuilder() {
    return new FreecellModelBuilder();
  }

  @Override
  protected RuleChecker getCascadePileRuleChecker() {
    return new MultiMoveCascadePileRuleChecker(this.getPiles(PileCategory.CASCADE).size(),
            this.getPiles(PileCategory.OPEN).size());
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
