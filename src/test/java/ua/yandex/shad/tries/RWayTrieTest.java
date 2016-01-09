package ua.yandex.shad.tries;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.utils.Tuple;

import static org.junit.Assert.*;

public class RWayTrieTest {
    RWayTrie rWayTrie;

    @Before
    public void setUp() throws Exception {
        String[] words = {"java", "javac", "javavm", "javadoc", "word",
                "words", "wordy", "wordpad", "wordface"};
        rWayTrie = new RWayTrie();
        for (String word : words) {
            rWayTrie.add(new Tuple(word, word.length()));
        }
    }

    @After
    public void tearDown() throws Exception {
        rWayTrie = null;
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(rWayTrie.size(), 9);

        rWayTrie.add(new Tuple("java", 4));
        assertEquals(rWayTrie.size(), 9);

        rWayTrie.add(new Tuple("jaba", 4));
        assertEquals(rWayTrie.size(), 10);
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(rWayTrie.contains("java"));
        assertFalse(rWayTrie.contains("jaba"));

        assertTrue(rWayTrie.contains("wordface"));
        assertFalse(rWayTrie.contains("wordbace"));
    }

    @Test
    public void testDelete() throws Exception {
        assertTrue(rWayTrie.contains("java"));
        rWayTrie.delete("java");
        assertFalse(rWayTrie.contains("java"));

        assertFalse(rWayTrie.contains("jaba"));
        rWayTrie.delete("jaba");
        assertFalse(rWayTrie.contains("jaba"));
    }

    @Test
    public void testWords() throws Exception {
        String[] input = {"aba", "abaka", "aka", "akaba", "babaka", "bakaba"};
        rWayTrie = new RWayTrie();
        for (String word : input) {
            rWayTrie.add(new Tuple(word, word.length()));
        }
        int i = 0;
        Iterable<String> words = rWayTrie.words();
        for (String word : words) {
            assertEquals(word, input[i++]);
        }
    }

    @Test
    public void testWordsWithPrefix() throws Exception {
        String[] input = {"aba", "aka", "abaka", "akaba", "babaka", "bakaba"};
        rWayTrie = new RWayTrie();
        for (String word : input) {
            rWayTrie.add(new Tuple(word, word.length()));
        }
        int i = 4;
        Iterable<String> words = rWayTrie.wordsWithPrefix("ba");
        for (String word : words) {
            assertEquals(word, input[i++]);
        }
    }

    @Test
    public void testSize() throws Exception {
        rWayTrie = new RWayTrie();
        assertEquals(0, rWayTrie.size());

        rWayTrie.add(new Tuple("test", 4));
        assertEquals(1, rWayTrie.size());

        rWayTrie.add(new Tuple("test", 4));
        assertEquals(1, rWayTrie.size());

        rWayTrie.add(new Tuple("tested", 6));
        assertEquals(2, rWayTrie.size());

        rWayTrie.delete("tested");
        assertEquals(1, rWayTrie.size());

        rWayTrie.delete("test");
        assertEquals(0, rWayTrie.size());
    }
}