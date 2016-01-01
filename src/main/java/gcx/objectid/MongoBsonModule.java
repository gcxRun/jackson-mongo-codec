package gcx.objectid;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Created by greg on 29/12/15.
 */
public class MongoBsonModule extends Module {

  @Override
  public String getModuleName() {
    return "MongoBsonModule";
  }

  @Override
  public Version version() {
    return new Version(0, 1, 0, "", "", "");
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addSerializers(new ObjectIdBsonSerializers());
    context.addDeserializers(new ObjectIdBsonDeserializers());
  }

  public static class ObjectIdBsonDeserializers extends SimpleDeserializers {

    public ObjectIdBsonDeserializers() {
      addDeserializer(ObjectId.class, new ObjectIdBsonDeserializer());
    }
  }

  public static class ObjectIdBsonSerializers extends SimpleSerializers {
    public ObjectIdBsonSerializers() {
      addSerializer(ObjectId.class, new ObjectIdBsonSerializer());
    }
  }

  public static class ObjectIdBsonSerializer extends JsonSerializer<ObjectId> {

    @Override
    public void serialize(ObjectId objectId,
                          JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {

      if (objectId == null) {
        provider.defaultSerializeNull(jgen);
      } else {
        jgen.writeObjectId(objectId);
      }
    }
  }

  public static class ObjectIdBsonDeserializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser jp,
                                DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

      Object object = jp.getObjectId();
      if (object == null) {
        return null;
      }
      if (object instanceof ObjectId) {
        return (ObjectId) object;
      } else {
        throw new JsonParseException("Bson objectId expected", jp.getCurrentLocation());
      }
    }
  }
}

