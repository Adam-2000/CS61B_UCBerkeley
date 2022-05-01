public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        private final T item;
        private Node previous;
        private Node next;

        Node(T item1, Node previous1, Node next1) {
            item = item1;
            previous = previous1;
            next = next1;
        }

    }

    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.previous = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.previous = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.previous, sentinel);
        sentinel.previous.next = newNode;
        sentinel.previous = newNode;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node currNode = sentinel.next;
        while (currNode != sentinel) {
            System.out.print(currNode.item);
            if (currNode.next != sentinel) {
                System.out.print(' ');
            }
            currNode = currNode.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T ret = sentinel.next.item;
        sentinel.next.next.previous = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return ret;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T ret = sentinel.previous.item;
        sentinel.previous.previous.next = sentinel;
        sentinel.previous = sentinel.previous.previous;
        size -= 1;
        return ret;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node cur = sentinel.next;
        while (index > 0) {
            cur = cur.next;
            index -= 1;
        }
        return cur.item;
    }

    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return getRecursive(sentinel.next, index);
    }

    private T getRecursive(Node node, int index) {
        if (index == 0) {
            return node.item;
        }
        if (node.next == null) {
            return null;
        }
        return getRecursive(node.next, index - 1);
    }
}
