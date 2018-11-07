import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import freecell.bean.Card;
import freecell.bean.CardValue;
import freecell.bean.Suit;
import freecell.controller.FreecellController;
import freecell.model.FreecellOperations;
import freecell.model.PileType;
import util.TestUtils;

/**
 * Created by gajjar.s, on 4:42 PM, 11/6/18
 */
public class FreecellControllerIsolationTest {

  private final Random random = new Random(1);

  @Test
  public void testIfControllerPassesCorrectInputToModel() {
    for (boolean shuffle : Arrays.asList(true, false)) {
      StringReader actualInput = new StringReader("C1 11 F1 C2 10 F2 C3 9 F3 Q");
      StringBuffer actualOutput = new StringBuffer();
      StringBuilder mockModelLog = new StringBuilder();
      StringBuilder expectedLog = new StringBuilder();
      StringBuilder expectedOutput = new StringBuilder();

      int codeForStartGame = random.nextInt();
      int codeForMove = random.nextInt();
      int codeForGameState = random.nextInt();
      int codeForIsGameOver = random.nextInt();
      int codeForGetDeck = random.nextInt();
      FreecellOperations<Card> mockModelWithLogger = new MockModelWithLogger(mockModelLog,
              codeForStartGame, codeForMove,
              codeForGameState,
              codeForIsGameOver, codeForGetDeck);
      List<Card> deckInMockModel = Arrays.asList(
              new Card(Suit.HEARTS, CardValue.ACE),
              new Card(Suit.DIAMONDS, CardValue.ACE));

      FreecellController freecellController = new FreecellController(actualInput, actualOutput);
      List<Card> deck = mockModelWithLogger.getDeck();
      expectedLog.append(codeForGetDeck).append(System.lineSeparator());

      freecellController.playGame(deck, mockModelWithLogger, shuffle);
      expectedLog.append(codeForStartGame).append(System.lineSeparator());
      expectedLog.append(deckInMockModel).append(System.lineSeparator());
      expectedLog.append(shuffle).append(System.lineSeparator());

      for (String string : Arrays.asList("CASCADE0 10 FOUNDATION0", "CASCADE1 9 FOUNDATION1",
              "CASCADE2 8 FOUNDATION2")) {
        expectedLog.append(codeForGameState).append(System.lineSeparator());

        expectedLog.append(codeForIsGameOver).append(System.lineSeparator());

        expectedLog.append(codeForMove).append(System.lineSeparator());
        expectedLog.append(string).append(System.lineSeparator());

        expectedOutput.append("YOLO").append(System.lineSeparator());
      }

      expectedLog.append(codeForGameState).append(System.lineSeparator());
      expectedLog.append(codeForIsGameOver).append(System.lineSeparator());

      expectedOutput.append("YOLO").append(System.lineSeparator());
      expectedOutput.append("Game quit prematurely.").append(System.lineSeparator());

      Assert.assertEquals(expectedLog.toString(), mockModelLog.toString());
      Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
    }
  }

  @Test
  public void testIfControllerDoesNotPropagateStartGameExceptionToCaller() {
    for (boolean shuffle : Arrays.asList(true, false)) {
      StringReader actualInput = new StringReader("C1 11 F1 C2 10 F2 C3 9 F3 Q");
      StringBuffer actualOutput = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellOperations<Card> mockModel = new MockModelWhichThrowsException1();

      FreecellController freecellController = new FreecellController(actualInput, actualOutput);
      freecellController.playGame(mockModel.getDeck(), mockModel, shuffle);

      expectedOutput.append("YOLO").append(System.lineSeparator());
      expectedOutput.append(TestUtils.CANNOT_START_THE_GAME_STRING).append(System.lineSeparator());
      Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
    }
  }

  @Test
  public void testIfControllerDoesNotPropagateIllegalArgumentExceptionOfMoveToCaller() {
    for (boolean shuffle : Arrays.asList(true, false)) {
      StringReader actualInput = new StringReader("C1 11 F1 C2 10 F2 C3 9 F3 Q");
      StringBuffer actualOutput = new StringBuffer();

      StringBuilder expectedOutput = new StringBuilder();

      FreecellOperations<Card> mockModel = new MockModelWhichThrowsException2();

      FreecellController freecellController = new FreecellController(actualInput, actualOutput);
      freecellController.playGame(mockModel.getDeck(),
              mockModel, shuffle);

      expectedOutput.append("YOLO").append(System.lineSeparator());
      expectedOutput.append(
              "Invalid move, please try again: mock illegal argument exception in move")
              .append(System.lineSeparator());
      expectedOutput.append(
              "Invalid move, please try again: mock illegal argument exception in move")
              .append(System.lineSeparator());
      expectedOutput.append(
              "Invalid move, please try again: mock illegal argument exception in move")
              .append(System.lineSeparator());
      expectedOutput.append(TestUtils.GAME_QUIT_STRING).append(System.lineSeparator());
      Assert.assertEquals(expectedOutput.toString(), actualOutput.toString());
    }
  }

  private static class MockModelWhichThrowsException1 implements FreecellOperations<Card> {

    @Override
    public List<Card> getDeck() {
      return Collections.emptyList();
    }

    @Override
    public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {
      throw new IllegalArgumentException("mock illegal argument exception in startGame");
    }

    @Override
    public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                     int destPileNumber) throws IllegalArgumentException, IllegalStateException {
      throw new IllegalArgumentException("mock illegal argument exception in move");
    }

    @Override
    public boolean isGameOver() {
      return false;
    }

    @Override
    public String getGameState() {
      return "YOLO";
    }
  }

  private static class MockModelWhichThrowsException2 extends MockModelWhichThrowsException1 {

    @Override
    public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {
      // overriding method to not throw exception
    }
  }

  private static class MockModelWithLogger implements FreecellOperations<Card> {

    private final StringBuilder log;
    private final int codeForStartGame;
    private final int codeForMove;
    private final int codeForGameState;
    private final int codeForIsGameOver;
    private final int codeForGetDeck;

    public MockModelWithLogger(StringBuilder log, int codeForStartGame, int codeForMove,
                               int codeForGameState, int codeForIsGameOver, int codeForGetDeck) {
      this.log = log;
      this.codeForStartGame = codeForStartGame;
      this.codeForMove = codeForMove;
      this.codeForGameState = codeForGameState;
      this.codeForIsGameOver = codeForIsGameOver;
      this.codeForGetDeck = codeForGetDeck;
    }


    @Override
    public List<Card> getDeck() {
      log.append(codeForGetDeck).append(System.lineSeparator());
      return Arrays.asList(
              new Card(Suit.HEARTS, CardValue.ACE),
              new Card(Suit.DIAMONDS, CardValue.ACE));
    }

    @Override
    public void startGame(List<Card> deck, boolean shuffle) throws IllegalArgumentException {
      log.append(codeForStartGame).append(System.lineSeparator());
      log.append(deck.toString()).append(System.lineSeparator());
      log.append(shuffle).append(System.lineSeparator());
    }

    @Override
    public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                     int destPileNumber) throws IllegalArgumentException, IllegalStateException {
      log.append(codeForMove).append(System.lineSeparator());
      log.append(source).append(pileNumber).append(" ");
      log.append(cardIndex).append(" ");
      log.append(destination).append(destPileNumber).append(System.lineSeparator());
    }

    @Override
    public boolean isGameOver() {
      log.append(codeForIsGameOver).append(System.lineSeparator());
      return false;
    }

    @Override
    public String getGameState() {
      log.append(codeForGameState).append(System.lineSeparator());
      return "YOLO";
    }
  }

}
