package freecell.controller;

import java.util.List;

import freecell.bean.Card;
import freecell.model.FreecellOperations;

/**
 * Created by gajjar.s, on 6:58 PM, 11/3/18
 */
public class FreecellController implements IFreecellController<Card> {

  public FreecellController(Readable readable, Appendable appendable) {

  }

  /**
   * Start and play a new game of freecell with the provided deck. This deck should be used as-is.
   * This method returns only when the game is over (either by winning or by quitting). If the given
   * deck is invalid in terms of Card then controller will transmit following message "Cannot start
   * the game".
   *
   * @param deck    the deck to be used to play this game
   * @param model   the model for the game
   * @param shuffle shuffle the deck if true, false otherwise
   * @throws IllegalArgumentException if the deck is null or invalid, or if the model is null
   * @throws IllegalStateException    if the controller is unable to read input or transmit output
   */
  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, boolean shuffle) throws IllegalArgumentException, IllegalStateException {

  }
}
