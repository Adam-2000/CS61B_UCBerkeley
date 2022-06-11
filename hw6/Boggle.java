import edu.princeton.cs.algs4.Heap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Boggle {
    
    // File path of dictionary file
    static final String dictPath = "words.txt";
    static final TrieSet trie = new TrieSet(dictPath);
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
        List<String> list = new ArrayList<>();
        In in = new In(boardFilePath);
        String[] lines = in.readAllLines();
        int M = lines.length;
        int N = lines[0].length();
        char[][] board = new char[M][N];
        boolean[][] mask = new boolean[M][N];
        MinPQ<String> pq = new MinPQ<String>(comparator);
        TrieSet.Node node = trie.getRoot();

    }

    private static void solve(int k, String curString, char[][] board, boolean[][] mask, int[] parentDir, int x, int y) {
        int[][] directions= {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int M = board.length;
        int N = board[0].length;
        mask[x][y] = true;
        for (int[] dirXY : directions) {
            int newX = x + dirXY[0];
            int newY = y + dirXY[1];
            if (newX < 0 || newX >= M || newY < 0 || newY >= N) {
                continue;
            }
            if (mask[newX][newY]) {
                continue;
            }
        }
    }
}
