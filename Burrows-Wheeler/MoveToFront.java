import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.Objects;

public class MoveToFront {
    private final static int R = 256;
    private static LinkedList<Character> encodeList = new LinkedList<>();
    private static LinkedList<Character> decodeList = new LinkedList<>();

    private static void initEncodeList() {
        encodeList = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            encodeList.add((char) i);
        }
    }

    private static void initDecodeList() {
        decodeList = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            decodeList.add((char) i);
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        initEncodeList();
        while (!StdIn.isEmpty()) {
            char c = StdIn.readChar();
            BinaryStdOut.write(encodeList.indexOf(c), 8);
            encodeList.remove(c);
            encodeList.addFirst(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        initDecodeList();
        while (!BinaryStdIn.isEmpty()) {
            char c = decodeList.get(BinaryStdIn.readInt(8));
            BinaryStdOut.write(c, 8);
            decodeList.remove(c);
            decodeList.addFirst(c);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (Objects.equals(args[0], "-")) {
            encode();
        } else if (Objects.equals(args[0], "+")) {
            decode();
        } else {
            throw new IllegalArgumentException(
                    "Please write '-' for encoding or '+' for decoding");
        }
    }
}