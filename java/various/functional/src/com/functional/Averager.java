package com.functional;

public class Averager {
    private double total = 0;
    private int count = 0;

    public void merge(Averager avg) {
        total += avg.total;
        count += avg.count;
    }

    public double get() {
        return total / count;
    }

    public void add(double element){
        total += element;
        count++;
    }

}
