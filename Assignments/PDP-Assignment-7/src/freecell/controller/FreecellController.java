package freecell.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import freecell.bean.Card;
import freecell.bean.PileCategory;
import freecell.model.FreecellOperations;
import util.Utils;

/**
 * <code>FreecellController</code> implements the FreecellController interface and provides the
 * facility to play the game of freeCell via a generic input and output streams mechanism.
 *
 * <p>The controller “runs” the program, effectively facilitating it through a sequence of
 * operations using inputs in source-independent/destination dependent manner. This implementation
 * of the controller gets inputs from a <code>Readable</code> which is an abstraction over a stream
 * of input "characters" and it passes it's output to an <code>Appendable</code> which is an
 * abstraction over a stream of output "characters".
 */
public class FreecellController implements IFreecellController<Card> {

  private static final String INVALID_DESTINATION_PILE_MESSAGE =
          "Invalid input, please enter destination pile again.";

  private static final String INVALID_CARD_INDEX_MESSAGE =
          "Invalid input, please enter card index again.";

  private static final String INVALID_MOVE_MESSAGE_STRING = "Invalid move, please try again";

  private static String GAME_QUIT_STRING = "Game quit prematurely.";

  private static final String GAME_OVER_STRING = "Game over.";

  private static final String INVALID_SOURCE_PILE_MESSAGE =
          "Invalid input, please enter source pile again.";

  private final Readable readable;
  private final Appendable appendable;

  /**
   * Constructs a <code>FreecellController</code> that takes in a Readable and Appendable object
   * that it uses to play the game of freeCell.
   *
   * @param readable   the readable object that is the source of characters
   * @param appendable the appendable object that is the destination of characters
   * @throws IllegalArgumentException if readable or appendable is null
   */
  public FreecellController(Readable readable, Appendable appendable)
          throws IllegalArgumentException {
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
   * <p>If the index of the pile is less than 1, then this method will throw an
   * IllegalArgumentException.
   *
   * <p>The controller “run” the game in the following sequence until the game is over:
   * Each transmission below should end with a newline.
   * <ul>
   * <li>Transmit game state to the Appendable object exactly as the model provides it.</li>
   * <li>If the game is ongoing, wait for user input from the Readable object. A valid user
   * input for a move is a sequence of three inputs (separated by spaces or newlines), asked one at
   * a time.</li>
   * <li>The source pile (e.g., "C1", as a single word). The pile number begins at 1, so that it is
   * more human-friendly.</li>
   * <li>If the game has been won, we pass the mesage "Game over." and end the game play. </li>
   * <li>The card index, again with the index beginning at 1.</li>
   * <li>The destination pile (e.g., "F2", as a single word). The pile number is again counted from
   * 1.</li>
   * <li>The controller will parse these inputs and pass the information on to the model to make
   * the move.</li>
   * <li>In case a move is invalid, then the controller passes a message to the appendable
   * object that the move was invalid and why. </li>
   * <li>If at any point, the input is either the letter 'q' or the letter 'Q', the controller
   * should transmit “Game quit prematurely.” and return.</li>
   * <li> If an input is unexpected (i.e. something other than 'q' or 'Q' to quit the game; a
   * letter other than 'C', 'F', 'O' to name a pile; anything that cannot be parsed to a valid
   * number after the pile letter; anything that is not a number for the card index) it should ask
   * the user to input it again.</li>
   * <li> If the user entered the source pile correctly but the card index incorrectly, the
   * controller should ask for only the card index again, not the source pile, and likewise for the
   * destination pile.</li>
   * </ul></p>
   *
   * <p>The controller will not propagate any exceptions thrown by the model to its caller.
   *
   * @param deck    the deck to be used to play this game
   * @param model   the model for the game
   * @param shuffle shuffle the deck if true, false otherwise
   * @throws IllegalArgumentException if the deck is null or invalid, or if the model is null
   * @throws IllegalStateException    if the controller is unable to read input or transmit output
   */
  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, boolean shuffle)
          throws IllegalArgumentException, IllegalStateException {

    Utils.requireNonNull(deck);
    Utils.requireNonNull(model);

    if (this.canStartGame(deck, model, shuffle)) {
      return;
    }

    Scanner scanner = new Scanner(this.readable);
    while (true) {
      // while the game is not over, keep waiting till game is not over.
      if (!model.isGameOver()) {
        // try and read the source pile first
        Optional<PileInfo> sourcePileInfoOptional = readPileInfo(scanner,
                INVALID_SOURCE_PILE_MESSAGE);
        if (!sourcePileInfoOptional.isPresent()) {
          // this means "q" was input
          break;
        }
        PileInfo sourcePileInfo = sourcePileInfoOptional.get();

        // try and read the card index next
        Optional<Integer> cardIndexOptional = readCardIndex(scanner);
        if (!cardIndexOptional.isPresent()) {
          // this means "q" was input
          break;
        }
        Integer cardIndex = cardIndexOptional.get();

        // try and read the destination pile in the end
        Optional<PileInfo> destinationPileInfoOptional = readPileInfo(scanner,
                INVALID_DESTINATION_PILE_MESSAGE);
        if (!destinationPileInfoOptional.isPresent()) {
          // this means "q" was input
          break;
        }
        PileInfo destinationPileInfo = destinationPileInfoOptional.get();

        // finally make a move
        this.makeMove(model, sourcePileInfo, destinationPileInfo, cardIndex);
      } else {
        // game is over, transmit message and then break
        this.transmitMessage(GAME_OVER_STRING);
        break;
      }
    }
  }

