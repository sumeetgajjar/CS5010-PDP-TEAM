package virtualgambling.model.persistence.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.file.Path;

import util.Utils;

public class JSONSerDes<T> implements SerDes<T> {
  private static final Gson GSON =
          new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
  private final Path path;
  private Type instanceType;

  public JSONSerDes(Path path, Type type) {
    this.path = Utils.requireNonNull(path);
//    this.instanceType = instanceType;
    this.instanceType = type;
  }


  @Override
  public void serialize(T data) throws IOException {
    String json = GSON.toJson(data);
    Utils.saveToFile(path, json);
  }

  @Override
  public T deserialize() throws IOException {
    String json = Utils.readStringFromFile(path);
    return GSON.fromJson(json, instanceType);
  }
}
