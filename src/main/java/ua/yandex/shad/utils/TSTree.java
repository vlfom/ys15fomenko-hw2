package ua.yandex.shad.utils;


import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.Tuple;

/**
 * Ternary Search Tree
 */
public class TSTree {
    private Node root;
    private DynamicArray<Tuple> tuples;
    private int size;

    public TSTree() {
        root = new Node();
    }

    public TSTree(Node root) {
        this();
        if( root != null ) {
            this.root = root;
            this.size = toTupleArray().size();
        }
    }

    public int size() {
        return size;
    }

    public boolean add(String s, int value) {
        int remSize = size;
        root = add(root, s, 0, value);
        return size != remSize;
    }

    private Node add(Node x, String s, int i, int value) {
        char c = s.charAt(i);
        if (c < x.c) {
            if( x.left == null ) {
                x.left = new Node();
                x.left.c = s.charAt(i);
            }
            x.left = add(x.left, s, i, value);
        } else if (c > x.c) {
            if( x.right == null ) {
                x.right = new Node();
                x.right.c = s.charAt(i);
            }
            x.right = add(x.right, s, i, value);
        } else if (i + 1 < s.length()) {
            if( x.middle == null ) {
                x.middle = new Node();
                x.middle.c = s.charAt(i+1);
            }
            x.middle = add(x.middle, s, i + 1, value);
        } else {
            if (x.value == -1) {
                size += 1;
            }
            x.value = value;
        }
        return x;
    }

    public Node find(String s) {
        if (s.isEmpty()) {
            return root;
        }
        return find(root, s, 0);
    }

    private Node find(Node x, String s, int i) {
        if (x == null) {
            return null;
        }
        char c = s.charAt(i);
        if (c < x.c) {
            return find(x.left, s, i);
        } else if (c > x.c) {
            return find(x.right, s, i);
        } else if (i + 1 < s.length()) {
            return find(x.middle, s, i + 1);
        } else {
            return x.middle;
        }
    }

    public boolean contains(String s) {
        return contains(root, s, 0);
    }

    private boolean contains(Node x, String s, int i) {
        if (x == null) {
            return false;
        }
        if (s.charAt(i) < x.c) {
            return contains(x.left, s, i);
        } else if (s.charAt(i) > x.c) {
            return contains(x.right, s, i);
        } else if (i + 1 < s.length()) {
            return contains(x.middle, s, i + 1);
        } else {
            return x.value != -1;
        }
    }

    public boolean remove(String s) {
        return remove(root, s, 0);
    }

    private boolean remove(Node x, String s, int i) {
        if (x == null) {
            return false;
        }
        boolean r;
        if (s.charAt(i) < x.c) {
            r = remove(x.left, s, i);
            if (r && emptyNode(x.left)) {
                x.left = null;
            }
            return r;
        } else if (s.charAt(i) > x.c) {
            r = remove(x.right, s, i);
            if (r && emptyNode(x.right)) {
                x.right = null;
            }
            return r;
        } else if (i + 1 < s.length()) {
            r = remove(x.middle, s, i + 1);
            if (r && emptyNode(x.middle)) {
                x.middle = null;
            }
            return r;
        } else {
            x.value = -1;
            size -= 1;
            return true;
        }
    }

    private boolean emptyNode(Node x) {
        return x.value == -1 && x.left == null && x.middle == null && x.right
                == null;
    }

    public DynamicArray<Tuple> toTupleArray() {
        tuples = new DynamicArray<>();
        collectNodes(root, "");
        return tuples;
    }

    private void collectNodes(Node x, String s) {
        if (x == null) {
            return;
        }
        if (x.value != -1) {
            tuples.add(new Tuple(s + x.c, x.value));
        }
        if (x.left != null) {
            collectNodes(x.left, s);
        }
        if (x.middle != null) {
            collectNodes(x.middle, s + x.c);
        }
        if (x.right != null) {
            collectNodes(x.right, s);
        }
    }

    private static class Node {
        private char c;
        private int value;
        private Node left, middle, right;

        Node() {
            this.value = -1;
        }
    }
}
