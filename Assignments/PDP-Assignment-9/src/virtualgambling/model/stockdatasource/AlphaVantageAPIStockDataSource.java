package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 1:08 PM, 11/25/18
 */
public class AlphaVantageAPIStockDataSource implements StockDataSource {

  private static final List<String> API_KEYS = Arrays.asList(
          "AOHAEN4D9GAK0UA3"
  );

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    return null;
  }

  private static class ApiUrlBuilder {
    private static final String BASE_URL = "https://www.alphavantage.co/query?";

    private String symbol;
    private String function;
    private String outputSize;
    private String apiKey;
    private String responseType;

    public ApiUrlBuilder setSymbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    public ApiUrlBuilder setFunction(String function) {
      this.function = function;
      return this;
    }

    public ApiUrlBuilder setOutputSize(String outputSize) {
      this.outputSize = outputSize;
      return this;
    }

    public ApiUrlBuilder setApiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public ApiUrlBuilder setResponseType(String responseType) {
      this.responseType = responseType;
      return this;
    }

    public URL build() throws MalformedURLException {
      return new URL(String
              .format("%sfunction=%s&outputsize=%s&symbol=%s&apikey=%s&datatype=%s",
                      BASE_URL,
                      this.function,
                      this.outputSize,
                      this.symbol,
                      this.apiKey,
                      this.responseType
              ));
    }
  }
}
