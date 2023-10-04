import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class SeamCarver {
    private Picture pic;
    private int[] pixelTo;
    private double[] distTo;
    private double[] pixelEnergy;
    private IndexMinPQ<Double> pq;
    private static boolean energySet = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {throw new IllegalArgumentException();}
        pic = picture;
        pixelTo = new int[width()*height()];
        distTo = new double[width()*height()];
        pixelEnergy = new double[width()*height()];
        pq = new IndexMinPQ<Double>(width()*height());
    }

    //  to find pixels on grid, provided (x,y)
    private int pixelNumber(int x, int y) {
        return y*width() + x;
    }

    // find number of column given number of pixel
    private int colFromNumber(int pixelNumber) {
        return pixelNumber / width();
    }

    // find number of row given number of pixel
    private int rowFromNumber(int pixelNumber) {
        return pixelNumber % width();
    }

    // set the energy of the pixels
    private void setEnergy() {
        for (int row = 0; row < width(); row++) {
            for (int col = 0; col < height(); col++) {
                // set border pixels energy
                if (row == 0 ||
                        col == 0 ||
                        row == width() - 1 ||
                        col == height() - 1) {
                    pixelEnergy[pixelNumber(row, col)] = 1000;
                    distTo[pixelNumber(row, col)] = 1000;
                }
                // calculate and set energy of inner pixels
                else {
                    pixelEnergy[pixelNumber(row, col)] = calculateEnergy(row, col);
                    distTo[pixelNumber(row, col)] = Double.POSITIVE_INFINITY;
                }
                System.out.println("This is the pixel (" + row + ", " + col + ") energy " +
                        pixelEnergy[pixelNumber(row, col)]);
            }
        }
        energySet = true;
    }

    // calculate the energy of a pixel
    private double calculateEnergy(int x, int y) {
        Color rightColor = new Color(pic.getRGB(x+1, y));
        Color leftColor = new Color(pic.getRGB(x-1, y));
        int [] rightRGB = {rightColor.getRed(), rightColor.getGreen(), rightColor.getBlue()};
        int [] leftRGB = {leftColor.getRed(), leftColor.getGreen(), leftColor.getBlue()};
        Color upColor = new Color(pic.getRGB(x, y-1));
        Color downColor = new Color(pic.getRGB(x, y+1));
        int [] upRGB = {upColor.getRed(), upColor.getGreen(), upColor.getBlue()};
        int [] downRGB = {downColor.getRed(), downColor.getGreen(), downColor.getBlue()};
        int result = 0;
        for (int i = 0; i < 3; i++) {
            result += ((rightRGB[i] - leftRGB[i])^2 + (upRGB[i] - downRGB[i])^2);
        }
        return Math.sqrt(result);
    }

    private void relax(int source, int pixel) {
        if (distTo[pixel] > distTo[source] + pixelEnergy[pixel]) {
            distTo[pixel] = distTo[source] + pixelEnergy[pixel];
            pixelTo[pixel] = source;
            // update pq
            if (pq.contains(pixel)) {
                pq.decreaseKey(pixel, distTo[pixel]);
            } else {
                pq.insert(pixel, distTo[pixel]);
            }
        }
    }

    // set vertical grid
    private void setVerticalGrid() {
        int source = 0;
        for (int row = 0; row < width(); row++) {
            source = pixelNumber(row, 0);
            distTo[source] = 0.0;
            pq.insert(source, 0.0);
            while (!pq.isEmpty()) {
                int pixel = pq.delMin();
                if (colFromNumber(pixel) == height()) {break;}
                // relax adj pixels
                if (rowFromNumber(pixel) == 0) { // relax bottom and bottom-right
                    int bottomPixel = pixelNumber(rowFromNumber(pixel), colFromNumber(pixel)+1);
                    int bottomRPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel)+1);
                    relax(pixel, bottomPixel);
                    relax(pixel, bottomRPixel);
                }
                if (rowFromNumber(pixel) == width()) { // relax bottom and bottom-left
                    int bottomLPixel = pixelNumber(rowFromNumber(pixel)-1, colFromNumber(pixel)+1);
                    int bottomPixel = pixelNumber(rowFromNumber(pixel), colFromNumber(pixel)+1);
                    relax(pixel, bottomPixel);
                    relax(pixel, bottomLPixel);
                }
                else { // relax all 3 adj pixels
                    int bottomPixel = pixelNumber(rowFromNumber(pixel), colFromNumber(pixel)+1);
                    int bottomLPixel = pixelNumber(rowFromNumber(pixel)-1, colFromNumber(pixel)+1);
                    int bottomRPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel)+1);
                    relax(pixel, bottomLPixel);
                    relax(pixel, bottomPixel);
                    relax(pixel, bottomRPixel);
                }
            }
        }
        // clean pq
        for (int i : pq) {
            pq.delete(i);
        }
    }

    // set horizontal grid
    private void setHorizontalGrid() {
        int source = 0;
        for (int col = 0; col < height(); col++) {
            source = pixelNumber(0, col);
            distTo[source] = 0.0;
            pq.insert(source, 0.0);
            while (!pq.isEmpty()) {
                int pixel = pq.delMin();
                if (rowFromNumber(pixel) == width()) {break;}
                // relax adj pixels
                if (colFromNumber(pixel) == 0) { // relax right and bottom-right
                    int rightPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel));
                    int bottomRPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel)+1);
                    relax(pixel, rightPixel);
                    relax(pixel, bottomRPixel);
                }
                if (colFromNumber(pixel) == height()) { // relax right and top-right
                    int rightPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel));
                    int topRPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel)-1);
                    relax(pixel, rightPixel);
                    relax(pixel, topRPixel);
                }
                else { // relax all 3 adj pixels
                    int rightPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel));
                    int bottomRPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel)+1);
                    int topRPixel = pixelNumber(rowFromNumber(pixel)+1, colFromNumber(pixel)-1);
                    relax(pixel, rightPixel);
                    relax(pixel, bottomRPixel);
                    relax(pixel, topRPixel);
                }
            }
        }
        // clean pq
        for (int i : pq) {
            pq.delete(i);
        }
    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return pixelEnergy[pixelNumber(x, y)];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (!energySet) {setEnergy();}
        setVerticalGrid();
        int[] bestSeam = new int[height()];
        double bestEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width(); i++) {
            int[] currentSeam = new int[height()];
            Stack<Integer> seamStack = new Stack<>();
            seamStack.push(rowFromNumber(pixelNumber(i, height()-1)));
            double currentSeamEnergy = energy(i, height()-1);
            int fromPixel = pixelTo[pixelNumber(i, height()-1)];
            for (int j = 0; j < height(); j++) {
                seamStack.push(rowFromNumber(fromPixel));
                currentSeamEnergy += pixelEnergy[fromPixel];
                fromPixel = pixelTo[fromPixel];
            }
            for (int k = 0; k < currentSeam.length; k++) {
                currentSeam[k] = seamStack.pop();
            }
            if (bestEnergy > currentSeamEnergy) {
                bestSeam = currentSeam;
            }
        }
        return bestSeam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!energySet) {setEnergy();}
        setHorizontalGrid();
        int[] bestSeam = new int[width()];
        double bestEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height(); i++) {
            int[] currentSeam = new int[width()];
            Stack<Integer> seamStack = new Stack<>();
            seamStack.push(colFromNumber(pixelNumber(width()-1, i)));
            double currentSeamEnergy = energy(width()-1, i);
            int fromPixel = pixelTo[pixelNumber(width()-1, i)];
            for (int j = 0; j < width(); j++) {
                seamStack.push(colFromNumber(fromPixel));
                currentSeamEnergy += pixelEnergy[fromPixel];
                fromPixel = pixelTo[fromPixel];
            }
            for (int k = 0; k < currentSeam.length; k++) {
                currentSeam[k] = seamStack.pop();
            }
            if (bestEnergy > currentSeamEnergy) {
                bestSeam = currentSeam;
            }
        }
        return bestSeam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {throw new IllegalArgumentException();}
        if (seam.length != height()) {throw new IllegalArgumentException();}
        for (int i = 0; i < seam.length; i++) {
            int difference = Math.abs(seam[i+1] - seam[i]);
            if (difference > 1 || difference < 0) {throw new IllegalArgumentException();}
        }
        Picture updatedPicture = new Picture(width()-1, height());
        double[] updatedPicEnergy = new double[(width() - 1) * height()];
        // restore left side
        int pixelToRemove;
        for (int col = 0; col < updatedPicture.height(); col++) {
            pixelToRemove = seam[col];
            for (int row = 0; row < updatedPicture.width(); row++) {
                if (row == pixelToRemove) {break;}
                updatedPicture.set(col, row, pic.get(col, row));
                updatedPicEnergy[pixelNumber(row, col)] = pixelEnergy[pixelNumber(row, col)];
            }
        }
        // restore right side
        for (int col = 0; col < updatedPicture.height(); col++) {
            pixelToRemove = seam[col];
            for (int row = pixelToRemove+1; row < updatedPicture.width(); row++) {
                updatedPicture.set(col, row, pic.get(col, row));
                updatedPicEnergy[pixelNumber(row, col)] = pixelEnergy[pixelNumber(row, col)];
            }
        }
        // update variables
        pixelEnergy = updatedPicEnergy;
        pic = updatedPicture;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {throw new IllegalArgumentException();}
        if (seam.length != width()) {throw new IllegalArgumentException();}
        for (int i = 0; i < seam.length; i++) {
            int difference = Math.abs(seam[i+1] - seam[i]);
            if (difference > 1 || difference < 0) {throw new IllegalArgumentException();}
        }
        Picture updatedPicture = new Picture(width(), height()-1);
        double[] updatedPicEnergy = new double[(width()) * (height()-1)];
        // restore upper side
        int pixelToRemove;
        for (int row = 0; row < updatedPicture.width(); row++) {
            pixelToRemove = seam[row];
            for (int col = 0; col < updatedPicture.height(); col++) {
                if (col == pixelToRemove) {break;}
                updatedPicture.set(row, col, pic.get(row, col));
                updatedPicEnergy[pixelNumber(col, row)] = pixelEnergy[pixelNumber(col, row)];
            }
        }
        // restore bottom side
        for (int row = 0; row < updatedPicture.width(); row++) {
            pixelToRemove = seam[row];
            for (int col = pixelToRemove+1; col < updatedPicture.height(); col++) {
                updatedPicture.set(row, col, pic.get(row, col));
                updatedPicEnergy[pixelNumber(col, row)] = pixelEnergy[pixelNumber(row, col)];
            }
        }
        // update variables
        pixelEnergy = updatedPicEnergy;
        pic = updatedPicture;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver seamCarver = new SeamCarver(new Picture
                (new File("C:\\Users\\dell\\Seam Carving\\src\\6x5.png")));
        int[] verticalSeam = seamCarver.findVerticalSeam();
        System.out.println("This is the lowest energy vertical seam: " + Arrays.toString(verticalSeam));
        System.out.println("We are going to remove it.");
        seamCarver.removeVerticalSeam(verticalSeam);
        System.out.println("Vertical seam has been removed!");
    }
}