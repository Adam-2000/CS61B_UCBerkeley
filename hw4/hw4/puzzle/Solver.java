package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.HashSet;

public class Solver {
    private class NodeWorldState {
        private final WorldState w;
        private final NodeWorldState parent;
        private final int d;
        private final int priority;
        private NodeWorldState(WorldState w, NodeWorldState parent, int d, int h) {
            this.w = w;
            this.parent = parent;
            this.d = d;
            this.priority = d + h;
        }
    }
    private class ComparatorWorldState implements Comparator<NodeWorldState> {
        @Override
        public int compare(NodeWorldState o1, NodeWorldState o2) {
            return o1.priority - o2.priority;
        }
    }
    private MinPQ pq;
    private NodeWorldState nodeGoal;
    public Solver(WorldState initial) {
        pq = new MinPQ<NodeWorldState>(new ComparatorWorldState());
        nodeGoal = null;
        pq.insert(new NodeWorldState(initial, null, 0, initial.estimatedDistanceToGoal()));
        HashSet<WorldState> marks = new HashSet<>();
        while (!pq.isEmpty()) {
            NodeWorldState currNode = (NodeWorldState) pq.delMin();
            WorldState currState = currNode.w;
            marks.add(currState);
            if (currState.isGoal()) {
                nodeGoal = currNode;
                return;
            }
            for (WorldState neighbor : currState.neighbors()) {
                if (currNode.parent == null || !currNode.parent.w.equals(neighbor)) {
                    if (!marks.contains(neighbor)) {
                        pq.insert(new NodeWorldState(neighbor, currNode, currNode.d + 1,
                                neighbor.estimatedDistanceToGoal()));
                    }
                }
            }
        }
    }

    public int moves() {
        if (nodeGoal != null) {
            return nodeGoal.d;
        }
        return -1;
    }
    public Iterable<WorldState> solution() {
        NodeWorldState currNode = nodeGoal;
        Stack<WorldState> sol = new Stack<>();
        while (currNode != null) {
            sol.push(currNode.w);
            currNode = currNode.parent;
        }
        return sol;
    }
}
