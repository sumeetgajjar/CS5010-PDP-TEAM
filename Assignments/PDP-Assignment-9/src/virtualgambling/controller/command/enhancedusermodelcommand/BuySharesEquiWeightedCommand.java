package virtualgambling.controller.command.enhancedusermodelcommand;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import util.Utils;
import virtualgambling.controller.command.Command;

/**
 * This class represents a Buy Share weighted command with the modification that each ticker has
 * equal weight. It implements the {@link Command} interface.
 */
public class BuySharesEquiWeightedCommand extends BuySharesWeightedCommand {
  /**
   * Constructs a BuySharesEquiWeightedCommand in terms of the tickerNames such that each ticker has
   * an equal weight.
   *
   * @param dateOfPurchase the date of purchase for stocks
   * @param tickerNames    set of ticker names
   */
  public BuySharesEquiWeightedCommand(Date dateOfPurchase, Set<String> tickerNames) {
    super(dateOfPurchase, getTickersWithWeights(Utils.requireNonNull(tickerNames)));
  }

  private static Map<String, Double> getTickersWithWeights(Set<String> tickerNames) {
    double weight = 100.0 / tickerNames.size();
    return tickerNames.stream().collect(Collectors.toMap(stock -> stock,
            stock -> weight));
  }
}
