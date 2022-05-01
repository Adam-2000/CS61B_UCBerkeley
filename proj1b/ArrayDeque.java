public class ArrayDeque<T> implements Deque<T> {

    private static final int RFACTOR = 2;
    private static final double USAGERATIO = 0.25;
    private static final int INITLENGTH = 8;
    private int size;
    private int afterFrontIdx;
    private int tailIdx;
    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[INITLENGTH];
        size = 0;
    }

    private void resize(int cap) {
        T[] newList = (T[]) new Object[cap];
        if (size == 0) {
            items = newList;
            return;
        }
        int cur = tailIdx;
        int newIdx = 0;
        do {
            newList[newIdx++] = items[cur];
            items[cur++] = null;
            if (cur == items.length) {
                cur = 0;
            }
        } while (cur != afterFrontIdx);
        tailIdx = 0;
        afterFrontIdx = size;
        items = newList;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(RFACTOR * items.length);
        }
        items[afterFrontIdx++] = item;
        if (afterFrontIdx == items.length) {
            afterFrontIdx = 0;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(RFACTOR * items.length);
        }
        if (--tailIdx < 0) {
            tailIdx = items.length - 1;
        }
        items[tailIdx] = item;
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
        if (size == 0) {
            return;
        }
        int cur = afterFrontIdx == 0 ? items.length - 1 : afterFrontIdx - 1;
        while (cur != tailIdx) {
            System.out.print(items[cur--]);
            System.out.print(' ');
            if (cur < 0) {
                cur = items.length - 1;
            }
        }
        System.out.print(items[cur]);
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (--afterFrontIdx < 0) {
            afterFrontIdx = items.length - 1;
        }
        T ret = items[afterFrontIdx];
        items[afterFrontIdx] = null;
        size -= 1;
        if (size < USAGERATIO * items.length && items.length >= RFACTOR * INITLENGTH) {
            resize(items.length / RFACTOR);
        }
        return ret;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T ret = items[tailIdx];
        items[tailIdx++] = null;
        if (tailIdx == items.length) {
            tailIdx = 0;
        }
        size -= 1;
        if (size < USAGERATIO * items.length && items.length >= RFACTOR * INITLENGTH) {
            resize(items.length / RFACTOR);
        }
        return ret;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int idx = afterFrontIdx - 1 - index;
        if (idx < 0) {
            idx += items.length;
        }
        return items[idx];
    }

}
