////
//// DO NOT MODIFY THIS FILE
////
//// You don't need to submit it, but you should make sure it compiles.
//// Further explanation appears below.
////


import freecell.model.FreecellModel;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.PileType;
import freecell.controller.IFreecellController;
import freecell.controller.FreecellController;
import freecell.model.FreecellOperations;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * This class is provided to check that your code implements the expected API.
 * If your code compiles with an unmodified version of this class, then it very
 * likely will also compile with the tests that we use to evaluate your code.
 */
public class ControllerTypeChecks {

  // This doesn't really need to be a dynamic method, since it doesn't use `this`
  static void checkSignatures() {
    Reader stringReader;
    StringBuffer out;

    checkNewModel(FreecellModel.getBuilder()
  .build(),
            FreecellModel.getBuilder()
  .build().getDeck());
    stringReader = new StringReader("C1 8 F1 q");
    out = new StringBuffer();
    checkNewController(
            FreecellModel.getBuilder().build(),

            new FreecellController(stringReader, out));

    checkNewController(
            FreecellMultiMoveModel.getBuilder().build(),

            new FreecellController(stringReader, out));
  }

  // This doesn't really need to be a dynamic method, since it doesn't use `this`
  static <K> void checkNewController(FreecellOperations<K> model,
                                     IFreecellController<K> controller) {
    String input = "4 3";

    try {
      controller.playGame(model.getDeck(), model,false);
    }
    catch (IllegalStateException e) {
      //the input or output did not work
    }
  }

  static <K> void checkNewModel(FreecellOperations<K> model, List<K> deck) {
    List<K> initialDeck = model.getDeck();
    model.startGame(initialDeck,false);
    model.move(PileType.CASCADE, 0, 7, PileType.FOUNDATION, 0);
    String result = model.getGameState();
    boolean done = model.isGameOver();
  }

  private ControllerTypeChecks() {
    throw new RuntimeException("Don't instantiate this: use it as a reference");
  }
}
