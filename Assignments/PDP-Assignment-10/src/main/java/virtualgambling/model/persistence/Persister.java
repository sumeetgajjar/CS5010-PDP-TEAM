package virtualgambling.model.persistence;

import java.io.IOException;

/**
 * {@link Persister} represents a single-method persistence operation that takes data from the host
 * model and persists it.
 *
 * <p>It represents a marker interface that signifies that the method provides the behaviour of
 * persisting data. Individual implementations will persist in different manner - file or any other
 * outputStreams.
 */
public interface Persister {
  /**
   * Persists data from the host model into an implementation specific target format.
   *
   * @throws IOException if IO operation fails
   */
  void persist() throws IOException;
}
