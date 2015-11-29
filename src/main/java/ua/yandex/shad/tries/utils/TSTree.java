package ua.yandex.shad.tries.utils;


import ua.yandex.shad.collections.DynamicArray;

import java.util.Iterator;

/**
 * Ternary Search Tree
 */
public class TSTree implements Iterable<Tuple> {
    private Node root;
    private int size;
    private String prefix;

    public TSTree(String prefix) {
        this.prefix = prefix;
        root = new Node(null);
    }

    private TSTree(Node root, String prefix) {
        this.prefix = prefix;
        if (root != null) {
            this.root = root;
            this.size = toTupleArray().size();
        } else {
            this.root = new Node(null);
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
            if (x.left == null) {
                x.left = new Node(x);
                x.left.c = s.charAt(i);
            }
            x.left = add(x.left, s, i, value);
        } else if (c > x.c) {
            if (x.right == null) {
                x.right = new Node(x);
                x.right.c = s.charAt(i);
            }
            x.right = add(x.right, s, i, value);
        } else if (i + 1 < s.length()) {
            if (x.middle == null) {
                x.middle = new Node(x);
                x.middle.c = s.charAt(i + 1);
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

    public Iterable<Tuple> find(String s) {
        class Iter implements Iterable<Tuple> {
            private String s;

            public Iter(String s) {
                this.s = s;
            }

            @Override
            public Iterator<Tuple> iterator() {
                if (s.isEmpty()) {
                    return new Itr(root);
                }
                return new Itr(find(root, s, 0));
            }
        }
        return new Iter(s);
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
        DynamicArray<Tuple> tuples = new DynamicArray<>();
        collectNodes(root, "", tuples);
        return tuples;
    }

    private void collectNodes(Node x, String s, DynamicArray<Tuple> tuples) {
        if (x.value != -1) {
            tuples.add(new Tuple(s + x.c, x.value));
        }
        if (x.left != null) {
            collectNodes(x.left, s, tuples);
        }
        if (x.middle != null) {
            collectNodes(x.middle, s + x.c, tuples);
        }
        if (x.right != null) {
            collectNodes(x.right, s, tuples);
        }
    }

    @Override
    public Iterator<Tuple> iterator() {
        return new Itr(root);
    }

    private static class Node {
        private char c;
        private int value;
        private Node parent, left, middle, right;
        private int depth;

        Node(Node parent) {
            this.parent = parent;
            this.value = -1;
            if (parent != null) {
                this.depth = parent.depth + 1;
            } else {
                this.depth = 1;
            }
        }

        public Pair<Node, String> getNext(String word, int minDepth) {
            Node get;
            if (middle != null) {
                get = middle.leftmost();
                return new Pair<>(get, word + get.c);
            } else if (right != null) {
                get = right.leftmost();
                return new Pair<>(get,
                        word.substring(0, word.length() - 1) + get.c);
            } else {
                Node pred, node;
                StringBuilder newWord = new StringBuilder(word);
                pred = parent;
                node = this;
                newWord.deleteCharAt(newWord.length() - 1);
                while (pred != null) {
                    if (node.getDepth() <= minDepth) {
                        return null;
                    }
                    if (pred.left == node) {
                        newWord.append(pred.c);
                        return new Pair<>(pred, newWord.toString());
                    } else if (pred.middle == node && pred.right != null) {
                        get = pred.right.leftmost();
                        newWord.deleteCharAt(newWord.length() - 1);
                        newWord.append(get.c);
                        return new Pair<>(get, newWord.toString());
                    } else {
                        if (pred.middle == node) {
                            newWord.deleteCharAt(newWord.length() - 1);
                        }
                        node = pred;
                        pred = node.parent;
                    }
                }
            }
            return null;
        }

        public Node leftmost() {
            Node leftm = this;
            while (leftm.left != null) {
                leftm = leftm.left;
            }
            return leftm;
        }

        public int getDepth() {
            return depth;
        }
    }

    private class Itr implements Iterator<Tuple> {
        private Node last;
        private String text;
        private int minDepth;

        public Itr(Node node) {
            if (node != null) {
                minDepth = node.getDepth();
                last = new Node(null);
                last.right = node;
                text = "@";
            } else {
                minDepth = 1;
            }
        }

        @Override
        public boolean hasNext() {
            if (last == null) {
                return false;
            }
            Node nextNode = last;
            String nextText = text;
            do {
                Pair<Node, String> get = nextNode.getNext(nextText, minDepth);
                if (get == null) {
                    return false;
                }
                nextNode = get.first;
                nextText = get.second;
            } while (nextNode.value == -1);
            return true;
        }

        @Override
        public Tuple next() {
            Node nextNode = last;
            String nextText = text;
            do {
                Pair<Node, String> get = nextNode.getNext(nextText, minDepth);
                nextNode = get.first;
                nextText = get.second;
            } while (nextNode.value == -1);
            last = nextNode;
            text = nextText;
            return new Tuple(text, last.value);
        }

        @Override
        public void remove() {
        }
    }
}
