package com.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SequenceIterator implements Iterator<Integer> {
    private Sequence sequence;
    private int nextValue;

    public SequenceIterator(Sequence sequence) {
        this.sequence = sequence;
        this.nextValue = sequence.getIncrement();
    }


    @Override
    public boolean hasNext() {
        return this.nextValue <= this.sequence.getLimit();
    }

    @Override
    public Integer next() {
        if(this.nextValue > this.sequence.getLimit()) {
            throw new NoSuchElementException("No such element");
        }

        int rv = this.nextValue;
        this.nextValue += this.sequence.getIncrement();

        return rv;
    }
}
