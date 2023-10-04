import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Objects;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = StdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int n = s.length();
        char[] t = new char[n];
        int first = 0;
        for (int i = 0; i < n; i++) {
            t[i] = csa.lastChar(i);
            if (csa.index(i) == 0) {
                first = i;
            }
        }
        BinaryStdOut.write(first, 8);
        for (char c : t) { BinaryStdOut.write(c, 8); }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt(8);
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] t = s.toCharArray();
        char[] firstCol = t.clone();
        Arrays.sort(firstCol);
        int[] next = new int[n];
        boolean[] nextMarked = new boolean[n];
        // deduce the first column of the sorted suffixes from t[]
        // and reconstruct the next[] array
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (firstCol[i] == t[j]) {
                    if (!nextMarked[j]) {
                        next[i] = j;
                        nextMarked[j] = true;
                        break;
                    }
                }
            }
        }
        // reconstruct the original input string
        StdOut.print(firstCol[first]);
        for (int i = next[first]; i != first; i = next[i]) {
            StdOut.print(firstCol[i]);
        }
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (Objects.equals(args[0], "-")) {
            transform();
        } else if (Objects.equals(args[0], "+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException(
                    "Please write '-' for transform or '+' for reverse transform");
        }
    }
}