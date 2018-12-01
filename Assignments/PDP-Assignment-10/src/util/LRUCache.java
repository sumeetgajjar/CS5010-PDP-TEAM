package util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents a LRU Key value cache of Generic Type. It extends {@link LinkedHashMap}
 * class.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

  private final int cacheCapacity;

  /**
   * Create an object of LRU Cache with the given capacity.
   *
   * <p>It will start removing older entries once the cache capacity is hit.
   *
   * @param cacheCapacity the capacity of the cache
   */
  public LRUCache(int cacheCapacity) {
    this.cacheCapacity = cacheCapacity;
  }

  /**
   * Returns true if the current size of the cache is greater than it's capacity.
   *
   * @return true if the current size of the cache is greater than it's capacity
   */
  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return this.size() > cacheCapacity;
  }
}
