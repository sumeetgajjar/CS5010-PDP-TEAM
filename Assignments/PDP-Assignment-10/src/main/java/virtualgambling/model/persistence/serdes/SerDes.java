package virtualgambling.model.persistence.serdes;

import java.io.IOException;

/**
 * {@link SerDes} is a generic serializer/deserializer that serializes given data and can
 * deserialize a data of type T.
 *
 * @param <T> the type of object to serialize/deserialize.
 */
public interface SerDes<T> {
  /**
   * This takes in the data to be serialized and serializes it - it might optionally throw an
   * exception if it cannot perform IO.
   *
   * @param data the data to be serialized
   * @throws IOException the exception to be thrown when IO cannot be performed.
   */
  void serialize(T data) throws IOException;

  /**
   * This method deserializes an object of type T and returns it from a source that needs to given
   * during a concrete instance construction.
   *
   * @return the object of type T that has been deserialized
   * @throws IOException if the object cannot be deserialized
   */
  T deserialize() throws IOException;
}
