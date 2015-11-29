package ua.yandex.shad.tries;

import org.junit.Test;
import ua.yandex.shad.tries.utils.Tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TupleTest {

    @Test
    public void testCompareTo() throws Exception {
        Tuple tuple1 = new Tuple("test", 4);
        Tuple tuple2 = new Tuple("test", 4);
        Tuple tuple3 = new Tuple("word", 4);
        Tuple tuple4 = new Tuple("banan", 5);
        Tuple tuple5 = new Tuple("test", 5);
        assertEquals(0, tuple1.compareTo(tuple2));
        assertTrue(tuple1.compareTo(tuple3) < 0);
        assertTrue(tuple1.compareTo(tuple4) < 0);
        assertTrue(tuple3.compareTo(tuple1) > 0);
        assertTrue(tuple4.compareTo(tuple1) > 0);

        assertFalse(tuple1.equals(new Integer(0)));
        assertTrue(tuple1.equals(tuple2));
        assertFalse(tuple1.equals(tuple3));
        assertFalse(tuple1.equals(tuple5));

        assertEquals(42, tuple1.hashCode());
    }
}