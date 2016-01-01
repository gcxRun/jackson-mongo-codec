package gcx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import gcx.joda.JodaTimeBsonModule;
import gcx.objectid.MongoBsonModule;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.After;
import org.junit.Before;

/**
 * Created by greg on 29/12/15.
 */
public class MongoTest
{
    private MongoClient client;
    protected MongoDatabase testDatabase;
    protected CodecRegistry registry;

    @Before
    public void setUp() {
        client = new MongoClient();
        testDatabase = client.getDatabase("test");

        JsonFactory jsonFactory = new CustomBsonFactory();

        ObjectMapper objectMapper = buildObjectMapperWithJodaSupport(jsonFactory);

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

    protected ObjectMapper buildObjectMapperWithJodaSupport(JsonFactory jsonFactory) {
        ObjectMapper mapper = new ObjectMapper(jsonFactory)
                .registerModule(new JodaTimeBsonModule())
                .registerModule(new MongoBsonModule())
                //.registerModule(new BsonModule())

                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}
