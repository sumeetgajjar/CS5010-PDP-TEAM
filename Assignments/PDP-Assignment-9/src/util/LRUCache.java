package util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by gajjar.s, on 2:05 PM, 11/25/18
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

  private final int cacheSize;

  public LRUCache(int cacheSize) {
    this.cacheSize = cacheSize;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return this.size() > cacheSize;
  }
}
