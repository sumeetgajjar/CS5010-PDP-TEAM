package freecell.model;

import java.util.List;

import freecell.bean.Card;
import util.Utils;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class FreecellModel implements FreecellOperations<Card> {

  private final int numberOfCascadePile;
  private final int numberOfOpenPile;

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
    this.numberOfCascadePile = numberOfCascadePile;
    this.numberOfOpenPile = numberOfOpenPile;
  }

  /**
   * Return a valid and complete deck of cards for a game of Freecell. There is no restriction
   * imposed on the ordering of these cards in the deck. An invalid deck is defined as a deck that
   * has one or more of these flaws:
   * <ul>
   * <li>It does not have 52 cards</li> <li>It has duplicate cards</li>
   * <li>It has at least one invalid card (invalid suit or invalid number)</li>
   * </ul>
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<Card> getDeck() {
    //todo discuss about returning the same order for a given instance
    return Utils.getShuffledDeck();
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
    return new FreecellModelBuilder();
  }

  public static class FreecellModelBuilder implements FreecellOperationsBuilder {
    private int numberOfCascadePile;
    private int numberOfOpenPile;

    private FreecellModelBuilder() {
      this.numberOfCascadePile = 8;
      this.numberOfOpenPile = 4;
    }

    @Override
    public FreecellOperationsBuilder cascades(int c) {
      this.numberOfCascadePile = c;
      return this;
    }

    @Override
    public FreecellOperationsBuilder opens(int o) {
      this.numberOfOpenPile = o;
      return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FreecellOperations build() {
      return new FreecellModel(numberOfCascadePile, numberOfOpenPile);
    }
  }
}
