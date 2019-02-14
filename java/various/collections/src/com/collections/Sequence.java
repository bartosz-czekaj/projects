package com.collections;

import java.util.Iterator;

public class Sequence implements  Iterable<Integer> {

    private final int start;
    private int increment;
    private int limit;

    public int getStart() {
        return start;
    }

    public int getIncrement() {
        return increment;
    }

    public int getLimit() {
        return limit;
    }

    public Sequence(int start, int increment, int limit) {
        this.start = start;
        this.increment = increment;
        this.limit = limit;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new SequenceIterator(this);
    }


}
