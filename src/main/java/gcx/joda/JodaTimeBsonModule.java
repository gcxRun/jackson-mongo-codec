package gcx.joda;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
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

    static public class JodaTimeBsonDeserializers extends SimpleDeserializers {

        public JodaTimeBsonDeserializers() {
            addDeserializer(DateTime.class, new DateTimeBsonDeserializer());
        }
    }

    static public class JodaTimeBsonSerializers extends SimpleSerializers {
        public JodaTimeBsonSerializers() {
            addSerializer(DateTime.class, new DateTimeBsonSerializer());
        }
    }

    static public class DateTimeBsonSerializer extends JsonSerializer<DateTime> {

        @Override
        public void serialize(DateTime jodaTime, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeObject(jodaTime.getMillis());
        }
    }

    static public class DateTimeBsonDeserializer extends JsonDeserializer<DateTime> {

        @Override
        public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            long ts = jp.getLongValue();
            return new DateTime(ts, DateTimeZone.UTC);
        }
    }
}
