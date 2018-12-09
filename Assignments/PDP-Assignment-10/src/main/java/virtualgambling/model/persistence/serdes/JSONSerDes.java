package virtualgambling.model.persistence.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import util.Utils;

/**
 * {@link JSONSerDes} is a generic serializer/deserializer that takes in the path to serialize to
 * and the {@link Type} of the object to serialize/deserialize.
 *
 * @param <T> the type of the object to serialize/deserialize
 */
public class JSONSerDes<T> implements SerDes<T> {
  private static final Gson GSON = new GsonBuilder()
          .excludeFieldsWithModifiers(Modifier.TRANSIENT)
          .setDateFormat("yyyy-MM-dd")
          .setPrettyPrinting()
          .create();
  private final Path path;
  private final Type instanceType;

  /**
   * Constructs a {@link JSONSerDes} with the given parameters.
   *
   * <p>It needs {@link Type} because gson requires it to work with generic types.
   *
   * @param path path of the input/output
   * @param type type of object that needs to be serializer/deserialize.
   */
  public JSONSerDes(Path path, Type type) throws IOException {
    this.path = Utils.requireNonNull(path);
    if (Objects.nonNull(this.path.getParent()) && Files.notExists(this.path.getParent())) {
      Files.createDirectory(this.path.getParent());
    }
    this.instanceType = Utils.requireNonNull(type);
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
