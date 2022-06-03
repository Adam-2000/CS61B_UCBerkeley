import edu.princeton.cs.algs4.Queue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> ret = new Queue<>();
        for (Item e : items) {
            Queue<Item> newQ = new Queue<>();
            newQ.enqueue(e);
            ret.enqueue(newQ);
        }
        return ret;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> ret = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            ret.enqueue(getMin(q1, q2));
        }
        while (!ret.isEmpty()) {
            q2.enqueue(ret.dequeue());
        }
        return q2;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        int N = items.size();
        if (N == 1) {
            return items;
        }
        Queue<Item> q1 = new Queue<>();
        for (int cnt = 0; cnt < N / 2; cnt++) {
            q1.enqueue(items.dequeue());
        }
        items = mergeSortedQueues(mergeSort(q1), mergeSort(items));
        return items;
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
        Queue<Integer> answerQ = mergeSort(inQ);
        for (int i = 0; i < 10; i++) {
            assertEquals((Integer) i, inQ.dequeue());
        }
    }
}
