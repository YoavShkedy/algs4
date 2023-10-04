import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.*;

public class FastCollinearPoints {
    ArrayList<Point> pointsList = new ArrayList<>();
    ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Point point : points) {
            if (point.equals(points[points.length-1])) {
                break;
            }
            for (int j = 0; j < points.length; j++) {
                if (j == points.length-1) {
                    break;
                }
                if (points[j+1].equals(point)) {
                    break;
                } else if (point.compareTo(points[j+1]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Point p = points[0];
        for (Point q : points) {
            q.slopeWithP = p.slopeTo(q);
            pointsList.add(q);
            if (q.equals(p)) {pointsList.remove(q);}
        }
        pointsList.sort(p.slopeOrder());
        int i = 0;
        Point q = pointsList.get(i);
        double slopeWithP = q.slopeWithP;
        while (i < pointsList.size()) {
            q = pointsList.get(i);
            slopeWithP = q.slopeWithP;
            i = i + 3;
            if (i >= pointsList.size()) {break;}
            if (pointsList.get(i).slopeWithP == slopeWithP) {
                while (pointsList.get(i).slopeWithP == slopeWithP) {
                    i++;
                    if (i >= pointsList.size()) {break;}
                }
                lineSegments.add(new LineSegment(q, pointsList.get(i-1)));
                System.out.println(lineSegments);
            }
            else {
                i = i - 2;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[numberOfSegments()]);
    }

    // sample client
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}