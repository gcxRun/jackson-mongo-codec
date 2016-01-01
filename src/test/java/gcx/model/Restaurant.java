package gcx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Restaurant {

    @JsonProperty("_id")
    public ObjectId id;

    public Address address;

    public String restaurantId;

    public List<Grade> grades;

    public String borough;

    public String cuisine;

}
