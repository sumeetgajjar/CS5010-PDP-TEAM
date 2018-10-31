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
import freecell.bean.PileCategory;
import freecell.bean.Suit;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;
import util.Utils;

/**
 * Represents tests that are run on the <code>FreeCellmodel</code> that implements
 * <code>FreeCellOperations</code>.
 */
public class FreecellModelTest {

  private final Random randomGenerator = new Random();

  @Test
  public void deckIsNotInvalid() {
    // this tests that size is 52, there are no duplicates and there are only valid cards
    Set<Card> expectedUnorderedDeck = new HashSet<>(getValidDeck());
    Assert.assertEquals(52, expectedUnorderedDeck.size());

    // cascade piles
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
          Assert.assertEquals("Invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWithNullOrEmptyDeck() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();
        try {
          model.startGame(null, true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(null, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(Collections.emptyList(), true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(Collections.emptyList(), false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWith51Cards() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deckWith51Cards = getValidDeck().subList(0, 51);
        try {
          model.startGame(deckWith51Cards, true);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(deckWith51Cards, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWith53Cards() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(deckWith53Cards, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWithCorrectSizeButNullCardInDeck() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(deckWith52Cards, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameFailsWithDuplicates() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.startGame(deckWithDuplicates, false);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }
      }
    }
  }

  @Test
  public void startGameWithShuffleDoesNotChangeGivenDeck() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {

        FreecellOperations<Card> freeCellOperations = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> validDeck = getValidDeck();
        List<Card> validDeckCopy = new ArrayList<>(validDeck);

        freeCellOperations.startGame(validDeck, true);
        Assert.assertEquals(validDeckCopy, validDeck);
      }
    }
  }

  @Test
  public void startGameWithShuffleTrueShufflesTheDeck() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {

        FreecellOperations<Card> freeCellOperations = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> deck = freeCellOperations.getDeck();
        freeCellOperations.startGame(deck, true);

        List<Card> shuffledDeck = new ArrayList<>();
        String gameState = freeCellOperations.getGameState();
        String[] stateLines = gameState.split(System.lineSeparator());
        for (int i = (4 + openPiles), j = 0; i < stateLines.length; i++, j++) {
          String[] split = stateLines[i].split(":");
          if (split.length == 2) {
            String actualCascadingPileString = split[1];
            List<Card> actualCardsInCascadingPile =
                    Arrays.stream(actualCascadingPileString.split(","))
                            .map(Card::parse)
                            .collect(Collectors.toList());

            shuffledDeck.addAll(actualCardsInCascadingPile);
          }
        }

        Assert.assertNotEquals(deck, shuffledDeck);
      }
    }
  }

  @Test
  public void startGameWithShuffleTrue() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {

        FreecellOperations<Card> freeCellOperations = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> validDeck = getValidDeck();

        freeCellOperations.startGame(validDeck, true);
        String gameState = freeCellOperations.getGameState();
        String[] stateLines = gameState.split(System.lineSeparator());

        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles,
                validDeck);

        Set<Card> actualCardsInCascadingPiles = new HashSet<>(52);
        for (int i = (4 + openPiles), j = 0; i < stateLines.length; i++, j++) {
          String[] split = stateLines[i].split(":");
          if (split.length == 2) {
            String actualCascadingPileString = split[1];
            Set<Card> actualCardsInCascadingPile = Arrays.stream(actualCascadingPileString.split(
                    ","))
                    .map(Card::parse)
                    .collect(Collectors.toSet());

            actualCardsInCascadingPiles.addAll(actualCardsInCascadingPile);
            List<Card> expectedCardsInCascadingPile = expectedCascadingPiles.get(j);

            Assert.assertEquals(expectedCardsInCascadingPile.size(),
                    actualCardsInCascadingPile.size());
          }
        }

        Assert.assertTrue(actualCardsInCascadingPiles.containsAll(validDeck));
      }
    }
  }

