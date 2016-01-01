package gcx;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 01/01/16.
 */
public class JodaTimeTest extends MongoTest {

  @Test
  public void testDateTime_Serial() {
    MongoCollection<Foo> foos = getMongoCollection("serial", Foo.class);
    foos.drop();

    Foo foo = new Foo();
    foos.insertOne(foo);

    Foo anotherFoo = foos.find().first();
    assertEquals(foo, anotherFoo);
  }

  @Test
  public void testDateTime_isMongoDateTime() {
    MongoCollection<Foo> foos = getMongoCollection("isDateTime", Foo.class);
    foos.drop();

    Foo foo = new Foo();
    foos.insertOne(foo);

    MongoCollection<Document> raws = testDatabase.getCollection("isDateTime");
    Document firstFoo = raws.find().first();
    assertTrue(firstFoo.containsKey("ts"));
    Date date = firstFoo.getDate("ts");

    assertEquals(foo.ts.getMillis(), date.getTime());
  }

  private static class Foo {
    public DateTime ts = new DateTime(DateTimeZone.UTC);

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Foo foo = (Foo) o;

      return ts != null ? ts.equals(foo.ts) : foo.ts == null;

    }

    @Override
    public int hashCode() {
      return ts != null ? ts.hashCode() : 0;
    }
  }

}
