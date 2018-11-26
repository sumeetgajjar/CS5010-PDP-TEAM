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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import util.LRUCache;
import util.Utils;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 1:08 PM, 11/25/18
 */
public class AlphaVantageAPIStockDataSource implements StockDataSource {

  private static final List<String> API_KEYS = Arrays.asList(
          "AOHAEN4D9GAK0UA3"
  );

  private static final LRUCache<String, Map<String, BigDecimal>> LRU_CACHE = new LRUCache<>(2);
  private static final String DISK_CACHE_ROOT_PATH = "StocksPriceCache";

  private static AlphaVantageAPIStockDataSource HOLDER;

  private int apiKeyIndex = 0;

  private AlphaVantageAPIStockDataSource() {

  }

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    date = Utils.removeTimeFromDate(date);

    BigDecimal stockPrice = getDataFromLruCache(tickerName, date);
    if (Objects.nonNull(stockPrice)) {
      return stockPrice;
    }

    try {
      stockPrice = getPriceFromDisk(tickerName, date);
      if (Objects.nonNull(stockPrice)) {
        return stockPrice;
      }
    } catch (IOException ignored) {
    }

    return getPriceFromApi(tickerName, date);
  }

  private BigDecimal getDataFromLruCache(String tickerName, Date date) {
    String dateString = Utils.getDefaultFormattedDateStringFromDate(date);
    if (LRU_CACHE.containsKey(tickerName)) {
      Map<String, BigDecimal> timeStampMap = LRU_CACHE.get(tickerName);
      if (timeStampMap.containsKey(dateString)) {
        return timeStampMap.get(dateString);
      }
    }
    return null;
  }

  private BigDecimal getPriceFromDisk(String tickerName, Date date) throws IOException {
    Path cachePath = getCacheFolderPath(tickerName);
    if (Files.isDirectory(cachePath)) {
      Path cacheFilePath = getCacheFilePath(tickerName);
      if (Files.exists(cacheFilePath)) {
        Map<String, BigDecimal> timeStampMap = readDataFromDisk(cacheFilePath);
        String dateString = Utils.getDefaultFormattedDateStringFromDate(date);
        if (timeStampMap.containsKey(dateString)) {
          addToLruCache(tickerName, timeStampMap);
          return timeStampMap.get(dateString);
        }
      }
    }

    return null;
  }

  private Path getCacheFilePath(String tickerName) {
    return Paths.get(DISK_CACHE_ROOT_PATH, tickerName, "data.csv");
  }

  private Map<String, BigDecimal> readDataFromDisk(Path cacheFilePath) throws IOException {
    File file = new File(cacheFilePath.toUri());
    Map<String, BigDecimal> timeStampMap = new HashMap<>(2000);
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

  private BigDecimal getPriceFromApi(String tickerName, Date date) {
    String dateString = Utils.getDefaultFormattedDateStringFromDate(date);

    try {
      Map<String, BigDecimal> timeStampMap = queryApi(tickerName);
      addToLruCache(tickerName, timeStampMap);

      BigDecimal stockPrice = timeStampMap.get(dateString);
      if (Objects.isNull(stockPrice)) {
        throw new StockDataNotFoundException(String.format("Stock Data Not found for: %s for %s",
                tickerName, Utils.getDefaultFormattedDateStringFromDate(date)));

      }
      return stockPrice;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<String, BigDecimal> addToLruCache(String tickerName,
                                                Map<String, BigDecimal> timeStampMap) {
    return LRU_CACHE.put(tickerName, timeStampMap);
  }

  public static void main(String[] args) throws IOException, ParseException {
    Date date = Utils.getDateFromDefaultFormattedDateString("2018" +
            "-11-23");

    AlphaVantageAPIStockDataSource dataSource = AlphaVantageAPIStockDataSource.getInstance();
    System.out.println(dataSource.getPrice("GOOG", date));
    System.out.println(dataSource.getPrice("GOOG", date));
    System.out.println(dataSource.getPrice("T", date));
    System.out.println(dataSource.getPrice("AAPL", date));
    System.out.println(dataSource.getPrice("ASC", date));
    System.out.println(dataSource.getPrice("GOOG", date));
  }

  private Map<String, BigDecimal> queryApi(String tickerName) throws IOException {
    URL url = getUrl(tickerName);

    Map<String, BigDecimal> dateToPriceMap = new HashMap<>(2000);
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

  private File getCacheFile(String tickerName) {
    Path cacheFolderPath = getCacheFolderPath(tickerName);
    if (!Files.exists(cacheFolderPath)) {
      File file = new File(cacheFolderPath.toUri());
      file.mkdir();
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
        throw new RuntimeException(String.format("API Limit exceeded: %s", message));
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
