package gcx;

import gcx.objectid.MongoObjectMapper;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.RawBsonDocument;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;


public class JacksonCodec<T> implements Codec<T> {

    private final Class<T> clazz;
    private final MongoObjectMapper objectMapper;
    private final Codec<RawBsonDocument> rawBsonDocumentCodec;

    public JacksonCodec(MongoObjectMapper objectMapper, CodecRegistry codecRegistry, Class<T> clazz) {
        this.clazz = clazz;
        this.objectMapper = objectMapper;
        this.rawBsonDocumentCodec = codecRegistry.get(RawBsonDocument.class);
    }

    public T decode(BsonReader reader, DecoderContext decoderContext) {
        try {
            return objectMapper.readValue(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        try {
            objectMapper.writeValue(writer, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getEncoderClass() {
        return clazz;
    }
}
