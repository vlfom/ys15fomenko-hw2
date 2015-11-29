package ua.yandex.shad.autocomplete;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PrefixMatchesTest {
    PrefixMatches prefixMatches;

    @Before
    public void setUp() throws Exception {
        File file = new File("src/test/resources/words-test.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream
                (file));
        Scanner scanner = new Scanner(reader);
        int n = scanner.nextInt(), k = 0;
        long w;
        String word;
        String[] words = new String[n];
        for (int i = 0; i < n; ++i) {
            w = scanner.nextLong();
            word = scanner.next();
            words[k++] = word;
        }
        prefixMatches = new PrefixMatches();
        prefixMatches.load(words);
    }

    @After
    public void tearDown() throws Exception {
        prefixMatches = null;
    }

    @Test
    public void testLoad() throws Exception {
        assertFalse(prefixMatches.contains("dummystring"));
        prefixMatches.load("dummystring");
        assertTrue(prefixMatches.contains("dummystring"));

        prefixMatches.load("dummystring_first", "dummystring_second",
                "dummystring_third");
        assertTrue(prefixMatches.contains("dummystring_first"));
        assertTrue(prefixMatches.contains("dummystring_second"));
        assertTrue(prefixMatches.contains("dummystring_third"));

        prefixMatches.load("a", "b", "c");
        assertFalse(prefixMatches.contains("a"));
        assertFalse(prefixMatches.contains("b"));
        assertFalse(prefixMatches.contains("c"));
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(prefixMatches.contains("java"));
        assertTrue(prefixMatches.contains("rules"));
        assertTrue(prefixMatches.contains("the"));
        assertTrue(prefixMatches.contains("world"));
    }

    @Test
    public void testDelete() throws Exception {
        prefixMatches.delete("java");
        prefixMatches.delete("rules");
        prefixMatches.delete("the");
        prefixMatches.delete("world");

        assertFalse(prefixMatches.contains("java"));
        assertFalse(prefixMatches.contains("rules"));
        assertFalse(prefixMatches.contains("the"));
        assertFalse(prefixMatches.contains("world"));
    }

    @Test
    public void testWordsWithPrefix() throws Exception {
        Iterable<String> matches;

        matches = prefixMatches.wordsWithPrefix("nike");
        assertEquals(Utils.iterableSize(matches), 5);

        matches = prefixMatches.wordsWithPrefix("nike", 3);
        assertEquals(Utils.iterableSize(matches), 5);

        matches = prefixMatches.wordsWithPrefix("nike", 1);
        assertEquals(Utils.iterableSize(matches), 3);

        matches = prefixMatches.wordsWithPrefix("z", 1);
        assertEquals(Utils.iterableSize(matches), 0);
    }

    @Test
    public void testSize() throws Exception {
        int remSize = prefixMatches.size();

        prefixMatches.load("world");
        assertEquals(prefixMatches.size(), remSize);

        prefixMatches.load("dummystring");
        assertEquals(prefixMatches.size(), remSize + 1);

        prefixMatches.delete("world");
        assertEquals(prefixMatches.size(), remSize);

        prefixMatches.delete("dummystring");
        assertEquals(prefixMatches.size(), remSize - 1);

        prefixMatches.load("world");
        assertEquals(prefixMatches.size(), remSize);
    }

    private static class Utils {
        public static int iterableSize(Iterable i) {
            Iterator x = i.iterator();
            int size = 0;
            while (x.hasNext()) {
                ++size;
                x.next();
            }
            return size;
        }
    }
}