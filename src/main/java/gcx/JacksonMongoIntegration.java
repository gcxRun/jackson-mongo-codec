package gcx;

import gcx.objectid.MongoObjectMapper;
import org.bson.codecs.configuration.CodecProvider;

/**
 * Created by greg on 29/12/15.
 */
public class JacksonMongoIntegration {

    public JacksonMongoIntegration(){

    }

    private MongoObjectMapper objectMapper;

    public void setObjectMapper(MongoObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    CodecProvider getCodecProvider(){
        return new JacksonCodecProvider(objectMapper);

    }
}