  @Test
  public void moveWithInvalidArguments() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, 1, 1, null, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(null, 1, 1, null, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, -1, 1, PileType.FOUNDATION, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, 1, -1, PileType.FOUNDATION, 1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.CASCADE, 1, 1, PileType.FOUNDATION, -1);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
        }
      }
    }
  }

  @Test
  public void moveBeforeStartingGame() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
  public void moveToInvalidPositionOrPile() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.FOUNDATION, 4, 0, PileType.OPEN, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.OPEN, openPiles, 0, PileType.CASCADE, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }


          //testing invalid source card Index
          List<Card> expectedCardsInFirstCascadingPile = getCardsInCascadingPiles(cascadingPiles,
                  deck).get(0);
          try {
            model.move(PileType.CASCADE, 0, expectedCardsInFirstCascadingPile.size(),
                    PileType.FOUNDATION, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.OPEN, 0, 0, PileType.CASCADE, 0);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }


          //testing invalid destination pileNumber
          try {
            model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 4);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, openPiles);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }

          try {
            model.move(PileType.OPEN, 0, 0, PileType.CASCADE, cascadingPiles);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
        }
      }
    }
  }

  @Test
  public void getGameStateReturnsEmptyStringBeforeStarting() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        Assert.assertEquals("", model.getGameState());
      }
    }
  }

  @Test
  public void moveCardFromCascadePileWithWrongCardIndexFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    for (boolean shuffle : Arrays.asList(true, false)) {
      model.startGame(deck, shuffle);

      String gameStateBeforeInvalidMove = model.getGameState();
      try {
        model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

      try {
        model.move(PileType.CASCADE, 0, 13, PileType.OPEN, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

      try {
        model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

      try {
        model.move(PileType.CASCADE, 0, 13, PileType.FOUNDATION, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
    }
  }

  @Test
  public void moveCardFromOpenPileWithWrongCardIndexFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    for (boolean shuffle : Arrays.asList(true, false)) {
      model.startGame(deck, shuffle);
      model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);

      String gameStateBeforeInvalidMove = model.getGameState();
      try {
        model.move(PileType.OPEN, 0, 1, PileType.CASCADE, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

      try {
        model.move(PileType.OPEN, 0, 1, PileType.FOUNDATION, 0);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

    }
  }

  @Test
  public void moveCardFromFoundationPileWithWrongCardIndexFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();


    model.startGame(deck, false);
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);

    String gameStateBeforeInvalidMove = model.getGameState();
    try {
      model.move(PileType.FOUNDATION, 0, 1, PileType.CASCADE, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

    try {
      model.move(PileType.FOUNDATION, 0, 1, PileType.OPEN, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
  }

  @Test
  public void testInitializationOfGame() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {

        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> validDeck = getValidDeck();

        model.startGame(validDeck, false);
        Assert.assertFalse(model.isGameOver());

        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles,
                validDeck);
        Assert.assertEquals(convertPilesToString(Utils.getListOfEmptyLists(4),
                Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
                model.getGameState());

        model.startGame(validDeck, true);
        Assert.assertFalse(model.isGameOver());
      }
    }
  }

  @Test
  public void startGameAfterMove() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {

        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> deck = model.getDeck();
        model.startGame(deck, false);

        List<List<Card>> expectedCascadingPiles1 = getCardsInCascadingPiles(cascadingPiles, deck);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPiles);
        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

        int lastPile = ((52 % cascadingPiles) + cascadingPiles - 1) % cascadingPiles;
        int lastCardIndex = 52 % cascadingPiles == 0 ? (52 / cascadingPiles) - 1 :
                (52 / cascadingPiles);

        Card lastCardFromLastPile = expectedCascadingPiles1.get(lastPile).remove(lastCardIndex);
        expectedOpenPiles.get(0).add(lastCardFromLastPile);
        model.move(PileType.CASCADE, lastPile, lastCardIndex, PileType.OPEN, 0);

        Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
                expectedCascadingPiles1), model.getGameState());
        Assert.assertFalse(model.isGameOver());

        model.startGame(deck, false);
        List<List<Card>> expectedCascadingPiles2 = getCardsInCascadingPiles(cascadingPiles, deck);
        Assert.assertEquals(convertPilesToString(Utils.getListOfEmptyLists(4),
                Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles2),
                model.getGameState());
        Assert.assertFalse(model.isGameOver());
      }
    }
  }

  @Test
  public void startGameWithShuffleFalse() {

    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {

      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {

        FreecellOperations<Card> freeCellOperations = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        List<Card> validDeck = getValidDeck();

        freeCellOperations.startGame(validDeck, false);
        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles,
                validDeck);
        Assert.assertEquals(convertPilesToString(Utils.getListOfEmptyLists(4),
                Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
                freeCellOperations.getGameState());
      }
    }
  }

  @Test
  public void moveCardFromOpenPileToCascadePileFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();
    model.startGame(deck, false);

    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);

    String gameStateBeforeInvalidMove = model.getGameState();
    try {
      model.move(PileType.OPEN, 0, 0, PileType.CASCADE, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
  }

  @Test
  public void moveCardFromOpenPileToOpenPileFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    for (boolean shuffle : Arrays.asList(false, true)) {
      model.startGame(model.getDeck(), shuffle);

      model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
      model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);

      String gameStateBeforeInvalidMove = model.getGameState();
      try {
        model.move(PileType.OPEN, 0, 0, PileType.OPEN, 1);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
    }
  }

  @Test
  public void moveCardFromOpenPileToFoundationPileFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();

    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    model.startGame(deck, false);
    //moving ace of spades from cascade pile 0 to open pile 0
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    //moving ace of diamonds from cascade pile 1 to open pile 1
    model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    //moving ace of spades to from open pile 0 foundation pile 0
    model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);

    String gameStateBeforeInvalidMove = model.getGameState();
    try {
      //moving ace of diamonds from open pile 1 to foundation pile 0
      model.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

    //moving two of spades from cascade pile 0 to foundation pile 0
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);

    gameStateBeforeInvalidMove = model.getGameState();
    try {
      //moving two of spades from open pile 0 to foundation pile 1
      model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
  }

  @Test
  public void moveCardFromFoundationPileToOpenPileFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    model.startGame(deck, false);
    //moving ace of spades from cascade pile 0 to foundation pile 0
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    //moving ace of diamonds from cascade pile 1 to open pile 0
    model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);

    String gameStateBeforeInvalidMove = model.getGameState();
    try {
      //moving ace of spades from foundation pile 0 to open pile 0
      model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
  }

  @Test
  public void moveCardFromFoundationPileToCascadePileFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    model.startGame(deck, false);
    //moving ace of spades from cascade pile 0 to foundation pile 0
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    //moving ace of diamonds from cascade pile 1 to foundation pile 1
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);

    String gameStateBeforeInvalidMove = model.getGameState();
    try {
      //moving ace of spades from foundation pile 0 to cascade pile 0
      model.move(PileType.FOUNDATION, 0, 0, PileType.CASCADE, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
  }

  @Test
  public void moveCardFromFoundationPileToFoundationPileFails() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    model.startGame(deck, false);
    //moving ace of spades from cascade pile 0 to foundation pile 0
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    //moving ace of diamonds from cascade pile 1 to foundation pile 1
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);

    String gameStateBeforeInvalidMove = model.getGameState();
    try {
      //moving ace of spades from foundation pile 0 to foundation pile 1
      model.move(PileType.FOUNDATION, 0, 0,
              PileType.FOUNDATION, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
    Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
  }

  @Test
  public void moveToSamePositionAsSourcePositionFails() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 51)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 51)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);
        List<List<Card>> expectedCascadingCardPiles =
                getCardsInCascadingPiles(cascadingPiles, deck);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPiles);
        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

        model.startGame(deck, false);

        //moving last ace to foundation pile
        int lastPileOfAce = ((52 % cascadingPiles) + cascadingPiles - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? (52 / cascadingPiles) - 1 :
                (52 / cascadingPiles);

        Card cardFromCascadingPile =
                expectedCascadingCardPiles.get(lastPileOfAce).remove(lastCardIndexOfAce);
        expectedFoundationPiles.get(0).add(cardFromCascadingPile);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce,
                PileType.FOUNDATION, 0);

        //moving a card to open pile
        lastCardIndexOfAce--;
        cardFromCascadingPile =
                expectedCascadingCardPiles.get(lastPileOfAce).remove(lastCardIndexOfAce);
        expectedOpenPiles.get(0).add(cardFromCascadingPile);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce,
                PileType.OPEN, 0);

        Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
                expectedCascadingCardPiles), model.getGameState());

        //moving cards to same position as source
        lastCardIndexOfAce--;

        try {
          model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce, PileType.CASCADE,
                  lastPileOfAce);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.move(PileType.FOUNDATION, 0, 0,
                  PileType.FOUNDATION, 0);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        try {
          model.move(PileType.OPEN, 0, 0, PileType.OPEN, 0);
          Assert.fail("should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }

        Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
                expectedCascadingCardPiles), model.getGameState());
      }
    }
  }

  @Test
  public void sequenceOfMethodInvocationForBuilderDoesNotMatter() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> modelWithCascadeFirst = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deck = modelWithCascadeFirst.getDeck();
        modelWithCascadeFirst.startGame(deck, false);

        List<List<Card>> expectedCascadingPile1 = getCardsInCascadingPiles(cascadingPiles, deck);
        String expectedGameState1 = convertPilesToString(Utils.getListOfEmptyLists(4),
                Utils.getListOfEmptyLists(openPiles), expectedCascadingPile1);
        Assert.assertEquals(expectedGameState1, modelWithCascadeFirst.getGameState());

        FreecellOperations<Card> modelWithCascadeAfter = FreecellModel.getBuilder()
                .opens(openPiles)
                .cascades(cascadingPiles)
                .build();

        modelWithCascadeAfter.startGame(deck, false);

        List<List<Card>> expectedCascadingPile2 = getCardsInCascadingPiles(cascadingPiles, deck);
        String expectedGameState2 = convertPilesToString(Utils.getListOfEmptyLists(4),
                Utils.getListOfEmptyLists(openPiles), expectedCascadingPile2);
        Assert.assertEquals(expectedGameState2, modelWithCascadeAfter.getGameState());

        Assert.assertEquals(expectedGameState1, expectedGameState2);
      }
    }
  }

  @Test
  public void cascadeToCascadeInvalidMoveFails() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deckWithAcesInTheEnd = getReverseSortedDeckWithAcesInTheEnd(model);
        model.startGame(deckWithAcesInTheEnd, false);

        int lastPileOfAce = ((52 % cascadingPiles) + cascadingPiles - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? (52 / cascadingPiles) - 1 :
                (52 / cascadingPiles);

        String gameStateBeforeInvalidMove = model.getGameState();
        try {
          model.move(PileType.CASCADE,
                  lastPileOfAce,
                  lastCardIndexOfAce,
                  PileType.CASCADE,
                  (lastPileOfAce - 1) % cascadingPiles);
          Assert.fail("Should have failed");
        } catch (IllegalArgumentException e) {
          Assert.assertEquals("Invalid input", e.getMessage());
        }
        Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
      }
    }
  }

  @Test
  public void cascadeEmptyPileToAnyFails() {
    for (int cascadingPiles : Arrays.asList(53, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        for (Boolean shuffle : Arrays.asList(true, false)) {
          model.startGame(model.getDeck(), shuffle);
          String gameStateBeforeInvalidMove = model.getGameState();
          try {
            model.move(PileType.CASCADE,
                    cascadingPiles - 1,
                    0,
                    PileType.CASCADE,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.CASCADE,
                    cascadingPiles - 1,
                    0,
                    PileType.CASCADE,
                    cascadingPiles - 2);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.CASCADE,
                    cascadingPiles - 1,
                    0,
                    PileType.OPEN,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.CASCADE,
                    cascadingPiles - 1,
                    0,
                    PileType.FOUNDATION,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
        }
      }
    }
  }

  @Test
  public void openEmptyToAnyFails() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(2, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        for (Boolean shuffle : Arrays.asList(true, false)) {
          model.startGame(model.getDeck(), shuffle);
          String gameStateBeforeInvalidMove = model.getGameState();
          try {
            model.move(PileType.OPEN,
                    openPiles - 1,
                    0,
                    PileType.OPEN,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.OPEN,
                    openPiles - 1,
                    0,
                    PileType.CASCADE,
                    cascadingPiles);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.OPEN,
                    openPiles - 1,
                    0,
                    PileType.FOUNDATION,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
        }
      }
    }
  }

  @Test
  public void foundationEmptyToAnyFails() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        for (Boolean shuffle : Arrays.asList(true, false)) {
          model.startGame(model.getDeck(), shuffle);
          String gameStateBeforeInvalidMove = model.getGameState();
          try {
            model.move(PileType.FOUNDATION,
                    0,
                    0,
                    PileType.FOUNDATION,
                    1);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.FOUNDATION,
                    0,
                    0,
                    PileType.CASCADE,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());

          try {
            model.move(PileType.FOUNDATION,
                    0,
                    0,
                    PileType.OPEN,
                    0);
            Assert.fail("Should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
        }
      }
    }
  }

  @Test
  public void cascadeToFullOpenFails() {
    int cascadingPiles = 4;
    int openPiles = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadingPiles)
            .opens(openPiles)
            .build();

    List<Card> deckWithAcesInTheEnd = getReverseSortedDeckWithAcesInTheEnd(model);
    model.startGame(deckWithAcesInTheEnd, false);


    int lastCardIndex = 12;
    for (int currentOpenPile = 0; currentOpenPile < openPiles; currentOpenPile++) {
      model.move(PileType.CASCADE, 0, lastCardIndex--, PileType.OPEN, currentOpenPile);
    }

    String gameStateBeforeInvalidMove = model.getGameState();
    for (int currentOpenPile = 0; currentOpenPile < openPiles; currentOpenPile++) {
      try {
        model.move(PileType.CASCADE, 0, lastCardIndex, PileType.OPEN, currentOpenPile);
        Assert.fail("Should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateBeforeInvalidMove, model.getGameState());
    }
  }

  @Test
  public void cascadeToFoundationInvalidMoveFails() {
    for (int cascadingPiles : Collections.singletonList(4)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deck = model.getDeck().stream()
                .filter(c -> !c.getCardValue().equals(CardValue.TWO))
                .collect(Collectors.toList());

        Arrays.stream(Suit.values()).forEach(suit -> deck.add(new Card(suit, CardValue.TWO)));

        model.startGame(deck, false);
        String gameStateWithoutMakingAnyMove = model.getGameState();

        for (int currentCascadePile = 0, currentFoundationPile = 0;
             currentCascadePile < 4;
             currentCascadePile++, currentFoundationPile++) {
          try {
            model.move(PileType.CASCADE, currentCascadePile,
                    12,
                    PileType.FOUNDATION,
                    currentFoundationPile);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateWithoutMakingAnyMove, model.getGameState());
        }
      }
    }
  }

  @Test
  public void unexpectedCardOnFoundationPileFails() {
    int cascadingPiles = 4;
    int openPiles = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadingPiles)
            .opens(openPiles)
            .build();

    model.startGame(getReverseSortedDeckWithAcesInTheEnd(model), false);

    for (int currentCascadePile = 0, currentFoundationPile = 0;
         currentCascadePile < 4;
         currentCascadePile++, currentFoundationPile++) {
      model.move(PileType.CASCADE, currentCascadePile,
              12,
              PileType.FOUNDATION,
              currentFoundationPile);
    }

    for (int currentCascadePile = 0, currentOpenPile = 0;
         currentCascadePile < 4;
         currentCascadePile++, currentOpenPile++) {
      model.move(PileType.CASCADE, currentCascadePile,
              11,
              PileType.OPEN,
              currentOpenPile);
    }

    String gameStateWithoutMakingInvalidMove = model.getGameState();
    for (int i = 0; i < 4; i++) {
      try {
        model.move(PileType.CASCADE, i, 10, PileType.FOUNDATION, i);
        Assert.fail("should have failed");
      } catch (IllegalArgumentException e) {
        Assert.assertEquals("Invalid input", e.getMessage());
      }
      Assert.assertEquals(gameStateWithoutMakingInvalidMove, model.getGameState());
    }
  }

  @Test
  public void foundationToFoundationIllegalMoveFails() {
    for (int cascadingPiles : Collections.singletonList(4)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        model.startGame(getReverseSortedDeckWithAcesInTheEnd(model), false);
        for (int currentCascadePile = 0, currentFoundationPile = 0; currentCascadePile < 4;
             currentCascadePile++, currentFoundationPile++) {
          model.move(PileType.CASCADE, currentCascadePile,
                  12,
                  PileType.FOUNDATION,
                  currentFoundationPile);
        }

        String gameStateWithoutMakingInvalidMove = model.getGameState();
        for (int i = 0; i < 4; i++) {
          try {
            model.move(PileType.FOUNDATION, i, 0,
                    PileType.FOUNDATION, (i + 1) % 4);
            Assert.fail("should have failed");
          } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input", e.getMessage());
          }
          Assert.assertEquals(gameStateWithoutMakingInvalidMove, model.getGameState());
        }
      }
    }
  }

  @Test
  public void moveCardFromFoundationToCascadeWorks() {
    int cascadePileCount = 4;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    model.startGame(deck, false);

    List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePileCount, deck);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

    //moving cards from cascade to foundation
    for (int sourcePileIndex = 0; sourcePileIndex < cascadePileCount; sourcePileIndex++) {
      Card cardFromCascadingPile = expectedCascadingPiles.get(sourcePileIndex).remove(12);
      expectedFoundationPiles.get(sourcePileIndex).add(cardFromCascadingPile);

      model.move(PileType.CASCADE, sourcePileIndex, 12,
              PileType.FOUNDATION, sourcePileIndex);

      Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
              expectedCascadingPiles), model.getGameState());
    }


    //moving cards from foundation to cascade
    for (int sourcePileIndex = 0; sourcePileIndex < cascadePileCount; sourcePileIndex++) {
      int destinationPileIndex = (sourcePileIndex + 1) % 4;

      Card cardFromFoundationPile = expectedFoundationPiles.get(sourcePileIndex).remove(0);
      expectedCascadingPiles.get(destinationPileIndex).add(cardFromFoundationPile);

      model.move(PileType.FOUNDATION, sourcePileIndex, 0,
              PileType.CASCADE, destinationPileIndex);

      Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
              expectedCascadingPiles), model.getGameState());
    }
  }

  @Test
  public void moveCardFromFoundationToFoundationWorks() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

        model.startGame(deck, false);
        //moving last ace to foundation pile
        int lastPileOfAce = ((52 % cascadingPiles) + cascadingPiles - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? (52 / cascadingPiles) - 1 :
                (52 / cascadingPiles);

        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

        List<List<Card>> expectedCascadePiles = getCardsInCascadingPiles(cascadingPiles, deck);
        Card lastAce = expectedCascadePiles.get(lastPileOfAce).remove(lastCardIndexOfAce);

        expectedFoundationPiles.get(0).add(0, lastAce);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce,
                PileType.FOUNDATION, 0);
        Assert.assertEquals(convertPilesToString(expectedFoundationPiles,
                Utils.getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        Card cardFromFoundationPile = expectedFoundationPiles.get(0).remove(0);
        expectedFoundationPiles.get(1).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 0,
                0, PileType.FOUNDATION, 1);
        Assert.assertEquals(convertPilesToString(expectedFoundationPiles,
                Utils.getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        cardFromFoundationPile = expectedFoundationPiles.get(1).remove(0);
        expectedFoundationPiles.get(2).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 1, 0,
                PileType.FOUNDATION, 2);
        Assert.assertEquals(convertPilesToString(expectedFoundationPiles,
                Utils.getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        cardFromFoundationPile = expectedFoundationPiles.get(2).remove(0);
        expectedFoundationPiles.get(3).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 2, 0,
                PileType.FOUNDATION, 3);
        Assert.assertEquals(convertPilesToString(expectedFoundationPiles,
                Utils.getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        cardFromFoundationPile = expectedFoundationPiles.get(3).remove(0);
        expectedFoundationPiles.get(0).add(cardFromFoundationPile);
        model.move(PileType.FOUNDATION, 3, 0,
                PileType.FOUNDATION, 0);
        Assert.assertEquals(convertPilesToString(expectedFoundationPiles,
                Utils.getListOfEmptyLists(openPiles),
                expectedCascadePiles), model.getGameState());

        Assert.assertFalse(model.isGameOver());
      }
    }
  }

  @Test
  public void moveCardFromCascadeToCascadeWorks() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10, 20, 100, 1000)) {
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

        deck.add(card1);
        deck.add(card2);

        model.startGame(deck, false);

        int sourceCascadingPileIndex =
                ((52 % cascadingPiles) + cascadingPiles - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? (52 / cascadingPiles) - 1 :
                (52 / cascadingPiles);
        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, deck);

        int destinationCascadingPileIndex = (sourceCascadingPileIndex--) % cascadingPiles;

        Card cardFromSourceCascadingPile =
                expectedCascadingPiles.get(sourceCascadingPileIndex).remove(lastCardIndexOfAce);
        expectedCascadingPiles.get(destinationCascadingPileIndex).add(cardFromSourceCascadingPile);
        model.move(PileType.CASCADE, sourceCascadingPileIndex, lastCardIndexOfAce,
                PileType.CASCADE, destinationCascadingPileIndex);

        Assert.assertEquals(convertPilesToString(Utils.getListOfEmptyLists(4),
                Utils.getListOfEmptyLists(openPiles), expectedCascadingPiles),
                model.getGameState());
        Assert.assertFalse(model.isGameOver());
      }
    }
  }

  @Test
  public void moveCascadeToEmptyCascadeWorks() {
    int cascadePileCount = 53;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();

    List<Card> deck = model.getDeck();
    List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePileCount, deck);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPileCount);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

    model.startGame(deck, false);

    Card cardFromCascadingPile = expectedCascadingPiles.get(0).remove(0);
    expectedCascadingPiles.get(cascadePileCount - 1).add(cardFromCascadingPile);

    model.move(PileType.CASCADE, 0, 0,
            PileType.CASCADE, cascadePileCount - 1);
    Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), model.getGameState());
    Assert.assertFalse(model.isGameOver());

    model.startGame(deck, true);
    model.move(PileType.CASCADE, 0, 0,
            PileType.CASCADE, cascadePileCount - 1);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void moveCardFromOpenPileToEmptyCascadePileWorks() {
    int cascadePileCount = 53;
    int openPileCount = 4;
    FreecellOperations<Card> model = FreecellModel.getBuilder()
            .cascades(cascadePileCount)
            .opens(openPileCount)
            .build();


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();
    List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePileCount, deck);
    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPileCount);
    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

    model.startGame(deck, false);

    Card cardFromCascadingPile = expectedCascadingPiles.get(0).remove(0);
    expectedOpenPiles.get(0).add(cardFromCascadingPile);
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), model.getGameState());

    Card cardFromOpenPile = expectedOpenPiles.get(0).remove(0);
    expectedCascadingPiles.get(cascadePileCount - 1).add(cardFromOpenPile);
    model.move(PileType.OPEN, 0, 0,
            PileType.CASCADE, cascadePileCount - 1);
    Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles), model.getGameState());


    model.startGame(model.getDeck(), true);

    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0,
            PileType.CASCADE, cascadePileCount - 1);
  }

  @Test
  public void moveCardFromOpenToOpenWorks() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(2, 4, 10, 20, 100, 1000)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();

        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

        model.startGame(deck, false);

        //moving last ace to open pile
        int lastPileOfAce = ((52 % cascadingPiles) + cascadingPiles - 1) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0
                ? (52 / cascadingPiles) - 1
                : (52 / cascadingPiles);

        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPiles);

        List<List<Card>> expectedCascadePiles = getCardsInCascadingPiles(cascadingPiles, deck);
        Card lastAce = expectedCascadePiles.get(lastPileOfAce).remove(lastCardIndexOfAce);

        expectedOpenPiles.get(0).add(0, lastAce);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce,
                PileType.OPEN, 0);

        Assert.assertEquals(convertPilesToString(Utils.getListOfEmptyLists(4),
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

          Assert.assertEquals(convertPilesToString(Utils.getListOfEmptyLists(4),
                  expectedOpenPiles,
                  expectedCascadePiles), model.getGameState());

          Assert.assertFalse(model.isGameOver());
        }
      }
    }
  }

  @Test
  public void moveCardAmongstFoundationAndOpen() {
    for (int cascadingPiles : Arrays.asList(4, 8, 10, 20, 100, 1000)) {
      for (int openPiles : Arrays.asList(1, 4, 10)) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadingPiles)
                .opens(openPiles)
                .build();


        List<Card> deck = getReverseSortedDeckWithAcesInTheEnd(model);

        model.startGame(deck, false);

        List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadingPiles, deck);
        List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(openPiles);

        List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);

        //moving last ace to open pile
        int lastPileOfAce = ((52 % cascadingPiles) - 1 + cascadingPiles) % cascadingPiles;
        int lastCardIndexOfAce = 52 % cascadingPiles == 0 ? (52 / cascadingPiles) - 1 :
                (52 / cascadingPiles);

        Card lastAce = expectedCascadingPiles.get(lastPileOfAce).remove(lastCardIndexOfAce);
        expectedFoundationPiles.get(0).add(lastAce);
        model.move(PileType.CASCADE, lastPileOfAce, lastCardIndexOfAce,
                PileType.FOUNDATION, 0);
        Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
                expectedCascadingPiles), model.getGameState());
        Assert.assertFalse(model.isGameOver());

        for (int foundationPilePosition = 0; foundationPilePosition < 4; foundationPilePosition++) {
          for (int openPilePosition = 0; openPilePosition < openPiles; openPilePosition++) {
            Card lastCardFromFoundationPile = expectedFoundationPiles
                    .get(foundationPilePosition)
                    .remove(0);
            expectedOpenPiles.get(openPilePosition).add(lastCardFromFoundationPile);
            model.move(PileType.FOUNDATION, foundationPilePosition, 0, PileType.OPEN,
                    openPilePosition);
            Assert.assertEquals(convertPilesToString(expectedFoundationPiles,
                    expectedOpenPiles,
                    expectedCascadingPiles), model.getGameState());
            Assert.assertFalse(model.isGameOver());

            Card lastCardFromOpenPile = expectedOpenPiles.get(openPilePosition).remove(0);
            expectedFoundationPiles.get(foundationPilePosition).add(lastCardFromOpenPile);
            model.move(PileType.OPEN, openPilePosition, 0, PileType.FOUNDATION,
                    foundationPilePosition);
            Assert.assertEquals(convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
                    expectedCascadingPiles), model.getGameState());
            Assert.assertFalse(model.isGameOver());
          }
          int destFoundationPile = (foundationPilePosition + 1) % 4;
          Card lastCardFromFoundationPile = expectedFoundationPiles
                  .get(foundationPilePosition)
                  .remove(0);
          expectedFoundationPiles.get(destFoundationPile).add(lastCardFromFoundationPile);
          model.move(PileType.FOUNDATION, foundationPilePosition, 0, PileType.FOUNDATION,
                  destFoundationPile);
        }
      }
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


    List<Card> deck = getDeckWithAlterColorSuitAndSameCardValue();

    Assert.assertFalse(model.isGameOver());

    List<List<Card>> expectedCascadingPiles = getCardsInCascadingPiles(cascadePileCount, deck);

    model.startGame(deck, false);
    Assert.assertFalse(model.isGameOver());

    List<List<Card>> expectedOpenPiles = Utils.getListOfEmptyLists(4);

    List<List<Card>> expectedFoundationPiles = Utils.getListOfEmptyLists(4);


    for (int cardIndex = 12; cardIndex >= 0; cardIndex--) {
      for (int cascadePileSourceIndex = 0, openPileDestinationIndex = 0;
           cascadePileSourceIndex < cascadePileCount;
           cascadePileSourceIndex++, openPileDestinationIndex++) {

        Card cardFromCascadedPile =
                expectedCascadingPiles.get(cascadePileSourceIndex).remove(cardIndex);
        expectedOpenPiles.get(openPileDestinationIndex).add(cardFromCascadedPile);

        model.move(PileType.CASCADE, cascadePileSourceIndex, cardIndex, PileType.OPEN,
                openPileDestinationIndex);
        Assert.assertFalse(model.isGameOver());

        String expectedGameState = convertPilesToString(expectedFoundationPiles,
                expectedOpenPiles, expectedCascadingPiles);
        Assert.assertEquals(expectedGameState, model.getGameState());
      }

      for (int cascadePileIndex = 0, openPileSourceIndex = 0;
           cascadePileIndex < cascadePileCount;
           cascadePileIndex++, openPileSourceIndex++) {

        Card cardFromOpenPile = expectedOpenPiles.get(openPileSourceIndex).remove(0);
        int cascadePileDestinationIndex = (cascadePileIndex + 1) % cascadePileCount;
        expectedCascadingPiles.get(cascadePileDestinationIndex).add(cardFromOpenPile);

        model.move(PileType.OPEN, openPileSourceIndex, 0, PileType.CASCADE,
                cascadePileDestinationIndex);
        Assert.assertFalse(model.isGameOver());

        String expectedGameState = convertPilesToString(expectedFoundationPiles,
                expectedOpenPiles, expectedCascadingPiles);
        Assert.assertEquals(expectedGameState, model.getGameState());
      }

      for (int cascadePileSourceIndex = 0, foundationPileIndex = 0;
           cascadePileSourceIndex < cascadePileCount;
           cascadePileSourceIndex++, foundationPileIndex++) {
        Card cardFromCascadePile =
                expectedCascadingPiles.get(cascadePileSourceIndex).remove(cardIndex);

        int foundationPileDestinationIndex = (foundationPileIndex + 1) % 4;
        expectedFoundationPiles.get(foundationPileDestinationIndex).add(cardFromCascadePile);

        model.move(PileType.CASCADE, cascadePileSourceIndex, cardIndex, PileType.FOUNDATION,
                foundationPileDestinationIndex);

        if (cardIndex == 0 && cascadePileSourceIndex == 3) {
          Assert.assertTrue(model.isGameOver());
        } else {
          Assert.assertFalse(model.isGameOver());
        }

        String expectedGameState = convertPilesToString(expectedFoundationPiles,
                expectedOpenPiles, expectedCascadingPiles);
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

    String expectedGameState = convertPilesToString(expectedFoundationPiles, expectedOpenPiles,
            expectedCascadingPiles);
    Assert.assertEquals(expectedGameState, model.getGameState());

    try {
      model.move(PileType.FOUNDATION, 0, 12, PileType.CASCADE, 0);
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      model.move(PileType.FOUNDATION, 0, 12, PileType.OPEN, 0);
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      model.move(PileType.FOUNDATION, 0, 12, PileType.FOUNDATION, 1);
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  private static String convertPilesToString(List<List<Card>> foundationPiles,
                                             List<List<Card>> openPiles,
                                             List<List<Card>> cascadePiles) {
    return pileToString(foundationPiles, PileCategory.FOUNDATION)
            + System.lineSeparator()
            + pileToString(openPiles, PileCategory.OPEN)
            + System.lineSeparator()
            + pileToString(cascadePiles, PileCategory.CASCADE);
  }

  private static String pileToString(List<List<Card>> piles, PileCategory pile) {
    List<String> listOfStrings = new ArrayList<>();
    for (List<Card> cards : piles) {
      StringBuilder lineString = new StringBuilder();
      for (Card card : cards) {
        lineString.append(" ").append(card).append(",");
      }
      listOfStrings.add(Utils.removeTheLastCharacterFrom(lineString.toString()));
    }

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < listOfStrings.size(); i++) {
      stringBuilder.append(pile.getSymbol());
      stringBuilder.append(i + 1).append(":");
      stringBuilder.append(listOfStrings.get(i));
      stringBuilder.append(System.lineSeparator());
    }
    return stringBuilder.toString().trim();
  }

  private static List<List<Card>> getListOfEmptyLists(int listSize) {
    List<List<Card>> expectedOpenPiles = new ArrayList<>(listSize);
    for (int i = 0; i < listSize; i++) {
      expectedOpenPiles.add(new LinkedList<>());
    }
    return expectedOpenPiles;
  }

  private static List<Card> getReverseSortedDeckWithAcesInTheEnd(FreecellOperations<Card> model) {
    List<Card> deck = model.getDeck();
    //sorting the deck so that all Aces shifts to the end of the deck
    deck.sort((o1, o2) -> o2.getCardValue().getPriority() - o1.getCardValue().getPriority());
    return deck;
  }

  private static List<Card> getValidDeck() {
    List<Card> deck = new ArrayList<>(52);
    for (Suit suit : Suit.values()) {
      for (CardValue cardValue : CardValue.values()) {
        deck.add(new Card(suit, cardValue));
      }
    }
    return deck;
  }

  private static List<List<Card>> getCardsInCascadingPiles(int cascadePileCount,
                                                           List<Card> validDeck) {
    List<List<Card>> expectedCascadingPiles = getListOfEmptyLists(cascadePileCount);

    int i = 0;
    int j = 0;
    while (i < validDeck.size()) {
      expectedCascadingPiles.get(j).add(validDeck.get(i));
      j = (j + 1) % cascadePileCount;
      i++;
    }

    return expectedCascadingPiles;
  }

  private static List<Card> getDeckWithAlterColorSuitAndSameCardValue() {
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
    return deck;
  }
}
