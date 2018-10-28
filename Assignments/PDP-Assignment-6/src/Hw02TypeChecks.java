import java.util.List;

import freecell.model.PileType;

/**
 * Do not modify this file. This file should compile correctly with your code!
 */
public class Hw02TypeChecks {

  public static void main(String[] args) {
    helper(FreecellModel
            .getBuilder()
            .build());
    helper(FreecellModel
            .getBuilder()
            .cascades(8)
            .opens(4)
            .build());

  }

  private static <T> void helper(freecell.model.FreecellOperations<T> model) {
    List<T> deck = model.getDeck();
    model.startGame(deck, false);
    model.move(PileType.CASCADE, 0, 6, PileType.CASCADE, 2);
  }
}
