package com.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SuperIterable<E> implements Iterable<E> {

    private Iterable<E> self;

    public SuperIterable(Iterable<E> self) {
        this.self = self;
    }

    public void forEvery(Consumer<E> cons) {
        StreamSupport.stream(self.spliterator(), false)
                .forEach(cons);
    }

    public SuperIterable<E> filter(Predicate<E> pred) {

        List<E> results = StreamSupport.stream(self.spliterator(), false)
                                        .filter(pred)
                                        .collect(Collectors.toList());

        return new SuperIterable<E>(results);
    }


    public <F> SuperIterable<F> flatMap(Function<E,SuperIterable<F>> op)
    {
        List<F> results = new ArrayList<>();

        StreamSupport.stream(self.spliterator(), false).forEach(e->op.apply(e).forEach(f-> results.add(f)));

        return new SuperIterable<F> (results);
    }

    public SuperIterable<E> filter2(Predicate<E> pred) {

        List<E> results = new ArrayList<>();

        self.forEach(e -> {
            if(pred.test(e))
                results.add(e);
        });

        return new SuperIterable<E>(results);
    }

    public <F> SuperIterable<F> map(Function<E,F> op) {
        List<F> results = new ArrayList<>();

        self.forEach(e -> results.add(op.apply(e)));

        return new SuperIterable<F> (results);
    }

    @Override
    public Iterator<E> iterator() {
        return self.iterator();
    }
}
