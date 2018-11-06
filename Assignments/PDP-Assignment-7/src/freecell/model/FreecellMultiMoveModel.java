package freecell.model;

import java.util.List;

import freecell.bean.Card;

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
  protected void commitMove(List<Card> sourcePile, int cardIndex, List<Card> destinationPile) {

  }

  public static class FreecellModelBuilder extends AbstractFreecellOperationsBuilder {
    @Override
    protected FreecellOperations<Card> getFreeCellOperationsInstance() {
      return new FreecellMultiMoveModel(numberOfCascadePile, numberOfOpenPile);
    }
  }
}
