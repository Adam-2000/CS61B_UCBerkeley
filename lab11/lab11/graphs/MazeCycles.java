package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int targetFound = -1;
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    private void dfs(int v, int parent) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (w != parent) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    announce();
                    dfs(w, v);
                    if (targetFound != -1) {
                        return;
                    }
                } else {
                    targetFound = w;
                    distTo[w] = distTo[v] + 1;
                    announce();
                    return;
                }
            }
        }
    }

    @Override
    public void solve() {
        for (int i = 1; i <= maze.N(); i++) {
            for (int j = 1; j <= maze.N(); j++) {
                int v = maze.xyTo1D(i, j);
                if (!marked[v]) {
                    distTo[v] = 0;
                    dfs(v, -1);
                    if (targetFound != -1) {
                        backTrackCircle(targetFound, -1);
                        return;
                    }
                }
            }
        }
    }

    // Helper methods go here
    private boolean backTrackCircle(int v, int parent) {
        if (v == targetFound && parent != -1) {
            return true;
        }
        for (int w : maze.adj(v)) {
            if (w != parent) {
                if (marked[w]) {
                    if (backTrackCircle(w, v)) {
                        edgeTo[w] = v;
                        announce();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

