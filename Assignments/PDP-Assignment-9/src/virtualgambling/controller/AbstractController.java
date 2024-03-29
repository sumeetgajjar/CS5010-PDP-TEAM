package virtualgambling.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import util.Utils;
import virtualgambling.view.View;

/**
 * This class represents a AbstractController which has a {@link View}. It implements the {@link
 * Controller} interface. It minimize the effort required to implement {@link Controller}
 * interface.
 */
abstract class AbstractController implements Controller {

  protected final View view;

  /**
   * Constructor to be invoked by the derived class.
   *
   * @param view the view
   * @throws IllegalArgumentException if the given view is null
   */
  AbstractController(View view) throws IllegalArgumentException {
    this.view = Utils.requireNonNull(view);
  }

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
    } catch (IOException | NoSuchElementException e) {
      throw new IllegalStateException("Cannot get data from view");
    }
  }

  protected String getStringInputFromUser(String message,
                                          Supplier<String> supplier,
                                          Consumer<String> consumer) {
    consumer.accept(message);
    return supplier.get();
  }

  protected Date getDateFromUser(String message, Supplier<String> supplier,
                                 Consumer<String> consumer) {
    while (true) {
      try {
        String dateString = getStringInputFromUser(message, supplier,
                consumer);
        return Utils.getDateFromDefaultFormattedDateString(dateString);
      } catch (ParseException e) {
        consumer.accept(e.getMessage());
      }
    }
  }

  protected Date getEndDateFromUser(String message, Supplier<String> supplier,
                                    Consumer<String> consumer) {
    while (true) {
      try {
        String dateString = getStringInputFromUser(message, supplier,
                consumer);
        if (dateString.equalsIgnoreCase("-")) {
          return null;
        } else {
          return Utils.getDateFromDefaultFormattedDateString(dateString);
        }
      } catch (ParseException e) {
        consumer.accept(e.getMessage());
      }
    }
  }

  protected long getLongInputFromUser(String messageToDisplay,
                                      Supplier<String> supplier,
                                      Consumer<String> consumer) {
    while (true) {
      try {
        return Long.parseLong(getStringInputFromUser(messageToDisplay, supplier, consumer));
      } catch (NumberFormatException e) {
        consumer.accept(String.format("Unparseable Input, %s", e.getMessage()));
      }
    }
  }

  protected int getIntegerInputFromUser(String messageToDisplay,
                                        Supplier<String> supplier,
                                        Consumer<String> consumer) {
    while (true) {
      try {
        return Integer.parseInt(getStringInputFromUser(messageToDisplay, supplier, consumer));
      } catch (NumberFormatException e) {
        consumer.accept(String.format("Unparseable Input, %s", e.getMessage()));
      }
    }
  }

  protected double getDoubleInputFromUser(String messageToDisplay,
                                          Supplier<String> supplier,
                                          Consumer<String> consumer) {
    while (true) {
      try {
        return Double.parseDouble(getStringInputFromUser(messageToDisplay, supplier, consumer));
      } catch (NumberFormatException e) {
        consumer.accept(String.format("Unparseable Input, %s", e.getMessage()));
      }
    }
  }

  protected BigDecimal getBigDecimalInputFromUser(String message,
                                                  Supplier<String> supplier,
                                                  Consumer<String> consumer) {
    while (true) {
      try {
        return new BigDecimal(getStringInputFromUser(message, supplier, consumer));
      } catch (NumberFormatException e) {
        consumer.accept("Unparseable Input");
      }
    }
  }

  protected boolean isQuitCommand(String commandString) {
    return commandString.equalsIgnoreCase("q")
            || commandString.equalsIgnoreCase("quit");
  }
}
