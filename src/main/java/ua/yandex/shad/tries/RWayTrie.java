package ua.yandex.shad.tries;

import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.utils.TSTree;
import ua.yandex.shad.tries.utils.Tuple;

public class RWayTrie implements Trie {
    private static final int R = 26;

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

    @Override
    public void add(Tuple t) {
        boolean added = node[getNodeIndex(t.getTerm())].add(
                t.getTerm().substring(2), t.getWeight());
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
        for (int i = 0; i < R * R; ++i) {
            for (Tuple tuple : node[i]) {
                allTuples.add(new Tuple(
                        String.valueOf((char) ('a' + i / R)) + String.valueOf(
                                (char) ('a' + i % R)) + tuple.getTerm(),
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
        DynamicArray<Tuple> tuples = new DynamicArray<>();
        for (Tuple tuple : node[getNodeIndex(s)].find(s.substring(2))) {
            tuples.add(tuple);
        }
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
