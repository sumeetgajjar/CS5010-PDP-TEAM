package virtualgambling.model.stockdatasource;

import java.util.function.BiFunction;

public class Retryer<K, V, R> {
  private final int numRetries;
  private final BiFunction<K, V, R> functionToRetry;

  public Retryer(int numRetries, BiFunction<K, V, R> functionToRetry) {
    this.numRetries = numRetries;
    this.functionToRetry = functionToRetry;
  }

  public R retry(K param1, V param2) {
    for (int i = 0; i < numRetries; i++) {
      try {
        R r = functionToRetry.apply(param1, param2);
        return r;
      } catch (Exception e) {

      }
    }
    throw new RuntimeException("failed to retry");
  }
}
