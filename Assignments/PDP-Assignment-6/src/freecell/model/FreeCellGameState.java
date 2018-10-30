package freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import util.Utils;

public class FreeCellGameState {
  public static final int FOUNDATION_PILE_COUNT = 4;
  private static final int TOTAL_NUMBER_OF_CARDS_IN_DECK = 52;
  private static final int NUMBER_OF_CARDS_INDIVIDUAL_SUIT = 13;
  private final List<List<Card>> foundationPiles;
  private final List<List<Card>> openPiles;
  private final List<List<Card>> cascadePiles;

  public FreeCellGameState(List<List<Card>> foundationPiles,
                           List<List<Card>> openPiles,
                           List<List<Card>> cascadePiles) {
    this.foundationPiles = foundationPiles;
    this.openPiles = openPiles;
    this.cascadePiles = cascadePiles;
  }

  private static String pileToString(List<List<Card>> piles, PileCategory pile) {
    List<String> listOfStrings = piles.stream()
            .map(listOfCards -> listOfCards.stream().map(Card::toString)
                    .collect(Collectors.joining(","))
            ).collect(Collectors.toList());

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < listOfStrings.size(); i++) {
      stringBuilder.append(pile.getSymbol());
      stringBuilder.append(i + 1);
      stringBuilder.append(":");
      stringBuilder.append(listOfStrings.get(i));
      stringBuilder.append(System.lineSeparator());
    }
    return stringBuilder.toString().trim();

  }

  @Override
  public String toString() {
    return pileToString(foundationPiles, PileCategory.FOUNDATION) +
            System.lineSeparator() +
            pileToString(openPiles, PileCategory.OPEN) +
            System.lineSeparator() +
            pileToString(cascadePiles, PileCategory.CASCADE);
  }

  public void makeMove(PileCategory source, int pileNumber, int cardIndex, PileCategory destination, int destPileNumber) {
    source = Utils.requireNonNull(source);

    destination = Utils.requireNonNull(destination);

    List<Card> sourcePile = getPiles(source, pileNumber);
    Card cardFromSource = getCardFromPile(cardIndex, sourcePile);

    List<Card> destinationPile = getPiles(destination, destPileNumber);
    boolean canPutCardInPile = destination.canPutCardInPile(cardFromSource, destinationPile);

    if (canPutCardInPile) {
      sourcePile.remove(cardIndex);
      destinationPile.add(cardFromSource);
    } else {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  public boolean hasGameCompleted() {
    long emptyCascadingPilesCount = cascadePiles.stream()
            .filter(List::isEmpty)
            .count();

    long emptyOpenPilesCount = openPiles.stream()
            .filter(List::isEmpty)
            .count();

    long fullFoundationPilesCount = foundationPiles.stream()
            .filter(pile -> pile.size() == NUMBER_OF_CARDS_INDIVIDUAL_SUIT)
            .count();

    return emptyCascadingPilesCount == cascadePiles.size()
            && emptyOpenPilesCount == openPiles.size()
            && fullFoundationPilesCount == FOUNDATION_PILE_COUNT;
  }

  public void startGame(List<Card> deck, boolean shuffle) {
    deck = requireValidDeck(deck);
    clearPiles();
    distributeDeck(deck, shuffle);
  }

  private void distributeDeck(List<Card> deck, boolean shuffle) {
    List<Card> deckCopy = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(deckCopy);
    }

    int i = 0, j = 0;
    while (i < deckCopy.size()) {
      cascadePiles.get(j).add(deckCopy.get(i));
      j = (j + 1) % cascadePiles.size();
      i++;
    }
  }

  private void clearPiles() {
    this.foundationPiles.forEach(List::clear);
    this.openPiles.forEach(List::clear);
    this.cascadePiles.forEach(List::clear);
  }

  private List<Card> requireValidDeck(List<Card> deck) {
    deck = Utils.requireNonNull(deck);

    if (deck.isEmpty() || deck.size() != TOTAL_NUMBER_OF_CARDS_IN_DECK) {
      throw new IllegalArgumentException("Invalid input");
    }

    long nullCardCount = deck.stream().filter(Objects::isNull).count();
    if (nullCardCount == 1) {
      throw new IllegalArgumentException("Invalid input");
    }

    Set<Card> cards = new HashSet<>(deck);
    if (cards.size() != TOTAL_NUMBER_OF_CARDS_IN_DECK) {
      throw new IllegalArgumentException("Invalid input");
    }

    return deck;
  }

  private List<Card> getPiles(PileCategory pileType, int index) {
    List<List<Card>> listOfCards = null;
    switch (pileType) {
      case FOUNDATION:
        listOfCards = this.foundationPiles;
        break;
      case OPEN:
        listOfCards = this.openPiles;
        break;
      case CASCADE:
        listOfCards = this.cascadePiles;
        break;
    }

    try {
      return listOfCards.get(index);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  private Card getCardFromPile(int cardIndex, List<Card> pile) {
    try {
      if (pile.size() - 1 != cardIndex) {
        throw new IllegalArgumentException("Invalid input");
      }
      return pile.get(cardIndex);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }
}
