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

  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, boolean shuffle) throws IllegalArgumentException, IllegalStateException {

  }
}
