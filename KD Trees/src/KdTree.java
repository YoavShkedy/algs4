import edu.princeton.cs.algs4.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class KdTree {
    Node root;
    Node treeRoot;
    private static final boolean vertical = true;
    private static final boolean horizontal = false;
    ArrayList<Point2D> pointList = new ArrayList<>();
    ArrayList<Point2D> rangeList = new ArrayList<>();
    double nearestPointD = 0;
    double currentPointD;
    Node nearestNode;

    private static class Node {
        Point2D point;
        Node left, right;
        boolean direction;

        private Node(Point2D p) {
            this.point = p;
        }

        private int compareX(Point2D p) {
            return Double.compare(this.point.x(), p.x());
        }

        private int compareY(Point2D p) {
            return Double.compare(this.point.y(), p.y());
        }
    }

    private Point2D get(Point2D p) {
        if (p == null) {throw new IllegalArgumentException();}
        if (isEmpty()) {
            System.out.println("Kdtree is empty.");
            throw new NoSuchElementException();
        }
        int cmp;
        while (root != null) {
            if (isVertical(root)) {
                cmp = root.compareX(p);
            } else {
                cmp = root.compareY(p);
            }
            if (cmp < 0) {
                root = root.left;
            }
            else if (cmp > 0) {
                root = root.right;
            }
            else {
                return root.point;
            }
        }
        root = treeRoot;
        return null;
    }

    private void rectPoints(RectHV rect) {
        if (rect == null) {throw new IllegalArgumentException();}
        if (isEmpty()) {
            System.out.println("Kdtree is empty.");
            throw new NoSuchElementException();
        }
        while (root != null) {
            // for root being vertical
            if (isVertical(root)) {
                if (root.point.x() > rect.xmax()) { // if rect is to the left of root
                    root = root.left; // go left
                }
                else if (root.point.x() < rect.xmin()) { // if rect is to the right of root
                    root = root.right; // go right
                }
                else if (root.point.x() <= rect.xmax() && root.point.x() >= rect.xmin()) { // if root is in between rect
                    if (rect.contains(root.point)) { // check whether rect contains root
                        rangeList.add(root.point); // if it does, add root to the list
                    }
                    Node returnRoot = root;
                    root = root.left;
                    rectPoints(rect);
                    root = returnRoot;
                    root = root.right;
                    rectPoints(rect);
                    root = returnRoot;
                    break;
                }
            }
            // for root being horizontal
            if (isHorizontal(root)) {
                if (root.point.y() > rect.ymax()) { // if rect is below the root
                    root = root.left; // go down
                }
                else if (root.point.y() < rect.ymin()) { // if rect is above the root
                    root = root.right; // go up
                }
                else if (root.point.y() <= rect.ymax() && root.point.y() >= rect.ymin()) { // if root is in between rect
                    if (rect.contains(root.point)) { // check whether rect contains root
                        rangeList.add(root.point); // if it does, add root to the list
                    }
                    Node returnRoot = root;
                    root = root.left;
                    rectPoints(rect); // check left subtree
                    root = returnRoot;
                    root = root.right;
                    rectPoints(rect); // check right subtree
                    root = returnRoot;
                    break;
                }
            }
        }
    }

    private boolean isVertical(Node x) {
        if (x == null) {return false;}
        return x.direction == vertical;
    }

    private boolean isHorizontal(Node x) {
        if (x == null) {return false;}
        return x.direction == horizontal;
    }

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointList.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointList.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {throw new IllegalArgumentException();}
        if (isEmpty()) {
            root = new Node(p);
            root.direction = vertical;
            pointList.add(root.point);
            treeRoot = root;
        }
        else {
            int cmp;
            if (isVertical(root)) {
                cmp = root.compareX(p);
            } else {
                cmp = root.compareY(p);
            }
            // if point is smaller than root point
            if (cmp < 0) {
                if (root.left == null) {
                    Node node = new Node(p);
                    root.left = node;
                    if (isHorizontal(root)) {
                        node.direction = vertical;
                    } else {
                        node.direction = horizontal;
                    }
                    pointList.add(node.point);
                }
                else {
                    root = root.left;
                    insert(p);
                }
            }
            // if point is greater than root point
            else if (cmp > 0) {
                if (root.right == null) {
                    Node node = new Node(p);
                    root.right = node;
                    if (isHorizontal(root)) {
                        node.direction = vertical;
                    } else {
                        node.direction = horizontal;
                    }
                    pointList.add(node.point);
                }
                else {
                    root = root.right;
                    insert(p);
                }
            }
            // if point equals to root point
            else {
                root.point = p;
            }
            root = treeRoot;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {throw new IllegalArgumentException();}
        if (isEmpty()) {
            System.out.println("Kdtree is empty.");
            throw new NoSuchElementException();
        }
        return get(p) != null;
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty()) {
            System.out.println("Kdtree is empty.");
            throw new NoSuchElementException();
        }
        for (Point2D point : pointList) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {throw new IllegalArgumentException();}
        if (isEmpty()) {
            System.out.println("Kdtree is empty.");
            throw new NoSuchElementException();
        }
        rectPoints(rect);
        return rangeList;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // Throw exceptions
        if (p == null) {throw new IllegalArgumentException();}
        if (isEmpty()) {System.out.println("Kdtree is empty."); throw new NoSuchElementException();}
        // If the root equals to the argument point then return it
        if (root.point.equals(p)) {nearestNode = root; return nearestNode.point;}
        // Go as long as root isn't null
        while (root != null) {
            currentPointD = root.point.distanceTo(p);
            if (nearestNode == null) {
                nearestPointD = currentPointD;
                nearestNode = root;
            }
            else if (currentPointD < nearestPointD) {
                nearestPointD = currentPointD;
                nearestNode = root;
            }
            else if (currentPointD > nearestPointD) {
                if (isVertical(root)) {
                    if (root.point.x() > p.x()) { // check only left subtree if p is to the left
                        if (root.left != null) {
                            root = root.left;
                            nearest(p);
                        }
                        break;
                    }
                    else if (root.point.x() < p.x()) { // check only right subtree if p is to the right
                        if (root.right != null) {
                            root = root.right;
                            nearest(p);
                        }
                        break;
                    }
                }
                if (isHorizontal(root)) {
                    if (root.point.y() > p.y()) { // check only left subtree if p is below
                        if (root.left != null) {
                            root = root.left;
                            nearest(p);
                        }
                        break;
                    }
                    else if (root.point.y() < p.y()) { // check only right subtree if p is above
                        if (root.right != null) {
                            root = root.right;
                            nearest(p);
                        }
                        break;
                    }
                }
            }
            // for root being vertical
            if (isVertical(root)) {
                if (root.left == null && root.right == null) { // if both subtrees are null
                    break;
                }
                // if p is to the left of root
                if (root.point.x() > p.x()) {
                    if (root.left == null) { // if left subtree is null
                        root = root.right; // check right subtree
                        nearest(p);
                        break;
                    }
                    else if (root.right == null) { // if right subtree is null
                        root = root.left; // check left subtree
                        nearest(p);
                        break;
                    }
                    else { // otherwise check both subtrees
                        Node rootReturn = root;
                        root = root.left; // check left subtree first
                        nearest(p);
                        root = rootReturn; // get back to root after finishing with left subtree
                        if (root == treeRoot) {
                            if (root.right.point.distanceTo(p) <= nearestPointD) {
                                root = root.right;
                                nearest(p);
                                break;
                            }
                        }
                        else {
                            root = root.right;
                            nearest(p);
                            break;
                        }
                    }
                }
                // if p is to the right of root
                else if (root.point.x() < p.x()) {
                    if (root.left == null) { // if left subtree is null
                        root = root.right; // check right subtree
                        nearest(p);
                        break;
                    }
                    else if (root.right == null) { // if right subtree is null
                        root = root.left; // check left subtree
                        nearest(p);
                        break;
                    }
                    else { // otherwise check both subtrees
                        Node rootReturn = root;
                        root = root.right; // check right subtree first
                        nearest(p);
                        root = rootReturn; // get back to root after finishing with left subtree
                        if (root == treeRoot) {
                            if (root.left.point.distanceTo(p) <= nearestPointD) {
                                root = root.left;
                                nearest(p);
                                break;
                            }
                        }
                        else {
                            root = root.left;
                            nearest(p);
                            break;
                        }
                    }
                }
                // if p.(x) = root.(x)
                else if (root.point.x() == p.x()) {
                    if (root.left == null) { // if left subtree is null
                        root = root.right; // check right subtree
                        nearest(p);
                        break;
                    }
                    else if (root.right == null) { // if right subtree is null
                        root = root.left; // check left subtree
                        nearest(p);
                        break;
                    }
                    else { // otherwise check both subtrees
                        Node rootReturn = root;
                        root = root.left; // check left subtree first
                        nearest(p);
                        root = rootReturn; // get back to root after finishing with left subtree
                        if (root == treeRoot) {
                            if (root.right.point.distanceTo(p) <= nearestPointD) {
                                root = root.right;
                                nearest(p);
                                break;
                            }
                        }
                        else {
                            root = root.right;
                            nearest(p);
                            break;
                        }
                    }
                }
            }
            // for root being horizontal
            else if (isHorizontal(root)) {
                if (root.left == null && root.right == null) { // if both subtrees are null
                    break;
                }
                // if p is below of root
                if (root.point.y() > p.y()) {
                    if (root.left == null) { // if left subtree is null
                        root = root.right; // check right subtree
                        nearest(p);
                        break;
                    }
                    else if (root.right == null) { // if right subtree is null
                        root = root.left; // check left subtree
                        nearest(p);
                        break;
                    }
                    else {
                        Node rootReturn = root;
                        root = root.left; // check left subtree first
                        nearest(p);
                        root = rootReturn; // get back to root after finishing with left subtree
                        if (root == treeRoot) {
                            if (root.right.point.distanceTo(p) <= nearestPointD) {
                                root = root.right;
                                nearest(p);
                                break;
                            }
                        }
                        else {
                            root = root.right;
                            nearest(p);
                            break;
                        }
                    }
                }
                // if p is above root
                else if (root.point.y() < p.y()) {
                    if (root.left == null) { // if left subtree is null
                        root = root.right; // check right subtree
                        nearest(p);
                        break;
                    }
                    else if (root.right == null) { // if right subtree is null
                        root = root.left; // check left subtree
                        nearest(p);
                        break;
                    }
                    else {
                        Node rootReturn = root;
                        root = root.right; // check right subtree first
                        nearest(p);
                        root = rootReturn; // get back to root after finishing with left subtree
                        if (root == treeRoot) {
                            if (root.left.point.distanceTo(p) <= nearestPointD) {
                                root = root.left;
                                nearest(p);
                                break;
                            }
                        }
                        else {
                            root = root.left; // check left subtree
                            nearest(p);
                            break;
                        }
                    }
                }
                // if p.(y) = root.(y)
                else if (root.point.y() == p.y()) {
                    if (root.left == null) { // if left subtree is null
                        root = root.right; // check right subtree
                        nearest(p);
                        break;
                    }
                    else if (root.right == null) { // if right subtree is null
                        root = root.left; // check left subtree
                        nearest(p);
                        break;
                    }
                    else { // otherwise check both subtrees
                        Node rootReturn = root;
                        root = root.left; // check left subtree first
                        nearest(p);
                        root = rootReturn; // get back to root after finishing with left subtree
                        if (root == treeRoot) {
                            if (root.right.point.distanceTo(p) <= nearestPointD) {
                                root = root.right;
                                nearest(p);
                                break;
                            }
                        }
                        else {
                            root = root.right;
                            nearest(p);
                            break;
                        }
                    }
                }
            }
        }
        return nearestNode.point;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
