package com.streams.spliterator;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class ArrayListSpliterator<E> implements Spliterator<E> {
    private final ArrayList<E> list;
    private int index;
    private int fence;
    private int expectedModCount;

    public ArrayListSpliterator(ArrayList<E> list, int index, int fence, int expectedModCount) {
        this.list = list;
        this.index = index;
        this.fence = fence;
        this.expectedModCount = expectedModCount;
    }

    public long estimateSize() {
        return (long) fence - index;
    }

    @Override
    public int characteristics() {
        return 0;
    }

    public boolean tryAdvance(Consumer<? super E> action) {
        final int hi = fence;
        int i = index;

        if(i < hi) {
            index = i + 1;
            E e = list.get(i);

            action.accept(e);

            return true;
        }

        return false;
    }

    public ArrayListSpliterator<E> trySplit() {
        final int hi = fence;
        final int lo = index;
        final int mid = (lo+hi) >>> 1;

        return (lo >= mid) ? null : new ArrayListSpliterator<>(list, lo, index = mid, expectedModCount);

    }
}
