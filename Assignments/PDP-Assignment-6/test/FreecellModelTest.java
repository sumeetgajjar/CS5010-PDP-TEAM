import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
