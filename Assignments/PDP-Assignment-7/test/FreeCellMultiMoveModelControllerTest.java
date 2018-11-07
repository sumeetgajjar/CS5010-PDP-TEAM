import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import freecell.bean.Card;
import freecell.controller.FreecellController;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import util.TestUtils;
import util.Utils;

public class FreeCellMultiMoveModelControllerTest extends FreecellModelControllerTest {
  //  @Override
  protected FreecellOperations<Card> getFreecellOperation(int cascadeCount, int openCount) {
    return FreecellMultiMoveModel.getBuilder().cascades(cascadeCount).opens(openCount).build();
  }

  @Test
  public void multiMoveModelOtherThanLastCardIndexMoveWorks() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int cardIndex = 13; cardIndex > 9; cardIndex--) {
      stringBuilder.append("C").append(1);
      stringBuilder.append(" ");
      stringBuilder.append(cardIndex);
      stringBuilder.append(" ");
      stringBuilder.append("O").append(14 - cardIndex);
      stringBuilder.append(" ");
    }

    stringBuilder.append("C").append(3).append(" ");
    stringBuilder.append(10).append(" "); // since 4 open piles and 1 empty cascade pile
    stringBuilder.append("C").append(1);
    stringBuilder.append(" ");
    stringBuilder.append("q");

    StringReader actualInput = new StringReader(stringBuilder.toString().trim());
    StringBuffer actualOutput = new StringBuffer();
    StringBuilder expectedOutput = new StringBuilder();

    FreecellController freecellController = new FreecellController(actualInput, actualOutput);
    FreecellOperations<Card> freecellOperations = this.getFreecellOperation(4, 8);

    List<Card> deck = TestUtils.getDeckForMultipleCardsMovementOnCascadePile();
    List<List<Card>> expectedCascadingPiles = TestUtils.getCardsInCascadingPiles(4, deck);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(8);

    freecellController.playGame(deck, freecellOperations, false);
    expectedOutput.append(TestUtils.convertPilesToString(
            expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
    expectedOutput.append(System.lineSeparator());


    for (int cardIndex = 12; cardIndex >= 9; cardIndex--) {
      Card removedCard = expectedCascadingPiles.get(0).remove(cardIndex);
      expectedOpenPiles.get(12 - cardIndex).add(removedCard);

      expectedOutput.append(TestUtils.convertPilesToString(
              expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));
      expectedOutput.append(System.lineSeparator());
    }

    int numberOfCardsMoved = 0;
    while (numberOfCardsMoved < 4) {
      Card removedCard = expectedCascadingPiles.get(2).remove(9);
      expectedCascadingPiles.get(0).add(removedCard);

      numberOfCardsMoved++;
    }
    expectedOutput.append(TestUtils.convertPilesToString(
            expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles));

    expectedOutput.append(System.lineSeparator());
    expectedOutput.append(GAME_QUIT_STRING);

    expectedOutput.append(System.lineSeparator());
    Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

}
