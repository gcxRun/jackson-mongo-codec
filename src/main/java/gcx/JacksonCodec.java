package gcx;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.io.IOException;


public class JacksonCodec<T> implements Codec<T> {

  private final Class<T> clazz;
  private final ObjectMapper objectMapper;

  /**
   * The objectMapper has to be created with a MongoBsonParser.
   * @param objectMapper a classic Jackson ObjectMapper
   * @param clazz the class
   */
  public JacksonCodec(ObjectMapper objectMapper,Class<T> clazz) {
    this.clazz = clazz;
    this.objectMapper = objectMapper;
  }

  /**
   * {@inheritDoc}
   */
  public T decode(BsonReader reader, DecoderContext decoderContext) {
    try {
      MongoBsonParser parser = new MongoBsonParser(reader);
      parser.setCodec(objectMapper.getFactory().getCodec());

      return objectMapper.readValue(parser, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
    try {
      MongoBsonGenerator generator = new MongoBsonGenerator(writer);
      generator.setCodec(objectMapper.getFactory().getCodec());

      objectMapper.writeValue(generator, value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Class<T> getEncoderClass() {
    return clazz;
  }
}
