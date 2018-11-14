package util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents utility functions that are independent of the state of the application.
 */
public class Utils {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Enforces that inputs are non null.
   *
   * @param input input to be checked for non-nullability
   * @param <T>   type of input
   * @return input as it is if input is not null
   * @throws IllegalArgumentException if input is null
   */
  public static <T> T requireNonNull(T input) throws IllegalArgumentException {
    if (Objects.isNull(input)) {
      throw new IllegalArgumentException("Invalid input");
    }
    return input;
  }

  /**
   * Returns a currency formatter for the default locale.
   *
   * @return currency formatter
   */
  public static NumberFormat getCurrencyNumberFormatter() {
    return NumberFormat.getCurrencyInstance(Locale.US);
  }

  /**
   * Formats the given date into a 'yyyy-MM-dd' format.
   *
   * @param date date to be formatted
   * @return formatted date in the form of a string
   */
  public static String getDefaultFormattedDateStringFromDate(Date date) {
    Utils.requireNonNull(date);
    return SIMPLE_DATE_FORMAT.format(date);
  }

  public static Date getDateFromDefaultFormattedDateString(String dateString) throws ParseException {
    Utils.requireNonNull(dateString);
    return SIMPLE_DATE_FORMAT.parse(dateString);
  }

  public static boolean checkTimeNotInBusinessHours(Date date) {
    int dayOfTheWeek = date.toInstant().get(ChronoField.DAY_OF_WEEK);
    if (dayOfTheWeek < 1 || dayOfTheWeek > 5) {
      return false;
    }

    int hour = date.toInstant().get(ChronoField.HOUR_OF_DAY);
    return hour >= 8 && hour <= 15;
  }
}
