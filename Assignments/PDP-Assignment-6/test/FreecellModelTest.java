import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.Suit;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;
import util.Utils;

/**
 * Created by gajjar.s, on 1:18 PM, 10/28/18
 */
public class FreecellModelTest {
  private final Random randomGenerator = new Random();

  @Test
  public void deckIsNotInvalid() {
    // this tests that size is 52, there are no duplicates and there are only valid cards
    Set<Card> expectedUnorderedDeck = new HashSet<>(getValidDeck());
    Assert.assertEquals(52, expectedUnorderedDeck.size());

    // cascade piles
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        //test invoking multiple calls to getDeck returns a valid deck
        Assert.assertEquals(expectedUnorderedDeck, new HashSet<>(model.getDeck()));
        Assert.assertEquals(expectedUnorderedDeck, new HashSet<>(model.getDeck()));
      }
    }
  }

  @Test
  public void constructingGameWithInvalidPiles() {
    for (int openPileCount : Arrays.asList(-1, 0)) {
      for (int cascadingPileCount : Arrays.asList(-1, 0, 1, 2, 3)) {
        try {
          FreecellModel.getBuilder()
                  .cascades(cascadingPileCount)
                  .opens(openPileCount)
                  .build();
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWithNullOrEmptyDeck() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();
        try {
          model.startGame(null, true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Input was null", e.getMessage());
        }

        try {
          model.startGame(null, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Input was null", e.getMessage());
        }

        try {
          model.startGame(Collections.emptyList(), true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Input was null", e.getMessage());
        }

        try {
          model.startGame(Collections.emptyList(), false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Input was null", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWith51Cards() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deckWith51Cards = getValidDeck().subList(0, 52);
        try {
          model.startGame(deckWith51Cards, true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }

        try {
          model.startGame(deckWith51Cards, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWith53Cards() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deckWith53Cards = getValidDeck();
        deckWith53Cards.add(new Card(
                Suit.values()[randomGenerator.nextInt(4)],
                CardValue.values()[randomGenerator.nextInt(13)]
        ));

        try {
          model.startGame(deckWith53Cards, true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }

        try {
          model.startGame(deckWith53Cards, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWithCorrectSizeButNullCardInDeck() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deckWith52Cards = getValidDeck().subList(0, 52);
        deckWith52Cards.add(null);

        try {
          model.startGame(deckWith52Cards, true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }

        try {
          model.startGame(deckWith52Cards, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWithDuplicates() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deckWithDuplicates = getValidDeck().subList(0, 52);
        deckWithDuplicates.add(deckWithDuplicates.get(randomGenerator.nextInt(51)));

        try {
          model.startGame(deckWithDuplicates, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }

        try {
          model.startGame(deckWithDuplicates, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid deck of cards", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameWithShuffleFalse() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {

        FreecellOperations<Card> freeCellOperations = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> validDeck = getValidDeck();

        freeCellOperations.startGame(validDeck, false);
        String gameState = freeCellOperations.getGameState();
        String[] stateLines = gameState.split(System.lineSeparator());

        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, validDeck);

        for (int i = (4 + openPiles), j = 0; i < stateLines.length; i++, j++) {
          String expectedCascadingPileString = expectedCascadingPiles.get(j).stream()
                  .map(Card::toString)
                  .collect(Collectors.joining(","));
          Assert.assertEquals(String.format(
                  "C%d:%s", j + 1, expectedCascadingPileString), stateLines[i]);
        }
      }
    }
  }

  @Test
  public void startGameWithShuffleTrue() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {

        FreecellOperations<Card> freeCellOperations = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> validDeck = getValidDeck();

        freeCellOperations.startGame(validDeck, true);
        String gameState = freeCellOperations.getGameState();
        String[] stateLines = gameState.split(System.lineSeparator());

        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, validDeck);

        Set<Card> actualCardsInCascadingPiles = new HashSet<>(52);
        for (int i = (4 + openPiles), j = 0; i < stateLines.length; i++, j++) {
          String actualCascadingPileString = stateLines[i].split(":")[1];
          Set<Card> actualCardsInCascadingPile = Arrays.stream(actualCascadingPileString.split(","))
                  .map(Card::parse)
                  .collect(Collectors.toSet());

          actualCardsInCascadingPiles.addAll(actualCardsInCascadingPile);
          List<Card> expectedCardsInCascadingPile = expectedCascadingPiles.get(j);

          Assert.assertEquals(expectedCardsInCascadingPile.size(), actualCardsInCascadingPile.size());
        }

        Assert.assertTrue(actualCardsInCascadingPiles.containsAll(validDeck));
      }
    }
  }

  @Test
  public void moveWithInvalidArguments() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deck = model.getDeck();
        for (boolean shuffle : Arrays.asList(true, false)) {
          model.startGame(deck, shuffle);

          try {
            model.move(null, 1, 1, PileType.CASCADE, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, 1, 1, null, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid input", e.getMessage());
          }

          try {
            model.move(null, 1, 1, null, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, -1, 1, PileType.FOUNDATION, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, 1, -1, PileType.FOUNDATION, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, 1, 1, PileType.FOUNDATION, -1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid input", e.getMessage());
          }
        }
      }
    }
  }

  @Test
  public void moveBeforeStartingGame() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        try {
          model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
        } catch (IllegalStateException e) {
          Assert.assertEquals("cannot move before starting game", e.getMessage());
        }
      }
    }
  }

  @Test
  public void moveToSamePositionAsSourcePosition() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = model.getDeck();
        //sorting the deck so that all Aces shifts to the end of the deck
        deck.sort((o1, o2) -> o2.getCardValue().getPriority() - o1.getCardValue().getPriority());

        model.startGame(deck, false);

        //moving last ace to foundation pile
        int lastPileOfAce = ((52 % cascadingPiles) - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? 52 / cascadingPiles : (52 / cascadingPiles) - 1;
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.FOUNDATION, 0);

        //moving a card to open pile
        lastCardIndexOfAce--;
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.OPEN, 0);

        //moving cards to same position as source
        lastCardIndexOfAce--;
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.CASCADE, lastPileOfAce);
        model.move(PileType.FOUNDATION, 0, 0, PileType.FOUNDATION, 0);
        model.move(PileType.OPEN, 0, 0, PileType.OPEN, 0);
      }
    }
  }

  @Test
  public void moveToInvalidPositionOrPile() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deck = model.getDeck();
        for (boolean shuffle : Arrays.asList(true, false)) {
          model.startGame(deck, shuffle);

          //testing invalid source pileNumber
          try {
            model.move(PileType.CASCADE, cascadingPiles, 0, PileType.FOUNDATION, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }

          try {
            model.move(PileType.FOUNDATION, 4, 0, PileType.OPEN, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }

          try {
            model.move(PileType.OPEN, openPiles, 0, PileType.CASCADE, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }


          //testing invalid source card Index
          List<Card> expectedCardsInFirstCascadingPile = getCardsInCascadingPiles(cascadingPiles, deck).get(0);
          try {
            model.move(PileType.CASCADE, 0, expectedCardsInFirstCascadingPile.size(), PileType.FOUNDATION, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }

          try {
            model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }

          try {
            model.move(PileType.OPEN, 0, 0, PileType.CASCADE, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }


          //testing invalid destination pileNumber
          try {
            model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 4);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }

          try {
            model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, openPiles);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }

          try {
            model.move(PileType.OPEN, 0, 0, PileType.CASCADE, cascadingPiles);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("invalid move", e.getMessage());
          }
        }
      }
    }
  }

  @Test
  public void isGameOverFalseForNonEmptyCascadePiles() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();
        for (boolean toShuffle : Arrays.asList(true, false)) {
          model.startGame(model.getDeck(), toShuffle);
          Assert.assertFalse(model.isGameOver());

          List<List<Card>> cardsInCascadingPiles = getCardsInCascadingPiles(cascadingPiles,
                  getValidDeck());

          // move from each cascade pile to an open pile and check isGameOver
          for (int i = 0; i < cascadingPiles; i++) {
            int openPileNumber = randomGenerator.nextInt(openPiles);
            model.move(PileType.CASCADE,
                    i,
                    cardsInCascadingPiles.get(i).size() - 1
                    , PileType.OPEN
                    , openPileNumber);
            Assert.assertFalse(model.isGameOver());
            model.move(PileType.OPEN,
                    openPileNumber,
                    0
                    , PileType.CASCADE
                    , i);
            Assert.assertFalse(model.isGameOver());
          }

          // check all valid moves within cascade piles and ensure that they do not affect
          // isGameOver

          for (int i = 0; i < cascadingPiles; i++) {
            for (int j = 0; j < cascadingPiles; j++) {
              if (i == j) {
                continue;
              }
              if (isValidCascadePileMove(cardsInCascadingPiles, i, j)) {
                model.move(PileType.CASCADE,
                        i,
                        cardsInCascadingPiles.get(i).size() - 1
                        , PileType.CASCADE
                        , j);

                Assert.assertFalse(model.isGameOver());

                model.move(PileType.CASCADE,
                        j,
                        cardsInCascadingPiles.get(j).size() - 1
                        , PileType.CASCADE
                        , i);

                Assert.assertFalse(model.isGameOver());
              }
            }
          }
        }
      }
    }
  }

  @Test
  public void getGameState() {
  }

  @Test
  public void getBuilder() {
  }

  private List<Card> getValidDeck() {
    List<Card> deck = new ArrayList<>(52);
    for (Suit suit : Suit.values()) {
      for (CardValue cardValue : CardValue.values()) {
        deck.add(new Card(suit, cardValue));
      }
    }
    return deck;
  }

  private List<List<Card>> getCardsInCascadingPiles(int cascadePile, List<Card> validDeck) {
    List<List<Card>> expectedCascadingPiles = new ArrayList<>(cascadePile);
    for (int i = 0; i < cascadePile; i++) {
      List<Card> cardsInCascadePile = new LinkedList<>();
      for (int j = 0; j < validDeck.size(); j += cascadePile) {
        cardsInCascadePile.add(validDeck.get(j));
      }
      expectedCascadingPiles.add(i, cardsInCascadePile);
    }
    return expectedCascadingPiles;
  }

  private boolean isValidCascadePileMove(List<List<Card>> cardsInCascadingPiles,
                                         int fromCascadePile,
                                         int toCascadePile) {
    Card lastInSource = Utils.getLast(cardsInCascadingPiles.get(fromCascadePile));
    Card lastInDestination = Utils.getLast(cardsInCascadingPiles.get(toCascadePile));

    return lastInDestination.getSuit().getColor() != lastInSource.getSuit().getColor()
            && lastInDestination.getCardValue().compareTo(lastInSource.getCardValue()) == 1;
  }
}
