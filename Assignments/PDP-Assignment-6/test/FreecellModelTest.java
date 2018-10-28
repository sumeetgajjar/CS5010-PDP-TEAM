import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import freecell.bean.Card;
import freecell.bean.Rank;
import freecell.bean.Suit;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;

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
    for (int i = 4; i <= 8; i++) {
      // open piles
      for (int j = 1; j <= 4; j++) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(i)
                .opens(j)
                .build();

        //test invoking multiple calls to getDeck returns a valid deck
        Assert.assertEquals(expectedUnorderedDeck, new HashSet<>(model.getDeck()));
        Assert.assertEquals(expectedUnorderedDeck, new HashSet<>(model.getDeck()));
      }
    }
  }

  @Test
  public void constructingGameWithInvalidPiles() {
    for (int openPileCount : Arrays.asList(-1, 0, 5)) {
      for (int cascadingPileCount : Arrays.asList(-1, 0, 1, 2, 3, 9)) {
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
    // cascade piles
    for (int cascadePileCount = 4; cascadePileCount <= 8; cascadePileCount++) {
      // open piles
      for (int OpenPileCount = 1; OpenPileCount <= 4; OpenPileCount++) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadePileCount)
                .opens(OpenPileCount)
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

    // cascade piles
    for (int cascadePileCount = 4; cascadePileCount <= 8; cascadePileCount++) {
      // open piles
      for (int openPileCount = 1; openPileCount <= 4; openPileCount++) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadePileCount)
                .opens(openPileCount)
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
    // cascade piles
    for (int cascadePileCount = 4; cascadePileCount <= 8; cascadePileCount++) {
      // open piles
      for (int openPileCount = 1; openPileCount <= 4; openPileCount++) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadePileCount)
                .opens(openPileCount)
                .build();

        List<Card> deckWith53Cards = getValidDeck();
        deckWith53Cards.add(new Card(
                Suit.values()[randomGenerator.nextInt(4)],
                Rank.values()[randomGenerator.nextInt(13)]
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
    // cascade piles
    for (int cascadePileCount = 4; cascadePileCount <= 8; cascadePileCount++) {
      // open piles
      for (int openPileCount = 1; openPileCount <= 4; openPileCount++) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadePileCount)
                .opens(openPileCount)
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
    // cascade piles
    for (int cascadePileCount = 4; cascadePileCount <= 8; cascadePileCount++) {
      // open piles
      for (int openPileCount = 1; openPileCount <= 4; openPileCount++) {
        FreecellOperations<Card> model = FreecellModel.getBuilder()
                .cascades(cascadePileCount)
                .opens(openPileCount)
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
  public void startGame() {
  }

  @Test
  public void move() {
  }

  @Test
  public void isGameOver() {
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
      for (Rank rank : Rank.values()) {
        deck.add(new Card(suit, rank));
      }
    }
    return deck;
  }
}
