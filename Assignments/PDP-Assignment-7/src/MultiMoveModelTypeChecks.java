////
//// DO NOT MODIFY THIS FILE
////
//// You don't need to submit it, but you should make sure it compiles.
//// Further explanation appears below.
////


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import freecell.model.FreecellModel;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;
import freecell.model.FreecellOperationsBuilder;
import freecell.model.PileType;
import freecell.controller.FreecellController;
import freecell.controller.IFreecellController;

/**
 * This class is provided to check that your code implements the expected API.
 * If your code compiles with an unmodified version of this class, then it very
 * likely will also compile with the tests that we use to evaluate your code.
 */
public class MultiMoveModelTypeChecks {

  // This doesn't really need to be a dynamic method, since it doesn't use `this`
  <T> void checkSignatures() {
    Reader stringReader;
    StringBuffer out;
    FreecellOperationsBuilder multiModelBuilder =
            FreecellMultiMoveModel.getBuilder();

    FreecellOperationsBuilder singleModelBuilder =
            FreecellModel.getBuilder();
    checkNewModel(
            multiModelBuilder.build(),
            multiModelBuilder.build().getDeck());

    //code below with the controller
    stringReader = new StringReader("C1 8 F1 q");
    out = new StringBuffer();
    checkNewController(
            multiModelBuilder.build()
            ,new FreecellController(stringReader, out));
    checkNewController(
            singleModelBuilder.build()
            ,new FreecellController(stringReader, out));

  }

  // This doesn't really need to be a dynamic method, since it doesn't use `this`
   private <K> void checkNewController(FreecellOperations<K> model,
                                       IFreecellController<K> controller) {
    String input = "4 3";

    try {
      controller.playGame(model.getDeck(), model,false);
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
  }

  private <K> void checkNewModel(FreecellOperations<K> model, List<K> deck) {
    List<K> initialDeck = model.getDeck();
    model.startGame(initialDeck,false);
    model.move(PileType.CASCADE, 0, 7, PileType.OPEN, 0);
    String result = model.getGameState();
    boolean done = model.isGameOver();
  }

  private MultiMoveModelTypeChecks() {
    throw new RuntimeException("Don't instantiate this: use it as a reference");
  }
}
