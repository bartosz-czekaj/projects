package com.example.unittest.mock.demo.impl;

import com.example.unittest.mock.demo.services.SomeDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SomeBusinessImpTest {

    @InjectMocks
    SomeBusinessImp someBusinessImp = new SomeBusinessImp();

    @Mock
    SomeDataService someDataService = mock(SomeDataService.class);


    @Test
    public void calculateSumSimple() {
        when(someDataService.retrieveAllData()).thenReturn(new int [] {1,2,3});

        assertEquals(someBusinessImp.calculateSumUsingDataService(), 6);
    }

    @Test
    public void calculateSumSingle() {
        when(someDataService.retrieveAllData()).thenReturn(new int [] {5});

        assertEquals(someBusinessImp.calculateSumUsingDataService(), 5);
    }

    @Test
    public void calculateSumEmpty() {
        when(someDataService.retrieveAllData()).thenReturn(new int [] {});

        assertEquals(someBusinessImp.calculateSumUsingDataService(), 0);
    }
}