package ua.yandex.shad.tries;

import ua.yandex.shad.tries.utils.Tuple;

public interface Trie {

    void add(Tuple word);

    boolean contains(String word);

    boolean delete(String word);

    Iterable<String> words();

    Iterable<String> wordsWithPrefix(String pref);

    int size();
}
