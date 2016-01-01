package gcx.model;

public class Circle extends Shape {
  double radius;

  public Circle() {
    super();
  }

  public Circle(int radius, ShapeColor color) {
    super(color);
    this.radius = radius;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    Circle circle = (Circle) o;

    return Double.compare(circle.radius, radius) == 0;

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    long temp;
    temp = Double.doubleToLongBits(radius);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  }
}
