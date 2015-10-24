package ua.yandex.shad.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.yandex.shad.collections.DynamicArray;
import ua.yandex.shad.tries.Tuple;

import static org.junit.Assert.*;

public class TSTreeTest {
    TSTree tsTree;

    @Before
    public void setUp() throws Exception {
        String[] words = {"java", "javac", "javavm", "javadoc", "word",
                "words", "wordy", "wordpad", "wordface"};
        tsTree = new TSTree();
        for (String word : words) {
            tsTree.add(word, word.length());
        }
    }

    @After
    public void tearDown() throws Exception {
        tsTree = null;
    }

    @Test
    public void testSizeAddRemove() throws Exception {
        assertEquals(9, tsTree.size());

        tsTree = new TSTree(null);
        assertEquals(0, tsTree.size());

        tsTree.add("apple", 5);
        assertEquals(1, tsTree.size());

        tsTree.add("egg", 3);
        assertEquals(2, tsTree.size());

        tsTree.add("apple", 5);
        assertEquals(2, tsTree.size());

        tsTree.remove("abble");
        assertEquals(2, tsTree.size());

        tsTree.remove("apple");
        assertEquals(1, tsTree.size());

        tsTree.remove("egg");
        assertEquals(0, tsTree.size());

        tsTree.add("article", 7);
        tsTree.add("arbitrary", 9);
        assertEquals(2, tsTree.size());

        tsTree.remove("arbitrary");
        assertEquals(1, tsTree.size());

        tsTree.remove("article");
        assertEquals(0, tsTree.size());

        tsTree.add("arbitrary", 9);
        tsTree.add("article", 7);
        tsTree.add("articled", 8);
        assertEquals(3, tsTree.size());

        tsTree.remove("articled");
        assertEquals(2, tsTree.size());

        tsTree.remove("article");
        assertEquals(1, tsTree.size());

        tsTree.remove("arbitrary");
        assertEquals(0, tsTree.size());
    }

    @Test
    public void testFind() throws Exception {
        Object node;
        node = tsTree.find("egg");
        assertNull(node);

        TSTree nodeTree = new TSTree(tsTree.find("ja"));
        assertEquals(4, nodeTree.size());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(tsTree.contains("java"));
        assertTrue(tsTree.contains("javac"));
        assertFalse(tsTree.contains("javab"));

        tsTree.remove("javac");
        assertTrue(tsTree.contains("java"));
        assertFalse(tsTree.contains("javac"));

        tsTree.remove("java");
        assertFalse(tsTree.contains("java"));
    }

    @Test
    public void testToTupleArray() throws Exception {
        DynamicArray<Tuple> tuples = tsTree.toTupleArray();
        assertEquals(9, tuples.size());
        for (Tuple tuple : tuples) {
            assertEquals(tuple.getWeight(), tuple.getTerm().length());
        }
    }
}