package gcx;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.bson.BsonReader;
import org.bson.BsonWriter;

/**
 * Created by greg on 29/12/15.
 */
public class CustomBsonFactory extends JsonFactory
{

    public JsonParser createParser(BsonReader reader) {
        CustomBsonParser parser = new CustomBsonParser(reader);
        parser.setCodec(this.getCodec());
        return parser;
    }

    public JsonGenerator createGenerator(BsonWriter writer) {
        CustomBsonGenerator generator = new CustomBsonGenerator(writer);
        generator.setCodec(this.getCodec());
        return generator;
    }
}
