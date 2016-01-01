package gcx.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CoordJsonSerializer extends JsonSerializer<Coord> {

    @Override
    public void serialize(Coord value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray(2);
        jgen.writeNumber(value._lat);
        jgen.writeNumber(value._long);
        jgen.writeEndArray();
    }
}
