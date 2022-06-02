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
    private int[] oldEdgeTo;
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        oldEdgeTo = new int[m.V()];
    }

    private void dfs(int v, int parent) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (w != parent) {
                if (!marked[w]) {
                    oldEdgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    announce();
                    dfs(w, v);
                    if (targetFound != -1) {
                        return;
                    }
                } else {
                    targetFound = w;
                    oldEdgeTo[w] = v;
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
                        backTrackCircle();
                        return;
                    }
                }
            }
        }
    }

    // Helper methods go here
    private void backTrackCircle() {
        int curr = edgeTo[targetFound] = oldEdgeTo[targetFound];
        while (curr != targetFound) {
            curr = edgeTo[curr] = oldEdgeTo[curr];
            announce();
        }
    }
}

