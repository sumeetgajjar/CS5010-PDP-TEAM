package virtualgambling.model.persistence.serdes;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.nio.file.Path;

import util.Utils;

public class JSONSerDes<T> implements SerDes<T> {
  private static final Genson GSON = new GensonBuilder()
          .useRuntimeType(true)
          .useConstructorWithArguments(true)
          .create();

  private final Path path;
  private GenericType<T> instanceType;

  public JSONSerDes(Path path, GenericType<T> genericType) {
    this.path = Utils.requireNonNull(path);
//    this.instanceType = instanceType;
    this.instanceType = genericType;
  }


  @Override
  public void serialize(T data) throws IOException {
    String json = GSON.serialize(data);
    Utils.saveToFile(path, json);
  }

  @Override
  public T deserialize() throws IOException {
    String json = Utils.readStringFromFile(path);
    return GSON.deserialize(json, instanceType);
  }
}
