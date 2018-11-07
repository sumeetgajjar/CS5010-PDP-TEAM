package freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import freecell.model.rulechecker.FoundationPileRuleChecker;
import freecell.model.rulechecker.OpenPileRuleChecker;
import freecell.model.rulechecker.RuleChecker;
import freecell.model.rulechecker.SingleMoveCascadePileRuleChecker;
import util.Utils;

/**
 * <code>AbstractFreecellModel</code> represents common operations for multiple implementations
 * of the FreeCellOperations interface. A future implementation has the option of directly
 * implementing the interface.
 *
 * <p>The class makes no changes to the public API of the interface, it merely abstracts common
 * functionality of the implementations.</p>
 */
public abstract class AbstractFreecellModel implements FreecellOperations<Card> {
  private static final int FOUNDATION_PILE_COUNT = 4;
  private static final int TOTAL_NUMBER_OF_CARDS_IN_DECK = 52;
  private static final int NUMBER_OF_CARDS_INDIVIDUAL_SUIT = 13;

  /**
   * Represents a mapping from pile category to piles.
   */
  protected final Map<PileCategory, List<List<Card>>> pilesMap;
  /**
   * Represents a mapping between PileCategory to a supplier of a rule checker.
   *
   * <p>The idea is that, that the mapping will provide a supplier of a rule checker that
   * evaluates lazily and returns a rule checker that in turn checks if a move is valid.
   */
  protected final Map<PileCategory, Supplier<RuleChecker<Card>>> ruleCheckerMap;
  protected boolean hasGameStarted;

  /**
   * Protected Constructor for invocation by subclass constructors. It throws {@link
   * IllegalArgumentException} for following given cases.
   * <ul>
   * <li>If the given numberOfCascadePile is less than 4 or is greater than 8</li>
   * <li>If the given numberOfOpenPile is less than 1 or is greater than 4</li>
   * </ul>
   *
   * <p>This is a protected constructor since the initialization
   * logic is common amongst implementations, however, there is no way to initialize a model using
   * the constructors. In order to construct a model, one must use the builders of the concrete
   * implementations in order to get a model.
   *
   * @param numberOfCascadePile the number of cascading piles for the game
   * @param numberOfOpenPile    the number of open piles for the game
   * @param numberOfCascadePile number of cascade piles
   * @param numberOfOpenPile    number of open piles
   * @throws IllegalArgumentException if the given params are invalid
   */
  protected AbstractFreecellModel(int numberOfCascadePile, int numberOfOpenPile) {
    if (numberOfCascadePile < 4 || numberOfOpenPile < 1) {
      throw new IllegalArgumentException("Invalid input");
    }

    this.pilesMap = getPilesMap(numberOfCascadePile, numberOfOpenPile);
    this.ruleCheckerMap = getRuleCheckerMap();
    this.hasGameStarted = false;
  }

