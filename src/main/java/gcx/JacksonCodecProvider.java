package gcx;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class JacksonCodecProvider implements CodecProvider {
  private final ObjectMapper objectMapper;

  public JacksonCodecProvider(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
    return new JacksonCodec<T>(objectMapper, registry, clazz);
  }
}

