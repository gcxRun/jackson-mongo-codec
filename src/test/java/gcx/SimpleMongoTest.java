package gcx;

import com.mongodb.client.MongoCollection;

import org.junit.Test;

import lombok.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by greg on 30/12/15.
 */
public class SimpleMongoTest extends MongoTest {

  @Test
  public void testInsertAThenRead() {
    MongoCollection<A> as = getMongoCollection("simple", A.class);
    as.drop();
    A a = new A();
    a.intValue = 32;
    a.longValue = 64;
    a.floatValue = (float) 3.14;
    a.name = "foo";

    as.insertOne(a);

    A anotherA = as.find().first();

    assertNotNull(anotherA);
    assertEquals(a, anotherA);
  }

  @Data
  static public class A {
    public String name;
    public int intValue;
    public long longValue;
    public float floatValue;
  }
}
