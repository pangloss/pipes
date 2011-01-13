package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.pipes.filter.AbstractCollectionFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

import java.util.Collection;

/**
 * The LabelCollectionFilterPipe either allows or disallows all Edges that have the provided label.
 *
 * @author: Darrick Wiebe (http://ofallpossibleworlds.wordpress.com)
 */
public class LabelCollectionFilterPipe extends AbstractCollectionFilterPipe<Edge, String> {

    public LabelCollectionFilterPipe(final Collection<String> labels, final ComparisonFilterPipe.Filter filter) {
        super(labels, filter);
    }

    protected Edge processNextStart() {
        while (true) {
            Edge edge = this.starts.next();
            if (this.compareToCollection(edge.getLabel())) {
                return edge;
            }
        }
    }
}
