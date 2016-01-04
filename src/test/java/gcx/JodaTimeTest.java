package gcx;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;

import lombok.Data;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 01/01/16.
 */
public class JodaTimeTest extends MongoTest {

  @Test
  public void testDateTime_Serial() {
    MongoCollection<Base> foos = getMongoCollection("serial", Base.class);
    foos.drop();

    Foo foo = new Foo(new DateTime(DateTimeZone.UTC));
    foos.insertOne(foo);

    Base anotherFoo = foos.find().first();
    assertEquals(foo, anotherFoo);
  }

  @Test
  public void testDateTime_isMongoDateTime() {
    MongoCollection<Foo> foos = getMongoCollection("isDateTime", Foo.class);
    foos.drop();

    Foo foo = new Foo(new DateTime(DateTimeZone.UTC));
    foos.insertOne(foo);

    MongoCollection<Document> raws = getMongoCollection("isDateTime");
    Document firstFoo = raws.find().first();
    assertTrue(firstFoo.containsKey("ts"));
    Date date = firstFoo.getDate("ts");

    assertEquals(foo.ts.getMillis(), date.getTime());
  }


  @JsonTypeInfo(
          use = JsonTypeInfo.Id.NAME,
          include = JsonTypeInfo.As.PROPERTY,
          property = "type")
  @JsonSubTypes({
          @JsonSubTypes.Type(value = Foo.class, name = "foo")
  })
  @Data
  public static abstract class Base {
    public String name = "base";
  }

  @Data
  public static class Foo extends Base {
    public DateTime ts = null;
    public Foo(){
      this.name = "foo";
    }
    public Foo(DateTime ts){
      this();
      this.ts = ts;
    }
  }

}
