package util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
   * Returns a currency formatted string of the number.
   *
   * @param number the number to format
   * @return currency formatter
   */
  public static String getFormattedCurrencyNumberString(Number number) {
    return NumberFormat.getCurrencyInstance(Locale.US).format(number);
  }

  /**
   * Formats the given date into a 'yyyy-MM-dd' format.
   *
   * @param date date to be formatted
   * @return formatted date in the form of a string
   */
  public static String getDefaultFormattedDateStringFromDate(Date date) {
    Utils.requireNonNull(date);
    SIMPLE_DATE_FORMAT.setLenient(false);
    return SIMPLE_DATE_FORMAT.format(date);
  }

  /**
   * Parses the given dateString and returns the {@link Date} Object. The format of the dateString
   * should be 'yyyy-MM-dd'.
   *
   * @param dateString the string to parse
   * @return the Date object.
   * @throws ParseException if the string cannot be parsed to an Date
   */
  public static Date getDateFromDefaultFormattedDateString(String dateString)
          throws ParseException {

    Utils.requireNonNull(dateString);
    SIMPLE_DATE_FORMAT.setLenient(false);
    return SIMPLE_DATE_FORMAT.parse(dateString);
  }


  /**
   * Returns true if the given date is a weekend, false otherwise.
   *
   * @param date the date to check
   * @return true if the given date is a weekend, false otherwise
   */
  public static boolean isNonWorkingDayOfTheWeek(Date date) {
    Calendar c = Utils.getCalendarInstance();
    c.setTime(date);
    int dayOfTheWeek = c.get(Calendar.DAY_OF_WEEK);

    return dayOfTheWeek < 2 || dayOfTheWeek > 6;
  }

  /**
   * Returns true if the given date is in Future, false otherwise.
   *
   * @param date the date to check
   * @return true if the given date is in Future, false otherwise.
   */
  public static boolean isFutureDate(Date date) {
    Date currentDate = Utils.getCalendarInstance().getTime();
    return date.compareTo(currentDate) > 0;
  }

  /**
   * Returns true if the given dates have same year, month and day component, false otherwise.
   *
   * @param date1 the date1
   * @param date2 the date2
   * @return true if the given dates have same year, month and day component, false otherwise.
   */
  public static boolean doesDatesHaveSameDay(Date date1, Date date2) {
    return removeTimeFromDate(date1).equals(removeTimeFromDate(date2));
  }

  /**
   * Returns a copy the given date with Hour, Minute, Second and Millisecond component set to 0.
   *
   * @param date the date
   * @return a copy the given date with Hour, Minute, Second and Millisecond component set to 0
   */
  public static Date removeTimeFromDate(Date date) {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  /**
   * Compares two doubles and ensures that they are within comparision threshold of delta.
   *
   * @param number1 number 1
   * @param number2 number 2
   * @param delta   threshold of allowance for floating point precision issues
   * @return true if the numbers are equal within delta, false otherwise
   */
  public static boolean areTwoDoublesEqual(double number1, double number2, double delta) {
    return Math.abs(number1 - number2) <= delta;
  }

  /**
   * Returns a Calendar object with lenient flag set to false.
   *
   * @return a Calendar object with lenient flag set to false
   */
  public static Calendar getCalendarInstance() {
    Calendar calendar = Calendar.getInstance();
    calendar.setLenient(false);
    return calendar;
  }

  /**
   * Returns the yesterday's date.
   *
   * @return the yesterday's date
   */
  public static Date getYesterdayDate() {
    Calendar calendar = getCalendarInstance();
    calendar.add(Calendar.DATE, -1);
    return calendar.getTime();
  }

  /**
   * Returns today's date.
   *
   * @return today's date
   */
  public static Date getTodayDate() {
    return getCalendarInstance().getTime();
  }

  /**
   * Returns the number of days between two dates in terms of absolute days.
   *
   * @param date1 first date
   * @param date2 second date
   * @return the number of days between the 2 dates
   */
  public static long absoluteDaysBetweenDates(Date date1, Date date2) {
    long difference = Math.abs(date1.getTime() - date2.getTime());
    return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
  }

  /**
   * Given a set of stocks, returns a Map of stock to its percentage. The percentage will be divided
   * equally amongst the given stocks.
   *
   * @param tickerNames the set of stocks
   * @return the Map of stock and its corresponding percentage
   */
  public static Map<String, Double> getStocksWithWeights(Set<String> tickerNames) {
    double weight = 100.0 / tickerNames.size();
    return tickerNames.stream().collect(Collectors.toMap(stock -> stock, stock -> weight));
  }
}
