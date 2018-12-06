package virtualgambling.model.persistence;

import java.io.IOException;

import virtualgambling.model.exceptions.PersistenceException;

public interface Loader {
  void load() throws IOException, PersistenceException;
}
