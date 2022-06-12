import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return left == null;
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
    Node root;
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char c : frequencyTable.keySet()) {
            pq.insert(new Node(c, frequencyTable.get(c), null, null));
        }
        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        root = pq.delMin();
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node cur = root;
        BitSequence prefixSeq = new BitSequence();
        for (int i = 0; i < querySequence.length(); i++) {
            int b = querySequence.bitAt(i);
            if (!cur.isLeaf()) {
                prefixSeq = prefixSeq.appended(b);
                cur = b == 0 ? cur.left : cur.right;
            } else {
                break;
            }
        }
        return new Match(prefixSeq, cur.ch);
    }
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> retMap = new HashMap<>();
        buildLookupTable(retMap, root, new BitSequence());
        return retMap;
    }

    private void buildLookupTable(Map<Character, BitSequence> map, Node curNode, BitSequence bs) {
        if (curNode.isLeaf()) {
            map.put(curNode.ch, bs);
            return;
        }
        buildLookupTable(map, curNode.left, bs.appended(0));
        buildLookupTable(map, curNode.right, bs.appended(1));
    }
}
