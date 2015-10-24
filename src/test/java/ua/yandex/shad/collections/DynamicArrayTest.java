package ua.yandex.shad.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

/**
 * Created by root on 24.10.15.
 */
public class DynamicArrayTest {
    DynamicArray<Integer> array;

    @Before
    public void setUp() throws Exception {
        array = new DynamicArray<>();
        for (int i = 1; i <= 50; ++i) {
            array.add(51 - i);
        }
    }

    @After
    public void tearDown() throws Exception {
        array = null;
    }

    @Test
    public void testSizeAddMerge() throws Exception {
        assertEquals(50, array.size());

        array.add(51);
        assertEquals(51, array.size());

        DynamicArray<Integer> array2 = new DynamicArray<>(Arrays.asList(52,
                53, 54));
        array.merge(array2);
        assertEquals(54, array.size());

        array = new DynamicArray<>();
        assertEquals(0, array.size());
    }

    @Test
    public void testGet() throws Exception {
        for (int i = 1; i <= 50; ++i) {
            assertEquals(51 - i, (int) array.get(i - 1));
        }
    }

    @Test
    public void testSet() throws Exception {
        assertEquals(1, (int) array.get(49));
        array.set(49, 0);
        assertEquals(0, (int) array.get(49));
    }

    @Test
    public void testIterator() throws Exception {
        Iterator iterator = array.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            assertEquals(51 - i++, iterator.next());
        }

        iterator = array.iterator();
        iterator.remove();
        assertEquals(49, array.size());
        assertEquals(49, (int) array.get(0));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNextException() throws Exception {
        Iterator iterator = array.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            assertEquals(51 - i++, iterator.next());
        }
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorRemoveException() throws Exception {
        Iterator iterator = array.iterator();
        int i = 1;
        while (iterator.hasNext()) {
            assertEquals(51 - i++, iterator.next());
        }
        iterator.remove();
    }

    @Test
    public void testSort() throws Exception {
        array.sort();
        for (int i = 1; i <= 50; ++i) {
            assertEquals(i, (int) array.get(i - 1));
        }
    }
}