package ua.yandex.shad.autocomplete;

import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.RWayTrie;
import ua.yandex.shad.tries.Trie;
import ua.yandex.shad.tries.Tuple;

public class PrefixMatches {

    private Trie trie;

    public PrefixMatches() {
        trie = new RWayTrie();
    }

    public int load(String... strings) {
        int remSize = trie.size();
        for (String string : strings) {
            String[] words = string.split(" ");
            for (String word : words) {
                if (word.length() > 2) {
                    trie.add(new Tuple(word, word.length()));
                }
            }
        }
        return trie.size() - remSize;
    }

    public int load(Tuple... tuples) {
        int remSize = trie.size();
        for (Tuple tuple : tuples) {
            if (tuple.term.length() > 2) {
                trie.add(tuple);
            }
        }
        return trie.size() - remSize;
    }

    public boolean contains(String word) {
        if( word.length() <= 2 )
            return false;
        return trie.contains(word);
    }

    public boolean delete(String word) {
        return trie.delete(word);
    }

    public Iterable<String> wordsWithPrefix(String pref, int k) {
        if (pref.length() < 2) {
            return null;
        }
        Iterable<String> words = trie.wordsWithPrefix(pref);
        DynamicArray<String> matchWords = new DynamicArray<>(words);
        DynamicArray<String> bestWords = new DynamicArray<>();
        int lastLen = 0;
        for (int i = 0; i < matchWords.size(); ++i) {
            String word = matchWords.get(i);
            if (word.length() != lastLen) {
                --k;
                if (k < 0) {
                    return bestWords;
                }
                lastLen = word.length();
            }
            bestWords.add(word);
        }
        return bestWords;
    }

    public Iterable<String> wordsWithPrefix(String pref) {
        return wordsWithPrefix(pref, 3);
    }

    public int size() {
        return trie.size();
    }
}
