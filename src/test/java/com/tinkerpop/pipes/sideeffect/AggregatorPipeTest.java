package com.tinkerpop.pipes.sideeffect;


import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.filter.CollectionFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import junit.framework.TestCase;

import java.util.*;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 */
public class AggregatorPipeTest extends TestCase {

    public void testReset() {
        List<String> aggregate = new ArrayList<String>();
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatorPipe<String> pipe = new AggregatorPipe<String>(aggregate);
        pipe.setStarts(list.iterator());
        assertTrue(pipe.hasNext());
        assertEquals(aggregate.get(0), "marko");
        assertEquals(aggregate.size(), 6);
        pipe.reset();
        assertEquals(aggregate.get(0), "marko");
        assertEquals(aggregate.size(), 6);
        assertFalse(pipe.hasNext());
        assertEquals(pipe.getSideEffect().getClass(), ArrayList.class);
        assertEquals(pipe.getSideEffect().size(), 0);
    }

    public void testAggregatorPipe() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatorPipe<String> pipe1 = new AggregatorPipe<String>(new ArrayList<String>());
        pipe1.setStarts(list.iterator());
        assertTrue(pipe1.hasNext());
        int counter = 0;
        while (pipe1.hasNext()) {
            assertEquals(pipe1.next(), list.get(counter));
            counter++;
        }
        assertEquals(counter, 6);
        assertEquals(pipe1.getSideEffect().size(), counter);
        assertEquals(list.size(), counter);
        for (int i = 0; i < counter; i++) {
            assertEquals(list.get(i), pipe1.getSideEffect().toArray()[i]);
        }

        pipe1.reset();
        assertEquals(0, pipe1.getSideEffect().size());
        pipe1.setStarts(list.iterator());
        counter = 0;
        assertTrue(pipe1.hasNext());
        assertEquals(6, pipe1.getSideEffect().size());
    }

    public void testSelfFilter() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        AggregatorPipe<String> pipe1 = new AggregatorPipe<String>(new ArrayList<String>());
        Pipe pipe2 = new CollectionFilterPipe<String>(pipe1.getSideEffect(), ComparisonFilterPipe.Filter.NOT_EQUAL);
        Pipeline<String, String> pipeline = new Pipeline<String, String>(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(list.iterator());
        int counter = 0;
        assertTrue(pipeline.hasNext());
        while (pipeline.hasNext()) {
            pipeline.next();
            counter++;
        }
        assertEquals(counter, 6);

        pipe1 = new AggregatorPipe<String>(new ArrayList<String>());
        pipe2 = new CollectionFilterPipe<String>(pipe1.getSideEffect(), ComparisonFilterPipe.Filter.EQUAL);
        pipeline = new Pipeline<String, String>(Arrays.asList(pipe1, pipe2));
        pipeline.setStarts(list.iterator());
        counter = 0;
        assertFalse(pipeline.hasNext());
        while (pipeline.hasNext()) {
            pipeline.next();
            counter++;
        }
        assertEquals(counter, 0);


    }

    public void testNullIterator() {
        List<String> list = Arrays.asList("marko", "antonio", "rodriguez", "was", "here", ".");
        Iterator<String> itty = list.iterator();
        int counter = 0;
        while (itty.hasNext()) {
            itty.next();
            counter++;
        }
        assertEquals(counter, 6);
        assertFalse(itty.hasNext());
        try {
            itty.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
        list = Arrays.asList(null, null, null, null, null, null);
        itty = list.iterator();
        counter = 0;
        while (itty.hasNext()) {
            itty.next();
            counter++;
        }
        assertEquals(counter, 6);
        assertFalse(itty.hasNext());
        try {
            itty.next();
            assertFalse(true);
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }

    public void testNoSuchElement() {
        List<String> list = Arrays.asList(null, null, null);
        AggregatorPipe<String> pipe1 = new AggregatorPipe<String>(new ArrayList<String>());
        pipe1.setStarts(list.iterator());
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            assertNull(pipe1.next());
        }
        assertEquals(counter, 3);
        try {
            pipe1.next();
            assertTrue(false);
        } catch (NoSuchElementException e) {
            assertFalse(false);
        }
        counter = 0;
        for (String s : pipe1.getSideEffect()) {
            assertNull(s);
            counter++;
        }
        assertEquals(counter, 3);
        assertEquals(pipe1.getSideEffect().size(), 3);
    }
}
