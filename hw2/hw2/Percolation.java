package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF union;
    private final WeightedQuickUnionUF unionCopy;
    private final boolean[][] gridIsOpen;
    private int idFullOpen;
    private int numOpenSite;
    private final int N;
    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        gridIsOpen = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                gridIsOpen[i][j] = false;
            }
        }
        idFullOpen = -1;
        numOpenSite = 0;
        this.N = N;
        union = new WeightedQuickUnionUF(N * N);
        unionCopy = new WeightedQuickUnionUF(N * N);
        for (int i = N * (N - 1); i < N * N - 1; i++) {
            unionCopy.union(i, i + 1);
        }
    }
    private int index2to1(int row, int col) {
        return row * N + col;
    }

    public void open(int row, int col) { // open the site (row, col) if it is not open already
        checkIndexBounded(row, col);
        if (gridIsOpen[row][col]) {
            return;
        }
        int idx21 = index2to1(row, col);
        if (row - 1 >= 0 && gridIsOpen[row - 1][col]) {
            union.union(idx21, index2to1(row - 1, col));
            unionCopy.union(idx21, index2to1(row - 1, col));
        }
        if (row + 1 < N && gridIsOpen[row + 1][col]) {
            union.union(idx21, index2to1(row + 1, col));
            unionCopy.union(idx21, index2to1(row + 1, col));
        }
        if (col - 1 >= 0 && gridIsOpen[row][col - 1]) {
            union.union(idx21, index2to1(row, col - 1));
            unionCopy.union(idx21, index2to1(row, col - 1));
        }
        if (col + 1 < N && gridIsOpen[row][col + 1]) {
            union.union(idx21, index2to1(row, col + 1));
            unionCopy.union(idx21, index2to1(row, col + 1));
        }
        int newRoot = union.find(idx21);
        if (idFullOpen == -1) {
            if (row == 0) {
                idFullOpen = newRoot;
            }
        } else {
            if (union.find(idFullOpen) == newRoot) {
                idFullOpen = newRoot;
            } else if (row == 0) {
                union.union(idx21, idFullOpen);
                unionCopy.union(idx21, idFullOpen);
                idFullOpen = union.find(idFullOpen);
            }
        }
        gridIsOpen[row][col] = true;
        numOpenSite++;
    }
    public boolean isOpen(int row, int col) { // is the site (row, col) open?
        checkIndexBounded(row, col);
        return gridIsOpen[row][col];
    }
    public boolean isFull(int row, int col) { // is the site (row, col) full?
        checkIndexBounded(row, col);
        if (idFullOpen == -1) {
            return false;
        }
        return union.find(index2to1(row, col)) == idFullOpen;
    }
    private void checkIndexBounded(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }
    public int numberOfOpenSites() { // number of open sites
        return numOpenSite;
    }
    public boolean percolates() { // does the system percolate?
//        for (int idx = (N - 1) * N; idx < N * N; idx++) {
//            if (union.find(idx) == idFullOpen) {
//                return true;
//            }
//        }
//        return false;
        if (idFullOpen == -1) {
            return false;
        }
        return unionCopy.connected(N * N - 1, idFullOpen);

    }
    public static void main(String[] args) { // use for unit testing (not required)
        return;
    }
}
