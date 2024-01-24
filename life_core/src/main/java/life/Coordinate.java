package life;

import java.util.Objects;

public class Coordinate {

     int x;
     int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {
        this.x=0;
        this.y=0;
    }

    public Coordinate(Coordinate coordinate){
       this.x=coordinate.x;
       this.y=coordinate.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Coordinate that) {
            return x == that.x && y == that.y;
        }

        return false;
    }

    @Override
    public int hashCode() {
//        return Objects.hash(x, y, y,x, x, y);
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
