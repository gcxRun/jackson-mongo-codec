package gcx;

import gcx.objectid.MongoObjectMapper;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

class JacksonCodecProvider implements CodecProvider {
    private final MongoObjectMapper objectMapper;

    public JacksonCodecProvider(final MongoObjectMapper bsonObjectMapper) {
        this.objectMapper = bsonObjectMapper;
    }

    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        return new JacksonCodec<T>(objectMapper, registry, clazz);
    }
}