  /**
   * Return a valid and complete deck of cards for a game of Freecell. There is no restriction
   * imposed on the ordering of these cards in the deck. An invalid deck is defined as a deck that
   * has one or more of these flaws:
   * <ul>
   * <li>It does not have 52 cards</li> <li>It has duplicate cards</li>
   * <li>It has at least one invalid card (invalid suit or invalid number or null)</li>
   * </ul>
   *
   * <p>The implementation returns a shuffled deck on each invocation.
   *
   * @return the deck of cards as a list
   */
  @Override
  public List<Card> getDeck() {
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
   * <p>The method will throw an IllegalArgumentException in the following cases:
   * <ul>
   * <li> If the deck is empty or null </li>
   * <li> If the deck size is not 52 </li>
   * <li> If the deck has duplicate cards </li>
   * <li> If the deck has null cards </li>
   * </ul>
   * </p>
   *
   * <p>The deck passed as input will not be mutated by <code>startGame</code></p>
   *
   * @param deck    the deck to be dealt
   * @param shuffle if true, shuffle the deck else deal the deck as-is
   * @throws IllegalArgumentException if the deck is invalid
   */
  @Override
  public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {
    requireValidDeck(deck);
    this.clearPiles();
    this.distributeDeck(deck, shuffle);
    this.hasGameStarted = true;
  }

  /**
   * Move a card from the given source pile to the given destination pile, if the move is valid. If
   * the destination of the card is same as original position then its a valid move.
   *
   * <p>The following inputs as moves are not allowed and results in throwing an
   * IllegalArgumentException:
   * <ul>
   * <li> PileType cannot be null </li>
   * <li> pileNumber and cardIndex cannot be negative </li>
   * <li> pileNumber and cardIndex cannot overflow the capacity of the pile </li>
   * <li> Any move from an empty pile </li>
   * <li> A move to a full open pile is invalid </li>
   * <li> A foundation pile cannot start with two </li>
   * <li> A foundation pile can only take a card of the same suit and exactly one higher in
   * rank, if it's given another card - it is an invalid move </li>
   * <li> If the given cardIndex is not the last card index of the source pile</li>
   * <li> If move is invoked with destination same as source </li>
   * </ul>
   *
   * <p>The following moves even if they have valid input parameters will result in throwing an
   * IllegalStateException
   * <ul>
   * <li> A move invoked before the game has started </li>
   * <li> A move invoked after game is over </li>
   * </ul>
   *
   * <p>There is no ordering of suits in foundation piles, when a valid move is made from any pile
   * to an empty foundation pile, that pile is now assigned to the suit entering it.
   *
   * <p>A card can be moved from any pile to any pile if it is not an invalid move.
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   * @throws IllegalArgumentException if the move is not possible {@link PileType})
   * @throws IllegalStateException    if a move is attempted before the game has starts or after
   *                                  game is over
   */
  @Override
  public void move(PileType source,
                   int pileNumber,
                   int cardIndex,
                   PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {
    if (this.hasGameStarted && !this.isGameOver()) {
      this.makeMove(PileCategory.getPileCategory(source),
              pileNumber,
              cardIndex,
              PileCategory.getPileCategory(destination),
              destPileNumber);
    } else {
      throw new IllegalStateException("cannot move before starting game or after game is over");
    }
  }

  /**
   * Signal if the game is over or not. If this method is invoked before starting the game then it
   * returns false.
   *
   * <p>A game is over when all these conditions are fulfilled:
   * <ul>
   * <li> If the all 4 foundation piles have 13 cards each </li>
   * <li> If all other types of piles are empty </li>
   * </ul>
   *
   * @return true if game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    List<List<Card>> cascadePiles = this.getPiles(PileCategory.CASCADE);
    long emptyCascadingPilesCount = cascadePiles.stream()
            .filter(List::isEmpty)
            .count();

    List<List<Card>> openPiles = this.getPiles(PileCategory.OPEN);
    long emptyOpenPilesCount = openPiles.stream()
            .filter(List::isEmpty)
            .count();

    long fullFoundationPilesCount = this.getPiles(PileCategory.FOUNDATION).stream()
            .filter(pile -> pile.size() == NUMBER_OF_CARDS_INDIVIDUAL_SUIT)
            .count();

    return emptyCascadingPilesCount == cascadePiles.size()
            && emptyOpenPilesCount == openPiles.size()
            && fullFoundationPilesCount == FOUNDATION_PILE_COUNT;
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
   * <p>If getGameState is invoked before starting the game it returns an empty string.
   *
   * @return the formatted string as above
   */
  @Override
  public String getGameState() {
    if (this.hasGameStarted) {
      return String.format("%s%s%s%s%s",
              pilesToString(this.getPiles(PileCategory.FOUNDATION), PileCategory.FOUNDATION),
              System.lineSeparator(),
              pilesToString(this.getPiles(PileCategory.OPEN), PileCategory.OPEN),
              System.lineSeparator(),
              pilesToString(this.getPiles(PileCategory.CASCADE), PileCategory.CASCADE));
    } else {
      return "";
    }
  }

  /**
   * Commits a move and mutates the state in the piles. This method is supposed to be called after
   * move validation is complete, however this method should not be public.
   *
   * @param sourcePile      source pile of cards
   * @param cardIndex       the index in the source pile of cards
   * @param destinationPile the destination pile of cards
   */
  protected abstract void commitMove(List<Card> sourcePile, int cardIndex,
                                     List<Card> destinationPile);


  /**
   * Returns a {@link RuleChecker<Card>} for Cascade Pile.
   *
   * @return a {@link RuleChecker<Card>} for Cascade Pile
   */
  protected RuleChecker<Card> getCascadePileRuleChecker() {
    return new SingleMoveCascadePileRuleChecker();
  }

  /**
   * Gets all the piles of a given pile category.
   *
   * @param pileCategory pile category enum
   * @return all piles in terms of a list of list of cards
   */
  protected List<List<Card>> getPiles(PileCategory pileCategory) {
    List<List<Card>> list = this.pilesMap.get(pileCategory);
    return Utils.requireNonNull(list);
  }

  private Map<PileCategory, List<List<Card>>> getPilesMap(int numberOfCascadePile,
                                                          int numberOfOpenPile) {

    Map<PileCategory, List<List<Card>>> map = new EnumMap<>(PileCategory.class);
    map.put(PileCategory.FOUNDATION, Utils.getListOfEmptyLists(FOUNDATION_PILE_COUNT));
    map.put(PileCategory.OPEN, Utils.getListOfEmptyLists(numberOfOpenPile));
    map.put(PileCategory.CASCADE, Utils.getListOfEmptyLists(numberOfCascadePile));
    return map;
  }

  private Map<PileCategory, Supplier<RuleChecker<Card>>> getRuleCheckerMap() {
    Map<PileCategory, Supplier<RuleChecker<Card>>> map = new EnumMap<>(PileCategory.class);
    map.put(PileCategory.FOUNDATION, FoundationPileRuleChecker::new);
    map.put(PileCategory.OPEN, OpenPileRuleChecker::new);
    map.put(PileCategory.CASCADE, this::getCascadePileRuleChecker);
    return map;
  }

  private List<Card> getPile(PileCategory pileCategory, int index) {
    List<List<Card>> piles = this.getPiles(pileCategory);
    try {
      return piles.get(index);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  private RuleChecker<Card> getRuleChecker(PileCategory pileCategory) {
    RuleChecker<Card> ruleChecker = this.ruleCheckerMap.get(pileCategory).get();
    return Utils.requireNonNull(ruleChecker);
  }

  private void clearPiles() {
    this.getPiles(PileCategory.FOUNDATION).forEach(List::clear);
    this.getPiles(PileCategory.CASCADE).forEach(List::clear);
    this.getPiles(PileCategory.OPEN).forEach(List::clear);
  }

  private void distributeDeck(List<Card> deck, boolean shuffle) {
    List<Card> deckCopy = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(deckCopy);
    }

    int cardIndex = 0;
    int pileNumber = 0;
    while (cardIndex < deckCopy.size()) {
      List<List<Card>> cascadePiles = this.getPiles(PileCategory.CASCADE);
      cascadePiles.get(pileNumber).add(deckCopy.get(cardIndex));
      pileNumber = (pileNumber + 1) % cascadePiles.size();
      cardIndex++;
    }
  }

  private void makeMove(PileCategory source,
                        int pileNumber,
                        int cardIndex,
                        PileCategory destination,
                        int destPileNumber) {

    Utils.requireNonNull(source);
    Utils.requireNonNull(destination);

    List<Card> sourcePile = getPile(source, pileNumber);
    boolean canGetCardFromThePile = this.getRuleChecker(source)
            .canGetCardsFromThePile(cardIndex, sourcePile);

    Card cardFromSource;
    if (canGetCardFromThePile) {
      cardFromSource = sourcePile.get(cardIndex);
    } else {
      throw new IllegalArgumentException("Invalid input");
    }

    List<Card> destinationPile = this.getPile(destination, destPileNumber);
    // we ask if the destination can put the card in it's pile, if it can't then it is an invalid
    // input
    boolean canPutCardInPile = this.getRuleChecker(destination)
            .canPutCardsInPile(cardFromSource, destinationPile);

    if (canPutCardInPile) {
      this.commitMove(sourcePile, cardIndex, destinationPile);
    } else {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  private static String pilesToString(List<List<Card>> piles, PileCategory pile) {
    List<String> listOfStrings = piles.stream().map(listOfCards -> listOfCards.stream()
            .map(card -> " " + card.toString()).collect(Collectors.joining(","))
    ).collect(Collectors.toList());

    StringBuilder stringBuilder = new StringBuilder();
    for (int pileIndex = 0; pileIndex < listOfStrings.size(); pileIndex++) {
      stringBuilder.append(pile.getSymbol());
      stringBuilder.append(pileIndex + 1);
      stringBuilder.append(":");
      stringBuilder.append(listOfStrings.get(pileIndex));
      stringBuilder.append(System.lineSeparator());
    }
    return stringBuilder.toString().trim();

  }

  /**
   * Checks if the given deck is valid, throws {@link IllegalArgumentException} otherwise. It throws
   * {@link IllegalArgumentException} if the given deck does not contains 52 card or if the given
   * deck contains a null card or if the given deck contains a duplicate card.
   *
   * @param deck the deck to check
   * @throws IllegalArgumentException if the given deck is invalid
   */
  private static void requireValidDeck(List<Card> deck) throws IllegalArgumentException {
    Utils.requireNonNull(deck);

    // deck has an incorrect size
    if (deck.isEmpty() || deck.size() != TOTAL_NUMBER_OF_CARDS_IN_DECK) {
      throw new IllegalArgumentException("Invalid input");
    }

    // deck has null cards
    long nullCardCount = deck.stream().filter(Objects::isNull).count();
    if (nullCardCount > 0) {
      throw new IllegalArgumentException("Invalid input");
    }

    // deck has duplicates
    Set<Card> cards = new HashSet<>(deck);
    if (cards.size() != TOTAL_NUMBER_OF_CARDS_IN_DECK) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  /**
   * <code>AbstractFreecellOperationsBuilder</code> implements
   * <code>FreecellOperationsBuilder</code>
   * and provides the ability to configure the number of cascade and open piles for the builder to
   * build a concrete implementation of FreecellOperations with the specified number of open and
   * cascade piles.
   */
  protected abstract static class AbstractFreecellOperationsBuilder
          implements FreecellOperationsBuilder {

    protected int numberOfCascadePile;
    protected int numberOfOpenPile;

    /**
     * Protected Constructor for invocation by subclass constructors. Constructs a
     * <code>AbstractFreecellOperationsBuilder</code> with default 8 cascadePiles and 4 openPiles
     * since those are default values for the popular online version of the game.
     */
    protected AbstractFreecellOperationsBuilder() {
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
    public FreecellOperations<Card> build() {
      return getFreeCellOperationsInstance();
    }

    /**
     * Return a concrete instance of a freeCellOperations model.
     *
     * @return freeCellOperations model
     */
    protected abstract FreecellOperations<Card> getFreeCellOperationsInstance();
  }
}
