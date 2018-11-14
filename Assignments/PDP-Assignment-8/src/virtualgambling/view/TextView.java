package virtualgambling.view;

import java.io.IOException;
import java.util.Scanner;

import virtualgambling.util.Utils;

/**
 * Created by gajjar.s, on 9:42 PM, 11/12/18
 */
public class TextView implements View {

  private final Readable readable;
  private final Appendable appendable;
  private final Scanner scanner;

  public TextView(Readable readable, Appendable appendable) throws IllegalArgumentException {
    this.readable = Utils.requireNonNull(readable);
    this.appendable = Utils.requireNonNull(appendable);
    this.scanner = new Scanner(this.readable);
  }

  @Override
  public String getInput() throws IOException {
    return scanner.nextLine();
  }

  @Override
  public void display(String text) throws IOException {
    this.appendable.append(text);
  }
}
