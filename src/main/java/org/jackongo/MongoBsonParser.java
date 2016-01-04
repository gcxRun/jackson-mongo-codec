package org.jackongo;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;

import org.bson.BsonDateTime;
import org.bson.BsonReader;
import org.bson.BsonType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;


/**
 * Implements a JsonParser by wrappping a Mongo BsonReader.
 */

public class MongoBsonParser extends JsonParser {

  private final BsonReader reader;

  ValueToken valueToken = new ValueToken();
  LinkedList<State> states = new LinkedList<State>();
  private JsonStreamContext context = new CustomJsonStreamContext();
  private ObjectCodec codec;

  /**
   * Constructs a JsonParser leveraging the reader.
   * @param reader - the wrapped reader
   */
  public MongoBsonParser(BsonReader reader) {
    this.reader = reader;

    reader.readBsonType();
    reader.readStartDocument();
    pushState(State.OBJECT);
  }

  private void reset() {
    valueToken.jsonToken = null;
    valueToken.name = null;
    valueToken.value = null;
    valueToken.stringValue = null;
  }

  @Override
  public ObjectCodec getCodec() {
    return codec;
  }

  @Override
  public void setCodec(ObjectCodec codec) {
    this.codec = codec;
  }

  @Override
  public Version version() {
    return null;
  }

  @Override
  public void close() throws IOException {

  }

  private void pushState(State state) {
    states.push(state);
  }

  private State peek() {
    return states.peek();
  }

  private State poll() {
    return states.poll();
  }

  private boolean isInDocument() {
    return peek() == State.OBJECT;
  }

  private boolean isInField() {
    return peek() == State.FIELD;
  }

  private boolean isInArray() {
    return peek() == State.ARRAY;
  }

  @Override
  public JsonToken nextToken() throws IOException, JsonParseException {

    //TODO clean this
    reset();

    if (isInDocument()) {
      BsonType currentBsonType = reader.readBsonType();

      switch (currentBsonType) {
        case END_OF_DOCUMENT:
          poll();
          reader.readEndDocument();
          valueToken.jsonToken = JsonToken.END_OBJECT;
          break;
        default:
          valueToken.jsonToken = JsonToken.FIELD_NAME;
          valueToken.name = reader.readName();
          pushState(State.FIELD);
      }
      return valueToken.jsonToken;
    }
    if (isInField()) {
      BsonType currentBsonType = reader.getCurrentBsonType();

      handleBsonType(currentBsonType);

      return valueToken.jsonToken;
    }
    if (isInArray()) {
      BsonType currentBsonType = reader.readBsonType();
      switch (currentBsonType) {
        case END_OF_DOCUMENT:
          poll();
          reader.readEndArray();
          valueToken.jsonToken = JsonToken.END_ARRAY;
          break;
        default:
          pushState(State.FIELD);
          handleBsonType(currentBsonType);
      }
      return valueToken.jsonToken;
    }
    return JsonToken.END_OBJECT;//we are done !!
  }

