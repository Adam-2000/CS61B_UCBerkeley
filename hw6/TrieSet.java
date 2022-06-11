import edu.princeton.cs.algs4.Stack;

import java.util.*;
public class TrieSet {
    public class Node {
        private boolean exists;
        private char charactor;
        private Map<Character, Node> links;

        public Node(char c) {
            links = new HashMap<Character, Node>();
            exists = false;
            charactor = c;
        }
        public boolean contains(char c) {
            return links.containsKey(c);
        }
        public Node get(char key) {
            return links.get(key);
        }
        public void put(char key, Node val) {
            links.put(key, val);
        }
        public boolean exists() {
            return exists;
        }
    }

    private void list(List<String> list, Node node, String string) {
        if (node.charactor != '\0') {
            string += Character.toString(node.charactor);
        }
        if (node.exists) {
            list.add(string);
        }
        for (char c : node.links.keySet()) {
            list(list, node.links.get(c), string);
        }

    }
    public List<String> list() {
        List<String> list = new ArrayList<>();
        list(list, root, "");
        return list;
    }

    private Node root;

    public TrieSet() {
        root = new Node('\0');
    }

    public TrieSet(String filepath) {
        root = new Node('\0');
        In in = new In(filepath);
        String[] lines = in.readAllLines();
        for (String line : lines) {
            put(line);
        }
    }

    public void put(String key) {
        Node currNode = root;
        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.contains(currChar)) {
                currNode.put(currChar, new Node(currChar));
            }
            currNode = currNode.get(currChar);
        }
        currNode.exists = true;
    }

    public boolean contains(String key) {
        Node currNode = root;
        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.contains(currChar)) {
                return false;
            }
            currNode = currNode.get(currChar);
        }
        return currNode.exists;
    }

    public Node getRoot() {
        return root;
    }
}
