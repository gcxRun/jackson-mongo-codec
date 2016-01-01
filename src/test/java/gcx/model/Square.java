package gcx.model;

/**
 * Created by greg on 29/12/15.
 */

public class Square extends Shape {
  double sideLength;

  public Square() {
  }

  public Square(int sideLength, ShapeColor color) {
    super(color);
    this.sideLength = sideLength;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    Square square = (Square) o;

    return Double.compare(square.sideLength, sideLength) == 0;

  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    long temp;
    temp = Double.doubleToLongBits(sideLength);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  public double getSideLength() {
    return sideLength;
  }

  public void setSideLength(double sideLength) {
    this.sideLength = sideLength;
  }
}
