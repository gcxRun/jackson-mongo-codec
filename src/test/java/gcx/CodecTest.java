package gcx;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import gcx.model.Address;
import gcx.model.Coord;
import gcx.model.Grade;
import gcx.model.Restaurant;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;


public class CodecTest extends MongoTest {



    private MongoCollection<Restaurant> getRestaurantMongoCollection() {
        return testDatabase.getCollection("restaurants", Restaurant.class)
                .withCodecRegistry(registry);
    }

/*
    //TODO move that our or the rest
    @Test
    public void testEncoderClass() {
        JacksonCodec<Restaurant> codec = new JacksonCodec<Restaurant>(new ObjectMapper(),
                MongoClient.getDefaultCodecRegistry(),
                Restaurant.class);

        codec.getEncoderClass().equals(Restaurant.class);
    }
*/
    @Test
    public void testFindFirstRestaurant() {


        MongoCollection<Restaurant> restaurants = getRestaurantMongoCollection();

        //"restaurant_id" : "40356018"
        Restaurant restaurant = restaurants.find(eq("restaurant_id", "40356018"), Restaurant.class).first();

        assertNotNull(restaurant);
        assertEquals(restaurant.restaurantId, "40356018");

        assertNotNull(restaurant.address);
        assertEquals(restaurant.address.zipcode, "11224");
        assertNotNull(restaurant.address.coord);
        assertEquals(-73.98241999999999, restaurant.address.coord._lat,0.001);
        assertEquals(40.579505, restaurant.address.coord._long,0.001);

        assertNotNull(restaurant.grades);
        assertEquals(restaurant.grades.size(), 4);
        assertEquals("A", restaurant.grades.get(2).grade);
        assertEquals(new DateTime(2012, 04, 13, 0, 0, 0, DateTimeZone.UTC), restaurant.grades.get(2).date);
    }

    @Test
    public void testInsertRestaurant() {
        MongoCollection<Restaurant> restaurants = getRestaurantMongoCollection();

        Restaurant restaurant = buildDummyRestaurant("NEW ONE");

        restaurants.insertOne(restaurant);

        UpdateResult result
                = restaurants.replaceOne(eq("restaurant_id", "NEW ONE"), restaurant);
        assertEquals(1, result.getModifiedCount());

        Restaurant fetchedRestaurant = restaurants.find(eq("restaurant_id", "NEW ONE"), Restaurant.class).first();
        assertNotNull(fetchedRestaurant);
        assertEquals(restaurant, fetchedRestaurant);

    }



    private Restaurant buildDummyRestaurant(String restaurantId) {
        Restaurant restaurant = new Restaurant();
        restaurant.restaurantId = restaurantId;
        restaurant.address = new Address();
        restaurant.address.zipcode = "12345";
        restaurant.address.coord = new Coord();
        restaurant.address.coord._long = 0.333;
        restaurant.address.coord._lat = 0.111111;
        restaurant.grades = new ArrayList<Grade>();
        Grade grade = new Grade();
        grade.grade = "B+";
        grade.score = 1234;
        grade.date = new DateTime(DateTimeZone.UTC);
        restaurant.grades.add(grade);
        return restaurant;
    }
}
