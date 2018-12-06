package virtualgambling.model.persistence;

import java.io.IOException;

import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.exceptions.PersistenceException;

/**
 * {@link Loader} represents a single-method loading operation that loads data into the host that
 * uses the loader.
 *
 * <p>It represents a marker interface that signifies that the method provides the behaviour of
 * loading data.
 */
public interface Loader {
  /**
   * Loads data into the host model that uses this loader.
   *
   * @throws IOException          if an IO operation fails
   * @throws PersistenceException if persistence operation other than IO operation fails
   * @param persistableUserModel
   */
  void load(PersistableUserModel persistableUserModel) throws IOException, PersistenceException;
}
