package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import junit.framework.TestCase;
import java.util.Arrays;

/**
 * @author: Marko A. Rodriguez (http://markorodriguez.com)
 * @author: Darrick Wiebe (http://ofallpossibleworlds.wordpress.com)
 */
public class LabelCollectionFilterPipeTest extends TestCase {

    public void testFilterLabelCollection() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        LabelCollectionFilterPipe pipe = new LabelCollectionFilterPipe(Arrays.asList("knows"), ComparisonFilterPipe.Filter.NOT_EQUAL);
        pipe.setStarts(marko.getOutEdges().iterator());
        assertTrue(pipe.hasNext());
        int counter = 0;
        while (pipe.hasNext()) {
            Edge e = pipe.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("2") || e.getInVertex().getId().equals("4"));
            counter++;
        }
        assertEquals(counter, 2);

        pipe = new LabelCollectionFilterPipe(Arrays.asList("knows"), ComparisonFilterPipe.Filter.EQUAL);
        pipe.setStarts(marko.getOutEdges().iterator());
        assertTrue(pipe.hasNext());
        counter = 0;
        while (pipe.hasNext()) {
            Edge e = pipe.next();
            assertEquals(e.getOutVertex(), marko);
            assertTrue(e.getInVertex().getId().equals("3"));
            counter++;
        }
        assertEquals(counter, 1);
    }
}
