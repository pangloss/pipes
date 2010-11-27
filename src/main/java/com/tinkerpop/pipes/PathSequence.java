package com.tinkerpop.pipes;

import java.util.Iterator;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class PathSequence implements Iterable<List>, Iterator<List> {

    private final Pipe pipe;

    public PathSequence(final Pipe pipe) {
        this.pipe = pipe;
        pipe.getPaths();
    }

    public PathSequence(final Pipe pipe, boolean enabled) {
        this.pipe = pipe;
        if (!enabled) {
            pipe.getPaths();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Iterator<List> iterator() {
        return this;
    }

    public List next() {
        this.pipe.next();
        return this.pipe.getPathForCurrentEnd();
    }

    public boolean hasNext() {
        return this.pipe.hasNext();
    }
}

