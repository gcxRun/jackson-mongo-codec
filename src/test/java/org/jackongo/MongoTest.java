package org.jackongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.jackongo.joda.JodaTimeBsonModule;
import org.jackongo.objectid.MongoBsonModule;
import org.junit.After;
import org.junit.Before;

/**
 * Created by greg on 29/12/15.
 */
public class MongoTest {
  protected MongoDatabase testDatabase;
  protected CodecRegistry registry;
  private MongoClient client;

  @Before
  public void setUp() {
    String host = System.getProperty("mongo.host","localhost");
    int port = Integer.parseInt(System.getProperty("mongo.port","27017"));

    client = new MongoClient(host,port);
    testDatabase = client.getDatabase("test");

    ObjectMapper objectMapper = buildObjectMapperWithJodaSupport();

    CodecProvider jacksonCodecProvider = new JacksonCodecProvider(objectMapper);

    registry = extendDefaultCodecRegistries(jacksonCodecProvider);
  }

  @After
  public void tearDown() {
    client.close();
  }

  protected CodecRegistry extendDefaultCodecRegistries(CodecProvider codecProvider) {
    return CodecRegistries.fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(codecProvider)
    );
  }

  protected <T> MongoCollection<T> getMongoCollection(String name, Class<T> clazz) {
    return testDatabase.getCollection(name, clazz)
            .withCodecRegistry(registry);
  }
  protected  MongoCollection<Document> getMongoCollection(String name) {
    return testDatabase.getCollection(name);
  }

  protected ObjectMapper buildObjectMapperWithJodaSupport() {
    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JodaTimeBsonModule())
            .registerModule(new MongoBsonModule())

            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }
}
