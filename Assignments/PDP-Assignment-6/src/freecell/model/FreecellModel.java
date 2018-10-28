package freecell.model;

import java.util.List;

import freecell.bean.Card;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class FreecellModel implements FreecellOperations<Card> {

  @Override
  public List<Card> getDeck() {
    return null;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {

  }

  /**
   * Move a card from the given source pile to the given destination pile, if the move is valid.
   *
   * <p>
   * The following inputs as moves are not allowed:
   * <ul>
   * <li> A card cannot be moved from Foundation pile to another Foundation pile. </li>
   * <li> PileType cannot be null </li>
   * <li> pileNumber and cardIndex cannot be negative </li>
   * <li> pileNumber and cardIndex cannot overflow </li>
   * </ul>
   * </p>
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   * @throws IllegalArgumentException if the move is not possible {@link PileType})
   * @throws IllegalStateException    if a move is attempted before the game has starts
   */
  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination, int destPileNumber) throws IllegalArgumentException, IllegalStateException {

  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public String getGameState() {
    return null;
  }

  public static FreecellOperationsBuilder getBuilder() {
    return null;
  }
}
