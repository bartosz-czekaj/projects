package com.example.mokito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SomeBusinessImplMockAnnotationsTest {
    @Mock
    private DataService dataServiceMock;
    @InjectMocks
    private SomeBusinessImpl someBussinesImpl;
    @Spy
    List<Integer> spiedList = new ArrayList<Integer>();

   /* @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }*/


    @Test
    public void findTheGreatestFromAllDataTest() {
        when(dataServiceMock.retrieveAllData()).thenReturn(new int [] {22,33,44});
        assertEquals(44, someBussinesImpl.findTheGreatestFromAllData());
    }

    @Test
    public  void findTheGreatestFromAllDataTest_under0() {
        when(dataServiceMock.retrieveAllData()).thenReturn(new int [] {-22,-33,-44});
        assertEquals(-22, someBussinesImpl.findTheGreatestFromAllData());
    }

    @Test
    public  void findTheGreatestFromAllDataTest_empty() {
        when(dataServiceMock.retrieveAllData()).thenReturn(new int [] {});
        assertThrows(IllegalArgumentException.class, () -> someBussinesImpl.findTheGreatestFromAllData());
    }

    @Test
    public  void findTheGreatestFromAllDataTest_spiedList() {

        spiedList.add(1);
        spiedList.add(2);

        Mockito.doReturn(IntStream.rangeClosed(99,101).boxed()) .when(spiedList).stream();

        int[] primitive = spiedList.stream().mapToInt(Integer::intValue).toArray();

        when(dataServiceMock.retrieveAllData()).thenReturn(primitive);
        assertEquals(101, someBussinesImpl.findTheGreatestFromAllData());
    }
}
