package Percolation.src;

public class Percolation {
    int[] siteId;
    boolean[] siteState;
    int[] size;
    public static int openSites = 0;
    static int n;

    // find root method
    private int root(int i) {
        while (i != siteId[i]) {
            siteId[i] = siteId[siteId[i]];
            i = siteId[i];
        }
        return i;
    }

    // union method
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) {return;}
        if (size[i] < size[j]) {
            siteId[i] = j;
            size[j] += size[i];
        }
        else {
            siteId[j] = i;
            size[i] += size[j];
        }
    }

    // find whether sites are connected
    public boolean connected(int p, int q) {
        return siteId[p] == siteId[q];
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int N) {
        n = N;
        if (N <= 0) {
            throw new IllegalArgumentException("Input must be greater than 0");
        } else {
            // n*n for the grid (+2 for topSite and bottomSite)
            siteState = new boolean[(n*n)+2]; // state is set to false by default
            siteId = new int[(n*n)+2];
            size = new int[(n*n)+2];
            for (int i = 0; i < ((n*n)+2); i++) {
                size[i] = 1;
                siteId[i] = i;
            }
            // open topSite and bottomSite
            siteState[(n*n)] = true;
            siteState[(n*n)+1] = true;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isFull(row, col)) { // if site is closed (state is false)
            int targetSite = (row*n) + col; // get the site from the row and column
            siteState[targetSite] = true; // open it
            openSites++; // update the number of open sites
            int upSite = (targetSite - n);
            int downSite = (targetSite + n);
            int leftSite = (targetSite - 1);
            int rightSite = (targetSite + 1);
            // Unite upSite
            if (!(row == 0)) { // if upSite is in grid
                int upRow = row-1;
                int upColumn = col;
                if (isOpen(upRow, upColumn)) { // and if upSite is open
                    union(targetSite, upSite); // unite targetSite with upSite
                }
            }
            // Unite downSite
            if (!(row == (n-1))) { // if downSite is in grid
                int downRow = row+1;
                int downColumn = col;
                if (isOpen(downRow, downColumn)) { // and if downSite is open
                    union(targetSite, downSite); // unite targetSite with downSite
                }
            }
            // Unite leftSite
            if (!(col == 0)) { // if leftSite is in grid
                int leftRow = row;
                int leftColumn = col-1;
                if (isOpen(leftRow, leftColumn)) { // and if leftSite is open
                    union(targetSite, leftSite); // unite targetSite with leftSite
                }
            }
            // Unite rightSite
            if (!(col == (n-1))) { // if rightSite is not in grid
                int rightRow = row;
                int rightColumn = col+1;
                if (isOpen(rightRow, rightColumn)) { // and if rightSite is open
                    union(targetSite, rightSite); // unite targetSite with rightSite
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int site = (row*n) + col;
        if ((row < 0 || col < 0) || (row >= n || col >= n)) {
            throw new IllegalArgumentException("Illegal row or column");
        }
        else {return siteState[site];}
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int site = (row*n) + col;
        if ((row < 0 || col < 0) || (row >= n || col >= n)) {
            throw new IllegalArgumentException("Illegal row or column");
        }
        else {return (!siteState[site]);}
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    public void setOpenSitesToZero() {
        openSites = 0;
    }

    // does the system percolate?
    public boolean percolates() { // unite top and bottom sites with linked opened sites
        int topSite = siteId[((n*n))];
        int bottomSite = siteId[((n*n)+1)];
        for(int c = 0; c < n; c++) { // for each site in the top row
            int r = 0;
            if(isOpen(r, c)) { // if site is open
                int site = (r*n) + c; // find site by its row and column
                union(site, topSite); // unite it with topSite
            }
        }
        for (int c = 0; c < n; c++) { // for each site in the bottom row
            int r = (n-1);
            if(isOpen(r, c)) { // if site is open
                int site = (r*n) + c; // find site by its row and column
                union(site, bottomSite); // unite it with topSite
            }
        }
        return connected(topSite, bottomSite);
    }

    // Get random number method
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}