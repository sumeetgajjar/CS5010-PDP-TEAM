package virtualgambling.model.persistence;

import java.io.IOException;

public interface Persister {
  void persist() throws IOException;
}
