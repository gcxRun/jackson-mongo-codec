package gcx.objectid;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcx.CustomBsonFactory;
import org.bson.BsonReader;
import org.bson.BsonWriter;

import java.io.IOException;

/**
 * Created by greg on 29/12/15.
 */
public class MongoObjectMapper extends ObjectMapper {



    public MongoObjectMapper(){
        super(new CustomBsonFactory());
    }

    @SuppressWarnings("unchecked")
    public <T> T readValue(BsonReader reader , Class<T> valueType) throws IOException {
        CustomBsonFactory bsonFactory = (CustomBsonFactory)_jsonFactory;
        return (T) _readMapAndClose(bsonFactory.createParser(reader), _typeFactory.constructType(valueType));
    }

    public <T> void writeValue(BsonWriter writer, T value) throws IOException {
        CustomBsonFactory bsonFactory = (CustomBsonFactory)_jsonFactory;
        _configAndWriteValue(bsonFactory.createGenerator(writer), value);

    }
}
