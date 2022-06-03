import edu.princeton.cs.algs4.Queue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        // Your code here!
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        if (items.size() <= 1) {
            return items;
        }
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        Item pivot = getRandomItem(items);
        for (Item e : items) {
            int compInt = e.compareTo(pivot);
            if (compInt < 0) {
                less.enqueue(e);
            } else if (compInt == 0) {
                equal.enqueue(e);
            } else {
                greater.enqueue(e);
            }
        }
        less = quickSort(less);
        greater = quickSort(greater);
        return catenate(catenate(less, equal), greater);
    }

    @Test
    public void main() {
        Queue<Integer> inQ = new Queue<>();
        inQ.enqueue(3);
        inQ.enqueue(1);
        inQ.enqueue(8);
        inQ.enqueue(0);
        inQ.enqueue(4);
        inQ.enqueue(7);
        inQ.enqueue(2);
        inQ.enqueue(6);
        inQ.enqueue(5);
        inQ.enqueue(9);
        Queue<Integer> answerQ = quickSort(inQ);
        for (int i = 0; i < 10; i++) {
            assertEquals((Integer) i, answerQ.dequeue());
        }
    }
}
