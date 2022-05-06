package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
public class PercolationStats {
    private final double[] thresholds;
    private static final long SEED = 12345;
    private final int T;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        // perform T independent experiments on an N-by-N grid
        thresholds = new double[T];
        this.T = T;
        StdRandom.setSeed(SEED);
        Percolation percolation;
        for (int t = 0; t < T; t++) {
            percolation = pf.make(N);
            do {
                percolation.open(StdRandom.uniform(N), StdRandom.uniform(N));
            } while (!percolation.percolates());
            thresholds[t] = (double) percolation.numberOfOpenSites() / (N * N);
        }
    }
    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(thresholds);
    }
    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(thresholds);
    }
    public double confidenceLow() { // low endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }
    public double confidenceHigh() { // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }
}
