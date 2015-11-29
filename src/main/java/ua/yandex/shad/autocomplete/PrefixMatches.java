package ua.yandex.shad.autocomplete;

import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.RWayTrie;
import ua.yandex.shad.tries.Trie;
import ua.yandex.shad.tries.utils.Tuple;

public class PrefixMatches {

    private static final int DEFAULT_HINTS = 3;

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

    public boolean contains(String word) {
        return word.length() > 2 && trie.contains(word);
    }

    public boolean delete(String word) {
        return trie.delete(word);
    }

    public Iterable<String> wordsWithPrefix(String pref, int k) {
        if (!longEnough(pref)) {
            return new DynamicArray<>();
        }
        Iterable<String> words = trie.wordsWithPrefix(pref);
        DynamicArray<String> bestWords = new DynamicArray<>();
        int lastLen = 0, remK = k;
        for (String word : words) {
            if (word.length() != lastLen) {
                --remK;
                if (remK < 0) {
                    return bestWords;
                }
                lastLen = word.length();
            }
            bestWords.add(word);
        }
        return bestWords;
    }

    public boolean longEnough(String pref) {
        return pref.length() >= 2;
    }

    public Iterable<String> wordsWithPrefix(String pref) {
        return wordsWithPrefix(pref, DEFAULT_HINTS);
    }

    public int size() {
        return trie.size();
    }
}
