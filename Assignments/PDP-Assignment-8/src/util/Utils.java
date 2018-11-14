package util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

  /**
   * Gets the last element from the list.
   *
   * @param list the list from which to get the last element from
   * @param <T>  the type of data in the list
   * @return the last element
   */
  public static <T> T getLast(List<T> list) throws IndexOutOfBoundsException {
    return list.get(list.size() - 1);
  }

  /**
   * Returns a list of empty lists.
   *
   * @param listSize number of empty list
   * @param <T>      type of the empty lists
   * @return a list of empty lists
   */
  public static <T> List<List<T>> getListOfEmptyLists(int listSize) {
    List<List<T>> expectedOpenPiles = new ArrayList<>(listSize);
    for (int i = 0; i < listSize; i++) {
      expectedOpenPiles.add(new ArrayList<>());
    }
    return expectedOpenPiles;
  }


  /**
   * Gets the first element from the list.
   *
   * @param collect the list from which the first element is needed
   * @param <T>     the type of the data in the list
   * @return the first element in the list
   */
  public static <T> T getFirst(List<T> collect) {
    Utils.requireNonNull(collect);
    return collect.get(0);
  }

  /**
   * Returns a string minus it's last character.
   *
   * @param string string from which last character is to be removed
   * @return string minus it's last character
   */
  public static String removeTheLastCharacterFrom(String string) {
    Utils.requireNonNull(string);
    if (string.isEmpty()) {
      return string;
    }
    return string.substring(0, string.length() - 1);
  }

  /**
   * Returns a sliced list of the given list from the given start index.
   *
   * @param fullList   the list to slice
   * @param startIndex the index of the list to start slicing
   * @param <T>        the type of the list
   * @return a sliced list of the given list from the given start index
   */
  public static <T> List<T> sliceList(List<T> fullList, int startIndex) {
    Utils.requireNonNull(fullList);
    return new ArrayList<>(fullList.subList(startIndex, fullList.size()));
  }

  /**
   * Returns a currency formatter for the default locale.
   *
   * @return currency formatter
   */
  public static NumberFormat getCurrencyNumberFormatter() {
    return NumberFormat.getCurrencyInstance();
  }

  /**
   * Formats the given date into a 'yyyy-MM-dd' format.
   *
   * @param date date to be formatted
   * @return formatted date in the form of a string
   */
  public static String getDefaultDateFormattedDate(Date date) {
    Utils.requireNonNull(date);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return simpleDateFormat.format(date);
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
