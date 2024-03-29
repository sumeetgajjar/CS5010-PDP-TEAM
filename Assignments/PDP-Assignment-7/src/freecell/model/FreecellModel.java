package freecell.model;

import java.util.List;

import freecell.bean.Card;

/**
 * <code>FreecellModel</code> implements the <code>FreecellOperations</code> interface and
 * represents the operations that can be helpful in playing an entire game of FreeCell.
 *
 * <p>We are flexible in this implementation and we allow as few as 1 open pile and as few as 4
 * cascade piles. We mandate 4 foundation piles and allow the user to play the game of freeCell by
 * allowing the user to get a deck of <b>valid</b> cards, making <b>valid</b> moves and viewing the
 * game state at any time. The game can be started by the user by passing a valid deck of cards and
 * a parameter that tells the game whether to shuffle cards or not.
 */
public class FreecellModel extends AbstractFreecellModel {

  /**
   * Constructs a {@link FreecellModel} object with the given params. It throws {@link
   * IllegalArgumentException} for following given cases.
   * <ul>
   * <li>If the given numberOfCascadePile is less than 4 or is greater than 8</li>
   * <li>If the given numberOfOpenPile is less than 1 or is greater than 4</li>
   * </ul>
   *
   * @param numberOfCascadePile the number of cascading piles for the game
   * @param numberOfOpenPile    the number of open piles for the game
   * @throws IllegalArgumentException if the given params are invalid
   */
  private FreecellModel(int numberOfCascadePile, int numberOfOpenPile) {
    super(numberOfCascadePile, numberOfOpenPile);
  }

  /**
   * Removes the card at given index from the source pile and pushes it into the destinationPile.
   *
   * @param sourcePile      source pile of cards
   * @param cardIndex       the index in the source pile of cards
   * @param destinationPile the destination pile of cards
   */
  @Override
  protected void commitMove(List<Card> sourcePile, int cardIndex, List<Card> destinationPile) {
    Card cardFromSource = sourcePile.remove(cardIndex);
    destinationPile.add(cardFromSource);
  }

  /**
   * Gets a new instance of the <code>FreeCellOperationsBuilder</code>.
   *
   * @return instance of the <code>FreeCellOperationsBuilder</code>
   */
  public static FreecellOperationsBuilder getBuilder() {
    return new FreecellModelBuilder();
  }

  /**
   * This class represents a builder for instantiating {@link FreecellModel} objects. <code
   * >FreecellModelBuilder</code> extends <code>AbstractFreecellOperationsBuilder</code> and
   * provides the ability to configure the number of cascade and open piles for the builder to build
   * a FreeCellModel with the specified number of open and cascade piles.
   */
  public static class FreecellModelBuilder extends AbstractFreecellOperationsBuilder {

    /**
     * Returns a Instance of {@link FreecellModel}.
     *
     * @return a Instance of {@link FreecellModel}
     */
    @Override
    protected FreecellOperations<Card> getFreeCellOperationsInstance() {
      return new FreecellModel(numberOfCascadePile, numberOfOpenPile);
    }
  }

}
