import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, validDeck);
        Assert.assertEquals(convertPilesIntoString(getListOfEmptyLists(4), getListOfEmptyLists(openPiles), expectedCascadingPiles), freeCellOperations.getGameState());
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

        Assert.assertFalse(model.isGameOver());
        Assert.assertEquals("", model.getGameState());

        try {
          model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
        } catch (IllegalStateException e) {
          Assert.assertEquals("cannot move before starting game", e.getMessage());
        }

        Assert.assertEquals("", model.getGameState());

        try {
          model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
        } catch (IllegalStateException e) {
          Assert.assertEquals("cannot move before starting game", e.getMessage());
        }

        Assert.assertEquals("", model.getGameState());
        try {
          model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
        } catch (IllegalStateException e) {
          Assert.assertEquals("cannot move before starting game", e.getMessage());
        }

        Assert.assertEquals("", model.getGameState());

        Assert.assertFalse(model.isGameOver());
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


        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

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

  @Test // todo review
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

  //todo check game state string where move is tested
  private static class GameState {

    private final List<List<Card>> foundationPile;
    private final List<List<Card>> openPile;
    private final List<List<Card>> cascadePile;

    private GameState(List<List<Card>> foundationPile,
                      List<List<Card>> openPile,
                      List<List<Card>> cascadePile) {
      this.foundationPile = foundationPile;
      this.openPile = openPile;
      this.cascadePile = cascadePile;
    }

    public List<List<Card>> getFoundationPile() {
      return foundationPile;
    }

    public List<List<Card>> getOpenPile() {
      return openPile;
    }

    public List<List<Card>> getCascadePile() {
      return cascadePile;
    }
  }

  @Test
  public void getGameStateReturnsEmptyStringBeforeStarting() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        Assert.assertEquals("", model.getGameState());
      }
    }
  }

  @Test
  public void sequenceOfMethodInvocationForBuilderDoesNotMatter() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> modelWithCascadeFirst = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deck = modelWithCascadeFirst.getDeck();
        modelWithCascadeFirst.startGame(deck, false);

        List<List<Card>> expectedCascadingPile1 = getCardsInCascadingPiles(cascadingPiles, deck);
        String expectedGameState1 = convertPilesIntoString(getListOfEmptyLists(4), getListOfEmptyLists(openPiles), expectedCascadingPile1);
        Assert.assertEquals(expectedGameState1, modelWithCascadeFirst.getGameState());

        FreecellOperations<Card> modelWithCascadeAfter = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        modelWithCascadeAfter.startGame(deck, false);

        List<List<Card>> expectedCascadingPile2 = getCardsInCascadingPiles(cascadingPiles, deck);
        String expectedGameState2 = convertPilesIntoString(getListOfEmptyLists(4), getListOfEmptyLists(openPiles), expectedCascadingPile2);
        Assert.assertEquals(expectedGameState2, modelWithCascadeAfter.getGameState());

        Assert.assertEquals(expectedGameState1, expectedGameState2);
      }
    }
  }

  @Test
  public void moveCardFromFoundationToFoundationWorks() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

        model.startGame(deck, false);
        //moving last ace to foundation pile
        int lastPileOfAce = ((52 % cascadingPiles) - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? 52 / cascadingPiles : (52 / cascadingPiles) - 1;

        List<List<Card>> expectedFoundationPiles = getListOfEmptyLists(4);

        List<List<Card>> expectedCascadePiles = getCardsInCascadingPiles(cascadingPiles, deck);
        Card lastAce = expectedCascadePiles.get(lastPileOfAce).remove(lastCardIndexOfAce);

        expectedFoundationPiles.get(0).add(0, lastAce);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.FOUNDATION, 0);
        Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        Card cardFromFoundationPile = expectedFoundationPiles.get(0).remove(0);
        expectedFoundationPiles.get(1).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 0, 0, PileType.FOUNDATION, 1);
        Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        cardFromFoundationPile = expectedFoundationPiles.get(1).remove(0);
        expectedFoundationPiles.get(2).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 1, 0, PileType.FOUNDATION, 2);
        Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        cardFromFoundationPile = expectedFoundationPiles.get(2).remove(0);
        expectedFoundationPiles.get(3).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 2, 0, PileType.FOUNDATION, 3);
        Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        cardFromFoundationPile = expectedFoundationPiles.get(3).remove(0);
        expectedFoundationPiles.get(0).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 3, 0, PileType.FOUNDATION, 0);
        Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        Assert.assertFalse(model.isGameOver());
      }
    }
  }

  @Test
  public void moveCardFromCascadeToCascadeWorks() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = getValidDeck();
        Card card1 = new Card(Suit.HEARTS, CardValue.ACE);
        Card card2 = new Card(Suit.SPADES, CardValue.TWO);
        deck = deck.stream()
                .filter(card -> !(card.equals(card1) || card.equals(card2)))
                .collect(Collectors.toList());

        deck.add(card2);
        deck.add(card1);

        model.startGame(deck, false);

        int sourceCascadingPileIndex = ((52 % cascadingPiles) - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? 52 / cascadingPiles : (52 / cascadingPiles) - 1;
        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, deck);

        int destinationCascadingPileIndex = (sourceCascadingPileIndex--) % cascadingPiles;

        Card cardFromSourceCascadingPile = expectedCascadingPiles.get(sourceCascadingPileIndex).get(lastCardIndexOfAce);
        expectedCascadingPiles.get(destinationCascadingPileIndex).add(cardFromSourceCascadingPile);
        model.move(PileType.CASCADE, sourceCascadingPileIndex, lastCardIndexOfAce, PileType.CASCADE, destinationCascadingPileIndex);

        Assert.assertEquals(convertPilesIntoString(getListOfEmptyLists(4), getListOfEmptyLists(openPiles), expectedCascadingPiles), model.getGameState());
        Assert.assertFalse(model.isGameOver());
      }
    }
  }

  @Test
  public void moveCardFromOpenToOpenWorks() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        for (boolean shuffle : Arrays.asList(true, false)) {
          List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

          model.startGame(deck, shuffle);

          //moving last ace to open pile
          int lastPileOfAce = ((52 % cascadingPiles) - 1) % cascadingPiles;
          int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? 52 / cascadingPiles : (52 / cascadingPiles) - 1;

          List<List<Card>> expectedOpenPiles = getListOfEmptyLists(openPiles);

          List<List<Card>> expectedCascadePiles = getCardsInCascadingPiles(cascadingPiles, deck);
          Card lastAce = expectedCascadePiles.get(lastPileOfAce).remove(lastCardIndexOfAce);

          expectedOpenPiles.get(0).add(0, lastAce);
          model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.OPEN, 0);

          Assert.assertEquals(convertPilesIntoString(getListOfEmptyLists(4),
                  expectedOpenPiles,
                  expectedCascadePiles),
                  model.getGameState());

          Assert.assertFalse(model.isGameOver());

          for (int sourceOpenPile = 0; sourceOpenPile < openPiles; sourceOpenPile++) {
            int destinationOpenPileIndex = (sourceOpenPile + 1) % openPiles;

            Card cardFromOpenPile = expectedOpenPiles.get(sourceOpenPile).remove(0);
            expectedOpenPiles.get(destinationOpenPileIndex).add(cardFromOpenPile);

            model.move(PileType.OPEN, sourceOpenPile, 0, PileType.OPEN,
                    destinationOpenPileIndex);

            Assert.assertEquals(convertPilesIntoString(getListOfEmptyLists(4),
                    expectedOpenPiles,
                    expectedCascadePiles), model.getGameState());

            Assert.assertFalse(model.isGameOver());

          }
        }
      }
    }
  }

  @Test
  public void moveCardAmongstFoundationAndOpen() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, Integer.MAX_VALUE)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, Integer.MAX_VALUE)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

        model.startGame(deck, false);

        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, deck);
        List<List<Card>> expectedOpenPiles = getListOfEmptyLists(openPiles);

        List<List<Card>> expectedFoundationPiles = getListOfEmptyLists(4);

        //moving last ace to open pile
        int lastPileOfAce = ((52 % cascadingPiles) - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? 52 / cascadingPiles : (52 / cascadingPiles) - 1;

        Card lastAce = expectedCascadingPiles.get(lastPileOfAce).remove(lastCardIndexOfAce);
        expectedFoundationPiles.get(0).add(lastAce);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.FOUNDATION, 0);
        Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles), model.getGameState());
        Assert.assertFalse(model.isGameOver());

        for (int foundationPilePosition = 0; foundationPilePosition < 4; foundationPilePosition++) {
          for (int openPilePosition = 0; openPilePosition < openPiles; openPilePosition++) {
            Card lastCardFromFoundationPile = expectedFoundationPiles.get(foundationPilePosition).remove(0);
            expectedOpenPiles.get(openPilePosition).add(lastCardFromFoundationPile);
            model.move(PileType.FOUNDATION, foundationPilePosition, 0, PileType.OPEN, openPilePosition);
            Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles), model.getGameState());
            Assert.assertFalse(model.isGameOver());

            Card lastCardFromOpenPile = expectedOpenPiles.get(openPilePosition).remove(0);
            expectedFoundationPiles.get(foundationPilePosition).add(lastCardFromOpenPile);
            model.move(PileType.OPEN, openPilePosition, 0, PileType.FOUNDATION, foundationPilePosition);
            Assert.assertEquals(convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles), model.getGameState());
            Assert.assertFalse(model.isGameOver());
          }
        }
      }
    }
  }

  private List<Card> getReverseSortedDeckWithAcesInTheEnd(FreecellOperations<Card> model) {
    List<Card> deck = model.getDeck();
    //sorting the deck so that all Aces shifts to the end of the deck
    deck.sort((o1, o2) -> o2.getCardValue().getPriority() - o1.getCardValue().getPriority());
    return deck;
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

  private List<List<Card>> getCardsInCascadingPiles(int cascadePileCount, List<Card> validDeck) {
    List<List<Card>> expectedCascadingPiles = new ArrayList<>(cascadePileCount);
    for (int i = 0; i < cascadePileCount; i++) {
      List<Card> cardsInCascadePile = new LinkedList<>();
      for (int j = 0; j < validDeck.size(); j += cascadePileCount) {
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

  private String convertPilesIntoString(List<List<Card>> foundationPiles,
                                        List<List<Card>> openPiles,
                                        List<List<Card>> cascadePiles) {
    StringBuilder builder = new StringBuilder();
    List<String> foundationLists = foundationPiles.stream()
            .map(listOfCards -> listOfCards.stream().map(Card::toString)
                    .collect(Collectors.joining(","))
            ).collect(Collectors.toList());
    addPileStringsToBuilder(builder, foundationLists, 'F');

    List<String> openLists = openPiles.stream()
            .map(listOfCards -> listOfCards.stream().map(Card::toString)
                    .collect(Collectors.joining(","))
            ).collect(Collectors.toList());
    addPileStringsToBuilder(builder, openLists, 'O');

    List<String> cascadeLists = cascadePiles.stream()
            .map(listOfCards -> listOfCards.stream().map(Card::toString)
                    .collect(Collectors.joining(","))
            ).collect(Collectors.toList());
    addPileStringsToBuilder(builder, cascadeLists, 'C');
    return builder.toString().trim();
  }

  private void addPileStringsToBuilder(StringBuilder builder, List<String> strings, char symbol) {
    for (int i = 0; i < strings.size(); i++) {
      builder.append(symbol);
      builder.append(i + 1);
      builder.append(":");
      builder.append(strings.get(i));
      builder.append(System.lineSeparator());
    }
  }

  @Test
  public void simulateEntireGame() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = new ArrayList<>(52);
    List<CardValue> cardValues = Arrays.stream(CardValue.values())
            .sorted(Comparator.comparingInt(CardValue::getPriority).reversed())
            .collect(Collectors.toList());

    for (CardValue cardValue : cardValues) {
      deck.add(new Card(Suit.SPADES, cardValue));
      deck.add(new Card(Suit.DIAMONDS, cardValue));
      deck.add(new Card(Suit.CLUBS, cardValue));
      deck.add(new Card(Suit.HEARTS, cardValue));
    }

    Assert.assertFalse(model.isGameOver());

    List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePileCount, deck);

    model.startGame(deck, false);
    Assert.assertFalse(model.isGameOver());

    List<List<Card>> expectedOpenPiles = getListOfEmptyLists(4);

    List<List<Card>> expectedFoundationPiles = getListOfEmptyLists(4);


    for (int cardIndex = 12; cardIndex >= 0; cardIndex--) {
      for (int cascadePileSouceIndex = 0, openPileDestinationIndex = 0; cascadePileSouceIndex < cascadePileCount; cascadePileSouceIndex++, openPileDestinationIndex++) {

        Card cardFromCascadedPile = expectedFoundationPiles.get(cascadePileSouceIndex).remove(cardIndex);
        expectedOpenPiles.get(openPileDestinationIndex).add(cardFromCascadedPile);

        model.move(PileType.CASCADE, cascadePileSouceIndex, cardIndex, PileType.OPEN, openPileDestinationIndex);
        Assert.assertFalse(model.isGameOver());

        String expectedGameState = convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles);
        Assert.assertEquals(expectedGameState, model.getGameState());
      }

      for (int cascadePileIndex = 0, openPileSourceIndex = 0; cascadePileIndex < cascadePileCount; cascadePileIndex++, openPileSourceIndex++) {

        Card cardFromOpenPile = expectedOpenPiles.get(openPileSourceIndex).get(0);
        int cascadePileDestinationIndex = (cascadePileIndex + 1) % cascadePileCount;
        expectedCascadingPiles.get(cascadePileDestinationIndex).add(cardFromOpenPile);

        model.move(PileType.OPEN, openPileSourceIndex, 0, PileType.CASCADE, cascadePileDestinationIndex);
        Assert.assertFalse(model.isGameOver());

        String expectedGameState = convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles);
        Assert.assertEquals(expectedGameState, model.getGameState());
      }

      for (int cascadePileSourceIndex = 0, foundationPileIndex = 0; cascadePileSourceIndex < cascadePileCount; cascadePileSourceIndex++, foundationPileIndex++) {
        Card cardFromCascadePile = expectedCascadingPiles.get(cascadePileSourceIndex).remove(cardIndex);

        int foundationPileDestinationIndex = (foundationPileIndex + 1) % 4;
        expectedFoundationPiles.get(foundationPileDestinationIndex).add(cardFromCascadePile);

        model.move(PileType.CASCADE, cascadePileSourceIndex, cardIndex, PileType.FOUNDATION, foundationPileDestinationIndex);

        if (cardIndex == 0 && cascadePileSourceIndex == 3) {
          Assert.assertTrue(model.isGameOver());
        } else {
          Assert.assertFalse(model.isGameOver());
        }

        String expectedGameState = convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles);
        Assert.assertEquals(expectedGameState, model.getGameState());
      }
    }

    Assert.assertTrue(model.isGameOver());

    long countOfFoundationPilesWith13Cards = expectedFoundationPiles.stream()
            .filter(cards -> cards.size() == 13)
            .count();
    Assert.assertEquals(4, countOfFoundationPilesWith13Cards);

    long countOfEmptyCascadingPiles = expectedCascadingPiles.stream()
            .filter(List::isEmpty)
            .count();
    Assert.assertEquals(cascadePileCount, countOfEmptyCascadingPiles);

    long countOfEmptyOpenPiles = expectedOpenPiles.stream()
            .filter(List::isEmpty)
            .count();
    Assert.assertEquals(openPileCount, countOfEmptyOpenPiles);

    String expectedGameState = convertPilesIntoString(expectedFoundationPiles, expectedOpenPiles, expectedCascadingPiles);
    Assert.assertEquals(expectedGameState, model.getGameState());

    try {
      model.move(PileType.FOUNDATION, 0, 12, PileType.CASCADE, 0);
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid move", e.getMessage());
    }

    try {
      model.move(PileType.FOUNDATION, 0, 12, PileType.OPEN, 0);
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid move", e.getMessage());
    }

    try {
      model.move(PileType.FOUNDATION, 0, 12, PileType.FOUNDATION, 1);
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid move", e.getMessage());
    }
  }

  private List<List<Card>> getListOfEmptyLists(int listSize) {
    List<List<Card>> expectedOpenPiles = new ArrayList<>(listSize);
    for (int i = 0; i < listSize; i++) {
      expectedOpenPiles.add(new LinkedList<>());
    }
    return expectedOpenPiles;
  }
}
