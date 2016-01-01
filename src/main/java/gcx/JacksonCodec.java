package gcx;

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
    private final CustomBsonFactory bsonFactory;

    public JacksonCodec(ObjectMapper objectMapper, CodecRegistry codecRegistry, Class<T> clazz) {
        this.clazz = clazz;
        this.objectMapper = objectMapper;
        this.bsonFactory = (CustomBsonFactory)objectMapper.getFactory();//TODO add check

    }

    public T decode(BsonReader reader, DecoderContext decoderContext) {
        try {

            return objectMapper.readValue(bsonFactory.createParser(reader),clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        try {
            objectMapper.writeValue(bsonFactory.createGenerator(writer), value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getEncoderClass() {
        return clazz;
    }
}
