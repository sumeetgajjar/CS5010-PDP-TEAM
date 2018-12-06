package util;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.strategy.OneTimeWeightedInvestmentStrategy;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;

/**
 * This class represents a Constants class. It contains various Constants that can be used by any
 * class.
 */
public class Constants {

  public static final Type PORTFOLIO_TYPE = new TypeToken<Portfolio>() {}.getType();

  public static final Type RECURRING_STRATEGY_TYPE =
          new TypeToken<RecurringWeightedInvestmentStrategy>() {}.getType();
  public static final Type ONETIME_STRATEGY_TYPE =
          new TypeToken<OneTimeWeightedInvestmentStrategy>() {}.getType();

  public static final String INVESTMENT_AMOUNT_MESSAGE = "Please enter the amount to invest";

  public static final String RECURRING_INVESTMENT_AMOUNT_MESSAGE =
          "Please enter the amount to invest in each recurring transaction";

  public static final String RECURRING_INTERVAL_MESSAGE = "Please the recurring interval(in days)";

  public static final String COMMISSION_MESSAGE =
          "Please enter the commission percentage per transaction";

  public static final String STOCK_PERCENTAGE_MESSAGE = "Please enter the percentage of the stock";

  public static final String STOCK_COUNT_MESSAGE = "Please enter the count of ticker names";

  public static final String INVESTMENT_DATE_MESSAGE = "Please enter the date for investment";

  public static final String END_DATE_MESSAGE =
          "Please enter the end date for recurring investment" +
                  "(Please press enter \"-\" if there is no end date)";

  public final static String START_DATE_MESSAGE =
          "Please enter the start date for recurring investment";

  public static final String PORTFOLIO_NAME_MESSAGE = "Please enter the portfolio name";

  public static final String STOCK_NAME_MESSAGE = "Please enter the stock name to purchase";

  public static final String SHARE_QUANTITY_MESSAGE =
          "Please enter the quantity of shares to purchase";

  public static final String COMMAND_NOT_FOUND_MESSAGE = "Command not found, please try again";

  public static final String INVALID_CHOICE_MESSAGE = "Invalid option, please enter valid option";

  public static final String STRATEGY_EXECUTION_EXCEPTION_MESSAGE = "Unable to buy even a single " +
          "stock";

  public static final String DEFAULT_PERSISTENCE_PATH = "Files";

  public static final String INVALID_INPUT = "Invalid input";

  public static final String PORTFOLIO_SUCCESSFULLY_LOADED_MESSAGE = "Portfolio Successfully " +
          "Loaded";

  public static final String LOAD_PORTFOLIO_FROM_FILE = "Enter the absolute path of " +
          "portfolio " +
          " file to load";

  public static final String PORTFOLIO_FILE_SAVE_NAME_MESSAGE = "Enter the absolute path of file " +
          "to save the portfolio";

  public static final String PORTFOLIO_SUCCESSFULLY_SAVED_MESSAGE = "Portfolio successfully saved";

  public static final String STRATEGY_SUCCESSFULLY_LOADED_MESSAGE = "Strategy Successfully " +
          "Loaded";

  public static final String LOAD_STRATEGY_FROM_FILE = "Enter the absolute path of " +
          "Strategy file to load";

  public static final String STRATEGY_FILE_SAVE_NAME_MESSAGE = "Enter the absolute path of file " +
          "to save the Strategy";

  public static final String STRATEGY_SUCCESSFULLY_SAVED_MESSAGE = "Strategy successfully saved";

  public static final String SELECT_STRATEGY_TO_CREATE_MESSAGE = "Enter 1 to create Recurrent " +
          "Strategy with equal weights for given Stocks"
          + System.lineSeparator()
          + "Enter 2 to create Recurrent strategy with different weights for given stocks";

  public static final String INVALID_OPTION_FOR_STRATEGY_CREATION_MESSAGE =
          "Invalid option, please enter either 1 or 2";
  public static final String ABSOLUTE_PATH_FILE_SAVE = "Enter the absolute path of file to save " +
          "the portfolio";

  public static final String PORTFOLIO_FILE_SAVE_SUCCESS = "Portfolio successfully saved";
}
