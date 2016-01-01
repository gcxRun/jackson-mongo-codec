package gcx;

import com.fasterxml.jackson.core.*;
import org.bson.BsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by greg on 30/12/15.
 */
public class CustomBsonGenerator extends JsonGenerator {
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

    private final BsonWriter writer;
    private ObjectCodec oc;

    public CustomBsonGenerator(BsonWriter writer) {
        this.writer = writer;
    }

    @Override
    public JsonGenerator setCodec(ObjectCodec oc) {
        this.oc = oc;
        return this;
    }

    @Override
    public ObjectCodec getCodec() {
        return this.oc;
    }

    @Override
    public Version version() {
        return null;
    }

    @Override
    public JsonGenerator enable(Feature f) {
        return this;
    }

    @Override
    public JsonGenerator disable(Feature f) {
        return this;
    }

    @Override
    public boolean isEnabled(Feature f) {
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
    public void writeRaw(char c) throws IOException {

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
    public void writeNumber(int v) throws IOException {
        writer.writeInt32(v);
    }

    @Override
    public void writeNumber(long v) throws IOException {
        writer.writeInt64(v);
    }

    @Override
    public void writeNumber(BigInteger v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeNumber(double v) throws IOException {
        writer.writeDouble(v);
    }

    @Override
    public void writeNumber(float v) throws IOException {
        writer.writeDouble((double) v);
    }

    @Override
    public void writeNumber(BigDecimal v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeNumber(String encodedValue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBoolean(boolean state) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeNull() throws IOException {
        writer.writeNull();
    }

    @Override
    public void writeObject(Object pojo) throws IOException {
        if (pojo instanceof ObjectId) {
            this.writeObjectId(pojo);
        }
        //hack to support native date
        else if (pojo instanceof Long) {
            Long ts = (Long) pojo;
            writer.writeDateTime(ts);
        } else
            throw new UnsupportedOperationException();
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
}
