package virtualgambling.model.stockdatasource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

import util.Utils;
import virtualgambling.model.bean.StockPrice;
import virtualgambling.model.exceptions.APILimitExceededException;
import virtualgambling.model.exceptions.RetryException;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * {@link AlphaVantageAPIStockDataSource} is a {@link StockDataSource} that fetches stock data from
 * the Alpha Vantage API.
 *
 * <p>This implementation is a singleton and the only way to get an instance of this data source
 * is to use the {@link AlphaVantageAPIStockDataSource#getInstance()} method.
 *
 * <p>The stock price that is returned is the closing price of the stock for a given day.
 */
public class AlphaVantageAPIStockDataSource implements StockDataSource {

  private static final List<String> API_KEYS = Arrays.asList(
          "AOHAEN4D9GAK0UA3",
          "K9IVD1LFU0ADK7DM",
          "GAS2GCX0FS3P1W2Q",
          "5BRAHEABQTTA26CY",
          "PUWAH0GSXGEZAGYR",
          "9CHRB2XSF3MPTUS4",
          "P5JQNGLDVSKL2NC8",
          "R5GZNGO7Z7RCI2YS",
          "4ZJVHW1GTDQUUP6H",
          "98GZ1P3QGTJ9AN5F",
          "7C6VFPULZK6DO30H"
  );

  private static final Utils.LRUCache<String, NavigableMap<String, StockPrice>> LRU_CACHE =
          new Utils.LRUCache<>(20);
  private static final Random RANDOM = new Random();
  private static final String DISK_CACHE_ROOT_PATH = "StocksPriceCache";

  private static AlphaVantageAPIStockDataSource HOLDER;

  private int apiKeyIndex = RANDOM.nextInt(API_KEYS.size());

  private Utils.BiFunctionRetryer<String, Date, StockPrice> biFunctionRetryer =
          new Utils.BiFunctionRetryer.RetryerBuilder<>(this::execute)
                  .setNumRetries(10)
                  .setBackOffSeconds(1)
                  .setExceptionClass(APILimitExceededException.class)
                  .createRetryer();

  private AlphaVantageAPIStockDataSource() {

  }

  @Override
  public StockPrice getPrice(String tickerName, Date date) throws StockDataNotFoundException,
          RetryException {
    try {
      return biFunctionRetryer.retry(tickerName, date);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private StockPrice execute(String tickerName, Date date) {
    String dateString = Utils.getDefaultFormattedDateStringFromDate(Utils.removeTimeFromDate(date));
    if (Utils.isFutureDate(date)) {
      throw new StockDataNotFoundException(String.format("Stock Data Not found for: %s for %s",
              tickerName, dateString));
    }

    StockPrice stockPrice = getDataFromLruCache(tickerName, dateString);
    if (Objects.nonNull(stockPrice)) {
      return stockPrice;
    }

    setAPIKeyIndex();

    if (addDataToLRUCacheIfAvailable(tickerName, dateString)) {
      NavigableMap<String, StockPrice> dateToBigDecimalMap = LRU_CACHE.get(tickerName);
      if (Objects.nonNull(dateToBigDecimalMap)) {
        Map.Entry<String, StockPrice> dateToPriceEntry =
                dateToBigDecimalMap.ceilingEntry(dateString);
        if (Objects.isNull(dateToPriceEntry) || Objects.isNull(dateToPriceEntry.getValue())) {
          throw new StockDataNotFoundException(String.format("Stock Data Not found for: %s for %s",
                  tickerName, dateString));
        }
        return dateToPriceEntry.getValue();
      }
    }
    throw new StockDataNotFoundException(String.format("Stock Data Not found for: %s for %s",
            tickerName, dateString));
  }

  private void setAPIKeyIndex() {
    apiKeyIndex = RANDOM.nextInt(API_KEYS.size());
  }

  private boolean addDataToLRUCacheIfAvailable(String tickerName, String dateString) {
    try {
      if (addDataToLRUCacheFromDisk(tickerName, dateString)) {
        return true;
      }
    } catch (IOException ignored) {
    }

    return addDataToLRUCacheFromAPI(tickerName, dateString);
  }

  private StockPrice getDataFromLruCache(String tickerName, String dateString) {
    if (LRU_CACHE.containsKey(tickerName)) {
      NavigableMap<String, StockPrice> timeStampMap = LRU_CACHE.get(tickerName);
      if (timeStampMap.containsKey(dateString)) {
        return timeStampMap.get(dateString);
      }
    }
    return null;
  }

  private boolean addDataToLRUCacheFromDisk(String tickerName, String dateString)
          throws IOException {
    Path cachePath = getCacheFolderPath(tickerName);
    if (Files.isDirectory(cachePath)) {
      Path cacheFilePath = getCacheFilePath(tickerName);
      if (Files.exists(cacheFilePath)) {
        NavigableMap<String, StockPrice> timeStampMap = readDataFromDisk(cacheFilePath);
        Map.Entry<String, StockPrice> lastEntry = timeStampMap.lastEntry();
        Map.Entry<String, StockPrice> firstEntry = timeStampMap.firstEntry();
        if (Objects.nonNull(firstEntry) && Objects.nonNull(lastEntry)) {
          if (lastEntry.getKey().compareTo(dateString) >= 0 &&
                  firstEntry.getKey().compareTo(dateString) <= 0) {
            addToLruCache(tickerName, timeStampMap);
            return true;

          }
        }
      }
    }
    return false;
  }

  private Path getCacheFilePath(String tickerName) {
    return Paths.get(DISK_CACHE_ROOT_PATH, tickerName, "data.csv");
  }

  private NavigableMap<String, StockPrice> readDataFromDisk(Path cacheFilePath) throws IOException {
    File file = new File(cacheFilePath.toUri());
    NavigableMap<String, StockPrice> timeStampMap = new TreeMap<>();
    try (BufferedReader reader =
                 new BufferedReader(
                         new InputStreamReader(
                                 new FileInputStream(
                                         file)))) {

      String header = reader.readLine();

      String temp;
      while (Objects.nonNull((temp = reader.readLine()))) {
        try {
          parseAndUpdateMap(temp, timeStampMap);
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return timeStampMap;
  }

  private Path getCacheFolderPath(String tickerName) {
    return Paths.get(DISK_CACHE_ROOT_PATH, tickerName);
  }

  private boolean addDataToLRUCacheFromAPI(String tickerName, String dateString) {

    try {
      NavigableMap<String, StockPrice> timeStampMap = queryApi(tickerName);
      Map.Entry<String, StockPrice> lastEntry = timeStampMap.lastEntry();
      Map.Entry<String, StockPrice> firstEntry = timeStampMap.firstEntry();

      if (Objects.nonNull(firstEntry) && Objects.nonNull(lastEntry)) {
        if (lastEntry.getKey().compareTo(dateString) >= 0 &&
                firstEntry.getKey().compareTo(dateString) <= 0) {
          addToLruCache(tickerName, timeStampMap);
          return true;

        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  private void addToLruCache(String tickerName,
                             NavigableMap<String, StockPrice> timeStampMap) {
    LRU_CACHE.put(tickerName, timeStampMap);
  }

  private NavigableMap<String, StockPrice> queryApi(String tickerName) throws IOException {
    URL url = getUrl(tickerName);

    NavigableMap<String, StockPrice> dateToPriceMap = new TreeMap<>();
    try (BufferedReader reader =
                 new BufferedReader(
                         new InputStreamReader(
                                 url.openStream()))) {

      File cacheFile = getCacheFile(tickerName);
      try (BufferedWriter writer = new BufferedWriter(
              new OutputStreamWriter(
                      new FileOutputStream(cacheFile)))) {

        String header = reader.readLine();
        this.isValidResponse(header, reader);

        writer.write(header);
        writer.newLine();

        String temp;
        while (Objects.nonNull((temp = reader.readLine()))) {
          parseAndUpdateMap(temp, dateToPriceMap);

          writer.write(temp);
          writer.newLine();
        }
      }
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }

    return dateToPriceMap;
  }

  private File getCacheFile(String tickerName) throws IOException {
    if (!Files.exists(Paths.get(DISK_CACHE_ROOT_PATH))) {
      Files.createDirectory(Paths.get(DISK_CACHE_ROOT_PATH));
    }
    Path cacheFolderPath = getCacheFolderPath(tickerName);
    if (!Files.exists(cacheFolderPath)) {
      Files.createDirectory(cacheFolderPath);
    }
    return new File(getCacheFilePath(tickerName).toUri());
  }

  private void parseAndUpdateMap(String temp, Map<String, StockPrice> timestampMap)
          throws ParseException {
    String[] split = temp.split(",");
    String timeStamp = split[0];
    String closingPrice = split[4];
    timestampMap.put(timeStamp,
            new StockPrice(new BigDecimal(closingPrice),
                    Utils.getDateFromDefaultFormattedDateString(timeStamp)
            ));
  }

  private void isValidResponse(String header, BufferedReader reader) throws IOException {
    if (header.equalsIgnoreCase("{")) {
      String message = reader.readLine();
      if (message.contains("Note")) {
        throw new APILimitExceededException(String.format("API Limit exceeded for key" +
                        " %s: %s",
                this.getApiKey(),
                message));
      }
      if (message.contains("Error")) {
        throw new StockDataNotFoundException("Stock Data Not found");
      }
      throw new RuntimeException(String.format("Invalid Api Call: %s", message));
    }
  }

  private URL getUrl(String tickerName) throws MalformedURLException {
    return new ApiUrlBuilder()
            .setApiKey(this.getApiKey())
            .setSymbol(tickerName)
            .build();
  }

  private String getApiKey() {
    return API_KEYS.get(this.apiKeyIndex++ % API_KEYS.size());
  }

  /**
   * Returns the Singleton instance of AlphaVantageAPIStockDataSource class.
   *
   * @return the Singleton instance of AlphaVantageAPIStockDataSource class.
   */
  public static AlphaVantageAPIStockDataSource getInstance() {
    if (Objects.isNull(HOLDER)) {
      synchronized (AlphaVantageAPIStockDataSource.class) {
        if (Objects.isNull(HOLDER)) {
          HOLDER = new AlphaVantageAPIStockDataSource();
        }
      }
    }
    return HOLDER;
  }


  private static class ApiUrlBuilder {
    private static final String BASE_URL = "https://www.alphavantage.co/query?";

    private String symbol;
    private String function;
    private String outputSize;
    private String apiKey;
    private String responseType;

    public ApiUrlBuilder() {
      this.function = "TIME_SERIES_DAILY";
      this.outputSize = "full";
      this.responseType = "csv";
    }

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
