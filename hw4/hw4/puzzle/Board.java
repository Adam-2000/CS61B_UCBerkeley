package hw4.puzzle;

import java.util.ArrayList;

public class Board implements WorldState {
    private int[][] tiles;
    private int N;
    private int holeX;
    private int holeY;
    private int cacheEstimatedDistanceToGoal;
    public Board(int[][] tiles) {
        N  = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    holeX = i;
                    holeY = j;
                }
            }
        }
        cacheEstimatedDistanceToGoal = -1;
    }
    public int tileAt(int i, int j) {
        if (i >= 0 && i < N && j >= 0 && j < N) {
            return tiles[i][j];
        }
        throw new java.lang.IndexOutOfBoundsException();
    }
    public int size() {
        return N;
    }
    @Override
    public Iterable<WorldState> neighbors() {
        ArrayList<WorldState> ret = new ArrayList<>();
        int[][] ijList = {{holeX - 1, holeY}, {holeX + 1, holeY},
                          {holeX, holeY - 1}, {holeX, holeY + 1}};
        int[][] copyTiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                copyTiles[i][j] = tiles[i][j];
            }
        }
        for (int[] ijPair : ijList) {
            int i = ijPair[0];
            int j = ijPair[1];
            if (i >= 0 && i < N && j >= 0 && j < N) {
                int movedValue = copyTiles[i][j];
                copyTiles[holeX][holeY] = movedValue;
                copyTiles[i][j] = 0;
                ret.add(new Board(copyTiles));
                copyTiles[holeX][holeY] = 0;
                copyTiles[i][j] = movedValue;
            }
        }
        return ret;
    }
    public int hamming() {
        int ret = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = tiles[i][j];
                if (value != 0) {
                    if (i * N + j != value - 1) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }
    private static int abs(int x) {
        if (x >= 0) {
            return x;
        } else {
            return -x;
        }
    }
    public int manhattan() {
        int ret = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = tiles[i][j];
                if (value != 0) {
                    int corrX = (value - 1) / N;
                    int corrY = (value - 1) % N;
                    ret += abs(corrX - i) + abs(corrY - j);
                }
            }
        }
        return ret;
    }
    @Override
    public int estimatedDistanceToGoal() {
        if (cacheEstimatedDistanceToGoal == -1) {
            cacheEstimatedDistanceToGoal = manhattan();
        }
        return cacheEstimatedDistanceToGoal;
    }
    @Override
    public boolean equals(Object y) {
        if (this.getClass() != y.getClass()) {
            return false;
        }
        if (y == null) {
            return false;
        }
        if (N != ((Board) y).N) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != ((Board) y).tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int res = 0;
        int cnt = 0;
        for (int[] line : tiles) {
            for (int v : line) {
                res += cnt * 31 + v;
            }
        }
        return res;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        // int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
