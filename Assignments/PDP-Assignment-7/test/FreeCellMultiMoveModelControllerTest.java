import freecell.bean.Card;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;

public class FreeCellMultiMoveModelControllerTest extends FreecellModelControllerTest {
//  @Override
  protected FreecellOperations<Card> getFreecellOperation() {
    return FreecellMultiMoveModel.getBuilder().cascades(4).opens(4).build();
  }
}
