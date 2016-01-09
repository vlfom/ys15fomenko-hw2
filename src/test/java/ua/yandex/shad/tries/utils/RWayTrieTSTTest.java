package ua.yandex.shad.tries.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.RWayTrie;

import java.util.Iterator;

import static org.junit.Assert.*;

public class RWayTrieTSTTest {
    RWayTrie trie;

    @Before
    public void setUp() throws Exception {
        String[] words = {"java", "javac", "javavm", "javadoc", "word",
                "words", "wordy", "wordpad", "wordface"};
        trie = new RWayTrie();
        for (String word : words) {
            trie.add(new Tuple(word, word.length()));
        }
    }

    @After
    public void tearDown() throws Exception {
        trie = null;
    }

    @Test
    public void testSizeAddRemove() throws Exception {
        assertEquals(9, trie.size());

        trie = new RWayTrie();
        assertEquals(0, trie.size());

        trie.add(new Tuple("apple", 5));
        assertEquals(1, trie.size());

        trie.add(new Tuple("egg", 3));
        assertEquals(2, trie.size());

        trie.add(new Tuple("apple", 5));
        assertEquals(2, trie.size());

        trie.delete("abble");
        assertEquals(2, trie.size());

        trie.delete("apple");
        assertEquals(1, trie.size());

        trie.delete("egg");
        assertEquals(0, trie.size());

        trie.add(new Tuple("article", 7));
        trie.add(new Tuple("arbitrary", 9));
        assertEquals(2, trie.size());

        trie.delete("arbitrary");
        assertEquals(1, trie.size());

        trie.delete("article");
        assertEquals(0, trie.size());

        trie.add(new Tuple("arbitrary", 9));
        trie.add(new Tuple("article", 7));
        trie.add(new Tuple("articled", 8));
        assertEquals(3, trie.size());

        trie.delete("articled");
        assertEquals(2, trie.size());

        trie.delete("article");
        assertEquals(1, trie.size());

        trie.delete("arbitrary");
        assertEquals(0, trie.size());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(trie.contains("java"));
        assertTrue(trie.contains("javac"));
        assertFalse(trie.contains("javab"));

        trie.delete("javac");
        assertTrue(trie.contains("java"));
        assertFalse(trie.contains("javac"));

        trie.delete("java");
        assertFalse(trie.contains("java"));
    }
}