package freecell.model;

import java.util.List;

import freecell.bean.Card;
import freecell.bean.GameState;
import freecell.bean.PileCategory;
import util.Utils;

/**
 * Created by gajjar.s, on 1:47 PM, 10/28/18
 */
public class FreecellModel implements FreecellOperations<Card> {
  private final GameState gameState;
  private boolean hasGameStarted;

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
    if (numberOfCascadePile < 4 || numberOfOpenPile < 1) {
      throw new IllegalArgumentException("Invalid input");
    }

    this.hasGameStarted = false;
    this.gameState = new GameState(Utils.getListOfEmptyLists(GameState.FOUNDATION_PILE_COUNT),
            Utils.getListOfEmptyLists(numberOfOpenPile),
            Utils.getListOfEmptyLists(numberOfCascadePile));
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
   * <p> The deck passed as input will not be mutated by <code>startGame</code></p>
   *
   * @param deck    the deck to be dealt
   * @param shuffle if true, shuffle the deck else deal the deck as-is
   * @throws IllegalArgumentException if the deck is invalid
   */
  @Override
  public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {
    this.gameState.startGame(deck, shuffle);
    this.hasGameStarted = true;
  }

  /**
   * Move a card from the given source pile to the given destination pile, if the move is valid. If
   * the destination of the card is same as original position then its a valid move.
   *
   * <p>
   * The following inputs as moves are not allowed:
   * <ul>
   * <li> A card cannot be moved from Foundation pile to another Foundation pile. </li>
   * <li> PileType cannot be null </li>
   * <li> pileNumber and cardIndex cannot be negative </li>
   * <li> pileNumber and cardIndex cannot overflow </li>
   * <li> A move invoked after game is over </li>
   * </ul>
   *
   * <p> There is no ordering of suits in foundation piles, when a valid move is made from any pile
   * to an empty foundation pile, that pile is now assigned to the suit entering it.
   *
   * <p> A card can be moved from any pile to any pile if it is an invalid move.
   *
   * <p> The following are invalid moves:
   * <ul>
   * <li> Any move from an empty pile </li>
   * <li> A move to a full open pile is invalid </li>
   * <li> A foundation pile cannot start with two </li>
   * <li> A foundation pile once assigned a suit cannot take a card of other suit </li>
   * <li> A foundation pile can only take a card of the same suit and exactly one higher in rank
   * </li>
   * <li> If the given cardIndex is not the last card index of the source pile</li>
   * </ul>
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
  public void move(PileType source,
                   int pileNumber,
                   int cardIndex,
                   PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    this.gameState.makeMove(PileCategory.getPileCategory(source),
            pileNumber, cardIndex, PileCategory.getPileCategory(destination), destPileNumber);
  }

  @Override
  public boolean isGameOver() {
    return gameState.hasGameCompleted();
  }

  /**
   * Return the present state of the game as a string. The string is formatted as follows:
   * <pre>
   * F1:[b]f11,[b]f12,[b],...,[b]f1n1[n] (Cards in foundation pile 1 in order)
   * F2:[b]f21,[b]f22,[b],...,[b]f2n2[n] (Cards in foundation pile 2 in order)
   * ...
   * Fm:[b]fm1,[b]fm2,[b],...,[b]fmnm[n] (Cards in foundation pile m in
   * order)
   * O1:[b]o11[n] (Cards in open pile 1)
   * O2:[b]o21[n] (Cards in open pile 2)
   * ...
   * Ok:[b]ok1[n] (Cards in open pile k)
   * C1:[b]c11,[b]c12,[b]...,[b]c1p1[n] (Cards in cascade pile 1 in order)
   * C2:[b]c21,[b]c22,[b]...,[b]c2p2[n] (Cards in cascade pile 2 in order)
   * ...
   * Cs:[b]cs1,[b]cs2,[b]...,[b]csps (Cards in cascade pile s in order)
   *
   * where [b] is a single blankspace, [n] is newline. Note that there is no
   * newline on the last line
   * </pre>
   *
   * <p> If getGameState is invoked before starting the game it returns an empty string.
   *
   * @return the formatted string as above
   */
  @Override
  public String getGameState() {
    if (this.hasGameStarted) {
      return this.gameState.toString();
    } else {
      return "";
    }
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
