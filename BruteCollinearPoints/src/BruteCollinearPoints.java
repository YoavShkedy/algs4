import edu.princeton.cs.algs4.StdRandom;

import java.util.*;

public class BruteCollinearPoints {
    ArrayList<LineSegment> lineSegments = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {throw new IllegalArgumentException();}
        for(Point p : points) {
            if (p == null) {throw new IllegalArgumentException();}
        }
        for (Point point : points) {
            if (point == points[points.length-1]) {break;}
            for (int j = 0; j < points.length; j++) {
                if (j == points.length - 1) {break;}
                if (points[j+1].equals(point)) {break;}
                else if (point == points[j+1]) {throw new IllegalArgumentException();}
            }
        }
        for (int first = 0; first < points.length - 3; first++) {
            for (int second = first + 1; second < points.length - 2; second++) {
                double slopeFS = points[first].slopeTo(points[second]);
                for (int third = second + 1; third < points.length - 1; third++) {
                    double slopeFT = points[first].slopeTo(points[third]);
                    if (slopeFS == slopeFT) {
                        for (int forth = third + 1; forth < points.length; forth++) {
                            double slopeFF = points[first].slopeTo(points[forth]);
                            if (slopeFS == slopeFF) {
                                lineSegments.add(new LineSegment(points[first], points[forth]));
                            }
                        }
                    }
                }

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

    // test
    public static void main(String[] args) {
        Point[] points = new Point[50];
        for (int i = 0; i < points.length; i++) {
            int randomNumber1 = StdRandom.uniform(0, 30);
            int randomNumber2 = StdRandom.uniform(0, 30);
            points[i] = new Point(randomNumber1, randomNumber2);
        }
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        int numberOfSegments = bcp.numberOfSegments();
        System.out.println(numberOfSegments);
    }
}