import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    // constructs the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        int y0 = this.y;
        int x0 = this.x;
        int y1 = that.y;
        int x1 = that.x;
        int compare = 0;
        if (y0 == y1) { // if y-coordinates are equal
            compare = x0 - x1; // compare by x-coordinates
        }
        else if (y0 > y1) { // if y-coordinates are not equal
            compare = y0 - y1; // compare by y-coordinates
        }
        return compare;
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {
        double y0 = this.y;
        double x0 = this.x;
        double y1 = that.y;
        double x1 = that.x;
        double slope = (y1 - y0) / (x1 - x0);
        if (x1 - x0 == 0) {
            slope = Double.POSITIVE_INFINITY;
        }
        else if (y0 == y1) {
            slope = +0.0;
        }
        else if (y0 == y1 && x0 == x1) {
            slope = Double.NEGATIVE_INFINITY;
        }
        return slope;
    }

    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        Comparator<Point> comparator = new Comparator<>() {
            @Override
            public int compare(Point one, Point two) {
                double slope1 = slopeTo(one);
                double slope2 = slopeTo(two);
                if (slope1 > slope2) {
                    return 1;
                }
                else if (slope1 < slope2) {
                    return -1;
                }
                else return 0;
            }
        };
        return comparator;
    }
}