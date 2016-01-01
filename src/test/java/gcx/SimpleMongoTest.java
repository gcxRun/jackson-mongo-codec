package gcx;

import com.mongodb.client.MongoCollection;
import gcx.model.A;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by greg on 30/12/15.
 */
public class SimpleMongoTest extends MongoTest{


    private MongoCollection<A> getSimpleAMongoCollection() {
        return testDatabase.getCollection("simple", A.class)
                .withCodecRegistry(registry);
    }

    @Test
    public void testInsertAThenRead(){
        MongoCollection<A> as = getSimpleAMongoCollection();
        as.drop();
        A a = new A();
        a.intValue = 32;
        a.longValue = 64;
        a.floatValue = (float) 3.14;
        a.name = "foo";

        as.insertOne(a);

        A anotherA = as.find().first();

        assertNotNull(anotherA);
        assertEquals(a,anotherA);
    }
}
