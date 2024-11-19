import java.util.ArrayList;
import java.util.List;

public class BTree<K extends Comparable<K>, V> {
    private static final int T = 3; // Minimum degree

    private class Node {
        int numKeys; // Number of keys in the node
        List<K> keys; // Keys in the node
        List<V> values; //Associated values
        List<Node> children; //Child pointers
        boolean isLeaf; //True if this node is a leaf

        Node(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
        }
    }

    private Node root;

    public BTree() {
        root = new Node(true);
    }

    //Search for a key in the tree
    public V search(K key) {
        return search(root, key);
    }

    private V search(Node node, K key) {
        int i = 0;

        //Find the first key greater than or equal to key
        while (i < node.numKeys && key.compareTo(node.keys.get(i)) > 0) {
            i++;
        }

        //If the key is found, return the value
        if (i < node.numKeys && key.compareTo(node.keys.get(i)) == 0) {
            return node.values.get(i);
        }

        //If the key is not found and this is a leaf, the key is not in the tree
        if (node.isLeaf) {
            return null;
        }

        //Go to the child
        return search(node.children.get(i), key);
    }

    //Insert a key-value pair into the tree
    public void insert(K key, V value) {
        Node rootNode = root;

        //If root full, split
        if (rootNode.numKeys == 2 * T - 1) {
            Node newRoot = new Node(false);
            newRoot.children.add(root);
            splitChild(newRoot, 0, rootNode);
            root = newRoot;
        }

        insertNonFull(root, key, value);
    }

    private void insertNonFull(Node node, K key, V value) {
        int i = node.numKeys - 1;

        if (node.isLeaf) {
            //Insert the new key into the correct position
            while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                i--;
            }

            node.keys.add(i + 1, key);
            node.values.add(i + 1, value);
            node.numKeys++;
        } else {
            //Find the child that will have the new key
            while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            i++;

            //If the child is full, split
            if (node.children.get(i).numKeys == 2 * T - 1) {
                splitChild(node, i, node.children.get(i));
                if (key.compareTo(node.keys.get(i)) > 0) {
                    i++;
                }
            }

            insertNonFull(node.children.get(i), key, value);
        }
    }

    private void splitChild(Node parent, int index, Node child) {
        Node sibling = new Node(child.isLeaf);
        sibling.numKeys = T - 1;

        //Copy the last (T-1) keys and values of child to sibling
        for (int j = 0; j < T - 1; j++) {
            sibling.keys.add(child.keys.remove(T));
            sibling.values.add(child.values.remove(T));
        }

        //If child is not a leaf, copy the last T children of child to sibling
        if (!child.isLeaf) {
            for (int j = 0; j < T; j++) {
                sibling.children.add(child.children.remove(T));
            }
        }

        child.numKeys = T - 1;

        //Insert the new child into the parent
        parent.children.add(index + 1, sibling);
        parent.keys.add(index, child.keys.remove(T - 1));
        parent.values.add(index, child.values.remove(T - 1));
        parent.numKeys++;
    }
}
