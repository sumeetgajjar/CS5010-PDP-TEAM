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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

import util.LRUCache;
import util.Utils;
import virtualgambling.model.exceptions.AlphaVantageAPILimitExceeded;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * {@link AlphaVantageAPIStockDataSource} is a {@link StockDataSource} that fetches stock data from
 * the Alpha Vantage API.
 *
 * <p> This implementation is a singleton and the only way to get an instance of this data source
 * is to use the {@link AlphaVantageAPIStockDataSource#getInstance()} method.
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

  private static final LRUCache<String, NavigableMap<String, BigDecimal>> LRU_CACHE =
          new LRUCache<>(2);
  private static final Random RANDOM = new Random();
  private static final String DISK_CACHE_ROOT_PATH = "StocksPriceCache";

  private static AlphaVantageAPIStockDataSource HOLDER;

  private int apiKeyIndex = RANDOM.nextInt(API_KEYS.size());

  private AlphaVantageAPIStockDataSource() {

  }

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    String dateString = Utils.getDefaultFormattedDateStringFromDate(Utils.removeTimeFromDate(date));

    BigDecimal stockPrice = getDataFromLruCache(tickerName, dateString);
    if (Objects.nonNull(stockPrice)) {
      return stockPrice;
    }

    if (addDataToLRUCacheIfAvailable(tickerName, dateString)) {
      return LRU_CACHE.get(tickerName).get(dateString);
    } else {
      NavigableMap<String, BigDecimal> dateToBigDecimalMap = LRU_CACHE.get(tickerName);
      if (Objects.nonNull(dateToBigDecimalMap)) {
        Map.Entry<String, BigDecimal> dateToPriceEntry =
                dateToBigDecimalMap.ceilingEntry(dateString);
        if (Objects.isNull(dateToPriceEntry) || Objects.isNull(dateToPriceEntry.getValue())) {
          throw new StockDataNotFoundException(String.format("Stock Data Not found for: %s for %s",
                  tickerName, dateString));
        }
        return dateToPriceEntry.getValue();

      }
      throw new StockDataNotFoundException(String.format("Stock Data Not found for: %s for %s",
              tickerName, dateString));
    }
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

  private BigDecimal getDataFromLruCache(String tickerName, String dateString) {
    if (LRU_CACHE.containsKey(tickerName)) {
      NavigableMap<String, BigDecimal> timeStampMap = LRU_CACHE.get(tickerName);
      if (timeStampMap.containsKey(dateString)) {
        return timeStampMap.get(dateString);
      }
    }
    return null;
  }

  private boolean addDataToLRUCacheFromDisk(String tickerName, String dateString) throws IOException {
    Path cachePath = getCacheFolderPath(tickerName);
    if (Files.isDirectory(cachePath)) {
      Path cacheFilePath = getCacheFilePath(tickerName);
      if (Files.exists(cacheFilePath)) {
        NavigableMap<String, BigDecimal> timeStampMap = readDataFromDisk(cacheFilePath);
        if (timeStampMap.containsKey(dateString)) {
          addToLruCache(tickerName, timeStampMap);
          return true;
        }
      }
    }

    return false;
  }

  private Path getCacheFilePath(String tickerName) {
    return Paths.get(DISK_CACHE_ROOT_PATH, tickerName, "data.csv");
  }

  private NavigableMap<String, BigDecimal> readDataFromDisk(Path cacheFilePath) throws IOException {
    File file = new File(cacheFilePath.toUri());
    NavigableMap<String, BigDecimal> timeStampMap = new TreeMap<>();
    try (BufferedReader reader =
                 new BufferedReader(
                         new InputStreamReader(
                                 new FileInputStream(
                                         file)))) {

      String header = reader.readLine();

      String temp;
      while (Objects.nonNull((temp = reader.readLine()))) {
        parseAndUpdateMap(temp, timeStampMap);
      }
    }
    return timeStampMap;
  }

  private Path getCacheFolderPath(String tickerName) {
    return Paths.get(DISK_CACHE_ROOT_PATH, tickerName);
  }

  private boolean addDataToLRUCacheFromAPI(String tickerName, String dateString) {

    try {
      NavigableMap<String, BigDecimal> timeStampMap = queryApi(tickerName);
      addToLruCache(tickerName, timeStampMap);

      BigDecimal stockPrice = timeStampMap.get(dateString);
      return !Objects.isNull(stockPrice);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void addToLruCache(String tickerName,
                             NavigableMap<String, BigDecimal> timeStampMap) {
    LRU_CACHE.put(tickerName, timeStampMap);
  }

  private NavigableMap<String, BigDecimal> queryApi(String tickerName) throws IOException {
    URL url = getUrl(tickerName);

    NavigableMap<String, BigDecimal> dateToPriceMap = new TreeMap<>();
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
    } catch (IOException e) {
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

  private void parseAndUpdateMap(String temp, Map<String, BigDecimal> timestampMap) {
    String[] split = temp.split(",");
    String timeStamp = split[0];
    String closingPrice = split[4];
    timestampMap.put(timeStamp, new BigDecimal(closingPrice));
  }

  private void isValidResponse(String header, BufferedReader reader) throws IOException {
    if (header.equalsIgnoreCase("{")) {
      String message = reader.readLine();
      if (message.contains("Note")) {
        throw new AlphaVantageAPILimitExceeded(String.format("API Limit exceeded for key %s: %s",
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
