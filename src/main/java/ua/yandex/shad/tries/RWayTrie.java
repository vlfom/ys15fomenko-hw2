package ua.yandex.shad.tries;

import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.utils.TSTree;

public class RWayTrie implements Trie {
    private static final int R = 26;
    private static final String[] NODES_STRING;

    static {
        NODES_STRING = new String[R * R];
        for (int i = 0; i < R; ++i) {
            for (int j = 0; j < R; ++j) {
                NODES_STRING[i * R + j] = String.valueOf((char) ('a' + i)) +
                        String.valueOf((char) ('a' + j));
            }
        }
    }

    private int size;
    private TSTree[] node;

    public RWayTrie() {
        node = new TSTree[R * R];
        for (int i = 0; i < R * R; ++i) {
            node[i] = new TSTree();
        }
    }

    private static int getNodeIndex(String s) {
        char a = Character.toLowerCase(s.charAt(0)),
                b = Character.toLowerCase(s.charAt(1));
        return (a - 'a') * R + b - 'a';
    }

    private static String getNodeString(int v) {
        return NODES_STRING[v];
    }

    @Override
    public void add(Tuple t) {
        boolean added = node[getNodeIndex(t.getTerm())].add(t.getTerm()
                .substring(2), t.getWeight());
        if (added) {
            size++;
        }
    }

    @Override
    public boolean contains(String word) {
        return node[getNodeIndex(word)].contains(word.substring(2));
    }

    @Override
    public boolean delete(String word) {
        boolean removed = node[getNodeIndex(word)].remove(word.substring(2));
        if (removed) {
            size--;
        }
        return removed;
    }

    @Override
    public Iterable<String> words() {
        DynamicArray<Tuple> allTuples = new DynamicArray<>();
        DynamicArray<Tuple> tuples;
        for (int i = 0; i < R * R; ++i) {
            tuples = node[i].toTupleArray();
            for (Tuple tuple : tuples) {
                allTuples.add(new Tuple(getNodeString(i) + tuple.getTerm(),
                        tuple.getWeight()));
            }
        }
        allTuples.sort();
        DynamicArray<String> words = new DynamicArray<>();
        for (Tuple tuple : allTuples) {
            words.add(tuple.getTerm());
        }
        return words;
    }

    @Override
    public Iterable<String> wordsWithPrefix(String s) {
        DynamicArray<Tuple> tuples = new TSTree(node[getNodeIndex(s)].find(s
                .substring(2))).toTupleArray();
        tuples.sort();
        DynamicArray<String> words = new DynamicArray<>();
        for (Tuple tuple : tuples) {
            words.add(s + tuple.getTerm());
        }
        return words;
    }

    @Override
    public int size() {
        return size;
    }

}
