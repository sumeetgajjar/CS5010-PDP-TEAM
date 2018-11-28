package virtualgambling.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

import util.Utils;
import virtualgambling.view.View;

abstract class AbstractController implements Controller {

  protected final View view;

  AbstractController(View view) {
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
    } catch (IOException e) {
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
        message = message + System.lineSeparator() + "Please press enter if there is not end date";
        String dateString = getStringInputFromUser(message, supplier,
                consumer);
        if (dateString.isEmpty()) {
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
        consumer.accept(e.getMessage());
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
        consumer.accept(e.getMessage());
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
        consumer.accept(e.getMessage());
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
        consumer.accept(e.getMessage());
      }
    }
  }

  protected boolean isQuitCommand(String commandString) {
    return commandString.equalsIgnoreCase("q")
            || commandString.equalsIgnoreCase("quit");
  }
}
