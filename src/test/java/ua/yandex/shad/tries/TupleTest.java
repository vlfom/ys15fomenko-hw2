package ua.yandex.shad.tries;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TupleTest {

    @Test
    public void testCompareTo() throws Exception {
        Tuple tuple1 = new Tuple("test", 4);
        Tuple tuple2 = new Tuple("test", 4);
        Tuple tuple3 = new Tuple("word", 4);
        Tuple tuple4 = new Tuple("banan", 5);
        assertEquals(0, tuple1.compareTo(tuple2));
        assertTrue(tuple1.compareTo(tuple3) < 0);
        assertTrue(tuple1.compareTo(tuple4) < 0);
        assertTrue(tuple3.compareTo(tuple1) > 0);
        assertTrue(tuple4.compareTo(tuple1) > 0);
    }
}