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

  /**
   * Deal a new game of freecell with the given deck, with or without shuffling it first. This
   * method first verifies that the deck is valid. It deals the deck among the cascade piles in
   * roundrobin fashion. Thus if there are 4 cascade piles, the 1st pile will get cards 0, 4, 8,
   * ..., the 2nd pile will get cards 1, 5, 9, ..., the 3rd pile will get cards 2, 6, 10, ... and
   * the 4th pile will get cards 3, 7, 11, .... Depending on the number of cascade piles, they may
   * have a different number of cards
   *
   * <p> The method will throw an IllegalArgumentException in the following cases:
   * <ul>
   * <li> If the deck is empty or null </li>
   * <li> If the deck size is not 52 </li>
   * <li> If the deck has duplicate cards </li>
   * <li> If the deck has null cards </li>
   * </ul>
   * </p>
   *
   * @param deck    the deck to be dealt
   * @param shuffle if true, shuffle the deck else deal the deck as-is
   * @throws IllegalArgumentException if the deck is invalid
   */
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
