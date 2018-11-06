package freecell.model;

import java.util.List;

import freecell.bean.Card;

/**
 * Created by gajjar.s, on 6:58 PM, 11/3/18
 */
public class FreecellMultiMoveModel implements FreecellOperations<Card> {

  @Override
  public List<Card> getDeck() {
    return null;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {

  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {

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
    return null;
  }
}
