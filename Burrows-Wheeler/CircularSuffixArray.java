import java.util.Arrays;

public class CircularSuffixArray {
    private int N;
    private Suffix[] suffixes;

    private static class Suffix implements Comparable<Suffix> {
        private final String text;
        private final int index;

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }
        private int length() {
            return text.length();
        }
        private char charAt(int i) {
            return text.charAt(i);
        }

        public int compareTo(Suffix that) {
            if (this == that) return 0;
            for (int i = 0; i < this.length(); i++) {
                if (this.text.charAt(i) < that.text.charAt(i)) { return -1; }
                if (this.text.charAt(i) > that.text.charAt(i)) { return +1; }
            }
            return this.length() - that.length();
        }

        public String toString() {
            return text;
        }
    }

    /** Creates a circular suffix array for String s **/
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Please provide a string");
        }
        N = s.length();
        suffixes = new Suffix[N];
        StringBuilder sb = new StringBuilder(N-1);
        for (int i = N-1; i >= 0; i--) {
            sb.append(s, i, N);
            if (i != 0) { sb.append(s, 0, i); }
            suffixes[i] = new Suffix(sb.toString(), i);
            sb = new StringBuilder(N-1);
        }
        Arrays.sort(suffixes);
    }

    /** Returns the length of String s **/
    public int length() {
        return N;
    }

    /** Returns the index of the ith sorted suffix **/
    public int index(int i) {
        if (i < 0  || i > length()-1) {
            throw new IllegalArgumentException("Argument is out of range");
        }
        return suffixes[i].index;
    }

    /** Returns the last character of the suffix at the given index **/
    public char lastChar(int indexOfSuffix) {
        return suffixes[indexOfSuffix].charAt(length()-1);
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        System.out.println("Length of string: " + csa.length());
        for (int i = 0; i < csa.length(); i++) {
            System.out.println("index of " + i + " sorted suffix: " + csa.index(i));
        }
    }
}