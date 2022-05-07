package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private final K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int compRes = key.compareTo(p.key);
        if (compRes == 0) {
            return p.value;
        }
        if (compRes < 0) {
            return getHelper(key, p.left);
        }
        return getHelper(key, p.right);
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        int compRes = key.compareTo(p.key);
        if (compRes == 0) {
            p.value = value;
            return p;
        }
        if (compRes < 0) {
            p.left = putHelper(key, value, p.left);
            return p;
        }
        p.right = putHelper(key, value, p.right);
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void keySetHelper(Node p, Set<K> set) {
        if (p == null) {
            return;
        }
        set.add(p.key);
        keySetHelper(p.left, set);
        keySetHelper(p.right, set);
    }


    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        keySetHelper(root, set);
        return set;
    }

    private class DeleteMaxStruct {
        private Node rootNode;
        private final Node maxNode;
        private DeleteMaxStruct(Node root, Node max) {
            rootNode = root;
            maxNode = max;
        }
    }
    private DeleteMaxStruct deleteMax(Node p) {
        if (p == null) {
            return new DeleteMaxStruct(null, null);
        }
        if (p.right == null) {
            return new DeleteMaxStruct(p.left, p);
        }
        DeleteMaxStruct ret = deleteMax(p.right);
        p.right = ret.rootNode;
        ret.rootNode = p;
        return ret;
    }
    private class RemoveHelperStruct {
        private Node p;
        private final V value;
        private RemoveHelperStruct(Node p, V value) {
            this.p = p;
            this.value = value;
        }
    }
    private RemoveHelperStruct removeHelper(Node p, K key) {
        if (p == null) {
            return new RemoveHelperStruct(null, null);
        }
        int compRes = key.compareTo(p.key);
        if (compRes == 0) {
            size--;
            if (p.left == null) {
                return new RemoveHelperStruct(p.right, p.value);
            }
            if (p.right == null) {
                return new RemoveHelperStruct(p.left, p.value);
            }
            DeleteMaxStruct deleteMaxRet = deleteMax(p.left);
            deleteMaxRet.maxNode.left = deleteMaxRet.rootNode;
            deleteMaxRet.maxNode.right = p.right;
            return new RemoveHelperStruct(deleteMaxRet.maxNode, p.value);
        }
        RemoveHelperStruct ret;
        if (compRes < 0) {
            ret = removeHelper(p.left, key);
            p.left = ret.p;
        } else {
            ret = removeHelper(p.right, key);
            p.right = ret.p;
        }
        ret.p = p;
        return ret;
    }
    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        RemoveHelperStruct ret = removeHelper(root, key);
        root = ret.p;
        return ret.value;
    }

    private RemoveHelperStruct removeHelper(Node p, K key, V value) {
        if (p == null) {
            return new RemoveHelperStruct(null, null);
        }
        int compRes = key.compareTo(p.key);
        if (compRes == 0) {
            if (value != p.value) {
                return new RemoveHelperStruct(p, null);
            }
            size--;
            if (p.left == null) {
                return new RemoveHelperStruct(p.right, p.value);
            }
            if (p.right == null) {
                return new RemoveHelperStruct(p.left, p.value);
            }
            DeleteMaxStruct deleteMaxRet = deleteMax(p.left);
            deleteMaxRet.maxNode.left = deleteMaxRet.rootNode;
            deleteMaxRet.maxNode.right = p.right;
            return new RemoveHelperStruct(deleteMaxRet.maxNode, p.value);
        }
        RemoveHelperStruct ret;
        if (compRes < 0) {
            ret = removeHelper(p.left, key, value);
            p.left = ret.p;
        } else {
            ret = removeHelper(p.right, key, value);
            p.right = ret.p;
        }
        ret.p = p;
        return ret;
    }
    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        RemoveHelperStruct ret = removeHelper(root, key, value);
        root = ret.p;
        return ret.value;
    }

    private class BSTMapIterator implements Iterator<K> {

        private class ParentedNode {
            Node node;
            ParentedNode parent;
            private boolean flagVisitedLeft;
            private boolean flagVisitedRight;
            private boolean flagVisitedSelf;
            private ParentedNode(Node node, ParentedNode parent) {
                this.node = node;
                this.parent = parent;
                flagVisitedLeft = false;
                flagVisitedRight = false;
                flagVisitedSelf = false;
            }
        }
        private ParentedNode iter;
        private int cnt;
        private BSTMapIterator() {
            iter = new ParentedNode(root, null);
            cnt = 0;
        }

        @Override
        public boolean hasNext() {
            return cnt < size;
        }

        @Override
        public K next() {
            if (cnt >= size) {
                return null;
            }
            ParentedNode newIter;
            if (iter.node.left != null && !iter.flagVisitedLeft) {
                newIter = new ParentedNode(iter.node.left, iter);
                iter.flagVisitedLeft = true;
                iter = newIter;
                return next();
            }
            if (!iter.flagVisitedSelf) {
                iter.flagVisitedSelf = true;
                cnt++;
                return iter.node.key;
            }
            if (iter.node.right != null && !iter.flagVisitedRight) {
                newIter = new ParentedNode(iter.node.right, iter);
                iter.flagVisitedRight = true;
                iter = newIter;
                return next();
            }
            iter = iter.parent;
            return next();
        }

    }
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
}
