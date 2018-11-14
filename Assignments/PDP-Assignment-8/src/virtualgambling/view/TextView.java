package virtualgambling.view;

import virtualgambling.util.Utils;

/**
 * Created by gajjar.s, on 9:42 PM, 11/12/18
 */
public class TextView implements View {

  private final Readable readable;
  private final Appendable appendable;

  public TextView(Readable readable, Appendable appendable) throws IllegalArgumentException {
    this.readable = Utils.requireNonNull(readable);
    this.appendable = Utils.requireNonNull(appendable);
  }

  @Override
  public String getInput() {


    return null;
  }

  @Override
  public void display(String text) {

  }
}
