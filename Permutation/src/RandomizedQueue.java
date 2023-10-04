import java.lang.*;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rq;
    private int N = 0;

    // resize array once it's full
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = rq[i];
        }
        rq = copy;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        rq = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (N == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {throw new IllegalArgumentException();}
        if (N == rq.length) {
            resize(2 * rq.length);
        }
        rq[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {throw new java.util.NoSuchElementException();}
        if (N > 0 && N == rq.length/4) {
            resize(rq.length / 2);
        }
        StdRandom.shuffle(rq, 0, N);
        Item item = rq[--N];
        rq[N] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {throw new java.util.NoSuchElementException();}
        Item item = rq[StdRandom.uniform(N)];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        StdRandom.shuffle(rq, 0, N);
        return new RandomArrayIterator();
    }

    private class RandomArrayIterator implements Iterator<Item> {
        private int i = N;

        public boolean hasNext() {
            return N > 0;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (isEmpty()) {throw new java.util.NoSuchElementException();}
            return rq[--N];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String[] cars = {"BMW", "Ferrari", "Porsche", "Toyota", "Tesla", "Mercedes", "Audi", "Mazda", "Hyundai"};
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        System.out.println("A list has been made. The list is empty. Let's add some cars to it:");
        for (String x : cars) {
            rq.enqueue(x);
            System.out.println(x + " has been added to the list!");
        }
        System.out.println("The list now contains " + rq.size() + " cars.");
        System.out.println("An example for a car in the list: " + rq.sample());
        System.out.println("Now, each time let's try to take a couple random cars off the list:");
        Iterator<String> iterator = rq.iterator();
        while(iterator.hasNext()) {
            String removedCar = rq.dequeue();
            System.out.println(removedCar + " has been removed from the list!");
            System.out.println("The list now contains " + rq.size() + " cars.");
        }
        System.out.println("The list is empty now!");
    }
}