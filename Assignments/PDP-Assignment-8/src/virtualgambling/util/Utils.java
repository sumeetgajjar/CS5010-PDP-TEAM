package virtualgambling.util;

import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Objects;

/**
 * Represents utility functions that are independent of the state of the application.
 */
public class Utils {

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

  public static boolean checkTimeNotInBusinessHours(Date date) {
    int dayOfTheWeek = date.toInstant().get(ChronoField.DAY_OF_WEEK);
    if (dayOfTheWeek < 1 || dayOfTheWeek > 5) {
      return false;
    }

    int hour = date.toInstant().get(ChronoField.HOUR_OF_DAY);
    return hour >= 8 && hour <= 15;
  }
}
