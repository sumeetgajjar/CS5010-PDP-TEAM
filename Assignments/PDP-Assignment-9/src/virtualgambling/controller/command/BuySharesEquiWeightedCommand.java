package virtualgambling.controller.command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class represents a Buy Share weighted command with the modification that each ticker has
 * equal weight. It implements the {@link Command} interface.
 */
public class BuySharesEquiWeightedCommand extends BuySharesWeightedCommand {
  /**
   * Constructs a BuySharesEquiWeightedCommand in terms of the tickerNames such that each ticker has
   * an equal weight.
   *
   * @param tickerNames list of ticker names
   */
  public BuySharesEquiWeightedCommand(List<String> tickerNames) {
    super(tickerNames,
            IntStream.range(0, tickerNames.size())
                    .mapToDouble(i -> 100.0 / tickerNames.size())
                    .boxed()
                    .collect(Collectors.toList()));
  }
}
