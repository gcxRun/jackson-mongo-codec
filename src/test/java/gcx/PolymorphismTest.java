package gcx;

import com.mongodb.client.MongoCollection;

import org.junit.Test;

import gcx.model.shapes.Circle;
import gcx.model.shapes.Shape;
import gcx.model.shapes.Square;

import static com.mongodb.client.model.Filters.eq;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 29/12/15.
 */
public class PolymorphismTest extends MongoTest {


  @Test
  public void testAddShapes() {
    MongoCollection<Shape> shapes = getMongoCollection("shapes", Shape.class);
    shapes.drop();
    Shape square = new Square(32, Shape.ShapeColor.BLACK);
    Shape circle = new Circle(64, Shape.ShapeColor.WHITE);
    shapes.insertOne(square);
    shapes.insertOne(circle);

    assertEquals(2, shapes.count());

    Shape potentialSquare = shapes.find(eq("color", "BLACK")).first();
    assertNotNull(potentialSquare);
    assert (potentialSquare instanceof Square);
    Square newSquare = (Square) potentialSquare;
    assertEquals(square, newSquare);

    Shape potentialCircle = shapes.find(eq("color", "WHITE")).first();
    assertNotNull(potentialCircle);
    assert (potentialCircle instanceof Circle);
    Circle newCircle = (Circle) potentialCircle;
    assertEquals(circle, newCircle);
  }

/**
 @Test public void testPlainMapper() throws IOException {
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
