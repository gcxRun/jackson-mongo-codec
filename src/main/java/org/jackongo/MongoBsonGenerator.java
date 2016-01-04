package org.jackongo;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;

import org.bson.BsonDateTime;
import org.bson.BsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by greg on 30/12/15.
 */
public class MongoBsonGenerator extends JsonGenerator {
  private final BsonWriter writer;
  private ObjectCodec codec;

  public MongoBsonGenerator(BsonWriter writer) {
    this.writer = writer;
  }

  @Override
  public JsonGenerator setCodec(ObjectCodec codec) {
    this.codec = codec;
    return this;
  }

  @Override
  public ObjectCodec getCodec() {
    return this.codec;
  }

  @Override
  public Version version() {
    return null;
  }

  @Override
  public JsonGenerator enable(Feature feature) {
    return this;
  }

  @Override
  public JsonGenerator disable(Feature feature) {
    return this;
  }

  @Override
  public boolean isEnabled(Feature feature) {
    return false;
  }

  @Override
  public int getFeatureMask() {
    return 0;
  }

  @Override
  public JsonGenerator setFeatureMask(int values) {
    return null;
  }

  @Override
  public JsonGenerator useDefaultPrettyPrinter() {
    return null;
  }

  @Override
  public void writeStartArray() throws IOException {
    writer.writeStartArray();
  }

  @Override
  public void writeEndArray() throws IOException {
    writer.writeEndArray();
  }

  @Override
  public void writeStartObject() throws IOException {
    writer.writeStartDocument();
  }

  @Override
  public void writeEndObject() throws IOException {
    writer.writeEndDocument();
  }

  @Override
  public void writeFieldName(String name) throws IOException {
    writer.writeName(name);
  }

  @Override
  public void writeFieldName(SerializableString name) throws IOException {
    writer.writeName(name.getValue());
  }

  @Override
  public void writeString(String text) throws IOException {
    writer.writeString(text);
  }

  @Override
  public void writeString(char[] text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeString(SerializableString text) throws IOException {
    writer.writeString(text.getValue());
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(String text) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(String text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(char ch) throws IOException {

  }

  @Override
  public void writeRawValue(String text) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRawValue(String text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRawValue(char[] text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int writeBinary(Base64Variant bv, InputStream data, int dataLength) throws IOException {
    throw new UnsupportedOperationException();

  }

  @Override
  public void writeNumber(int value) throws IOException {
    writer.writeInt32(value);
  }

  @Override
  public void writeNumber(long value) throws IOException {
    writer.writeInt64(value);
  }

  @Override
  public void writeNumber(BigInteger value) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeNumber(double value) throws IOException {
    writer.writeDouble(value);
  }

  @Override
  public void writeNumber(float value) throws IOException {
    writer.writeDouble((double) value);
  }

  @Override
  public void writeNumber(BigDecimal value) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeNumber(String encodedValue) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeBoolean(boolean value) throws IOException {
    writer.writeBoolean(value);
  }

  @Override
  public void writeNull() throws IOException {
    writer.writeNull();
  }

  @Override
  public void writeObject(Object pojo) throws IOException {

    if (pojo instanceof ObjectId) {
      this.writeObjectId(pojo);
    } else if (pojo instanceof BsonDateTime) {
      //hack to support native date
      BsonDateTime ts = (BsonDateTime) pojo;
      writer.writeDateTime(ts.getValue());
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public void writeObjectId(Object id) throws IOException {
    writer.writeObjectId((ObjectId) id);
  }

  @Override
  public void writeTree(TreeNode rootNode) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonStreamContext getOutputContext() {
    return new CustomJsonStreamContext();

  }

  @Override
  public void flush() throws IOException {
    //throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws IOException {
    //throw new UnsupportedOperationException();
  }

  static class CustomJsonStreamContext extends JsonStreamContext {

    @Override
    public JsonStreamContext getParent() {
      return null;
    }

    @Override
    public String getCurrentName() {
      return null;
    }
  }
}
