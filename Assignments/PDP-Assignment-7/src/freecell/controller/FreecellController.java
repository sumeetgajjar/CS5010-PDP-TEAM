package freecell.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import freecell.model.FreecellOperations;
import util.Utils;

/**
 * Created by gajjar.s, on 6:58 PM, 11/3/18
 */
public class FreecellController implements IFreecellController<Card> {

  private static final String INVALID_DESTINATION_PILE_MESSAGE = "Invalid input, please enter " +
          "destination pile again.";
  private static final String INVALID_CARD_INDEX_MESSAGE = "Invalid input, please enter card " +
          "index again.";
  private static final String INVALID_MOVE_MESSAGE_STRING = "Invalid move, please try again";
  private static String GAME_QUIT_STRING = "Game quit prematurely.";
  private static final String GAME_OVER_STRING = "Game over.";
  private static final String INVALID_SOURCE_PILE_MESSAGE = "Invalid input, please enter source " +
          "pile again.";

  private final Readable readable;
  private final Appendable appendable;

  public FreecellController(Readable readable, Appendable appendable) throws IllegalArgumentException {
    Utils.requireNonNull(readable);
    Utils.requireNonNull(appendable);
    this.readable = readable;
    this.appendable = appendable;
  }

  /**
   * Start and play a new game of freecell with the provided deck. This deck should be used as-is.
   * This method returns only when the game is over (either by winning or by quitting). If the given
   * deck is invalid in terms of Card then controller will transmit following message "Cannot start
   * the game" and return control to the caller.
   *
   * @param deck    the deck to be used to play this game
   * @param model   the model for the game
   * @param shuffle shuffle the deck if true, false otherwise
   * @throws IllegalArgumentException if the deck is null or invalid, or if the model is null
   * @throws IllegalStateException    if the controller is unable to read input or transmit output
   */
  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, boolean shuffle) throws IllegalArgumentException, IllegalStateException {
    Utils.requireNonNull(deck);
    Utils.requireNonNull(model);

    try {
      model.startGame(deck, shuffle);
      //todo check this to make this more specific
    } catch (Exception e) {
      this.transmitGameState(model);
      this.transmitMessage("Cannot start the game");
      return;
    }

    Scanner scanner = new Scanner(this.readable);

    String inputString;
    while (true) {
      this.transmitGameState(model);

      if (!model.isGameOver()) {
        inputString = getNextInput(scanner);
        if (toQuit(inputString)) {
          break;
        }
        PileInfo sourcePileInfo = parsePileInfo(inputString, INVALID_SOURCE_PILE_MESSAGE);

        inputString = getNextInput(scanner);
        if (toQuit(inputString)) {
          break;
        }
        int cardIndex = readCardIndex(inputString);

        inputString = getNextInput(scanner);
        if (toQuit(inputString)) {
          break;
        }
        PileInfo destinationPileInfo = parsePileInfo(inputString, INVALID_DESTINATION_PILE_MESSAGE);

        try {
          model.move(
                  sourcePileInfo.getPileCategory().getPileType(),
                  sourcePileInfo.getPileIndex(),
                  cardIndex,
                  destinationPileInfo.getPileCategory().getPileType(),
                  destinationPileInfo.getPileIndex()
          );
        } catch (IllegalArgumentException e) {
          this.transmitMessage(String.format("%s: %s", INVALID_MOVE_MESSAGE_STRING,
                  e.getMessage()));
        }
      } else {
        this.transmitMessage(GAME_OVER_STRING);
      }
    }
  }

  private boolean toQuit(String inputString) {
    if (inputString.equalsIgnoreCase("q")) {
      this.transmitMessage(GAME_QUIT_STRING);
      return true;
    }
    return false;
  }

  private int readCardIndex(String cardIndexString) {
    while (true) {
      try {
        return Integer.parseInt(cardIndexString);
      } catch (IllegalArgumentException e) {
        this.transmitMessage(INVALID_CARD_INDEX_MESSAGE);
      }
    }
  }

  private PileInfo parsePileInfo(String inputString, String message) {
    while (true) {
      try {
        return this.parsePileString(inputString);
      } catch (IllegalArgumentException e) {
        this.transmitMessage(message);
      }
    }
  }

  private String getNextInput(Scanner scanner) {
    try {
      return scanner.next();
    } catch (Exception e) {
      throw new IllegalStateException("cannot read from readable");
    }
  }

  private PileInfo parsePileString(String pileString) {
    char pilePrefix = pileString.charAt(0);
    PileCategory pileCategory = PileCategory.getPileCategory(pilePrefix);
    int pileIndex = Integer.parseInt(pileString.substring(1));
    return new PileInfo(pileCategory, pileIndex);
  }

  private void transmitGameState(FreecellOperations<Card> model) {
    this.transmitMessage(model.getGameState());
  }

  private void transmitMessage(String message) {
    try {
      appendable.append(message);
      appendable.append(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException("cannot write to appendable");
    }
  }

  private static class PileInfo {
    private final PileCategory pileCategory;
    private final int pileIndex;

    private PileInfo(PileCategory pileCategory, int pileIndex) {
      this.pileCategory = pileCategory;
      this.pileIndex = pileIndex;
    }

    public PileCategory getPileCategory() {
      return pileCategory;
    }

    public int getPileIndex() {
      return pileIndex;
    }
  }
}
