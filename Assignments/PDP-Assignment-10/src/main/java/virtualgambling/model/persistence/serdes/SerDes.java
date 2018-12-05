package virtualgambling.model.persistence.serdes;

import java.io.IOException;

public interface SerDes<T> {
  void serialize(T data) throws IOException;

  T deserialize() throws IOException;
}
