package virtualgambling.controller.command;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class represents a {@link AbstractCommand} and it implements {@link Command} interface. It
 * has a {@link Supplier} and a {@link Consumer} for IO purposes. Other {@link Command}
 * implementation can extend this class to avoid redundancy.
 */
public abstract class AbstractCommand implements Command {

  protected final Supplier<String> supplier;
  protected final Consumer<String> consumer;

  /**
   * Protected constructor, to be used by the derived classes.
   *
   * @param supplier the supplier of type string
   * @param consumer the consumer of type string
   */
  protected AbstractCommand(Supplier<String> supplier, Consumer<String> consumer) {
    this.supplier = supplier;
    this.consumer = consumer;
  }
}
