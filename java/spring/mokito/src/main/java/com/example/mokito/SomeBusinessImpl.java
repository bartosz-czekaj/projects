package com.example.mokito;

import java.util.Arrays;

public class SomeBusinessImpl {
    private DataService dataService;

    public SomeBusinessImpl(DataService dataService) {
        super();
        this.dataService = dataService;
    }

    public int findTheGreatestFromAllData() {
        int returnVal = Arrays.stream(dataService.retrieveAllData()).max().orElseThrow(() -> new IllegalArgumentException("array shouldn't be empty"));
        return returnVal;
    }

}
