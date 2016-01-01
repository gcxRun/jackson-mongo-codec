package gcx.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CoordJsonDeserializer extends JsonDeserializer<Coord> {
    @Override
    public Coord deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Coord coord = new Coord();

        JsonToken token = jp.nextToken();
        coord._lat = jp.getNumberValue().doubleValue();
        token = jp.nextToken();
        coord._long = jp.getNumberValue().doubleValue();

        token = jp.nextToken();//close array

        return coord;
    }
}
