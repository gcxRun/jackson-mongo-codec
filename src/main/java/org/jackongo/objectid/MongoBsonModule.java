package org.jackongo.objectid;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
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

    private ObjectCodec codec;

    @Override
    public void serialize(ObjectId objectId,
                          JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {

      if (objectId == null) {
        provider.defaultSerializeNull(jgen);
      } else {
        //popObjectCodec(jgen);
        jgen.writeEmbeddedObject(objectId);
        //pushObjectCodec(jgen);
      }
    }

    private void popObjectCodec(JsonGenerator jgen) {
      this.codec = jgen.getCodec();
      jgen.setCodec(null);
    }
    private void pushObjectCodec(JsonGenerator jgen) {
      jgen.setCodec(codec);
    }
  }

  public static class ObjectIdBsonDeserializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser jp,
                                DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

      Object object = jp.getEmbeddedObject();
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

