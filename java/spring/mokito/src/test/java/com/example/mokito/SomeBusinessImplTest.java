package com.example.mokito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SomeBusinessImplTest {

    private SomeBusinessImpl someBussinesImpl;
    private DataService dataServiceMock;

    @BeforeEach
    public void init() {
        dataServiceMock = mock(DataService.class);
        someBussinesImpl = new SomeBusinessImpl(dataServiceMock);
    }
            
    
    @Test
    void findTheGreatestFromAllDataTest() {
        when(dataServiceMock.retrieveAllData()).thenReturn(new int [] {22,33,44});

        int ret =  someBussinesImpl.findTheGreatestFromAllData();

        assertEquals(44, ret);
    }

    @Test
    void findTheGreatestFromAllDataTest_under0() {
        when(dataServiceMock.retrieveAllData()).thenReturn(new int [] {-22,-33,-44});

        int ret =  someBussinesImpl.findTheGreatestFromAllData();

        assertEquals(-22, ret);

    }
}