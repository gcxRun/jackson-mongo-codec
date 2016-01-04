package gcx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 03/01/16.
 */
public class ObjectIdTest extends MongoTest {

  public static class A {

    @JsonProperty("_id")
    public String id;

    public String name;
  }

  @Test
  public void testObjectId_as_id()
  {
    MongoCollection<A> as = getMongoCollection("idObj",A.class);
    as.drop();

    A a = new A();
    a.name = "foo";
    a.id = "12345";
    as.insertOne(a);

    A maybeA = as.find().first();
    assertEquals("foo",maybeA.name);
  }

}
