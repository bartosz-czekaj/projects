package com.example.unittest.mock.demo.impl;

import com.example.unittest.mock.demo.services.SomeDataService;

import java.util.Arrays;


public class SomeBusinessImp {
    public SomeDataService getSomeDataService() {
        return someDataService;
    }

    public void setSomeDataService(SomeDataService someDataService) {
        this.someDataService = someDataService;
    }

    private SomeDataService someDataService;

    public int calculateSum(int [] data){
        return Arrays.stream(data).sum();
    }

    public int calculateSumUsingDataService() {
        int[] data = someDataService.retrieveAllData();
        return calculateSum(data);
    }
}
