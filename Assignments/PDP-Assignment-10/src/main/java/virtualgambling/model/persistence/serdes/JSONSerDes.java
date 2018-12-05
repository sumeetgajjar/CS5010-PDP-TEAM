package virtualgambling.model.persistence.serdes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;

import util.Utils;

public class JSONSerDes<T> implements SerDes<T> {
  private static final Gson GSON = new Gson();
  private final Path path;

  public JSONSerDes(Path path) {
    this.path = path;
  }


  @Override
  public void serialize(T data) throws IOException {
    String json = GSON.toJson(data);
    Utils.saveToFile(path, json);
  }

  @Override
  public T deserialize() throws IOException {
    Type instanceType = new TypeToken<T>() {
    }.getType();
    String json = Utils.readStringFromFile(path);
    return GSON.fromJson(json, instanceType);
  }
}
