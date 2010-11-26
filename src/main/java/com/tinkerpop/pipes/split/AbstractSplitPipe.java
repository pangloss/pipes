package com.tinkerpop.pipes.split;

import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PathSequence;

import java.util.*;

/**
 * The AbstractSplitPipe provides all the base functionality necessary to create a SplitPipe.
 * The internal class SplitQueuePipe is a queue-based pipe and is what is returned by getSplit().
 * The reason for using a queue internal to each split is that when a particular split requires a next() object, it must next()'d from the SplitPipe.
 * If the next() object is not routed to the requesting SplitQueuePipe, then its placed in the queue of the respective split.
 * Thus, if a split has no legal objects coming to it and is next()'d, then all the other splits will have objects in their queue.
 * This model raises the potential for an out of memory exception. To avoid such problems, make use of non-exhaustive merges.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class AbstractSplitPipe<S> extends AbstractPipe<S, S> implements SplitPipe<S> {

    protected final List<SplitQueuePipe<S>> splits = new ArrayList<SplitQueuePipe<S>>();

    public AbstractSplitPipe(final int numberOfSplits) {
        for (int i = 0; i < numberOfSplits; i++) {
            this.addSplit();
        }
    }

    public void addSplit() {
        this.splits.add(new SplitQueuePipe<S>(this));
    }

    public SplitQueuePipe<S> getSplit(final int splitNumber) {
        return this.splits.get(splitNumber);
    }

    protected S processNextStart() throws NoSuchElementException {
        return this.starts.next();
    }

    public List getPath() {
        if (this.starts instanceof Pipe) {
            Pipe pipe = (Pipe) this.starts;
            List pathElements = pipe.getPath();
            return pathElements;
        } else {
            ArrayList pathElements = new ArrayList();
            return pathElements;
        }
    }

    public class SplitQueuePipe<S> implements Pipe<S, S> {

        private final Queue<S> queue = new LinkedList<S>();
        private final SplitPipe<S> splitPipe;

        private final Queue<List> pathQueue = new LinkedList<List>();
        private boolean pathEnabled = false;
        private List currentPath;

        public SplitQueuePipe(final SplitPipe<S> splitPipe) {
            this.splitPipe = splitPipe;
        }

        public void add(final S element) {
            this.queue.add(element);
        }

        public List splitPath() {
            return splitPipe.getPath();
        }

        public void addPath(List path) {
            ArrayList temp = new ArrayList();
            temp.addAll(path);
            this.pathQueue.add(temp);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            if (this.queue.isEmpty()) {
                this.prepareNext();
                return !this.queue.isEmpty();
            } else {
                return true;
            }
        }

        public S next() {
            if (this.queue.isEmpty()) {
                this.prepareNext();
            }
            if (this.queue.isEmpty())
                throw new NoSuchElementException();
            else {
                S temp = this.queue.remove();
                if (pathEnabled) {
                    this.currentPath = this.pathQueue.remove();
                }
                this.prepareNext();
                return temp;
            }
        }

        private void prepareNext() {
            if (this.queue.isEmpty()) {
                while (this.splitPipe.hasNext()) {
                    this.splitPipe.routeNext();
                    if (!this.queue.isEmpty())
                        return;
                }
            }
        }

        public Iterable<List> getPaths() {
            this.splitPipe.getPaths();
            this.pathEnabled = true;
            return new PathSequence(this);
        }

        public List getPath() {
            if (!this.pathEnabled) {
                throw new UnsupportedOperationException("To use path(), you must call getPaths() before iteration begins");
            }
            List<?> path = new ArrayList();
            path.addAll(this.currentPath);
            return path;
        }

        public void setStarts(final Iterator<S> starts) {
            throw new UnsupportedOperationException();
        }

        public void setStarts(final Iterable<S> starts) {
            throw new UnsupportedOperationException();
        }

        public Iterator<S> iterator() {
            return this;
        }
    }
}
