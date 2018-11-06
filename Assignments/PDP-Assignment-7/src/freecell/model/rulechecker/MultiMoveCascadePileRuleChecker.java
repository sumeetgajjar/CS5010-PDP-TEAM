package freecell.model.rulechecker;

import java.util.List;

import freecell.bean.Card;
import util.Utils;

/**
 * Created by gajjar.s, on 12:37 PM, 11/6/18
 */
public class MultiMoveCascadePileRuleChecker extends SingleMoveCascadePileRuleChecker {
  private final long cascadePileCount;
  private final long openPileCount;

  public MultiMoveCascadePileRuleChecker(long cascadePileCount, long openPileCount) {
    this.cascadePileCount = cascadePileCount;
    this.openPileCount = openPileCount;
  }

  @Override
  public boolean canGetCardsFromThePile(int cardIndex, List<Card> pile) {
    if (cardIndex < 0 || cardIndex >= pile.size()) {
      return false;
    }

    if (!ifEnoughCascadeAndOpenPilesAreEmpty(pile.size() - cardIndex)) {
      return false;
    }
    return doSourcePileCardsFormValidBuild(Utils.sliceList(pile, cardIndex));
  }

  private boolean doSourcePileCardsFormValidBuild(List<Card> sliceList) {
    for (int currentCardIndex = 0; currentCardIndex < sliceList.size() - 1; currentCardIndex++) {
      Card currentCard = sliceList.get(currentCardIndex);
      Card nextCard = sliceList.get(currentCardIndex + 1);
      if (currentCard.getSuit().getColor() == nextCard.getSuit().getColor()) {
        return false;
      }

      if (currentCard.getCardValue().getPriority() - nextCard.getCardValue().getPriority() != 1) {
        return false;
      }
    }
    return true;
  }

  private boolean ifEnoughCascadeAndOpenPilesAreEmpty(int numberOfCardsToBeMoved) {
    return numberOfCardsToBeMoved <= (openPileCount + 1) * Math.pow(2, cascadePileCount);
  }
}
