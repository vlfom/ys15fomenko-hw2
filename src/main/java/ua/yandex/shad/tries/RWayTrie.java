package ua.yandex.shad.tries;

import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.utils.Pair;
import ua.yandex.shad.tries.utils.Tuple;

import java.util.Iterator;

public class RWayTrie implements Trie {
    private static final int R = 26;
    private static final String[] NODES_PREFIXES;

    static {
        NODES_PREFIXES = new String[R * R];
        for (int i = 0; i < R; ++i) {
            for (int j = 0; j < R; ++j) {
                NODES_PREFIXES[i * R + j] = String.valueOf((char) ('a' + i))
                        + String.valueOf((char) ('a' + j));
            }
        }
    }

    private int size;
    private Node[] node;

    public RWayTrie() {
        node = new Node[R * R];
        for (int i = 0; i < R * R; ++i) {
            node[i] = new Node(null);
        }
    }

    private static int getNodeIndex(String s) {
        char a = Character.toLowerCase(s.charAt(0)),
                b = Character.toLowerCase(s.charAt(1));
        return (a - 'a') * R + b - 'a';
    }

    @Override
    public void add(Tuple t) {
        add(node[getNodeIndex(t.getTerm())], t.getTerm().substring(2), 0,
                t.getWeight());
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

    @Override
    public boolean contains(String word) {
        return contains(node[getNodeIndex(word)], word.substring(2), 0);
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

    @Override
    public boolean delete(String word) {
        return remove(node[getNodeIndex(word)], word.substring(2), 0);
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

    private Node find(Node x, String s) {
        if (s.isEmpty()) {
            return x;
        } else {
            return find(x, s, 0);
        }
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

    @Override
    public Iterable<String> words() {
        return new NodeIterator(
                node,
                NODES_PREFIXES
        );
    }

    @Override
    public Iterable<String> wordsWithPrefix(String s) {
        return new NodeIterator(
                new Node[]{ find(node[getNodeIndex(s)], s.substring(2)) },
                new String[]{ s.substring(0,2) }
        );
    }

    @Override
    public int size() {
        return size;
    }

    private static class Node {
        Node left;
        Node middle;
        Node right;
        private char c;
        private int value;
        private Node parent;
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

        public int getDepth() {
            return depth;
        }
    }

    private class NodeIterator implements Iterable<String> {
        private Node[] nodes;
        private String[] prefixes;

        public NodeIterator(Node[] nodes, String[] prefixes) {
            this.nodes = nodes;
            this.prefixes = prefixes;
        }

        @Override
        public Iterator<String> iterator() {
            return new ComplexItr();
        }

        private class ComplexItr implements Iterator<String> {
            private int i;
            private Itr iterator;

            public ComplexItr() {
                i = 0;
                iterator = null;
            }

            @Override
            public boolean hasNext() {
                if (iterator == null) {
                    if (i < nodes.length) {
                        iterator = new Itr(nodes[i]);
                        ++i;
                        return hasNext();
                    } else {
                        return false;
                    }
                } else {
                    if (iterator.hasNext()) {
                        return true;
                    } else {
                        if (i < nodes.length) {
                            iterator = new Itr(nodes[i]);
                            ++i;
                            return hasNext();
                        } else {
                            iterator = null;
                            return false;
                        }
                    }
                }
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    return null;
                }
                return prefixes[i-1] + iterator.next();
            }

            @Override
            public void remove() {
            }
        }

        private class Itr implements Iterator<String> {
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
                    Pair<Node, String> get = getNext(nextNode, nextText,
                            minDepth);
                    if (get == null) {
                        return false;
                    }
                    nextNode = get.getFirst();
                    nextText = get.getSecond();
                } while (nextNode.value == -1);
                return true;
            }

            @Override
            public String next() {
                Node nextNode = last;
                String nextText = text;
                do {
                    Pair<Node, String> get = getNext(nextNode, nextText,
                            minDepth);
                    nextNode = get.getFirst();
                    nextText = get.getSecond();
                } while (nextNode.value == -1);
                last = nextNode;
                text = nextText;
                return text;
            }

            @Override
            public void remove() {
            }

            private Pair<Node, String> getNext(Node x, String word, int
                    minDepth) {

                Node get;
                if (x.middle != null) {
                    get = leftmost(x.middle);
                    return new Pair<>(get, word + get.c);
                } else if (x.right != null) {
                    get = leftmost(x.right);
                    return new Pair<>(get,
                            word.substring(0, word.length() - 1) + get.c);
                } else {
                    Node pred, node;
                    StringBuilder newWord = new StringBuilder(word);
                    pred = x.parent;
                    node = x;
                    newWord.deleteCharAt(newWord.length() - 1);
                    while (pred != null) {
                        if (node.getDepth() <= minDepth) {
                            return null;
                        }
                        if (pred.left == node) {
                            newWord.append(pred.c);
                            return new Pair<>(pred, newWord.toString());
                        } else if (pred.middle == node && pred.right != null) {
                            get = leftmost(pred.right);
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

            private Node leftmost(Node x) {
                Node leftm = x;
                while (leftm.left != null) {
                    leftm = leftm.left;
                }
                return leftm;
            }
        }
    }
}
