package virtualgambling.controller.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by gajjar.s, on 5:24 PM, 11/27/18
 */
public abstract class AbstractCommand implements Command {

  protected final Supplier<String> supplier;
  protected final Consumer<String> consumer;

  protected AbstractCommand(Supplier<String> supplier, Consumer<String> consumer) {
    this.supplier = supplier;
    this.consumer = consumer;
  }
}
