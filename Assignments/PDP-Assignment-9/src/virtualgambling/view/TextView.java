package virtualgambling.view;

import java.io.IOException;
import java.util.Scanner;

import util.Utils;


/**
 * This class represents a {@link TextView}. It implements the {@link View} interface. It can read
 * text inputs from User using {@link Readable} and display the output to the User using {@link
 * Appendable}.
 */
public class TextView implements View {

  private final Scanner scanner;
  private final Appendable appendable;

  /**
   * Constructs a object of {@link TextView} using the given params.
   *
   * @param readable   the readable to read from
   * @param appendable the appendable to write to
   * @throws IllegalArgumentException if the given params are null
   */
  public TextView(Readable readable, Appendable appendable) throws IllegalArgumentException {
    Readable readable1 = Utils.requireNonNull(readable);
    this.appendable = Utils.requireNonNull(appendable);
    this.scanner = new Scanner(readable1);
  }

  /**
   * Returns an entire line of input read from {@link Readable}.
   *
   * @return an entire line of input read from {@link Readable}
   */
  @Override
  public String getInput() {
    return scanner.next();
  }

  /**
   * Writes the given text to the appendable.
   *
   * @param text the text to display
   * @throws IOException if unable to write to appendable
   */
  @Override
  public void display(String text) throws IOException {
    this.appendable.append(text);
  }
}
