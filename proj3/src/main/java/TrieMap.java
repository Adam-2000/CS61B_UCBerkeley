import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

public class TrieMap {
    public class Node {
        private boolean exists;
        private char charactor;
        Map<Character, Node> links;
        List<GraphDB.Node> gNodes = new LinkedList<>();
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
        public char getCharactor() {
            return charactor;
        }
        public void getList(List<String> set, String str) {
            if (exists) {
                set.add(str);
            }
            for (char c : links.keySet()) {
                links.get(c).getList(set, str + c);
            }
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

    public TrieMap() {
        root = new Node('\0');
    }

//    public TrieSet(String filepath) {
//        root = new Node('\0');
//        In in = new In(filepath);
//        String[] lines = in.readAllLines();
//        for (String line : lines) {
//            put(line);
//        }
//    }

    public void put(String key, GraphDB.Node gNode) {
        Node currNode = root;
        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            if (!currNode.contains(currChar)) {
                currNode.put(currChar, new Node(currChar));
            }
            currNode = currNode.get(currChar);
        }
        currNode.exists = true;
        currNode.gNodes.add(gNode);
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

    public Node findStringNode(String str) {
        Node currNode = root;
        for (int i = 0; i < str.length(); i++) {
            char currChar = str.charAt(i);
            if (!currNode.contains(currChar)) {
                return null;
            }
            currNode = currNode.get(currChar);
        }
        if (currNode.exists) {
            return currNode;
        }
        return null;
    }
    public Node getRoot() {
        return root;
    }
}
