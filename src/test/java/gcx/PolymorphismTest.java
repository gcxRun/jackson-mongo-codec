package gcx;

import com.mongodb.client.MongoCollection;
import gcx.model.Circle;
import gcx.model.Shape;
import gcx.model.Square;
import org.junit.Test;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by greg on 29/12/15.
 */
public class PolymorphismTest extends MongoTest{


    private MongoCollection<Shape> getShapeMongoCollection() {
        return testDatabase.getCollection("shapes", Shape.class)
                .withCodecRegistry(registry);
    }

    @Test
    public void testAddShapes()
    {
        MongoCollection<Shape> shapes = getShapeMongoCollection();
        shapes.drop();
        Shape square = new Square(32, Shape.ShapeColor.BLACK);
        Shape circle = new Circle(64,Shape.ShapeColor.WHITE);
        shapes.insertOne(square);
        shapes.insertOne(circle);

        assertEquals(2,shapes.count());

        Shape potentialSquare = shapes.find(eq("color", "BLACK")).first();
        assertNotNull(potentialSquare);
        assert(potentialSquare instanceof Square);
        Square newSquare = (Square)potentialSquare;
        assertEquals(square,newSquare);

        Shape potentialCircle = shapes.find(eq("color", "WHITE")).first();
        assertNotNull(potentialCircle);
        assert(potentialCircle instanceof Circle);
        Circle newCircle = (Circle)potentialCircle;
        assertEquals(circle,newCircle);
    }

/**
    @Test
    public void testPlainMapper() throws IOException {
        Shape square = new Square(32, Shape.ShapeColor.BLACK);
        Shape circle = new Circle(64,Shape.ShapeColor.WHITE);

        ArrayList<Shape> shapes = new ArrayList<Shape>();
        shapes.add(square);
        shapes.add(circle);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(square);


        //System.out.println(result);


        String result2 = "{\"color\":\"BLACK\",\"type\":\"square\",\"sideLength\":32.0}";
        Shape o = mapper.readValue(result2,Shape.class);

        System.out.println(o);
    }
    **/
}
