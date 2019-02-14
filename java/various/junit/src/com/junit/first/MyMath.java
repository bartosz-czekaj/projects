package com.junit.first;

import java.util.Arrays;

public class MyMath {

    public int sum(int[] numbers){
        return Arrays.stream(numbers).sum();
    }
}
