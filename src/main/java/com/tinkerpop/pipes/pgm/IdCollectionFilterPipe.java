package com.tinkerpop.pipes.pgm;

import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.pipes.filter.AbstractCollectionFilterPipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;

import java.util.Collection;

/**
 * An IdCollectionFilterPipe will take a collection of object ids and a Filter.NOT_EQUAL or Filter.EQUAL argument.
 * If an incoming object's id is contained (or not contained) in the provided collection, then it is emitted (or not emitted).
 *
 * @author Darrick Wiebe (ofallpossibleworlds.wordpress.com)
 */
public class IdCollectionFilterPipe extends AbstractCollectionFilterPipe<Element, Object> {
    public IdCollectionFilterPipe(final Collection ids, final ComparisonFilterPipe.Filter filter) {
      super(ids, filter);
    }

    protected Element processNextStart() {
        while (true) {
            Element element = this.starts.next();
            if (this.compareToCollection(element.getId())) {
                return element;
            }
        }
    }
}
