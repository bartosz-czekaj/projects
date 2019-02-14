package com.functional;

public interface ExceptionFunction<E,F> {
    F applay(E e) throws Throwable;


}