  private Optional<Integer> readCardIndex(Scanner scanner) throws IllegalArgumentException {
    while (true) {
      String inputString = getNextInput(scanner);
      if (toQuit(inputString)) {
        return Optional.empty();
      }

      try {
        int cardIndex = Integer.parseInt(inputString) - 1;
        if (cardIndex < 0) {
          throw new IllegalArgumentException("invalid input");
        }
        return Optional.of(cardIndex);
      } catch (IllegalArgumentException e) {
        this.transmitMessage(INVALID_CARD_INDEX_MESSAGE);
      }
    }
  }

  private Optional<PileInfo> readPileInfo(Scanner scanner, String message) {
    while (true) {
      String inputString = getNextInput(scanner);
      if (toQuit(inputString)) {
        return Optional.empty();
      }

      try {
        return Optional.of(this.parsePileString(inputString));
      } catch (IllegalArgumentException e) {
        this.transmitMessage(message);
      }
    }
  }

  private boolean canStartGame(List<Card> deck, FreecellOperations<Card> model, boolean shuffle) {
    try {
      model.startGame(deck, shuffle);
      this.transmitGameState(model);
    } catch (Exception e) {
      this.transmitGameState(model);
      this.transmitMessage("Cannot start the game");
      return true;
    }
    return false;
  }

  private void makeMove(FreecellOperations<Card> model, PileInfo sourcePileInfo,
                        PileInfo destinationPileInfo, int cardIndex) {
    try {
      model.move(
              sourcePileInfo.getPileCategory().getPileType(),
              sourcePileInfo.getPileIndex(),
              cardIndex,
              destinationPileInfo.getPileCategory().getPileType(),
              destinationPileInfo.getPileIndex()
      );
      this.transmitGameState(model);
    } catch (IllegalArgumentException e) {
      this.transmitMessage(String.format("%s: %s", INVALID_MOVE_MESSAGE_STRING,
              this.getInvalidMoveMessage(sourcePileInfo, cardIndex, destinationPileInfo)));
    }
  }

  private String getInvalidMoveMessage(PileInfo sourcePileInfo, int cardIndex,
                                       PileInfo destinationPileInfo) {
    return String.format("cannot move card of Index:%d from pile:%s to pile:%s",
            (cardIndex + 1), sourcePileInfo, destinationPileInfo);
  }

  private boolean toQuit(String inputString) {
    if (inputString.equalsIgnoreCase("q")) {
      this.transmitMessage(GAME_QUIT_STRING);
      return true;
    }
    return false;
  }

  private String getNextInput(Scanner scanner) throws IllegalStateException {
    try {
      return scanner.next();
    } catch (Exception e) {
      throw new IllegalStateException("cannot read from readable");
    }
  }

  private PileInfo parsePileString(String pileString) throws IllegalArgumentException {
    char pilePrefix = pileString.charAt(0);
    PileCategory pileCategory = PileCategory.getPileCategory(pilePrefix);
    int pileIndex = Integer.parseInt(pileString.substring(1)) - 1;
    if (pileIndex < 0) {
      throw new IllegalArgumentException("negative pile index");
    }
    return new PileInfo(pileCategory, pileIndex);
  }

  private void transmitGameState(FreecellOperations<Card> model) {
    this.transmitMessage(model.getGameState());
  }

  private void transmitMessage(String message) throws IllegalStateException {
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

    PileCategory getPileCategory() {
      return pileCategory;
    }

    int getPileIndex() {
      return pileIndex;
    }

    @Override
    public String toString() {
      return String.format("%s%d", pileCategory.getSymbol(), pileIndex + 1);
    }
  }
}
