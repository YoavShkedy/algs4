import Percolation.src.*;
import java.util.ArrayList;

public class PercolationStats {
    static ArrayList<Integer> openSitesList = new ArrayList<>();
    static double trialsNumber;
    static double mean;
    static double mean2;
    static double stdev;
    static double stdevSum;
    static double N;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {throw new IllegalArgumentException("n and trials must be greater than 0");}
        trialsNumber = trials;
        N = n;
        for (int t = 0; t < trials; t++) { // do t trials
            Percolation percolation = new Percolation(n);
            percolation.setOpenSitesToZero(); // set openSites = 0
            while (true) { // open max n*n sites
                int randomRow = Percolation.getRandomNumberInRange(0, (n - 1));
                int randomColumn = Percolation.getRandomNumberInRange(0, (n - 1));
                percolation.open(randomRow, randomColumn);
                if (percolation.percolates()) { // when percolation occurs
                    openSitesList.add(percolation.numberOfOpenSites()); // add the number of openSites to the list
                    break;
                }
            }
        }
        System.out.println("mean = " + mean());
        System.out.println("stddev = " + stddev());
        System.out.println("95% confidence interval = " + "[" + confidenceLo() + ", " + confidenceHi() + "]");
    }

    // sample mean of percolation threshold
    public double mean() {
        int openSitesSum = 0;
        for (int x : openSitesList) {
            openSitesSum += x;
            mean = (openSitesSum/trialsNumber);
        }
        return mean2 = mean/(N*N);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        for (int x : openSitesList) {
            double i = ((x-mean)*(x-mean));
            stdevSum += i;
        }
        stdev = Math.sqrt(stdevSum/(trialsNumber-1));
        return stdev/(N*N);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean - ((1.96*stdev)/(Math.sqrt(trialsNumber))))/(N*N);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean + ((1.96*stdev)/(Math.sqrt(trialsNumber))))/(N*N);
    }

    // test client (see below)
    public static void main(String[] args) {
        int arg0 = Integer.parseInt(args[0]);
        int arg1 = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(arg0, arg1);
    }
}