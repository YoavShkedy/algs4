import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    TreeSet<Point2D> treeSet;

    // construct an empty set of points
    public PointSET() {
        treeSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {throw new IllegalArgumentException();}
        treeSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {throw new IllegalArgumentException();}
        return treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : treeSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {throw new IllegalArgumentException();}
        ArrayList<Point2D> rectPoints = new ArrayList<>();
        for (Point2D point : treeSet) {
            if (rect.contains(point)) {
                rectPoints.add(point);
            }
        }
        return rectPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {throw new IllegalArgumentException();}
        if (treeSet.isEmpty()) {return null;}
        Point2D cPoint = treeSet.ceiling(p);
        Point2D fPoint = treeSet.floor(p);
        assert cPoint != null;
        double cDistance = cPoint.distanceTo(p);
        assert fPoint != null;
        double fDistance = fPoint.distanceTo(p);
        if (cDistance > fDistance) {
            return fPoint;
        }
        else if (cDistance < fDistance) {
            return cPoint;
        }
        else {
            return cPoint;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        RectHV r = new RectHV(0.4, 0.3, 0.8, 0.6);
        PointSET pointSET = new PointSET();
        Point2D point1 = new Point2D(0.0, 0.0);
        Point2D point2 = new Point2D(0.1, 0.4);
        Point2D point3 = new Point2D(0.6, 0.5);
        pointSET.insert(point1);
        pointSET.insert(point2);
        pointSET.insert(point3);
        pointSET.draw();
        r.draw();
        for (Point2D point : pointSET.range(r)) {
            System.out.println("This point is contained in r: " + point);
        }
        Point2D randomPoint = new Point2D(0.4, 0.3);
        Point2D nearestPoint = pointSET.nearest(randomPoint);
        System.out.println("This is the nearest point to " + randomPoint + " : " + nearestPoint);
    }
}