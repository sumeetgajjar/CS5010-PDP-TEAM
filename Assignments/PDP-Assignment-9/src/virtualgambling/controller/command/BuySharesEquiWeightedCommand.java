package virtualgambling.controller.command;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import util.Utils;

/**
 * This class represents a Buy Share weighted command with the modification that each ticker has
 * equal weight. It implements the {@link Command} interface.
 */
public class BuySharesEquiWeightedCommand extends BuySharesWeightedCommand {
  /**
   * Constructs a BuySharesEquiWeightedCommand in terms of the tickerNames such that each ticker has
   * an equal weight.
   *
   * @param tickerNames set of ticker names
   */
  public BuySharesEquiWeightedCommand(Set<String> tickerNames) {
    super(getTickersWithWeights(Utils.requireNonNull(tickerNames)));
  }

  private static Map<String, Double> getTickersWithWeights(Set<String> tickerNames) {
    double weight = 100.0 / tickerNames.size();
    return tickerNames.stream().collect(Collectors.toMap(stock -> stock,
            stock -> weight));
  }
}
