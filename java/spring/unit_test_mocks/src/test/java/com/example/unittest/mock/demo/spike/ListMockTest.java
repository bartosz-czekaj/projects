package com.example.unittest.mock.demo.spike;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ListMockTest {

    @Test
    public void test_multiple_return() {
        List listMock = mock(List.class);

        when(listMock.size()).thenReturn(5).thenReturn(10);

        assertEquals(5, listMock.size());
        assertEquals(10, listMock.size());
    }

    @Test
    public void test_argument_matcher() {
        List listMock = mock(List.class);

        when(listMock.get(anyInt())).thenReturn("ala ma kota");

        assertEquals("ala ma kota", listMock.get(1));
        assertEquals("ala ma kota", listMock.get(1000));
    }

    @Test
    public void test_verify() {
        List<String>listMock = mock(List.class);

        when(listMock.get(anyInt())).thenReturn("ala ma kota");

        //SUT
        String value1 = listMock.get(0);
        String value2 = listMock.get(1);

        verify(listMock).get(0);
        verify(listMock, times(2)).get(anyInt());
        verify(listMock, atLeast(1)).get(anyInt());
        verify(listMock, atMost(2)).get(anyInt());
        verify(listMock, never()).get(2);
    }

    @Test
    public void test_argument_capture() {
        List<String>listMock = mock(List.class);

        //SUT
        listMock.add("tekst");

        //verification
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(listMock).add(captor.capture());
        assertEquals("tekst", captor.getValue());

        /*
            class A {
        public void foo(OtherClass other) {
            SomeData data = new SomeData("Some inner data");
            other.doSomething(data);
                }
            }
            Now if you want to check the inner data you can use the captor:

            // Create a mock of the OtherClass
            OtherClass other = mock(OtherClass.class);

            // Run the foo method with the mock
            new A().foo(other);

            // Capture the argument of the doSomething function
            ArgumentCaptor<SomeData> captor = ArgumentCaptor.forClass(SomeData.class);
            verify(other, times(1)).doSomething(captor.capture());

            // Assert the argument
            SomeData actual = captor.getValue();
            assertEquals("Some inner data", actual.innerData);
         */

    }

    @Test
    public void test_all_arguments_capture() {
        List<String>listMock = mock(List.class);
        AtomicInteger counter = new AtomicInteger(0);
        //SUT
        listMock.add("tekst1");
        listMock.add("tekst2");

        //verification
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(listMock, times(2)).add(captor.capture());

        captor.getAllValues().stream().forEach(item -> {
            int value = counter.addAndGet(1);
            assertEquals(String.format("tekst%d", value), item);
        });
    }

    @Test
    public void test_spying() {
        ArrayList arrayListSpy = spy(ArrayList.class);//when you ant original dependency

        arrayListSpy.add("one");
        arrayListSpy.add("two");

        verify(arrayListSpy).add("one");
        verify(arrayListSpy).add("two");

        assertEquals(2, arrayListSpy.size());
    }

    @Test
    public void test_spying_mock() {
        List<String> list = new ArrayList<String>();
        List<String> spyList = spy(list);

        assertEquals(0, spyList.size());

        doReturn(100).when(spyList).size();
        assertEquals(100, spyList.size());
    }

    @Test
    public void test_spying_mock_stream() {
        List<String> list = new ArrayList<String>();
        List<String> spyList = spy(list);

        assertEquals(0, spyList.size());

        doReturn(Stream.generate(()->"tekst").limit(10)).when(spyList).stream();
        assertTrue( spyList.stream().allMatch(item-> item.equals("tekst")));
    }
}
