package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    private class Qnode implements Comparable {
        private final int v;
        private final int priority;
        Qnode(int v, int p) {
            this.v = v;
            this.priority = p;
        }
        @Override
        public int compareTo(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return -1;
            }
            return this.priority - ((Qnode) o).priority;
        }
    }
    /** Finds vertex estimated to be closest to target. */
    private int popMinimumUnmarked() {
        return -1;
    }

    /** Performs an A star search from vertex s. */
    private void astar() {
        MinPQ<Qnode> pq = new MinPQ<>();
        pq.insert(new Qnode(s, 0 + h(s)));
        marked[s] = true;
        announce();
        if (s == t) {
            return;
        }
        while (!pq.isEmpty()) {
            Qnode node = pq.delMin();
            int v = node.v;
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    announce();
                    if (w == t) {
                        return;
                    }
                    pq.insert(new Qnode(w, distTo[w] + h(w)));
                }
            }
        }
    }

    @Override
    public void solve() {
        astar();
    }

}

