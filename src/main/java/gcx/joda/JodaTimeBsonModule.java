package gcx.joda;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import org.bson.BsonDateTime;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;

public class JodaTimeBsonModule extends Module {
  @Override
  public String getModuleName() {
    return "JodaTimeBsonModule";
  }

  @Override
  public Version version() {
    return new Version(0, 1, 0, "", "", "");
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addSerializers(new JodaTimeBsonSerializers());
    context.addDeserializers(new JodaTimeBsonDeserializers());
  }

  public static class JodaTimeBsonDeserializers extends SimpleDeserializers {

    public JodaTimeBsonDeserializers() {
      addDeserializer(DateTime.class, new DateTimeBsonDeserializer());
    }
  }

  public static class JodaTimeBsonSerializers extends SimpleSerializers {
    public JodaTimeBsonSerializers() {
      addSerializer(DateTime.class, new DateTimeBsonSerializer());
    }
  }

  public static class DateTimeBsonSerializer extends JsonSerializer<DateTime> {

    private ObjectCodec codec;

    @Override
    public void serialize(DateTime jodaTime,
                          JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {
      long ts = jodaTime.getMillis();
      BsonDateTime dateTime = new BsonDateTime(ts);

      popObjectCodec(jgen);
      jgen.writeObject(dateTime);
      pushObjectCodec(jgen);
    }

    private void popObjectCodec(JsonGenerator jgen) {
      this.codec = jgen.getCodec();
      jgen.setCodec(null);
    }

    private void pushObjectCodec(JsonGenerator jgen) {
      jgen.setCodec(codec);
    }
  }


  public static class DateTimeBsonDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser jp,
                                DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
      Object object = jp.getEmbeddedObject();
      if (object instanceof BsonDateTime) {
        long ts =  ((BsonDateTime) object).getValue();
        return new DateTime(ts, DateTimeZone.UTC);
      } else {
        throw new JsonParseException("BsonDateTime expected", jp.getCurrentLocation());
      }
    }
  }
}
