package freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import freecell.model.rulechecker.FoundationPileRuleChecker;
import freecell.model.rulechecker.OpenPileRuleChecker;
import freecell.model.rulechecker.RuleChecker;
import freecell.model.rulechecker.SingleMoveCascadePileRuleChecker;
import util.Utils;

/**
 * Created by gajjar.s, on 1:31 PM, 11/6/18
 */
public abstract class AbstractFreecellModel implements FreecellOperations<Card> {
  private static final int FOUNDATION_PILE_COUNT = 4;
  private static final int TOTAL_NUMBER_OF_CARDS_IN_DECK = 52;
  private static final int NUMBER_OF_CARDS_INDIVIDUAL_SUIT = 13;

  protected final Map<PileCategory, List<List<Card>>> pilesMap;
  protected final Map<PileCategory, RuleChecker<Card>> ruleCheckerMap;
  protected boolean hasGameStarted;

  protected AbstractFreecellModel(int numberOfCascadePile, int numberOfOpenPile) {
    if (numberOfCascadePile < 4 || numberOfOpenPile < 1) {
      throw new IllegalArgumentException("Invalid input");
    }

    this.pilesMap = getPilesMap(numberOfCascadePile, numberOfOpenPile);
    this.ruleCheckerMap = getRuleCheckerMap();
    this.hasGameStarted = false;
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

  private static Card getCardFromPile(int cardIndex, List<Card> pile) {
    try {
      // can only get last card and no other card
      if (pile.size() - 1 != cardIndex) {
        throw new IllegalArgumentException("Invalid input");
      }
      return pile.get(cardIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  private static void requireValidDeck(List<Card> deck) {
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

  protected Map<PileCategory, List<List<Card>>> getPilesMap(int numberOfCascadePile,
                                                            int numberOfOpenPile) {

    Map<PileCategory, List<List<Card>>> map = new EnumMap<>(PileCategory.class);
    map.put(PileCategory.FOUNDATION, Utils.getListOfEmptyLists(FOUNDATION_PILE_COUNT));
    map.put(PileCategory.OPEN, Utils.getListOfEmptyLists(numberOfOpenPile));
    map.put(PileCategory.CASCADE, Utils.getListOfEmptyLists(numberOfCascadePile));
    return map;
  }

  protected Map<PileCategory, RuleChecker<Card>> getRuleCheckerMap() {
    Map<PileCategory, RuleChecker<Card>> map = new EnumMap<>(PileCategory.class);
    map.put(PileCategory.FOUNDATION, new FoundationPileRuleChecker());
    map.put(PileCategory.OPEN, new OpenPileRuleChecker());
    map.put(PileCategory.CASCADE, new SingleMoveCascadePileRuleChecker());
    return map;
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

  private List<List<Card>> getPiles(PileCategory pileCategory) {
    List<List<Card>> list = this.pilesMap.get(pileCategory);
    return Utils.requireNonNull(list);
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

  private List<Card> getPile(PileCategory pileCategory, int index) {
    List<List<Card>> piles = this.getPiles(pileCategory);
    try {
      return piles.get(index);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  private RuleChecker<Card> getRuleChecker(PileCategory pileCategory) {
    RuleChecker<Card> ruleChecker = this.ruleCheckerMap.get(pileCategory);
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

  protected abstract void commitMove(List<Card> sourcePile, int cardIndex,
                                     List<Card> destinationPile);

  /**
   * <code>FreecellModelBuilder</code> implements <code>FreecellOperationsBuilder</code> and
   * provides the ability to configure the number of cascade and open piles for the builder to build
   * a FreeCellModel with the specified number of open and cascade piles.
   */
  protected abstract static class AbstractFreecellOperationsBuilder implements FreecellOperationsBuilder {

    protected int numberOfCascadePile;
    protected int numberOfOpenPile;

    /**
     * Constructs a <code>FreecellModelBuilder</code> with default 8 cascadePiles and 4 openPiles
     * since those are default values for the popular online version of the game.
     */
    protected AbstractFreecellOperationsBuilder() {
      this.numberOfCascadePile = 8;
      this.numberOfOpenPile = 4;
    }

    protected abstract FreecellOperations<Card> getFreeCellOperationsInstance();

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
  }
}
