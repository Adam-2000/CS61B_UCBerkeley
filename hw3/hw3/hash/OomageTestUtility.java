package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        for (int i = 0; i < M; i++) {
            buckets[i] = 0;
        }
        final int N = oomages.size();
        for (Oomage oomage : oomages) {
            buckets[(oomage.hashCode() & 0x7FFFFFFF) % M] += 1;
        }
        for (int n : buckets) {
            if (n <= 0.02 * N || n >= 0.4 * N) {
                return false;
            }
        }
        return true;
    }
}
