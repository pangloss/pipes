package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * @author Darrick Wiebe (ofallpossibleworlds.wordpress.com)
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class IdCollectionFilterPipeTest extends TestCase {

    public void testFilterIdsCollection1() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        VertexEdgePipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        EdgeVertexPipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        IdCollectionFilterPipe pipe3 = new IdCollectionFilterPipe(Arrays.asList("3"), ComparisonFilterPipe.Filter.NOT_EQUAL);
        Pipeline<Vertex,Vertex> pipeline = new Pipeline<Vertex,Vertex>(pipe1, pipe2, pipe3);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Vertex vertex = pipeline.next();
            assertEquals("lop", vertex.getProperty("name"));
            counter++;
        }
        assertEquals(counter, 1);
    }

     public void testFilterIdsCollection2() {
        Graph graph = TinkerGraphFactory.createTinkerGraph();
        Vertex marko = graph.getVertex("1");
        VertexEdgePipe pipe1 = new VertexEdgePipe(VertexEdgePipe.Step.OUT_EDGES);
        EdgeVertexPipe pipe2 = new EdgeVertexPipe(EdgeVertexPipe.Step.IN_VERTEX);
        IdCollectionFilterPipe pipe3 = new IdCollectionFilterPipe(Arrays.asList("3"), ComparisonFilterPipe.Filter.EQUAL);
        Pipeline<Vertex,Vertex> pipeline = new Pipeline<Vertex,Vertex>(pipe1, pipe2, pipe3);
        pipeline.setStarts(Arrays.asList(marko));
        int counter = 0;
        while (pipeline.hasNext()) {
            Vertex vertex = pipeline.next();
            assertNotNull(vertex.getProperty("name"));
            assert(!vertex.getProperty("name").equals("lop"));
            counter++;
        }
        assertEquals(counter, 2);
    }
}
