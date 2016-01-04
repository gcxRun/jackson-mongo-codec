package org.jackongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by greg on 03/01/16.
 */
public class ObjectIdTest extends MongoTest {

  public static class A {
    @JsonProperty("_id")
    public String id;
    public String name;

    public ObjectId[] ids;
  }


  /**
   * _id is a String, it can be set from the client as long as unique
   */
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
    assertEquals(a.name,maybeA.name);

    assertNotNull(maybeA.id);
    assertEquals(a.id,maybeA.id);

    //check it is an $oid for Mongo
    MongoCollection<Document> raws = getMongoCollection("isDateTime");
    Document rawA = raws.find().first();
    ObjectId oid = rawA.getObjectId("_id"); //throw classcatexception if not $oid
    assertNotNull(oid);
  }

  @Test
  public void testObjectId_as_id_null()
  {
    MongoCollection<A> as = getMongoCollection("idObj",A.class);
    as.drop();

    A a = new A();
    a.name = "foo";
    a.id = null;

    as.insertOne(a);

    A maybeA = as.find().first();

    assertNotNull(maybeA.id);
  }

  @Test
  public void testObjectId_array_of_ObjectId()
  {
    MongoCollection<A> as = getMongoCollection("idObj",A.class);
    as.drop();

    A a = new A();
    a.ids = new ObjectId[] {new ObjectId(),new ObjectId()};

    as.insertOne(a);

    A maybeA = as.find().first();

    assertNotNull(maybeA.ids);
    assertEquals(2,maybeA.ids.length);
  }

}
