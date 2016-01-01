package gcx;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;


public class JacksonCodec<T> implements Codec<T> {

  private final Class<T> clazz;
  private final ObjectMapper objectMapper;
  private final JsonFactory jsonFactory;

  /**
   * The objectMapper has to be created with a CustomBsonParser.
   * @param objectMapper a classic Jackson ObjectMapper
   * @param codecRegistry a Mongo code registry
   * @param clazz the class
   */
  public JacksonCodec(ObjectMapper objectMapper, CodecRegistry codecRegistry, Class<T> clazz) {
    this.clazz = clazz;
    this.objectMapper = objectMapper;
    this.jsonFactory = objectMapper.getFactory();
  }

  /**
   * {@inheritDoc}
   */
  public T decode(BsonReader reader, DecoderContext decoderContext) {
    try {
      CustomBsonParser parser = new CustomBsonParser(reader);
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
      CustomBsonGenerator generator = new CustomBsonGenerator(writer);
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
