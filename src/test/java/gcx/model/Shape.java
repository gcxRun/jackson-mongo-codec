package gcx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.bson.types.ObjectId;

/**
 * Created by greg on 29/12/15.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Circle.class, name = "circle"),
        @JsonSubTypes.Type(value = Square.class, name = "square")
})

public abstract class Shape {

  public ShapeColor color;
  @JsonProperty("_id")
  public ObjectId id = new ObjectId();

  protected Shape() {
    this.color = ShapeColor.NO;
  }

  protected Shape(ShapeColor color) {
    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Shape shape = (Shape) o;

    if (color != shape.color) return false;
    return id.equals(shape.id);

  }

  @Override
  public int hashCode() {
    int result = color.hashCode();
    result = 31 * result + id.hashCode();
    return result;
  }

  public ShapeColor getColor() {
    return color;
  }

  public void setColor(ShapeColor color) {
    this.color = color;
  }

  public enum ShapeColor {
    WHITE("white"),
    BLACK("black"),
    NO("no");
    String value;

    ShapeColor(String value) {
      this.value = value;
    }
  }
}
