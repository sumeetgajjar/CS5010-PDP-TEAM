package virtualgambling.controller;

import java.io.IOException;

import util.Utils;
import virtualgambling.view.View;

abstract class AbstractController implements Controller {

  private final View view;

  AbstractController(View view) {
    this.view = Utils.requireNonNull(view);
  }

  abstract String getWelcomeMessage();

  protected void displayOnView(String text) throws IllegalStateException {
    try {
      this.view.display(text);
      this.view.display(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException("Cannot display data on view");
    }
  }

  protected String getInputFromView() throws IllegalStateException {
    try {
      return view.getInput();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot get data from view");
    }
  }

}
