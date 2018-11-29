package util;

import virtualgambling.model.exceptions.RetryException;

/**
 * {@link BiFunctionRetryer} represents a retrying mechanism that allows a client to retry any
 * {@link java.util.function.BiFunction}
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
public class BiFunctionRetryer<T, U, R> {
  private final int numRetries;
  private final BiFunctionWithCheckedException<T, U, R> functionToRetry;
  private final Class<? extends Throwable> exceptionClass;

  private BiFunctionRetryer(
          int numRetries,
          BiFunctionWithCheckedException<T, U, R> functionToRetry,
          Class<? extends Throwable> exceptionClass) {
    this.numRetries = numRetries;
    this.functionToRetry = functionToRetry;
    this.exceptionClass = exceptionClass;
  }

  /**
   * Retry the function numEntries number of times.
   *
   * @param param1 param of type T
   * @param param2 param of type U
   * @return R after running {@link BiFunctionWithCheckedException} at most the number of times one
   * needs to retry.
   * @throws RuntimeException wraps any exception thrown by {@link BiFunctionWithCheckedException}
   *                          and throws it
   * @throws RetryException   in case it fails to get results after retrying numRetries times
   */
  public R retry(T param1, U param2) throws RuntimeException, RetryException {
    for (int i = 0; i < numRetries; i++) {
      try {
        return functionToRetry.apply(param1, param2);
      } catch (Throwable e) {
        if (!exceptionClass.isInstance(e)) {
          throw new RuntimeException(e);
        }
      }
    }
    throw new RetryException(String.format("failed to get results after retrying %s number of " +
            "times", numRetries));
  }

  @FunctionalInterface
  public interface BiFunctionWithCheckedException<T, U, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u) throws Exception;
  }

  /**
   * A Retryer Builder that builds a retryer.
   *
   * @param <T> the type of the first argument to the function
   * @param <U> the type of the second argument to the function
   * @param <R> the type of the result of the function
   */
  public static class RetryerBuilder<T, U, R> {
    private int numRetries;
    private BiFunctionWithCheckedException<T, U, R> functionToRetry;
    private Class<? extends Throwable> exceptionClass;

    public RetryerBuilder<T, U, R> setNumRetries(int numRetries) {
      this.numRetries = numRetries;
      return this;
    }

    public RetryerBuilder<T, U, R> setFunctionToRetry(
            BiFunctionWithCheckedException<T, U, R> functionToRetry) {
      this.functionToRetry = functionToRetry;
      return this;
    }

    public RetryerBuilder<T, U, R> setExceptionClass(Class<? extends Throwable> exceptionClass) {
      this.exceptionClass = exceptionClass;
      return this;
    }

    public BiFunctionRetryer<T, U, R> createRetryer() {
      return new BiFunctionRetryer<>(numRetries, functionToRetry, exceptionClass);
    }
  }
}