  private void handleBsonType(BsonType currentBsonType) {
    switch (currentBsonType) {
      case ARRAY:
        reader.readStartArray();
        poll();
        pushState(State.ARRAY);
        valueToken.jsonToken = JsonToken.START_ARRAY;
        break;
      case DOCUMENT:
        reader.readStartDocument();
        poll();
        pushState(State.OBJECT);
        valueToken.jsonToken = JsonToken.START_OBJECT;
        break;
      case OBJECT_ID:
        valueToken.jsonToken = JsonToken.VALUE_EMBEDDED_OBJECT;
        valueToken.value = reader.readObjectId();
        poll();
        break;
      case STRING:
        valueToken.jsonToken = JsonToken.VALUE_STRING;
        valueToken.stringValue = reader.readString();
        poll();
        break;
      case END_OF_DOCUMENT:
        break;
      case DOUBLE:
        valueToken.jsonToken = JsonToken.VALUE_NUMBER_FLOAT;
        valueToken.doubleValue = reader.readDouble();
        poll();
        break;
      case INT32:
        valueToken.jsonToken = JsonToken.VALUE_NUMBER_INT;
        valueToken.intValue = reader.readInt32();
        poll();
        break;
      case INT64:
        valueToken.jsonToken = JsonToken.VALUE_NUMBER_INT;
        valueToken.longValue = reader.readInt64();
        poll();
        break;
      case DATE_TIME:
        valueToken.jsonToken = JsonToken.VALUE_EMBEDDED_OBJECT;
        valueToken.value = new BsonDateTime(reader.readDateTime());
        poll();
        break;
      case BOOLEAN:
        boolean value = reader.readBoolean();
        valueToken.jsonToken = value ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
        poll();
        break;
      case NULL:
        valueToken.jsonToken = JsonToken.VALUE_NULL;
        poll();
        break;
      case DB_POINTER:
        valueToken.jsonToken = JsonToken.VALUE_EMBEDDED_OBJECT;
        valueToken.value = reader.readDBPointer();
        break;
      case TIMESTAMP:
        valueToken.jsonToken = JsonToken.VALUE_EMBEDDED_OBJECT;
        valueToken.value = reader.readTimestamp();
        break;
      case BINARY:
      case UNDEFINED:
      case REGULAR_EXPRESSION:
      case JAVASCRIPT:
      case SYMBOL:
      case JAVASCRIPT_WITH_SCOPE:

      case MIN_KEY:
      case MAX_KEY:
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public JsonToken nextValue() throws IOException, JsonParseException {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonParser skipChildren() throws IOException, JsonParseException {
    return this; //throw new UnsupportedOperationException();
  }

  @Override
  public boolean isClosed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonToken getCurrentToken() {
    return valueToken.jsonToken;
  }

  @Override
  public int getCurrentTokenId() {
    JsonToken jsonToken = getCurrentToken();
    return jsonToken.id();
  }

  @Override
  public boolean hasCurrentToken() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean hasTokenId(int id) {
    if (id == 5 && valueToken != null && valueToken.name != null) {
      return true;
    }
    return getCurrentTokenId() == id;
  }

  @Override
  public boolean hasToken(JsonToken token) {
    return valueToken.jsonToken == token;
  }

  @Override
  public String getCurrentName() throws IOException {
    return valueToken.name;
  }

  @Override
  public JsonStreamContext getParsingContext() {
    return context;
  }

  @Override
  public JsonLocation getTokenLocation() {
    return new JsonLocation(this, 1, -1, -1);
  }

  @Override
  public JsonLocation getCurrentLocation() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearCurrentToken() {
    //throw new UnsupportedOperationException();
  }

  @Override
  public JsonToken getLastClearedToken() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void overrideCurrentName(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getText() throws IOException {
    return valueToken.stringValue;
  }

  @Override
  public char[] getTextCharacters() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getTextLength() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getTextOffset() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean hasTextCharacters() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Number getNumberValue() throws IOException {
    return valueToken.doubleValue;
  }

  @Override
  public NumberType getNumberType() throws IOException {

    switch (reader.getCurrentBsonType()) {
      case INT32:
        return NumberType.INT;
      case DOUBLE:
        return NumberType.DOUBLE;
      default:
        return NumberType.INT;
    }
  }

  @Override
  public int getIntValue() throws IOException {
    return valueToken.intValue;
  }

  @Override
  public long getLongValue() throws IOException {
    return valueToken.longValue;
  }

  @Override
  public BigInteger getBigIntegerValue() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public float getFloatValue() throws IOException {
    return (float) valueToken.doubleValue;
  }

  @Override
  public double getDoubleValue() throws IOException {
    return valueToken.doubleValue;
  }

  @Override
  public BigDecimal getDecimalValue() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getEmbeddedObject() throws IOException {
    return valueToken.value;
  }

  @Override
  public byte[] getBinaryValue(Base64Variant bv) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getValueAsString(String def) throws IOException {
    return valueToken.stringValue;
  }

  static enum State {
    OBJECT,
    FIELD,
    ARRAY
  }

  static class ValueToken {
    JsonToken jsonToken;
    String name;
    Object value;
    int intValue;
    long longValue;
    double doubleValue;
    String stringValue;


    public String toString() {
      return jsonToken + " " + name;
    }
  }

  class CustomJsonStreamContext extends JsonStreamContext {
    @Override
    public JsonStreamContext getParent() {
      return null;
    }

    @Override
    public String getCurrentName() {
      return valueToken.name;
    }
  }
}
