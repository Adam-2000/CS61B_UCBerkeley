import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    static TrieSet trie = new TrieSet(dictPath);
    public void setDictPath(String path) {
        dictPath = path;
        trie = new TrieSet(path);
    }
    public static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1 == null || o2 == null) {
                throw new IllegalArgumentException();
            }
            int lengthDif = o1.length() - o2.length();
            if (lengthDif != 0) {
                return lengthDif;
            }
            return -o1.compareTo(o2);
        }
    }
    private static StringComparator comparator = new StringComparator();

    private static class MinHeapSet {
        private int capacity;
        private MinPQ<String> pq = new MinPQ<>(comparator);
        private HashSet<String> set = new HashSet<>();
        MinHeapSet(int k) {
            capacity = k;
        }
        public void add(String s) {
            if (!set.contains(s)) {
                set.add(s);
                pq.insert(s);
                if (pq.size() > capacity) {
                    set.remove(pq.delMin());
                }
            }
        }

        public boolean isEmpty() {
            return pq.isEmpty();
        }
        public int size() {
            return pq.size();
        }
        public String pop() {
            String ret = pq.delMin();
            set.remove(ret);
            return ret;
        }
    }
    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        LinkedList<String> list = new LinkedList<>();
        In in = new In(boardFilePath);
        String[] lines = in.readAllLines();
        int M = lines.length;
        int N = lines[0].length();
        char[][] board = new char[M][N];
        boolean[][] mask = new boolean[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = lines[i].charAt(j);
                mask[i][j] = false;
            }
        }
        MinHeapSet heapSet = new MinHeapSet(k);
        final TrieSet.Node root = trie.getRoot();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                solve("", board, mask, i, j, root, heapSet);
            }
        }
        while (!heapSet.isEmpty()) {
            list.addFirst(heapSet.pop());
        }
        return list;
    }

    private static void add2Q(MinPQ<String> pq, String s, int k) {
        pq.insert(s);
        if (pq.size() > k) {
            pq.delMin();
        }
    }
    private static void solve(String curString, final char[][] board, final boolean[][] mask,
                              final int x, final int y, final TrieSet.Node parent,
                              final MinHeapSet hs) {
        int M = board.length;
        int N = board[0].length;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        if (x < 0 || x >= M || y < 0 || y >= N) {
            return;
        }
        if (mask[x][y]) {
            return;
        }
        char c = board[x][y];
        if (!parent.contains(c)) {
            return;
        }
        mask[x][y] = true;
        TrieSet.Node node = parent.get(c);
        curString += c;
        if (node.exists() && curString.length() >= 3) {
            hs.add(curString);
        }
        for (int[] dirXY : directions) {
            int newX = x + dirXY[0];
            int newY = y + dirXY[1];
            solve(curString, board, mask, newX, newY, node, hs);
        }
        mask[x][y] = false;
    }
}
